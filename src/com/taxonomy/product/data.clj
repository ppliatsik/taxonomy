(ns com.taxonomy.product.data
  (:require [clojure.string :as clj.str])
  (:import [com.couchbase.client.java.document JsonDocument]
           [com.couchbase.client.java.document.json JsonArray JsonObject]
           [com.couchbase.client.java.query N1qlQuery]))

(def properties
  [:name :created-by :published :description :delivery-methods
   :deployment-models :product-categories :cost-model :security-mechanisms
   :non-functional-guarantees :protection-types :security-properties
   :protected-items :threats :restrictions :open-source :freely-available
   :test-version :test-duration :product-interfaces :product-company :marketplaces :support])

(defn json-object->edn
  [data]
  (when data
    (let [result (->> properties
                      (reduce (fn [acc property]
                                (assoc acc property (.get data (name property))))
                              {}))]
      (assoc result :id (.get data "id")))))

(defn edn->json-object
  [data]
  (when data
    (let [json-object (JsonObject/create)]
      (->> properties
           (map (fn [property]
                  (.put json-object (name property) (get data property)))))
      json-object)))

(defn get-from-n1ql-result
  [result]
  (->> (.allRows result)
       (map (fn [row]
              (json-object->edn (.value row))))))

(defn create-product
  [{:keys [bucket]} product]
  (let [name (some-> name
                     clj.str/lower-case
                     (clj.str/replace #"\s" ""))

        json-object   (edn->json-object (assoc product :name-q name))
        json-document (.create JsonDocument (:id product) json-object)]
    (.insert bucket json-document)
    product))

(defn publish-product
  [{:keys [bucket]} product]
  (let [json-object   (-> (edn->json-object product)
                         (.put "published" true))
        json-document (.create JsonDocument (:id product) json-object)
        new-doc       (.upsert bucket json-document)]
    (json-object->edn (.content new-doc))))

(defn unpublish-product
  [{:keys [bucket]} product]
  (let [json-object   (-> (edn->json-object product)
                          (.put "published" false))
        json-document (.create JsonDocument (:id product) json-object)
        new-doc       (.upsert bucket json-document)]
    (json-object->edn (.content new-doc))))

(defn search-products
  [{:keys [bucket]} params logical-operator]
  (let [params (JsonObject/create)
        query  (N1qlQuery/parameterized "select * from products where published = true" params)
        result (.query bucket query)]
    (get-from-n1ql-result result)))

(defn get-product-by-name
  [{:keys [bucket]} {:keys [name]}]
  (let [name   (some-> name
                       clj.str/lower-case
                       (clj.str/replace #"\s" ""))
        params (-> (JsonObject/create)
                   (.put "username" name))
        query  (N1qlQuery/parameterized "select * from products where name-q = $name" params)
        result (.query bucket query)]
    (-> (get-from-n1ql-result result)
        first)))

(defn get-my-products
  [{:keys [bucket]} {:keys [username]}]
  (let [params (-> (JsonObject/create)
                   (.put "username" username))
        query  (N1qlQuery/parameterized "select * from products where created-by = $username" params)
        result (.query bucket query)]
    (get-from-n1ql-result result)))

(defn get-product-by-id
  [{:keys [bucket]} {:keys [id]}]
  (let [result (.get bucket id)]
    (json-object->edn (.content result))))

(defn get-products-by-id
  [{:keys [bucket]} ids]
  (let [params (-> (JsonObject/create)
                   (.put "ids" (JsonArray/from ids)))
        query  (N1qlQuery/parameterized "select * from products use keys $ids" params)
        result (.query bucket query)]
    (get-from-n1ql-result result)))

(defn get-all-products
  [{:keys [bucket]}]
  (let [query  (N1qlQuery/simple "select * from products where published = true")
        result (.query bucket query)]
    (get-from-n1ql-result result)))

(defn delete-product-by-id
  [{:keys [bucket]} {:keys [id]}]
  (.remove bucket id))
