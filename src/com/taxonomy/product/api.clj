(ns com.taxonomy.product.api
  (:require [clojure.string :as clj.str]
            [com.taxonomy.http.http-response :as http-response]
            [com.taxonomy.product :as product]
            [com.taxonomy.product.data :as data]
            [com.taxonomy.end-user :as end-user])
  (:import [java.util UUID]))

(defn- get-min-max-values
  [products]
  (let [property-fn (fn [prop]
                      (->> products
                           (map #(get % prop))
                           (map #(select-keys % [:property :value]))
                           (group-by :property)
                           (reduce-kv (fn [m k v]
                                        (let [new-v (->> v
                                                         (map :value)
                                                         sort)]
                                          (assoc m (clj.str/replace k #"\s" "") new-v)))
                                      {})))]
    {:non-functional-guarantees-values (property-fn :non-functional-guarantees)
     :restrictions-values              (property-fn :restrictions)
     :test-durations                   (->> products
                                            (map #(:test-duration %))
                                            sort)}))

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
   {:keys [non-functional-guarantees-w restrictions-w test-duration-w]
    :or {test-duration-w 0M}}
   {:keys [non-functional-guarantees-values restrictions-values test-durations]}]
  (let [score-fn (fn [data weights values]
                   (->> data
                        (map (fn [res]
                               (when-let [w (get weights (clj.str/replace (:property res) #"\s" ""))]
                                 (let [min-value (or (first (get values (clj.str/replace (:property res) #"\s" ""))) 0M)
                                       max-value (or (last (get values (clj.str/replace (:property res) #"\s" ""))) 0M)
                                       v         (if (:direction-of-values res)
                                                   (get-positive-direction-local-score (:value res) min-value max-value)
                                                   (get-negative-direction-local-score (:value res) min-value max-value))]
                                   (* w v)))))
                        (remove nil?)
                        (reduce +)))

        non-functional-guarantees-score (score-fn (:non-functional-guarantees product)
                                                  non-functional-guarantees-w
                                                  non-functional-guarantees-values)
        restrictions-score              (score-fn (:restrictions product) restrictions-w restrictions-values)
        test-duration-score             (* test-duration-w
                                           (get-positive-direction-local-score
                                             (:test-duration product)
                                             (or (first test-durations) 0M)
                                             (or (last test-durations) 0M)))]
    (+ non-functional-guarantees-score restrictions-score test-duration-score)))

(defn- classify-products
  [products weights]
  (let [min-max-values (get-min-max-values products)]
    (->> products
         (map (fn [product]
                (let [score (compute-score product weights min-max-values)]
                  (assoc product :score score))))
         (sort-by :score)
         vec)))

(defn get-total-weight
  [weights]
  (+ (as-> (:non-functional-guarantees-w weights) $
           (map :value $)
           (reduce + $)
           (or $ 0M))
     (as-> (:restrictions-w weights) $
           (map :value $)
           (reduce + $)
           (or $ 0M))
     (or (:test-duration-w weights) 0.0)))

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
  (let [logical-operator (if (or (not user-info)
                                 (not (-> parameters :body :logical-operator)))
                           "AND" (-> parameters :body :logical-operator))
        products         (data/search-products graph (-> parameters :body :criteria) logical-operator)]
    (if user-info
      (http-response/ok products)
      (http-response/ok (->> products (take guest-products-limit) vec)))))

(defn products-classification
  [{:keys [graph parameters user-info guest-products-limit] :as request}]
  (let [weights (-> parameters :body :weights)
        total-w (get-total-weight weights)]
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
        total-w (get-total-weight weights)]
    (cond (not= 1M total-w)
          (http-response/invalid {:result :failure
                                  :reason ::product/wrong-weights})

          :else
          (let [logical-operator (if (or (not user-info)
                                         (not (-> parameters :body :logical-operator)))
                                   "AND" (-> parameters :body :logical-operator))
                products         (-> (data/search-products graph (-> parameters :body :criteria) logical-operator)
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
