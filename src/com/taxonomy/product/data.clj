(ns com.taxonomy.product.data
  (:require [clojure.string :as clj.str]
            [datascript.core :as d]))

(defn product->entity
  [product]
  (let [name-q (some-> (:name product)
                       clj.str/lower-case
                       (clj.str/replace #"\s" ""))]
    [[:db/add (:id product) :created-by (:created-by product)]
     [:db/add (:id product) :name (:name product)]
     [:db/add (:id product) :name-q name-q]
     [:db/add (:id product) :is-published false]
     [:db/add (:id product) :product-company (:product-company product)]
     ]))

(defn create-product
  [graph product]
  (let [entity (product->entity product)]
    (d/transact! graph entity)))

(defn publish-product
  [graph {:keys [id] :as product}]
  (d/transact! graph [{:db/id        id
                       :is-published true}]))

(defn unpublish-product
  [graph {:keys [id] :as product}]
  (d/transact! graph [{:db/id        id
                       :is-published false}]))

(defn search-products
  [graph params]
  )

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
  (d/entity graph id))

(defn delete-product-by-id
  [graph {:keys [id]}]
  (d/transact! graph [[:db.fn/retractEntity id]]))
