(ns com.taxonomy.product.ui.my-products.model
  (:require [re-frame.core :as rf]
            [com.taxonomy.product :as product]))

(def paths [:ui/forms ::product/my-products :data])
(def data-path (rf/path [:ui/forms ::product/my-products :data]))
(def metadata
  {:data-path paths})

(rf/reg-event-fx
  ::init
  [data-path]
  (fn [_ _]
    {:db {:hide-delete-confirmation-box true}
     :fx [[:dispatch [:ajax/get {:uri     "/api/my-products"
                                 :success ::init-success
                                 :failure ::init-failure}]]]}))

(rf/reg-event-db
  ::init-success
  [data-path]
  (fn [db [_ response]]
    (assoc db :products response)))

(rf/reg-event-fx
  ::init-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::delete
  [data-path]
  (fn [{:keys [db]} _]
    (when-let [id (:id db)]
      {:fx [[:dispatch [:ajax/delete {:uri     (str "/api/products/" id)
                                      :success ::delete-success
                                      :failure ::delete-failure}]]]})))

(rf/reg-event-fx
  ::delete-success
  [data-path]
  (fn [_ _]
    {:fx [[:dispatch [::init]]
          [:dispatch [:ui/push-notification {:title :com.taxonomy.ui/success
                                             :body  ::product/product-deleted
                                             :type  :success}]]]}))

(rf/reg-event-fx
  ::delete-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-db
  ::show-delete-confirmation-box
  [data-path]
  (fn [db [_ id]]
    (assoc db :hide-delete-confirmation-box false
              :id id)))

(rf/reg-event-db
  ::hide-delete-confirmation-box
  [data-path]
  (fn [db _]
    (assoc db :hide-delete-confirmation-box true
              :id nil)))

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
