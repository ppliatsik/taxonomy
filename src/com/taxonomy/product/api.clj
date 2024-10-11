(ns com.taxonomy.product.api
  (:require [com.taxonomy.http.http-response :as http-response]
            [com.taxonomy.product :as product]
            [com.taxonomy.product.data :as data]
            [com.taxonomy.end-user :as end-user]))

(defn create-product
  [{:keys [products parameters user-info] :as request}]
  (let [product (data/get-product-by-name products (:body parameters))]
    (cond (and (not (end-user/is-admin? user-info))
               (not (end-user/is-user? user-info)))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          product
          (http-response/invalid {:result :failure
                                  :reason ::product/product-already-exists})

          :else
          (http-response/ok {:result  :success
                             :payload (data/create-product products (:body parameters))}))))

(defn publish-product
  [{:keys [products parameters user-info] :as request}]
  (let [product (data/get-product-by-id products (:path parameters))]
    (cond (and (not (end-user/is-admin? user-info))
               (not (end-user/is-current-user? user-info (:created-by product))))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          (nil? product)
          (http-response/not-found {:result :failure
                                    :reason ::product/product-not-exists})

          :else
          (http-response/ok {:result  :success
                             :payload (data/publish-product products product)}))))

(defn unpublish-product
  [{:keys [products parameters user-info] :as request}]
  (let [product (data/get-product-by-id products (:path parameters))]
    (cond (and (not (end-user/is-admin? user-info))
               (not (end-user/is-current-user? user-info (:created-by product))))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          (nil? product)
          (http-response/not-found {:result :failure
                                    :reason ::product/product-not-exists})

          :else
          (http-response/ok {:result  :success
                             :payload (data/unpublish-product products product)}))))

(defn products-match
  [{:keys [products parameters] :as request}]
  (let [products (data/search-products products (:query parameters))]
    (http-response/ok products)))

(defn products-classification
  [{:keys [products parameters] :as request}]
  ;; classification of products
  )

(defn products-discovery
  [{:keys [products parameters] :as request}]
  (let [products (data/search-products products (:query parameters))]
    ;; classification of products
    (http-response/ok products)))

(defn get-product
  [{:keys [products parameters] :as request}]
  (let [product (data/get-product-by-id products (:path parameters))]
    (if product
      (http-response/ok product)
      (http-response/not-found {:result :failure
                                :reason ::product/product-not-exists}))))

(defn delete-product
  [{:keys [products parameters user-info] :as request}]
  (let [product (data/get-product-by-id products (:path parameters))]
    (cond (and (not (end-user/is-admin? user-info))
               (not (end-user/is-current-user? user-info (:created-by product))))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          (nil? product)
          (http-response/not-found {:result :failure
                                    :reason ::product/product-not-exists})

          :else
          (do
            (data/delete-product-by-id products (:path parameters))
            (http-response/ok {:result :success})))))
