(ns com.taxonomy.product.data
  (:require [clojure.string :as clj.str]
            [datascript.core :as d]))

(defn- ->tuple
  [id attribute value]
  [:db/add id attribute value])

(defn product->entity
  [product]
  (let [name-q (some-> (:name product)
                       clj.str/lower-case
                       (clj.str/replace #"\s" ""))]
    (as-> product $
          (assoc $ :name-q name-q)
          (assoc $ :published false)
          (map (fn [[k v]]
                 (->tuple (:id product) k v)))
          (into []))))

(defn create-product
  [graph product]
  (let [entity (product->entity product)]
    (d/transact! graph entity)))

(defn publish-product
  [graph {:keys [id] :as product}]
  (d/transact! graph [{:db/id     id
                       :published true}]))

(defn unpublish-product
  [graph {:keys [id] :as product}]
  (d/transact! graph [{:db/id     id
                       :published false}]))

(defn search-products
  [graph params]
  (let [products []]
    (filter #(:is-published %) products)))

(defn get-product-by-name
  [graph {:keys [name]}]
  (let [name (some-> name
                     clj.str/lower-case
                     (clj.str/replace #"\s" ""))]
    (d/q '[:find  (pull ?e [*])
           :where [?e :name-q name]] graph)))

(defn get-my-products
  [graph {:keys [username]}]
  (d/q '[:find  (pull ?e [*])
         :where [?e :created-by username]] graph))

(defn get-product-by-id
  [graph {:keys [id]}]
  (d/pull [graph '*' id]))

(defn get-products-by-id
  [graph ids]
  (d/pull-many [graph '*' ids]))

(defn get-all-products
  [graph]
  (d/pull [graph '*']))

(defn delete-product-by-id
  [graph {:keys [id]}]
  (d/transact! graph [[:db.fn/retractEntity id]]))
