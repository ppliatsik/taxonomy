(ns com.taxonomy.product.ui.list.model
  (:require [re-frame.core :as rf]
            [clojure.spec.alpha :as s]
            [spec-tools.core :as st]
            [com.taxonomy.ui.util :as util]
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
  (->> db
       :criteria
       vals
       (remove #(nil? (-> % :match-value)))
       vec))

(defn get-weights
  [{:keys [weights]}]
  {:non-functional-guarantees-w (:non-functional-guarantees weights)
   :restrictions-w              (:restrictions weights)
   :test-duration-w             (:test-duration weights)})

(rf/reg-event-fx
  ::init
  [data-path]
  (fn [_ _]
    {:db {:criteria {}
          :weights  {}}
     :fx [[:dispatch [:ajax/get {:uri     "/api/config/security-mechanisms"
                                 :success ::get-security-mechanisms-success
                                 :failure ::get-security-mechanisms-failure}]]
          [:dispatch [:ajax/get {:uri     "/api/config/threats"
                                 :success ::get-threats-success
                                 :failure ::get-threats-failure}]]
          [:dispatch [:ajax/get {:uri     "/api/config/products-choices"
                                 :success ::get-products-choices-success
                                 :failure ::get-products-choices-failure}]]]}))

(rf/reg-event-db
  ::get-security-mechanisms-success
  [data-path]
  (fn [db [_ response]]
    (assoc db :security-mechanisms (util/get-all-keys response))))

(rf/reg-event-fx
  ::get-security-mechanisms-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-db
  ::get-threats-success
  [data-path]
  (fn [db [_ response]]
    (assoc db :threats (util/get-all-keys response))))

(rf/reg-event-fx
  ::get-threats-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

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
  ::set-criterion-value
  [data-path]
  (fn [db [_ property value]]
    (let [old-v (get-in db [:criteria property])
          new-v (merge old-v
                       {:property-name property
                        :match-value   value})]
      (assoc-in db [:criteria property] new-v))))

(rf/reg-event-db
  ::set-criterion-operator
  [data-path]
  (fn [db [_ property value]]
    (let [old-v (get-in db [:criteria property])
          new-v (merge old-v
                       {:property-name property
                        :operator      value})]
      (assoc-in db [:criteria property] new-v))))

(rf/reg-event-db
  ::set-criterion-not
  [data-path]
  (fn [db [_ property value]]
    (let [old-v (get-in db [:criteria property])
          new-v (merge old-v
                       {:property-name property
                        :not           value})]
      (assoc-in db [:criteria property] new-v))))

(rf/reg-event-db
  ::set-input
  [data-path]
  (fn [db [_ k v]]
    (assoc-in db [:weight k] v)))

(rf/reg-event-db
  ::set-multi-weight-input
  [data-path]
  (fn [db [_ product-key property weight]]
    (assoc-in db [:weights product-key property] weight)))

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
