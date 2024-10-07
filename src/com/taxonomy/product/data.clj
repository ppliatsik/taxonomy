(ns com.taxonomy.product.data
  (:require [clojure.string :as clj.str]))

(defn create-product
  [db data]
  )

(defn get-product-by-name
  [db params]
  (let [data (some-> (:name params)
                     clj.str/lower-case
                     (clj.str/replace #"\s" ""))]
    ))
