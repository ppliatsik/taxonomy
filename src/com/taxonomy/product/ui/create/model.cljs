(ns com.taxonomy.product.ui.create.model
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.routes :as routes]
            [com.taxonomy.product :as product]))

(def paths [:ui/forms ::product/create :data])
(def data-path (rf/path [:ui/forms ::product/create :data]))
(def metadata
  {:data-path paths})

(rf/reg-event-fx
  ::init
  [data-path]
  (fn [_ _]
    {:db {}}))

(rf/reg-event-fx
  ::create
  [data-path]
  (fn [{:keys [db]} _]
    (let [params {}]
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
