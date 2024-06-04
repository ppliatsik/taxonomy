(ns com.taxonomy.system
  (:require [aero.core :as ac]
            [integrant.core :as ig]
            [clojure.spec.alpha :as s]))

(defmethod ac/reader 'ig/ref
  [opts tag value]
  (ig/ref value))

(defn load-config
  [filename]
  (when filename
    (ac/read-config filename)))

(s/def ::configuration
  (s/keys :req [:http/service :db/pg]))

(defn config [filename]
  (s/assert ::configuration (load-config filename)))
