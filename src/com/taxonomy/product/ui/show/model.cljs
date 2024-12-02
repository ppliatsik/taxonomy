(ns com.taxonomy.product.ui.show.model
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.routes :as routes]
            [com.taxonomy.product :as product]))

(def paths [:ui/forms ::product/show :data])
(def data-path (rf/path [:ui/forms ::product/show :data]))
(def metadata
  {:data-path paths})

(defn get-all-keys
  [m]
  (reduce (fn [acc [k v]]
            (if (map? v)
              (apply conj acc (name k) (get-all-keys v))
              (conj acc (name k))))
          []
          m))

(rf/reg-sub
  ::form-data
  (fn [db _]
    (get-in db paths)))

(rf/reg-event-fx
  ::init
  [data-path]
  (fn [{:keys [db]} [_ id]]
    {:db (assoc db :id id
                   :hide-delete-confirmation-box true)
     :fx [[:dispatch [:ajax/get {:uri     (str "/api/products/" id)
                                 :success ::init-success
                                 :failure ::init-failure}]]]}))

(rf/reg-event-db
  ::init-success
  [data-path]
  (fn [db [_ response]]
    {:db (assoc db :product response)}))

(rf/reg-event-fx
  ::init-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::publish
  [data-path]
  (fn [{:keys [db]} _]
    {:fx [[:dispatch [:ajax/put {:uri     (str "/api/products/" (:id db) "/publish")
                                 :success ::publish-success
                                 :failure ::publish-failure}]]]}))

(rf/reg-event-fx
  ::publish-success
  [data-path]
  (fn [{:keys [db]} [_ {:keys [payload]}]]
    {:db (assoc db :product payload)
     :fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/success
                                             :body  ::product/product-published
                                             :type  :success}]]]}))

(rf/reg-event-fx
  ::publish-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::unpublish
  [data-path]
  (fn [{:keys [db]} _]
    {:fx [[:dispatch [:ajax/put {:uri     (str "/api/products/" (:id db) "/unpublish")
                                 :success ::unpublish-success
                                 :failure ::unpublish-failure}]]]}))

(rf/reg-event-fx
  ::unpublish-success
  [data-path]
  (fn [{:keys [db]} [_ {:keys [payload]}]]
    {:db (assoc db :product payload)
     :fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/success
                                             :body  ::product/product-unpublished
                                             :type  :success}]]]}))

(rf/reg-event-fx
  ::unpublish-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::delete
  [data-path]
  (fn [{:keys [db]} _]
    {:fx [[:dispatch [:ajax/delete {:uri     (str "/api/products/" (:id db))
                                    :success ::delete-success
                                    :failure ::delete-failure}]]]}))

(rf/reg-event-fx
  ::delete-success
  [data-path]
  (fn [_ _]
    {:fx [[:url (routes/secaas-products-management)]
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
  (fn [db _]
    (assoc db :hide-delete-confirmation-box false)))

(rf/reg-event-db
  ::hide-delete-confirmation-box
  [data-path]
  (fn [db _]
    (assoc db :hide-delete-confirmation-box true)))

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
