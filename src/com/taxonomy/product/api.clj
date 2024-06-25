(ns com.taxonomy.product.api
  (:require [com.taxonomy.http.http-response :as http-response]
            [com.taxonomy.product.data :as data]))

(defn create-product
  [{:keys [db parameters] :as request}]
  )

(defn publish-product
  [{:keys [db parameters] :as request}]
  )

(defn unpublish-product
  [{:keys [db parameters] :as request}]
  )

(defn get-products
  [{:keys [db parameters] :as request}]
  )

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
