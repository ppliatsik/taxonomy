(ns com.taxonomy.system
  (:require [aero.core :as ac]
            [integrant.core :as ig]
            [clojure.spec.alpha :as s]
            [buddy.core.keys :as bk]
            [java-time :as jt]
            [chime.core :as chime]
            [clojure.edn :as edn]
            [datascript.core :as d]
            [datascript.db :as db]
            [com.taxonomy.end-user.data :as end-user.data]
            [com.taxonomy.product.data :as product.data])
  (:import [java.lang AutoCloseable]
           [java.util UUID]))

(defmethod ac/reader 'ig/ref
  [opts tag value]
  (ig/ref value))

(defn load-config
  [filename]
  (when filename
    (ac/read-config filename)))

(s/def ::configuration
  (s/keys :req [:http/service :db/pg :auth/keys :invalid-token/scheduler]))

(defn config [filename]
  (s/assert ::configuration (load-config filename)))

(defmethod ig/init-key :auth/keys [_ {:keys [public-key-file
                                             private-key-file] :as cfg}]
  (-> cfg
      (assoc :public-key  (bk/public-key public-key-file)
             :private-key (bk/private-key private-key-file))
      (dissoc :public-key-file :private-key-file)))

(defmethod ig/halt-key! :auth/keys [_ _]
  )

(defmethod ig/init-key :email/host [_ cfg]
  cfg)

(defmethod ig/halt-key! :email/host [_ _]
  )

(defmethod ig/init-key :invalid-token/scheduler [_ {:keys [db hour minute enable] :as cfg}]
  (when enable
    (let [times     (chime/periodic-seq (-> (jt/local-time hour minute)
                                            (jt/zoned-date-time (jt/zone-id))
                                            (jt/instant))
                                        (jt/period 1 :days))
          scheduler (chime/chime-at times
                                    (fn [_]
                                      (end-user.data/delete-invalid-user-confirmation-token db)))]
      (assoc cfg :scheduler scheduler))))

(defmethod ig/halt-key! :invalid-token/scheduler [_ {:keys [scheduler]}]
  (when scheduler
    (.close ^AutoCloseable scheduler)))

(defmethod ig/init-key :graph/products [_ {:keys [products-file] :as cfg}]
  (let [products     (->> products-file
                          slurp
                          (edn/read-string {:readers *data-readers*}))
        _ (prn "products" products)
        initial-data (->> products
                          (mapcat (fn [product]
                                    (product.data/product->entity (-> product
                                                                      (assoc :id (str 1))
                                                                      (assoc :created-by "admin")))))
                          vec)
        _ (prn "initial-data" initial-data)
        graph        (-> (d/empty-db)
                         (d/db-with initial-data))]
    graph))

(defmethod ig/halt-key! :graph/products [_ _]
  )

(defmethod ig/init-key :er/security-mechanisms [_ {:keys [file]}]
  (let [sm (->> file
                slurp
                (edn/read-string {:readers *data-readers*}))]
    sm))

(defmethod ig/halt-key! :er/security-mechanisms [_ _]
  )

(defmethod ig/init-key :er/threats [_ {:keys [file]}]
  (let [threats (->> file
                     slurp
                     (edn/read-string {:readers *data-readers*}))]
    threats))

(defmethod ig/halt-key! :er/threats [_ _]
  )
