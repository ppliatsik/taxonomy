(ns com.taxonomy.product.api
  (:require [com.taxonomy.http.http-response :as http-response]
            [com.taxonomy.product :as product]
            [com.taxonomy.product.data :as data]
            [com.taxonomy.end-user :as end-user]))

(def default-limit 10)
(def default-offset 0)

(defn create-product
  [{:keys [db parameters user-info] :as request}]
  (let [product (data/get-product-by-name db (:body parameters))]
    (cond (and (not (end-user/is-admin? user-info))
               (not (end-user/is-user? user-info)))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          product
          (http-response/invalid {:result :failure
                                  :reason ::product/product-already-exists})

          :else
          (http-response/ok {:result  :success
                             :payload (data/create-product db (:body parameters))}))))

(defn publish-product
  [{:keys [db parameters user-info] :as request}]
  (let [product (data/get-product-by-id db (:path parameters))]
    (cond (and (not (end-user/is-admin? user-info))
               (not (end-user/is-current-user? user-info (:created-by product))))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          (nil? product)
          (http-response/not-found {:result :failure
                                    :reason ::product/product-not-exists})

          :else
          (http-response/ok {:result  :success
                             :payload (data/publish-product db product)}))))

(defn unpublish-product
  [{:keys [db parameters user-info] :as request}]
  (let [product (data/get-product-by-id db (:path parameters))]
    (cond (and (not (end-user/is-admin? user-info))
               (not (end-user/is-current-user? user-info (:created-by product))))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          (nil? product)
          (http-response/not-found {:result :failure
                                    :reason ::product/product-not-exists})

          :else
          (http-response/ok {:result  :success
                             :payload (data/unpublish-product db product)}))))

(defn products-match
  [{:keys [db parameters] :as request}]
  (let [limit  (or (-> parameters :query :limit) default-limit)
        offset (or (-> parameters :query :offset) default-offset)
        params (merge (:query parameters)
                      {:limit  limit
                       :offset offset})

        products     (data/get-products db params)
        products-cnt (data/get-products-count db)]
    {:status     200
     :body       products
     :pagination {:limit       limit
                  :offset      offset
                  :total-count (:count products-cnt)}}))

(defn products-classification
  [{:keys [db parameters] :as request}]
  )

(defn products-discovery
  [{:keys [db parameters] :as request}]
  )

(defn get-product
  [{:keys [db parameters] :as request}]
  (let [product (data/get-product-by-id db (:path parameters))]
    (if product
      (http-response/ok product)
      (http-response/not-found {:result :failure
                                :reason ::product/product-not-exists}))))

(defn delete-product
  [{:keys [db parameters user-info] :as request}]
  (let [product (data/get-product-by-id db (:path parameters))]
    (cond (and (not (end-user/is-admin? user-info))
               (not (end-user/is-current-user? user-info (:created-by product))))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          (nil? product)
          (http-response/not-found {:result :failure
                                    :reason ::product/product-not-exists})

          :else
          (do
            (data/delete-product db (:path parameters))
            (http-response/ok {:result :success})))))
