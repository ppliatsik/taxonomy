(ns com.taxonomy.product.api
  (:require [com.taxonomy.http.http-response :as http-response]
            [com.taxonomy.product :as product]
            [com.taxonomy.product.data :as data]))

(def default-limit 10)
(def default-offset 0)

(defn create-product
  [{:keys [db parameters] :as request}]
  (let [product (data/create-product db (:body parameters))]
    (http-response/ok {:result  :success
                       :payload product})))

(defn publish-product
  [{:keys [db parameters] :as request}]
  (let [product (data/get-product-by-id db (:path parameters))]
    (if product
      (http-response/ok {:result  :success
                         :payload (data/publish-product db product)})
      (http-response/not-found {:result ::product/product-not-exists}))))

(defn unpublish-product
  [{:keys [db parameters] :as request}]
  (let [product (data/get-product-by-id db (:path parameters))]
    (if product
      (http-response/ok {:result  :success
                         :payload (data/unpublish-product db product)})
      (http-response/not-found {:result ::product/product-not-exists}))))

(defn get-products
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
    (http-response/one-or-404 product)))

(defn delete-product
  [{:keys [db parameters] :as request}]
  (let [_ (data/delete-product db (:path parameters))]
    (http-response/ok {:result :success})))
