(ns com.taxonomy.product.ui.list.model
  (:require [re-frame.core :as rf]
            [clojure.spec.alpha :as s]
            [spec-tools.core :as st]
            [com.taxonomy.product :as product]))

(def paths [:ui/forms ::product/list :data])
(def data-path (rf/path [:ui/forms ::product/list :data]))
(def metadata
  {:data-path paths})

(defn check-fields
  [data]
  (assoc data :correct-inputs
              (and (s/valid? ::product/non-functional-guarantees-w
                             (st/coerce ::product/non-functional-guarantees-w (:non-functional-guarantees data) st/string-transformer))
                   (s/valid? ::product/restrictions-w
                             (st/coerce ::product/restrictions-w (:restrictions data) st/string-transformer))
                   (s/valid? ::product/test-duration-w
                             (st/coerce ::product/weight (:test-duration data) st/string-transformer))
                   (= 1.0 (+ (as-> (:non-functional-guarantees data) $
                                   (map :weight $)
                                   (map #(st/coerce ::product/weight % st/string-transformer) $)
                                   (reduce + $)
                                   (or $ 0M))
                             (as-> (:restrictions data) $
                                   (map :weight $)
                                   (map #(st/coerce ::product/weight % st/string-transformer) $)
                                   (reduce + $)
                                   (or $ 0M))
                             (or (st/coerce ::product/weight (:test-duration data) st/string-transformer) 0.0))))))

(defn get-criteria
  [db]
  (-> db :criteria vals vec))

(defn get-weights
  [db]
  {:non-functional-guarantees-w (:non-functional-guarantees db)
   :restrictions-w              (:restrictions db)
   :test-duration-w             (:test-duration db)})

(rf/reg-event-fx
  ::init
  [data-path]
  (fn [_ _]
    {:db {:criteria {}}
     :fx [[::get-security-mechanisms]
          [::get-threats]
          [::get-products-choices]]}))

(rf/reg-event-fx
  ::get-security-mechanisms
  [data-path]
  (fn [_ _]
    :fx [[:dispatch [:ajax/get {:uri     "/api/config/security-mechanisms"
                                :success ::get-security-mechanisms-success
                                :failure ::get-security-mechanisms-failure}]]]))

(rf/reg-event-db
  ::get-security-mechanisms-success
  [data-path]
  (fn [db [_ response]]
    (assoc db :security-mechanisms response)))

(rf/reg-event-fx
  ::get-security-mechanisms-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::get-threats
  [data-path]
  (fn [_ _]
    :fx [[:dispatch [:ajax/get {:uri     "/api/config/threats"
                                :success ::get-threats-success
                                :failure ::get-threats-failure}]]]))

(rf/reg-event-db
  ::get-threats-success
  [data-path]
  (fn [db [_ response]]
    (assoc db :threats response)))

(rf/reg-event-fx
  ::get-threats-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::get-products-choices
  [data-path]
  (fn [_ _]
    :fx [[:dispatch [:ajax/get {:uri     "/api/config/products-choices"
                                :success ::get-products-choices-success
                                :failure ::get-products-choices-failure}]]]))

(rf/reg-event-db
  ::get-products-choices-success
  [data-path]
  (fn [db [_ response]]
    (assoc db :products-choices response)))

(rf/reg-event-fx
  ::get-products-choices-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::match
  [data-path]
  (fn [{:keys [db]} _]
    (let [params {:criteria (get-criteria db)}]
      {:fx [[:dispatch [:ajax/post {:uri     "/api/products-match"
                                    :params  params
                                    :success ::match-success
                                    :failure ::match-failure}]]]})))

(rf/reg-event-db
  ::match-success
  [data-path]
  (fn [db [_ response]]
    (assoc db :products response)))

(rf/reg-event-fx
  ::match-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::classification
  [data-path]
  (fn [{:keys [db]} _]
    (let [params {:weights (get-weights db)
                  :ids     (->> db :products (map :id) vec)}]
      {:fx [[:dispatch [:ajax/post {:uri     "/api/products-classification"
                                    :params  params
                                    :success ::classification-success
                                    :failure ::classification-failure}]]]})))

(rf/reg-event-db
  ::classification-success
  [data-path]
  (fn [db [_ response]]
    (assoc db :products response)))

(rf/reg-event-fx
  ::classification-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::discovery
  [data-path]
  (fn [{:keys [db]} _]
    (let [params {:criteria (get-criteria db)
                  :weights  (get-weights db)}]
      {:fx [[:dispatch [:ajax/post {:uri     "/api/products-discovery"
                                    :params  params
                                    :success ::discovery-success
                                    :failure ::discovery-failure}]]]})))

(rf/reg-event-db
  ::discovery-success
  [data-path]
  (fn [db [_ response]]
    (assoc db :products response)))

(rf/reg-event-fx
  ::discovery-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-db
  ::set-criterion
  [data-path]
  (fn [db [_ property operator value]]
    (let [v {:property-name property
             :operator      operator
             :match-value   value}]
      (-> db
          (assoc-in [:criteria property] v)
          (assoc property value)))))

(rf/reg-event-db
  ::set-input
  [data-path]
  (fn [db [_ k v]]
    (assoc db k v)))

(rf/reg-event-db
  ::set-multi-weight-input
  [data-path]
  (fn [db [_ product-key property weight]]
    (assoc-in db [product-key property] weight)))

(rf/reg-sub
  ::form-data
  (fn [db _]
    (get-in db paths)))

(rf/reg-sub
  ::ui-model
  :<- [::form-data]
  :<- [:ui/language]
  (fn [[data language] _]
    (-> metadata
        (assoc :language language)
        (merge data)
        (check-fields))))
