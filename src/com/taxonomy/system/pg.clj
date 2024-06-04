(ns com.taxonomy.system.pg
  (:require [integrant.core :as ig]
            [hikari-cp.core :refer :all]
            [clojure.tools.logging :as log]
            [hugsql.core :as hugs]
            [hugsql.adapter :as adapter]
            [clj-postgresql.core]
            [clj-postgresql.types :as pg-types]
            [clj-postgresql.spatial]
            [cheshire.generate]
            [java-time :as jt]
            [cheshire.core :as json]
            [camel-snake-kebab.core :as csk]
            [clojure.walk :as walk]
            [hugsql.parameters]
            [clojure.java.jdbc :as jdbc]
            [com.taxonomy.util :as util])
  (:import [java.sql PreparedStatement]))

(defmethod ig/init-key :db/pg [_ spec]
  (log/info "Starting PostgreSQL connection pool")
  {:datasource (make-datasource spec)})

(defmethod ig/halt-key! :db/pg [_ pool]
  (log/info "Stopping PostgreSQL connection pool")
  (close-datasource (:datasource pool)))

(defmethod pg-types/read-pgobject :jsonb
  [^org.postgresql.util.PGobject x]
  (when-let [val (.getValue x)]
    (json/parse-string val keyword)))

(cheshire.generate/add-encoder
  java.time.ZonedDateTime
  (fn [c jsonGenerator]
    (.writeString jsonGenerator
                  (str c))))

(cheshire.generate/add-encoder
  java.time.LocalDate
  (fn [c jsonGenerator]
    (.writeString jsonGenerator
                  (str c))))

(defn ->kebab-case-keyword* [k]
  (cond
    (not (keyword? k)) (csk/->kebab-case k)
    (not (namespace k)) (csk/->kebab-case-keyword k)
    (namespace k) (let [nn (namespace k)
                        kk (name k)]
                    (keyword (csk/->kebab-case nn) (csk/->kebab-case kk)))
    :else (csk/->kebab-case k)))

(defn- result->kebab0 [res]
  (walk/postwalk (fn [x] (if (map? x)
                           (reduce-kv (fn [m k v]
                                        (assoc m (->kebab-case-keyword* k) v))
                                      {}
                                      x)
                           x))
                 res))

(defn result-one->kebab
  [this result options]
  (result->kebab0 (adapter/result-one this result options)))

(defn result-many->kebab
  [this result options]
  (->> (adapter/result-many this result options)
       (map result->kebab0)
       vec))

(defn result-raw->kebab
  [this result options]
  (->> (adapter/result-raw this result options)
       (map csk/->kebab-case)))

(defn- ->s-flex
  "Converts (recursively for a sequence) to snake-case string(s)."
  [id-or-ids]
  (if (coll? id-or-ids)
    (map ->s-flex id-or-ids)
    (csk/->snake_case (name id-or-ids))))

(defmethod hugs/hugsql-result-fn :one [sym] 'com.taxonomy.system.pg/result-one->kebab)

(defmethod hugs/hugsql-result-fn :1 [sym] 'com.taxonomy.system.pg/result-one->kebab)

(defmethod hugs/hugsql-result-fn :many [sym] 'com.taxonomy.system.pg/result-many->kebab)

(defmethod hugs/hugsql-result-fn :* [sym] 'com.taxonomy.system.pg/result-many->kebab)

(defmethod hugsql.parameters/apply-hugsql-param :i
  [param data options]
  (let [data (update data (:name param) ->s-flex)]
    (hugsql.parameters/identifier-param param data options)))

(defmethod hugsql.parameters/apply-hugsql-param :i*
  [param data options]
  (let [data (update data (:name param) ->s-flex)]
    (hugsql.parameters/identifier-param-list param data options)))

(defmethod hugs/hugsql-result-fn :default [sym] 'com.taxonomy.system.pg/result-raw->kebab)

(defmethod hugs/hugsql-result-fn :raw [sym] 'com.taxonomy.system.pg/result-raw->kebab)

(extend-protocol jdbc/ISQLValue
  clojure.lang.Keyword
  (sql-value [v] (util/keyword->string v)))

(extend-protocol jdbc/ISQLValue
  java.util.Date
  (sql-value [v]
    (jt/sql-timestamp (jt/zoned-date-time v (jt/zone-id)))))

(extend-protocol jdbc/ISQLValue
  java.sql.Date
  (sql-value [v]
    (jt/sql-timestamp v)))

(extend-protocol jdbc/IResultSetReadColumn
  java.sql.Time
  (result-set-read-column [val rsmeta idx]
    (jt/local-time val)))

(extend-protocol jdbc/ISQLParameter
  java.sql.Timestamp
  (set-parameter [v ^PreparedStatement s ^long i]
    (.setTimestamp s i v)))

(extend-protocol jdbc/ISQLValue
  java.time.ZonedDateTime
  (sql-value [v] (jt/sql-timestamp v)))
