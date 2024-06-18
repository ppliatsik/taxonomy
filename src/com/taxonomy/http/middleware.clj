(ns com.taxonomy.http.middleware
  (:require [spec-tools.core :as st]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [com.taxonomy.end-user :as end-user]))

(defn inject-system
  [system]
  (fn [h]
    (fn [req]
      (h (merge req system)))))

(defn coerce-user-creation-body
  [handler]
  (fn [{:keys [body-params] :as req}]
    (let [coerced-body (st/coerce ::end-user/create-user-request body-params st/string-transformer)]
      (if (s/valid? ::end-user/create-user-request coerced-body)
        (handler (assoc-in req [:parameters :body] coerced-body))
        (do
          (expound/expound ::end-user/create-user-request coerced-body)
          {:status 400
           :body   (s/explain-str ::end-user/create-user-request coerced-body)})))))

(defn coerce-user-update-body
  [handler]
  (fn [{:keys [body-params] :as req}]
    (let [coerced-body (st/coerce ::end-user/update-user-info-request body-params st/string-transformer)]
      (if (s/valid? ::end-user/update-user-info-request coerced-body)
        (handler (assoc-in req [:parameters :body] coerced-body))
        (do
          (expound/expound ::end-user/update-user-info-request coerced-body)
          {:status 400
           :body   (s/explain-str ::end-user/update-user-info-request coerced-body)})))))
