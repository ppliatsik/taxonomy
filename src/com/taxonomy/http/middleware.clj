(ns com.taxonomy.http.middleware
  (:require [com.taxonomy.http.token :as http.token]))

(defn inject-system
  [system]
  (fn [h]
    (fn [req]
      (h (merge req system)))))

(defn inject-user
  []
  (fn [handler]
    (fn [{:keys [cookies headers auth-keys] :as request}]
      (let [token     (or (get-in cookies ["X-Auth-Token" :value])
                          (get-in headers ["x-auth-token"]))
            user-info (when-let [user (http.token/unsign token auth-keys)]
                        (update user :roles set))]
        (handler (assoc request :user-info user-info))))))
