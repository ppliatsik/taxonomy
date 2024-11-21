(ns com.taxonomy.end-user.api
  (:require [java-time :as jt]
            [com.taxonomy.util :as util]
            [com.taxonomy.http.http-response :as http-response]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.end-user.data :as data]
            [com.taxonomy.http.token :as token]
            [com.taxonomy.http.email :as email])
  (:import [java.util UUID]))

(def default-limit 10)
(def default-offset 0)

(defn create-token-and-send-email-account-activation
  [{:keys [db server-address activation-token-valid-time email-host]}
   {:keys [username email]}]
  (let [token (str (UUID/randomUUID))
        _     (data/create-confirmation-token* db {:username username
                                                   :token    token
                                                   :valid-to (-> (jt/local-date-time)
                                                                 (jt/plus (jt/minutes activation-token-valid-time)))})]
    (email/send email-host
                {:to      email
                 :subject "User account activation"
                 :body    (str "<a href=" server-address "/#/email-activate-account?token="
                               token ">Click to activate account</a>")})))

(defn login
  [{:keys [db parameters auth-keys token-valid-time email-host] :as request}]
  (let [username     (-> parameters :body :username)
        password     (util/string->md5 (-> parameters :body :password))
        user         (data/get-user-by-username-and-password db {:username username
                                                                 :password password})
        user-by-name (data/get-user-by-username* db {:username username})]
    (cond (nil? user-by-name)
          (http-response/invalid {:result :failure
                                  :reason ::end-user/user-not-exists})

          (and user-by-name (nil? user) (> 3 (:login-fails user-by-name)))
          (do
            (data/increase-login-fails* db {:username username})
            (http-response/invalid {:result :failure
                                    :reason ::end-user/wrong-credentials}))

          (and user-by-name (nil? user) (<= 3 (:login-fails user-by-name)))
          (do
            (data/deactivate-user db {:username username})
            (email/send email-host
                        {:to      (:email user-by-name)
                         :subject "User account deactivation"
                         :body    "Your account has been deactivated due to too many failed logins"})
            (http-response/invalid {:result :failure
                                    :reason ::end-user/user-deactivated}))

          (not (:active user))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/user-is-inactive})

          :else
          (do
            (data/reset-login-fails* db {:username username})
            (http-response/ok {:result  :success
                               :payload (assoc user :token (token/sign user auth-keys token-valid-time))})))))

(defn logout
  [{:keys [user-info cookies] :as req}]
  (if (and user-info (get-in cookies ["X-Auth-Token" :value]))
    (http-response/ok {:result :success})
    (http-response/invalid {:result :failure
                            :reason ::end-user/user-is-not-login})))

(defn email-activate-account
  [{:keys [db parameters] :as request}]
  (let [token (-> parameters :body :token)
        ck    (data/get-valid-confirmation-token-by-token db {:token token})
        user  (data/get-user-by-username* db ck)]
    (cond (or (nil? user)
              (jt/before? (jt/local-date-time (:valid-to ck))
                          (jt/local-date-time)))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-token})

          (:active user)
          (http-response/invalid {:result :failure
                                  :reason ::end-user/user-already-active})

          :else
          (http-response/ok {:result  :success
                             :payload (data/activate-user-by-email db user)}))))

(defn resend-email-activation-account
  [{:keys [db parameters] :as request}]
  (let [user (data/get-user-by-username* db (:path parameters))]
    (cond (nil? user)
          (http-response/invalid {:result :failure
                                  :reason ::end-user/user-not-exists})

          (:active user)
          (http-response/invalid {:result :failure
                                  :reason ::end-user/user-already-active})

          :else
          (do
            (data/delete-user-confirmation-token db (:path parameters))
            (create-token-and-send-email-account-activation request user)
            (http-response/ok {:result :success})))))

(defn create-user
  [{:keys [db parameters] :as request}]
  (cond (data/get-user-by-username-and-email db (:body parameters))
        (http-response/invalid {:result :failure
                                :reason ::end-user/user-already-exists})

        (not= (-> parameters :body :password)
              (-> parameters :body :password-verification))
        (http-response/invalid {:result :failure
                                :reason ::end-user/user-provides-different-passwords})

        :else
        (let [data (-> (:body parameters)
                       (update :password util/string->md5)
                       (assoc :roles (into-array String ["user"])))
              user (data/create-user db data)]
          (create-token-and-send-email-account-activation request user)
          (http-response/ok {:result  :success
                             :payload user}))))

