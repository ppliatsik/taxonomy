(ns com.taxonomy.product.data
  (:require [clojure.string :as clj.str]))

(defn create-product
  [products data]
  (swap! products assoc data)
  data)

(defn publish-product
  [products {:keys [id] :as product}]
  (let [new-products (->> @products
                          (map (fn [p]
                                 (if (= (:id p) id)
                                   (assoc p :is-published? true)
                                   p))))]
    (reset! products new-products)
    (->> @products
         (filter #(= id (:id %)))
         first)))

(defn unpublish-product
  [products {:keys [id] :as product}]
  (let [new-products (->> @products
                          (map (fn [p]
                                 (if (= (:id p) id)
                                   (assoc p :is-published? false)
                                   p))))]
    (reset! products new-products)
    (->> @products
         (filter #(= id (:id %)))
         first)))

(defn search-products
  [products params]
  )

(defn get-product-by-name
  [products {:keys [name]}]
  (let [name (some-> name
                     clj.str/lower-case
                     (clj.str/replace #"\s" ""))]
    (->> @products
         (filter #(= name (:name %)))
         first)))

(defn get-product-by-id
  [products {:keys [id]}]
  (->> @products
       (filter #(= id (:id %)))
       first))

(defn delete-product-by-id
  [products {:keys [id]}]
  (let [new-products (remove #(= id (:id %)) @products)]
    (reset! products new-products)))
