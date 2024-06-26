(ns com.taxonomy.product.data
  (:require [hugsql.core :as hugs]
            [clojure.string :as clj.str]))

(hugs/def-db-fns "com/taxonomy/product/sql/product.sql")
(hugs/def-sqlvec-fns "com/taxonomy/product/sql/product.sql")

(def default-product
  {})

(defn create-product
  [db data]
  (let [product (create-product* db (merge default-product data))]
    product))

(defn get-product-by-name
  [db params]
  (let [data (some-> (:name params)
                     clj.str/lower-case
                     (clj.str/replace #"\s" ""))]
    (get-product-by-name* db {:name data})))
