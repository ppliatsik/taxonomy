(ns com.taxonomy.product.data
  (:require [clojure.string :as clj.str]
            [cheshire.core :as che]
            [clojure.data.json :as json])
  (:import [com.couchbase.client.java.document JsonDocument]
           [com.couchbase.client.java.document.json JsonArray JsonObject]
           [com.couchbase.client.java.query N1qlQuery]))

(def properties
  [:name :creator :published :description :delivery-methods
   :deployment-models :product-categories :cost-model :security-mechanisms
   :non-functional-guarantees :protection-types :security-properties
   :protected-items :threats :restrictions :open-source :freely-available
   :test-version :test-duration :product-interfaces :product-company :marketplaces :support])

(defn json-object->edn
  [data]
  (when data
    (let [result (reduce (fn [acc property]
                           (let [v (.get data (name property))]
                             (if (instance? JsonArray v)
                               (assoc acc property (vec (che/parse-string (str v) keyword)))
                               (assoc acc property (.get data (name property))))))
                         {}
                         properties)]
      (assoc result :id (.get data "id")))))

(defn edn->json-object
  [data]
  (when data
    (let [j (json/read-json (che/encode data))]
      (JsonObject/fromJson (json/json-str j)))))

(defn get-from-n1ql-result
  [result]
  (reduce (fn [acc row]
            (conj acc (json-object->edn (.value row))))
          []
          (.allRows result)))

(defn create-product
  [{:keys [bucket]} product]
  (let [name (some-> (:name product)
                     clj.str/lower-case
                     (clj.str/replace #"\s" ""))

        json-object   (edn->json-object (assoc product :name-q name))
        json-document (JsonDocument/create (:id product) json-object)]
    (.insert bucket json-document)
    product))

(defn publish-product
  [{:keys [bucket]} product]
  (let [json-object   (-> (edn->json-object product)
                          (.put "published" true))
        json-document (JsonDocument/create (:id product) json-object)
        new-doc       (.upsert bucket json-document)]
    (json-object->edn (.content new-doc))))

(defn unpublish-product
  [{:keys [bucket]} product]
  (let [json-object   (-> (edn->json-object product)
                          (.put "published" false))
        json-document (JsonDocument/create (:id product) json-object)
        new-doc       (.upsert bucket json-document)]
    (json-object->edn (.content new-doc))))

(defn get-query
  [params logical-operator]
  (let [params (reduce (fn [acc {:keys [property-name match-value]}]
                         (.put acc (name property-name) match-value))
                       (JsonObject/create)
                       params)
        query  "select p.* from products p where published = true"]
    (N1qlQuery/parameterized query params)))

(defn search-products
  [{:keys [bucket]} {:keys [params logical-operator]}]
  (let [query  (if (seq params)
                 (get-query params logical-operator)
                 (N1qlQuery/simple "select p.* from products p where published = true"))
        result (.query bucket query)]
    (get-from-n1ql-result result)))

(defn get-product-by-name
  [{:keys [bucket]} {:keys [name]}]
  (let [name   (some-> name
                       clj.str/lower-case
                       (clj.str/replace #"\s" ""))
        params (-> (JsonObject/create)
                   (.put "name" name))
        query  (N1qlQuery/parameterized "select p.* from products p where `name-q` = $name" params)
        result (.query bucket query)]
    (-> (get-from-n1ql-result result)
        first)))

(defn get-my-products
  [{:keys [bucket]} {:keys [username]}]
  (let [params (-> (JsonObject/create)
                   (.put "username" username))
        query  (N1qlQuery/parameterized "select p.* from products p where creator = $username" params)
        result (.query bucket query)]
    (get-from-n1ql-result result)))

(defn get-product-by-id
  [{:keys [bucket]} {:keys [id]}]
  (let [result (.get bucket id)]
    (json-object->edn (.content result))))

(defn get-products-by-id
  [{:keys [bucket]} ids]
  (let [params (-> (JsonObject/create)
                   (.put "ids" (JsonArray/from (into-array String ids))))
        query  (N1qlQuery/parameterized "select p.* from products p use keys $ids" params)
        result (.query bucket query)]
    (get-from-n1ql-result result)))

(defn get-all-products
  [{:keys [bucket]}]
  (let [query  (N1qlQuery/simple "select p.* from products p where published = true")
        result (.query bucket query)]
    (get-from-n1ql-result result)))

(defn delete-product-by-id
  [{:keys [bucket]} {:keys [id]}]
  (.remove bucket id))
