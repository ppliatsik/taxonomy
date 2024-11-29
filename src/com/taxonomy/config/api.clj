(ns com.taxonomy.config.api
  (:require [com.taxonomy.http.http-response :as http-response]))

(defn get-security-mechanisms
  [{:keys [security-mechanisms] :as req}]
  (http-response/ok security-mechanisms))

(defn get-threats
  [{:keys [threats] :as req}]
  (http-response/ok threats))
