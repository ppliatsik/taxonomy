(ns com.taxonomy.end-user.data
  (:require [hugsql.core :as hugs]))

(hugs/def-db-fns "com/taxonomy/end_user/sql/end_user.sql")
(hugs/def-sqlvec-fns "com/taxonomy/end_user/sql/end_user.sql")

(defn create-user
  [db data]
  (let [user (create-user* db data)]
    (dissoc user :password)))

(defn activate-user
  [db data]
  (let [user (activate-user* db data)]
    (dissoc user :password)))

(defn activate-user-by-email
  [db data]
  (let [user (activate-user-by-email* db data)]
    (dissoc user :password)))

(defn deactivate-user
  [db data]
  (let [user (deactivate-user* db data)]
    (dissoc user :password)))

(defn change-user-password
  [db data]
  (let [user (change-user-password* db data)]
    (dissoc user :password)))

(defn update-user-info
  [db data]
  (let [user (update-user-info* db data)]
    (dissoc user :password)))

(defn get-user-by-username
  [db data]
  (let [user (get-user-by-username* db data)]
    (dissoc user :password)))
