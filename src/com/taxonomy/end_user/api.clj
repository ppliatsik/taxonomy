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
  [{:keys [db server-address activation-token-valid-time]}
   username email]
  (let [token (str (UUID/randomUUID))
        _     (data/create-confirmation-token* db {:username username
                                                   :token    token
                                                   :valid-to (-> (jt/local-date-time)
                                                                 (jt/plus (jt/minutes activation-token-valid-time)))})]
    (email/send {:to      [email]
                 :subject "User account activation"
                 :body    (str "<a href=" server-address "/email-activate-account?token="
                               token ">Click to activate account</a>")})))

(defn login
  [{:keys [db parameters auth-keys token-valid-time] :as request}]
  (let [username (-> parameters :path :username)
        password (util/string->md5 (-> parameters :body :password))
        user     (data/get-user-by-username-and-password db {:username username
                                                             :password password})]
    (if (:active user)
      (http-response/ok {:result  :success
                         :payload (assoc user :token (token/sign user auth-keys token-valid-time))})
      (http-response/invalid {:result :failure
                              :reason ::end-user/user-not-exists}))))

(defn email-activate-account
  [{:keys [db parameters] :as request}]
  (let [token (-> parameters :query :token)
        ck    (data/get-valid-confirmation-token-by-token db {:token token})
        user  (data/get-user-by-username* db ck)]
    (cond (nil? user)
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
  (let [user (data/get-user-by-username* db (:path parameters))
        ck   (data/get-confirmation-token-by-username db (:path parameters))]
    (cond (nil? user)
          (http-response/invalid {:result :failure
                                  :reason ::end-user/user-not-exists})

          (:active user)
          (http-response/invalid {:result :failure
                                  :reason ::end-user/user-already-active})

          (jt/after? (jt/instant (:valid-to ck))
                     (jt/instant))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/valid-token-exists})

          :else
          (do
            (data/delete-user-confirmation-token db (:path parameters))
            (create-token-and-send-email-account-activation request (:username user) (:email user))
            (http-response/ok {:result :success})))))

(defn create-user
  [{:keys [db parameters] :as request}]
  (cond (data/get-user-by-username* db (:path parameters))
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
          (create-token-and-send-email-account-activation request (:username user) (:email user))
          (http-response/ok {:result  :success
                             :payload user}))))

(defn activate-user
  [{:keys [db parameters] :as request}]
  (let [user (data/get-user-by-username* db (:path parameters))]
    (cond (nil? user)
          (http-response/invalid {:result :failure
                                  :reason ::end-user/user-not-exists})

          (:active user)
          (http-response/invalid {:result :failure
                                  :reason ::end-user/user-already-active})

          :else
          (http-response/ok {:result  :success
                             :payload (data/activate-user db user)}))))

(defn deactivate-user
  [{:keys [db parameters] :as request}]
  (if (data/get-active-user-by-username db (:path parameters))
    (let [user   (data/deactivate-user db (:path parameters))
          params {:username (-> parameters :path :username)
                  :password (-> util/create-password util/string->md5)}
          _      (data/change-user-password db params)]
      (http-response/ok {:result  :success
                         :payload user}))
    (http-response/invalid {:result :failure
                            :reason ::end-user/user-not-exists})))

(defn change-user-password
  [{:keys [db parameters] :as request}]
  (let [user (data/get-active-user-by-username db (:path parameters))]
    (cond (nil? user)
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
  [{:keys [db parameters] :as request}]
  (if (data/get-user-by-username-and-email db (merge (:path parameters)
                                                     (:body parameters)))
    (let [password (-> util/create-password util/string->md5)
          email    (-> parameters :body :email)
          params   {:username (-> parameters :path :username)
                    :password password}
          _        (data/change-user-password db params)]
      (email/send {:to      [email]
                   :subject "New account password"
                   :body    (str "Your new password is: " password ".\nChange it after login")})
      (http-response/ok {:result :success}))
    (http-response/invalid {:result :failure
                            :reason ::end-user/user-not-exists})))

(defn update-user-info
  [{:keys [db parameters] :as request}]
  (if-let [old-user (data/get-active-user-by-username db (:path parameters))]
    (let [params (merge old-user
                        (:body parameters))
          user   (data/update-user-info db params)]
      (when (and (not= (:email old-user) (:email user))
                 (:email-activation user))
        (data/deactivate-user db user)
        (create-token-and-send-email-account-activation request (:username user) (:email user)))
      (http-response/ok {:result  :success
                         :payload user}))
    (http-response/invalid {:result :failure
                            :reason ::end-user/user-not-exists})))

(defn get-users
  [{:keys [db parameters] :as request}]
  (let [limit  (or (-> parameters :query :limit) default-limit)
        offset (or (-> parameters :query :offset) default-offset)
        params {:limit  limit
                :offset offset}

        users     (data/get-users db params)
        users-cnt (data/get-users-count db)]
    {:status     200
     :body       users
     :pagination {:limit       limit
                  :offset      offset
                  :total-count (:count users-cnt)}}))

(defn get-user
  [{:keys [db parameters] :as request}]
  (let [user (data/get-user-by-username db (:path parameters))]
    (http-response/one-or-404 user)))

(defn delete-user
  [{:keys [db parameters] :as request}]
  (let [_ (data/delete-user db (:path parameters))]
    (http-response/ok {:result :success})))
