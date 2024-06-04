(ns com.taxonomy.http.routes
  (:require [reitit.ring :as ring]
            [reitit.coercion.spec]
            [expound.alpha :as expound]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.ring.coercion :as coercion]
            [reitit.dev.pretty :as pretty]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.parameters :as parameters]
            [ring.middleware.cookies :as cookies]
            [ring.middleware.session :as session]
            [reitit.ring.middleware.dev]
            [reitit.ring.spec :as spec]
            [muuntaja.core :as m]
            [clojure.stacktrace :as stacktrace]
            [clojure.tools.logging :as log]
            [com.taxonomy.http.middleware :as mw])
  (:import [java.util Date]))

(defn- session-store-options
  []
  (let [opts {:cookie-name  "taxonomy-session-id"
              :cookie-attrs {:http-only true}}]
    (log/info "Session store options" opts)
    opts))

(defn ensure-session
  [handler tag]
  (fn [req]
    (log/trace "ensure-session for request / tag" tag)
    (if (and (:session req) (not (empty? (:session req))))
      (do
        (log/trace ">> session already exists for tag" tag " => " (:session req))
        (handler req))
      (do
        (log/trace "-- session missing for tag" tag " => creating new session")
        (assoc (handler req) :session {:created-at (.getTime (Date.))})))))

(defn coercion-error-handler
  [status]
  (let [printer          (expound/custom-printer {:theme :figwheel-theme, :print-specs? true})
        coercion-handler (exception/create-coercion-handler status)
        handlers         {:dev     coercion-handler
                          :staging coercion-handler
                          :prod    (fn [_ _]
                                     {:status status
                                      :reason :invalid-payload})}]
    (fn [exception {:keys [build] :as request}]
      (printer (-> exception ex-data :problems))
      (let [handler (get handlers build coercion-handler)]
        (handler exception request)))))

(def coerce-options
  {:coerce-response? reitit.coercion.spec/coerce-response?
   :transformers     {:body     {:default reitit.coercion.spec/string-transformer
                                 :formats {"application/json" reitit.coercion.spec/json-transformer}}
                      :string   {:default reitit.coercion.spec/string-transformer}
                      :response {:default reitit.coercion.spec/no-op-transformer}}})

(def muuntaja-instance
  (m/create m/default-options))

(defn- standard-middleware
  [service-map]
  [;; inject the db
   (mw/inject-system service-map)
   ;; auth & cookies
   cookies/wrap-cookies
   ;; swagger feature
   swagger/swagger-feature
   ;; query-params & form-params
   parameters/parameters-middleware
   ;; content-negotiation
   muuntaja/format-negotiate-middleware
   ;; encoding response body
   muuntaja/format-response-middleware
   ;; decoding request body ;; moved before exception, so that body is read
   muuntaja/format-request-middleware
   ;; exception handling
   (exception/create-exception-middleware
     (merge exception/default-handlers
            {::exception/wrap (fn [handler e request]
                                (stacktrace/print-stack-trace e)
                                (println "PARAMETERS")
                                (clojure.pprint/pprint
                                  (select-keys request [:body-params :path-params :query-params]))
                                (handler e request))}
            {:reitit.coercion/request-coercion  (coercion-error-handler 400)
             :reitit.coercion/response-coercion (coercion-error-handler 500)}))
   ;; coercing request parameters
   coercion/coerce-request-middleware
   ;; coercing response bodys
   coercion/coerce-response-middleware
   ;; multipart
   multipart/multipart-middleware])

(defn router
  [service-map]
  (ring/ring-handler
    (ring/router
      [["/swagger/swagger.json"
        {:get {:no-doc  true
               :swagger {:info {:title       "Sample Taxonomy API"
                                :description "With pedestal & reitit-http"}}
               :handler (swagger/create-swagger-handler)}}]
       ;; Main API routes
       ["/api" {:middleware (standard-middleware service-map)}
        ]]

      ;; A Load of reitit-interceptors
      {;;:reitit.middleware/transform reitit.ring.middleware.dev/print-request-diffs ;; pretty diffs
       :validate  spec/validate ;; enable spec validation for route data
       :exception pretty/exception
       :data      {:coercion   (reitit.coercion.spec/create coerce-options)
                   :muuntaja   muuntaja-instance}})

    ;; optional default ring handler (if no routes have matched)
    (ring/routes
      (swagger-ui/create-swagger-ui-handler
        {:url    "/swagger/swagger.json"
         :path   "/swagger/"
         :config {:validatorUrl     nil
                  :operationsSorter "alpha"}})
      (ring/create-default-handler))

    ;; Session middleware
    ;; See: https://github.com/metosin/reitit/issues/205
    {:middleware [[session/wrap-session (session-store-options)]
                  [ensure-session "main-api"]]}))
