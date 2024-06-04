(ns com.taxonomy.end-user.data
  (:require [hugsql.core :as hugs]))

(hugs/def-db-fns "com/taxonomy/end_user/sql/end_user.sql")
(hugs/def-sqlvec-fns "com/taxonomy/end_user/sql/end_user.sql")
