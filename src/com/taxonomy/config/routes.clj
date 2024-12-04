(ns com.taxonomy.config.routes
  (:require [com.taxonomy.config :as config]
            [com.taxonomy.config.api :as api]))

(def routes
  [["/config"
    ["/security-mechanisms"
     {:swagger {:tags ["config"]}
      :name    ::config/security-mechanisms
      :get     {:summary "Get security-mechanisms"
                :handler api/get-security-mechanisms}}]
    ["/threats"
     {:swagger {:tags ["config"]}
      :name    ::config/threats
      :get     {:summary "Get threats"
                :handler api/get-threats}}]
    ["/products-choices"
     {:swagger {:tags ["config"]}
      :name    ::config/products-choices
      :get     {:summary "Get products choices"
                :handler api/get-products-choices}}]]])