(defn activate-user
  [{:keys [db parameters user-info] :as request}]
  (let [user (data/get-user-by-username* db (:path parameters))]
    (cond (not (end-user/is-admin? user-info))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          (nil? user)
          (http-response/invalid {:result :failure
                                  :reason ::end-user/user-not-exists})

          (:active user)
          (http-response/invalid {:result :failure
                                  :reason ::end-user/user-already-active})

          :else
          (do
            (data/reset-login-fails* db user)
            (http-response/ok {:result  :success
                               :payload (data/activate-user db user)})))))

(defn deactivate-user
  [{:keys [db parameters user-info] :as request}]
  (let [user (data/get-active-user-by-username db (:path parameters))]
    (cond (and (not (end-user/is-admin? user-info))
               (not (end-user/is-current-user? user-info (-> parameters :path :username))))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          (nil? user)
          (http-response/not-found {:result :failure
                                    :reason ::end-user/user-not-exists})

          :else
          (http-response/ok {:result  :success
                             :payload (data/deactivate-user db (:path parameters))}))))

(defn change-user-password
  [{:keys [db parameters user-info] :as request}]
  (let [user (data/get-active-user-by-username db (:path parameters))]
    (cond (and (not (end-user/is-admin? user-info))
               (not (end-user/is-current-user? user-info (-> parameters :path :username))))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          (nil? user)
          (http-response/invalid {:result :failure
                                  :reason ::end-user/user-not-exists})

          (not= (:password user)
                (util/string->md5 (-> parameters :body :old-password)))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/user-provides-wrong-password})

          (not= (-> parameters :body :password)
                (-> parameters :body :password-verification))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/user-provides-different-passwords})

          :else
          (let [params {:username (-> parameters :path :username)
                        :password (util/string->md5 (-> parameters :body :password))}
                user   (data/change-user-password db params)]
            (http-response/ok {:result  :success
                               :payload user})))))

(defn reset-user-password
  [{:keys [db parameters email-host] :as request}]
  (let [user (data/get-user-by-email db (:body parameters))]
    (cond (nil? user)
          (http-response/invalid {:result :failure
                                  :reason ::end-user/user-not-exists})

          :else
          (let [password (util/create-password)
                email    (-> parameters :body :email)
                params   {:username (:username user)
                          :password (util/string->md5 password)}
                _        (data/change-user-password db params)]
            (email/send email-host
                        {:to      email
                         :subject "New account password"
                         :body    (str "Your new password is: " password "\nChange it after login.")})
            (http-response/ok {:result :success})))))

(defn update-user-info
  [{:keys [db parameters user-info] :as request}]
  (let [old-user (data/get-user-by-username db (:path parameters))]
    (cond (and (not (end-user/is-admin? user-info))
               (not (end-user/is-current-user? user-info (-> parameters :path :username))))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          (nil? old-user)
          (http-response/not-found {:result :failure
                                    :reason ::end-user/user-not-exists})

          :else
          (let [params (merge old-user
                              (:body parameters))
                user   (data/update-user-info db params)]
            (when (and (not= (:email old-user) (:email user))
                       (:email-activation user))
              (data/deactivate-user db user)
              (create-token-and-send-email-account-activation request user))
            (http-response/ok {:result  :success
                               :payload user})))))

(defn get-users
  [{:keys [db parameters user-info] :as request}]
  (if (not (end-user/is-admin? user-info))
    (http-response/invalid {:result :failure
                            :reason ::end-user/invalid-user})
    (let [limit  (or (-> parameters :query :limit) default-limit)
          offset (or (-> parameters :query :offset) default-offset)
          params {:limit  limit
                  :offset offset
                  :q      (-> parameters :query :q)}

          users     (data/get-users db params)
          users-cnt (data/get-users-count db)]
      {:status 200
       :body   {:results    users
                :pagination {:limit  limit
                             :offset offset
                             :total  (:count users-cnt)}}})))

(defn get-user
  [{:keys [db parameters user-info] :as request}]
  (if (and (not (end-user/is-admin? user-info))
           (not (end-user/is-current-user? user-info (-> parameters :path :username))))
    (http-response/invalid {:result :failure
                            :reason ::end-user/invalid-user})
    (let [user (data/get-user-by-username db (:path parameters))]
      (if user
        (http-response/ok user)
        (http-response/not-found {:result :failure
                                  :reason ::end-user/user-not-exists})))))

(defn delete-user
  [{:keys [db parameters user-info] :as request}]
  (let [user (data/get-user-by-username db (:path parameters))]
    (cond (and (not (end-user/is-admin? user-info))
               (not (end-user/is-current-user? user-info (-> parameters :path :username))))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          (nil? user)
          (http-response/not-found {:result :failure
                                    :reason ::end-user/user-not-exists})

          :else
          (do
            (data/delete-user db (:path parameters))
            (http-response/ok {:result :success})))))
