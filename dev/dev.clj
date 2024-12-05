(ns dev
  (:require [integrant.repl :as ig]
            [com.taxonomy.system :as system]))

(defn reset []
      (integrant.repl/set-prep! (constantly (system/load-config "resources/config.edn")))
      (ig/reset))

(defn halt [] (ig/halt))

(defn sys [] integrant.repl.state/system)
(defn db [] (:db/pg (sys)))
(defn couchbase [] (:db/couchbase (sys)))
