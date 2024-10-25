(ns com.taxonomy.product.api
  (:require [com.taxonomy.http.http-response :as http-response]
            [com.taxonomy.product :as product]
            [com.taxonomy.product.data :as data]
            [com.taxonomy.end-user :as end-user])
  (:import [java.util UUID]))

(defn create-product
  [{:keys [graph parameters user-info] :as request}]
  (cond (and (not (end-user/is-admin? user-info))
             (not (end-user/is-user? user-info)))
        (http-response/invalid {:result :failure
                                :reason ::end-user/invalid-user})

        (data/get-product-by-name graph (:body parameters))
        (http-response/invalid {:result :failure
                                :reason ::product/product-already-exists})

        :else
        (let [data (-> (:body parameters)
                       (assoc :id (str (UUID/randomUUID)))
                       (assoc :created-by (:username user-info)))]
          (http-response/ok {:result  :success
                             :payload (data/create-product graph data)}))))

(defn publish-product
  [{:keys [graph parameters user-info] :as request}]
  (let [product (data/get-product-by-id graph (:path parameters))]
    (cond (and (not (end-user/is-admin? user-info))
               (not (end-user/is-current-user? user-info (:created-by product))))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          (nil? product)
          (http-response/not-found {:result :failure
                                    :reason ::product/product-not-exists})

          :else
          (http-response/ok {:result  :success
                             :payload (data/publish-product graph product)}))))

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
  [{:keys [graph parameters user-info] :as request}]
  (let [products (data/search-products graph (:query parameters))]
    (if user-info
      (http-response/ok products)
      (http-response/ok (filter #(:is-published %) products)))))

(defn products-classification
  [{:keys [graph parameters user-info] :as request}]
  ;; classification of products
  )

(defn products-discovery
  [{:keys [graph parameters user-info] :as request}]
  (let [products (data/search-products graph (:query parameters))]
    ;; classification of products
    (if user-info
      (http-response/ok products)
      (http-response/ok (filter #(:is-published %) products)))))

(defn get-my-products
  [{:keys [graph user-info] :as request}]
  (if (and (not (end-user/is-admin? user-info))
           (not (end-user/is-user? user-info)))
    (http-response/invalid {:result :failure
                            :reason ::end-user/invalid-user})
    (http-response/ok (data/get-my-products graph user-info))))

(defn get-product
  [{:keys [graph parameters user-info] :as request}]
  (let [product (data/get-product-by-id graph (:path parameters))]
    (if (or (not product)
            (and (not (:is-published product))
                 (not (end-user/is-admin? user-info))
                 (not (end-user/is-user? user-info))))
      (http-response/not-found {:result :failure
                                :reason ::product/product-not-exists})
      (http-response/ok product))))

(defn delete-product
  [{:keys [graph parameters user-info] :as request}]
  (let [product (data/get-product-by-id graph (:path parameters))]
    (cond (and (not (end-user/is-admin? user-info))
               (not (end-user/is-current-user? user-info (:created-by product))))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          (nil? product)
          (http-response/not-found {:result :failure
                                    :reason ::product/product-not-exists})

          :else
          (do
            (data/delete-product-by-id graph (:path parameters))
            (http-response/ok {:result :success})))))
