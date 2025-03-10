(ns com.taxonomy.product.ui.create.model
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.routes :as routes]
            [com.taxonomy.ui.util :as util]
            [com.taxonomy.product :as product]))

(def paths [:ui/forms ::product/create :data])
(def data-path (rf/path [:ui/forms ::product/create :data]))
(def metadata
  {:data-path paths})

(defn get-product-data
  [db]
  {:name                      (:name db)
   :description               (:description db)
   :delivery-methods          (:delivery-methods db)
   :deployment-models         (:deployment-models db)
   :product-categories        (:product-categories db)
   :cost-model                (:cost-model db)
   :security-mechanisms       (->> (:selected-security-mechanisms db) (map keyword) vec)
   :non-functional-guarantees (:non-functional-guarantees db)
   :protection-types          (:protection-types db)
   :security-properties       (:security-properties db)
   :protected-items           (:protected-items db)
   :threats                   (->> (:selected-threats db) (map keyword) vec)
   :restrictions              (:restrictions db)
   :open-source               (:open-source db)
   :freely-available          (:freely-available db)
   :test-version              (:test-version db)
   :test-duration             (:test-duration db)
   :product-interfaces        (:product-interfaces db)
   :product-company           (:product-company db)
   :marketplaces              (:marketplaces db)
   :support                   (:support db)})

(def required-fields
  #{:name :security-mechanisms :threats})

(rf/reg-event-fx
  ::init
  [data-path]
  (fn [_ _]
    {:db {}
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
  ::create
  [data-path]
  (fn [{:keys [db]} _]
    (let [params (get-product-data db)]
      {:fx [[:dispatch [:ajax/post {:uri     "/api/products"
                                    :params  params
                                    :success ::create-success
                                    :failure ::create-failure}]]]})))

(rf/reg-event-fx
  ::create-success
  [data-path]
  (fn [_ _]
    {:db {}
     :fx [[:url (routes/my-products)]
          [:dispatch [:ui/push-notification {:title :com.taxonomy.ui/success
                                             :body  ::product/product-created
                                             :type  :success}]]]}))

(rf/reg-event-fx
  ::create-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:db {}
     :fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-db
  ::set-input
  [data-path]
  (fn [db [_ k v]]
    (assoc db k v)))

(rf/reg-event-db
  ::clear-input
  [data-path]
  (fn [db [_ k]]
    (assoc db k [])))

(rf/reg-event-db
  ::set-line-field-input
  [data-path]
  (fn [db [_ property idx field value]]
    (assoc-in db [property idx field] value)))

(rf/reg-event-db
  ::clear-line-field-input
  [data-path]
  (fn [db [_ property idx field]]
    (assoc-in db [property idx field] [])))

(rf/reg-event-db
  ::add-line
  [data-path]
  (fn [db [_ property]]
    (cond (= :cost-model property)
          (update db property conj {:cost-model-types nil
                                    :charge-packets   nil
                                    :time-charge-type nil})

          (= :non-functional-guarantees property)
          (update db property conj {:property            nil
                                    :operator            nil
                                    :value               nil
                                    :metric              nil
                                    :direction-of-values nil
                                    :unit                nil})

          (= :restrictions property)
          (update db property conj {:property            nil
                                    :operator            nil
                                    :value               nil
                                    :metric              nil
                                    :direction-of-values false
                                    :unit                nil})

          (= :support property)
          (update db property conj {:support-types          nil
                                    :support-daily-duration nil
                                    :support-package-number nil}))))

(rf/reg-event-db
  ::remove-line
  [data-path]
  (fn [db [_ property idx]]
    (update db property (fn [elements]
                          (vec (concat (take idx elements)
                                       (drop (+ idx 1) elements)))))))

(rf/reg-event-db
  ::add-smt
  [data-path]
  (fn [db [_ selected-property values]]
    (assoc db selected-property values)))

(rf/reg-event-db
  ::remove-smt
  [data-path]
  (fn [db [_ selected-property values]]
    (assoc db selected-property values)))

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
        (merge data))))
