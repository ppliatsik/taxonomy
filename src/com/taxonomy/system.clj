(ns com.taxonomy.system
  (:require [aero.core :as ac]
            [integrant.core :as ig]
            [clojure.spec.alpha :as s]
            [buddy.core.keys :as bk]
            [java-time :as jt]
            [chime.core :as chime]
            [clojure.edn :as edn]
            [com.taxonomy.end-user.data :as end-user.data])
  (:import [java.lang AutoCloseable]))

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
  (let [products      (->> products-file
                           slurp
                           (edn/read-string {:readers *data-readers*}))
        products-atom (atom {})]
    (reset! products-atom products)
    products-atom))

(defmethod ig/halt-key! :graph/products [_ _]
  )
