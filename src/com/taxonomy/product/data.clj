(ns com.taxonomy.product.data
  (:require [hugsql.core :as hugs]))

(hugs/def-db-fns "com/taxonomy/product/sql/product.sql")
(hugs/def-sqlvec-fns "com/taxonomy/product/sql/product.sql")

(def default-product
  {})

(defn create-product
  [db data]
  (let [product (create-product* db (merge default-product data))]
    product))
