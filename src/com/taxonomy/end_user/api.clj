(ns com.taxonomy.end-user.api
  (:require [com.taxonomy.util :as util]
            [com.taxonomy.http.http-response :as http-response]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.end-user.data :as data]
            [com.taxonomy.http.token :as token]))

(def default-limit 10)
(def default-offset 0)

(defn login
  [{:keys [db parameters auth-keys token-valid-time] :as request}]
  (let [username (-> parameters :path :username)
        password (util/string->md5 (-> parameters :body :password))
        user     (data/get-user-by-username-and-password db {:username username
                                                             :password password})]
    (if user
      (http-response/ok {:result  :success
                         :payload (assoc user :token (token/sign user auth-keys token-valid-time))})
      (http-response/invalid {:result :failure
                              :reason ::end-user/user-not-exists}))))

(defn create-user
  [{:keys [db parameters] :as request}]
  (cond (data/get-user-by-username db (:path parameters))
        (http-response/invalid {:result :failure
                                :reason ::end-user/user-already-exists})

        (not= (-> parameters :body :password)
              (-> parameters :body :password-verification))
        (http-response/invalid {:result :failure
                                :reason ::end-user/user-provides-different-passwords})

        :else
        (let [data (update (:body parameters) :password util/string->md5)
              user (data/create-user db data)]
          ;; TODO send email
          (http-response/ok {:result  :success
                             :payload user}))))

(defn activate-user
  [{:keys [db parameters] :as request}]
  (if (data/get-user-by-username db (:path parameters))
    (let [user (data/activate-user db (:path parameters))]
      (http-response/ok {:result  :success
                         :payload user}))
    (http-response/invalid {:result :failure
                            :reason ::end-user/user-not-exists})))

(defn change-user-password
  [{:keys [db parameters] :as request}]
  (let [user (data/get-user-by-username db (:path parameters))]
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
      ;; TODO send email with new password
      (http-response/ok {:result :success}))
    (http-response/invalid {:result :failure
                            :reason ::end-user/user-not-exists})))

(defn update-user-info
  [{:keys [db parameters] :as request}]
  (if-let [old-user (data/get-user-by-username db (:path parameters))]
    (let [params (merge old-user
                        (:body parameters))
          user   (data/update-user-info db params)]
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
