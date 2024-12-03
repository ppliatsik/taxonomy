(ns com.taxonomy.product.api
  (:require [com.taxonomy.http.http-response :as http-response]
            [com.taxonomy.product :as product]
            [com.taxonomy.product.data :as data]
            [com.taxonomy.end-user :as end-user])
  (:import [java.util UUID]))

(defn- get-min-max-values
  [products]
  (let [charge-packets                   (->> products
                                              (map #(get-in % [:cost-model :charge-packets]))
                                              sort)
        non-functional-guarantees-values (->> products
                                              (map #(get-in % [:non-functional-guarantees :value]))
                                              sort)
        restrictions-values              (->> products
                                              (map #(get-in % [:restrictions :value]))
                                              sort)
        test-durations                   (->> products
                                              (map #(:test-duration %))
                                              sort)]
    {:min-charge-packets                  (or (first charge-packets) 0M)
     :max-charge-packets                  (or (last charge-packets) 0M)
     :min-non-functional-guarantees-value (or (first non-functional-guarantees-values) 0M)
     :max-non-functional-guarantees-value (or (last non-functional-guarantees-values) 0M)
     :min-restrictions-value              (or (first restrictions-values) 0M)
     :max-restrictions-value              (or (last restrictions-values) 0M)
     :min-test-duration                   (or (first test-durations) 0M)
     :max-test-duration                   (or (last test-durations) 0M)}))

(defn- get-positive-direction-local-score
  "Formula for positive local score is: (x_{ij} - min_k(x_{ik})) / (max_k(x_{ik}) - min_k(x_{ik}))"
  [value min-value max-value]
  (if value
    (let [divisor (if (= 0M (- max-value min-value))
                    1M
                    (- max-value min-value))]
      (- value (/ min-value divisor)))
    0M))

(defn- get-negative-direction-local-score
  "Formula for negative local score is: (max_k(x_{ik}) - x_{ij}) / (max_k(x_{ik}) - min_k(x_{ik}))"
  [value min-value max-value]
  (if value
    (let [divisor (if (= 0M (- max-value min-value))
                    1M
                    (- max-value min-value))]
      (- (/ (- max-value value) divisor)))
    0M))

(defn- compute-score
  "Formula for score is: score_j = Sum_i (w_i * score_{ij}),
  where score_{ij} is the score of each property and w_i its weight."
  [product
   {:keys [charge-packets-w non-functional-guarantees-value-w restrictions-value-w test-duration-w]
    :or {charge-packets-w 0M non-functional-guarantees-value-w 0M restrictions-value-w 0M test-duration-w 0M}}
   min-max-values]
  (let [charge-packets-score            (* charge-packets-w
                                           (get-positive-direction-local-score
                                             (get-in product [:cost-model :charge-packets])
                                             (:min-charge-packets min-max-values)
                                             (:max-charge-packets min-max-values)))
        non-functional-guarantees-score (* non-functional-guarantees-value-w
                                           (if (get-in product [:non-functional-guarantees :direction-of-values])
                                             (get-positive-direction-local-score
                                               (get-in product [:non-functional-guarantees :value])
                                               (:min-non-functional-guarantees-value min-max-values)
                                               (:max-non-functional-guarantees-value min-max-values))
                                             (get-negative-direction-local-score
                                               (get-in product [:non-functional-guarantees :value])
                                               (:min-non-functional-guarantees-value min-max-values)
                                               (:max-non-functional-guarantees-value min-max-values))))
        restrictions-score              (* restrictions-value-w
                                           (if (get-in product [:restrictions :direction-of-values])
                                             (get-positive-direction-local-score
                                               (get-in product [:restrictions :value])
                                               (:min-restrictions-value min-max-values)
                                               (:max-restrictions-value min-max-values))
                                             (get-negative-direction-local-score
                                               (get-in product [:restrictions :value])
                                               (:min-restrictions-value min-max-values)
                                               (:max-restrictions-value min-max-values))))
        test-duration-score             (* test-duration-w
                                           (get-positive-direction-local-score
                                             (:test-duration product)
                                             (:min-test-duration min-max-values)
                                             (:max-test-duration min-max-values)))]
    (+ charge-packets-score non-functional-guarantees-score restrictions-score test-duration-score)))

(defn- classify-products
  [products weights]
  (let [min-max-values (get-min-max-values products)]
    (->> products
         (map (fn [product]
                (let [score (compute-score product weights min-max-values)]
                  (assoc product :score score))))
         (sort-by :score)
         vec)))

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
    (cond (not (end-user/is-current-user? user-info (:created-by product)))
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
    (cond (not (end-user/is-current-user? user-info (:created-by product)))
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
  (let [weights (-> parameters :body :weights)
        total-w (->> weights
                     vals
                     (remove nil?)
                     (reduce +))]
    (cond (not= 1M total-w)
          (http-response/invalid {:result :failure
                                  :reason ::product/wrong-weights})

          :else
          (let [products (if (seq (-> parameters :body :ids))
                           (data/get-products-by-id graph (-> parameters :body :ids))
                           (data/get-all-products graph))
                products (classify-products products weights)]
            (if user-info
              (http-response/ok products)
              (http-response/ok (->> products (take guest-products-limit) vec)))))))

(defn products-discovery
  [{:keys [graph parameters user-info guest-products-limit] :as request}]
  (let [weights (-> parameters :body :weights)
        total-w (->> weights
                     vals
                     (remove nil?)
                     (reduce +))]
    (cond (not= 1M total-w)
          (http-response/invalid {:result :failure
                                  :reason ::product/wrong-weights})

          :else
          (let [products (-> (data/search-products graph (:query parameters))
                             (classify-products weights))]
            (if user-info
              (http-response/ok products)
              (http-response/ok (->> products (take guest-products-limit) vec)))))))

(defn get-my-products
  [{:keys [graph user-info] :as request}]
  (if-not user-info
    (http-response/invalid {:result :failure
                            :reason ::end-user/invalid-user})
    (http-response/ok (data/get-my-products graph user-info))))

(defn get-product
  [{:keys [graph parameters user-info] :as request}]
  (let [product (data/get-product-by-id graph (:path parameters))]
    (if (or (not product)
            (and (not (:published product))
                 (not (end-user/is-current-user? user-info (:created-by product)))))
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
