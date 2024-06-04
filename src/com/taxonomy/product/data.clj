(ns com.taxonomy.product.data
  (:require [hugsql.core :as hugs]))

(hugs/def-db-fns "com/taxonomy/product/sql/product.sql")
(hugs/def-sqlvec-fns "com/taxonomy/product/sql/product.sql")
