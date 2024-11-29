(ns com.taxonomy.product.ui.create.model
  (:require [re-frame.core :as rf]
            [clojure.string :as clj.str]
            [com.taxonomy.ui.routes :as routes]
            [com.taxonomy.product :as product]))

(def paths [:ui/forms ::product/create :data])
(def data-path (rf/path [:ui/forms ::product/create :data]))
(def metadata
  {:data-path paths})

(defn check-fields
  [data]
  (assoc data :correct-inputs (not (clj.str/blank? (:name data)))))

(defn get-product-data
  [db]
  {:name                      (:name db)
   :description               (:description db)
   :delivery-methods          (:delivery-methods db)
   :deployment-models         (:deployment-models db)
   :product-categories        (:product-categories db)
   :cost-model                (:cost-model db)
   :security-mechanisms       (:selected-security-mechanisms db)
   :non-functional-guarantees (:non-functional-guarantees db)
   :protection-types          (:protection-types db)
   :security-properties       (:security-properties db)
   :protected-items           (:protected-items db)
   :threats                   (:selected-threats db)
   :restrictions              (:restrictions db)
   :open-source               (:open-source db)
   :freely-available          (:freely-available db)
   :test-version              (:test-version db)
   :test-duration             (:test-duration db)
   :product-interfaces        (:product-interfaces db)
   :product-company           (:product-company db)
   :marketplaces              (:marketplaces db)
   :support                   (:support db)})

(rf/reg-event-fx
  ::init
  [data-path]
  (fn [_ _]
    {:db {}
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
    {:fx [[:url (routes/my-products)]
          [:dispatch [:ui/push-notification {:title :com.taxonomy.ui/success
                                             :body  ::product/product-created
                                             :type  :success}]]]}))

(rf/reg-event-fx
  ::create-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-db
  ::set-input
  [data-path]
  (fn [db [_ k v]]
    (assoc db k v)))

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
