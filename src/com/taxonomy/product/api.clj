(ns com.taxonomy.product.api
  (:require [com.taxonomy.http.http-response :as http-response]
            [com.taxonomy.product :as product]
            [com.taxonomy.product.data :as data]
            [com.taxonomy.end-user :as end-user])
  (:import [java.util UUID]))

(defn- compute-score
  [product weights]
  0)

(defn- classify-products
  [products weights]
  (->> products
       (map (fn [product]
              (let [score (compute-score product weights)]
                (assoc product :score score))))
       (sort-by :score)
       vec))

(defn create-product
  [{:keys [graph parameters user-info] :as request}]
  (cond (and (not (end-user/is-admin? user-info))
             (not (end-user/is-user? user-info)))
        (http-response/invalid {:result :failure
                                :reason ::end-user/invalid-user})

        (data/get-product-by-name graph (:body parameters))
        (http-response/invalid {:result :failure
                                :reason ::product/product-already-exists})

        :else
        (let [data (-> (:body parameters)
                       (assoc :id (str (UUID/randomUUID)))
                       (assoc :created-by (:username user-info)))]
          (http-response/ok {:result  :success
                             :payload (data/create-product graph data)}))))

(defn publish-product
  [{:keys [graph parameters user-info] :as request}]
  (let [product (data/get-product-by-id graph (:path parameters))]
    (cond (and (not (end-user/is-admin? user-info))
               (not (end-user/is-current-user? user-info (:created-by product))))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          (nil? product)
          (http-response/not-found {:result :failure
                                    :reason ::product/product-not-exists})

          :else
          (http-response/ok {:result  :success
                             :payload (data/publish-product graph product)}))))

(defn unpublish-product
  [{:keys [products parameters user-info] :as request}]
  (let [product (data/get-product-by-id products (:path parameters))]
    (cond (and (not (end-user/is-admin? user-info))
               (not (end-user/is-current-user? user-info (:created-by product))))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          (nil? product)
          (http-response/not-found {:result :failure
                                    :reason ::product/product-not-exists})

          :else
          (http-response/ok {:result  :success
                             :payload (data/unpublish-product products product)}))))

(defn products-match
  [{:keys [graph parameters user-info guest-products-limit] :as request}]
  (let [products (data/search-products graph (:query parameters))]
    (if user-info
      (http-response/ok products)
      (http-response/ok (->> products (take guest-products-limit) vec)))))

(defn products-classification
  [{:keys [graph parameters user-info guest-products-limit] :as request}]
  (let [products (if (seq (-> parameters :body :ids))
                   (data/get-products-by-id graph (-> parameters :body :ids))
                   (data/get-all-products graph))
        products (classify-products products (-> parameters :body :weights))]
    (if user-info
      (http-response/ok products)
      (http-response/ok (->> products (take guest-products-limit) vec)))))

(defn products-discovery
  [{:keys [graph parameters user-info guest-products-limit] :as request}]
  (let [products (-> (data/search-products graph (:query parameters))
                     (classify-products (-> parameters :body :weights)))]
    (if user-info
      (http-response/ok products)
      (http-response/ok (->> products (take guest-products-limit) vec)))))

(defn get-my-products
  [{:keys [graph user-info] :as request}]
  (if (and (not (end-user/is-admin? user-info))
           (not (end-user/is-user? user-info)))
    (http-response/invalid {:result :failure
                            :reason ::end-user/invalid-user})
    (http-response/ok (data/get-my-products graph user-info))))

(defn get-product
  [{:keys [graph parameters user-info] :as request}]
  (let [product (data/get-product-by-id graph (:path parameters))]
    (if (or (not product)
            (and (not (:is-published product))
                 (not (end-user/is-admin? user-info))
                 (not (end-user/is-user? user-info))))
      (http-response/not-found {:result :failure
                                :reason ::product/product-not-exists})
      (http-response/ok product))))

(defn delete-product
  [{:keys [graph parameters user-info] :as request}]
  (let [product (data/get-product-by-id graph (:path parameters))]
    (cond (and (not (end-user/is-admin? user-info))
               (not (end-user/is-current-user? user-info (:created-by product))))
          (http-response/invalid {:result :failure
                                  :reason ::end-user/invalid-user})

          (nil? product)
          (http-response/not-found {:result :failure
                                    :reason ::product/product-not-exists})

          :else
          (do
            (data/delete-product-by-id graph (:path parameters))
            (http-response/ok {:result :success})))))
