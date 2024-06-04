(ns com.taxonomy.main
  (:require [clojure.tools.logging :as log]
            [integrant.core :as ig]
            [clojure.spec.alpha :as s]
            [com.taxonomy.system :as system]))

(def system nil)
(def config nil)

(defn- start-system!
  [current-config]
  (alter-var-root #'config (constantly current-config))
  (alter-var-root #'system (constantly (ig/init current-config))))

(defn- halt-system! []
  (ig/halt! system))

(defn -main
  [& [config-filename]]
  (log/info :msg "Starting...")
  (s/check-asserts true)
  (start-system! (system/load-config config-filename))
  (.addShutdownHook
    (Runtime/getRuntime)
    (Thread. #(do (log/info :msg "Stopping...")
                  (halt-system!)))))
