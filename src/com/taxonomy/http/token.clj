(ns com.taxonomy.http.token
  (:require [buddy.sign.jwt :as jwt]
            [clojure.tools.logging :as log]
            [java-time :as jt]))

(defn sign
  [claim auth-keys token-valid-time]
  (jwt/sign (assoc claim :exp (-> (jt/local-date-time)
                                  (jt/plus (jt/minutes token-valid-time))
                                  (jt/to-sql-timestamp)))
            (:private-key auth-keys)
            {:alg :rs256}))

(defn unsign
  [token auth-keys]
  (try
    (jwt/unsign token (:public-key auth-keys) {:alg :rs256})
    (catch Exception ex
      (log/error "Error at token unsign" ex))))
