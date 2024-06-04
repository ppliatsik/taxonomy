(ns user
  (:require [clojure.tools.namespace.repl :as ns-tools]))

(ns-tools/disable-unload!)
(ns-tools/set-refresh-dirs "src" "dev" "test")

(defn dev []
  (require 'dev)
  (in-ns 'dev))

(defn fix
  []
  (let [res (ns-tools/refresh-all)]
    (when (instance? Exception res)
      (throw res))))
