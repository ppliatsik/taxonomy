(ns com.taxonomy.http.http
  (:require [integrant.core :as ig]
            [clojure.pprint :as clj.pprint]
            [clojure.tools.logging :as log]
            [ring.adapter.jetty9 :as jetty]
            [com.taxonomy.http.routes :as routes]))

(defmethod ig/init-key :http/service [_ service-map]
  (log/info "Starting JETTY"
            (with-out-str (clj.pprint/pprint service-map)) "...")
  (jetty/run-jetty (routes/router service-map)
                   (-> service-map
                       (select-keys [:port :host])
                       (assoc :join? false))))

(defmethod ig/halt-key! :http/service [_ server]
  (log/info "Stopping JETTY")
  (.stop server))
