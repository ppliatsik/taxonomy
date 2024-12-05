(ns com.taxonomy.product.data
  (:require [clojure.string :as clj.str]))

(defn json-document->edn
  [json-document]
  (when json-document
    ))

(defn edn->json-object
  [edn]
  (when edn
    ))

(defn create-product
  [{:keys [bucket]} product]
  )

(defn publish-product
  [couchbase {:keys [id] :as product}]
  )

(defn unpublish-product
  [couchbase {:keys [id] :as product}]
  )

(defn search-products
  [{:keys [cluster]} params logical-operator]
  )

(defn get-product-by-name
  [{:keys [cluster]} {:keys [name]}]
  (let [name   (some-> name
                       clj.str/lower-case
                       (clj.str/replace #"\s" ""))
        result (.query cluster "select * from products where name-q = ")]
    ))

(defn get-my-products
  [{:keys [cluster]} {:keys [username]}]
  )

(defn get-product-by-id
  [{:keys [bucket]} {:keys [id]}]
  (let [result (.get bucket id)]
    (json-document->edn result)))

(defn get-products-by-id
  [{:keys [cluster]} ids]
  )

(defn get-all-products
  [{:keys [cluster]}]
  (let [result (.query cluster "select * from products")]
    ))

(defn delete-product-by-id
  [{:keys [bucket]} {:keys [id]}]
  (.remove bucket id))
