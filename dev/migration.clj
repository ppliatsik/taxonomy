(ns migration
  (:require [migratus.core :as migratus]))

(def db-info
  {:dbname    "taxonomy"
   :dbtype    "postgresql"
   :classname "org.postgresql.Driver"
   :port      15432
   :host      "localhost"
   :user      "test"
   :password  "test"})

(def config
  {:store                :database
   :migration-dir        "migrations/"
   :migration-table-name "migratus"
   :init-script          "init.sql"
   :db                   db-info})

(defn init
      []
      (migratus/init config))

(defn migrate
      []
      (migratus/migrate config))

(defn rollback
      []
      (migratus/rollback config))
