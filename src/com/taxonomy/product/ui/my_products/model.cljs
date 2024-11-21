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
  (fn [{:keys [db]} _]
    {:fx [[:dispatch [:ajax/get {:uri     "/api/my-products"
                                 :success ::init-success
                                 :failure ::init-failure}]]]}))

(rf/reg-event-db
  ::init-success
  [data-path]
  (fn [db [_ response]]
    {:db (assoc db :products response)}))

(rf/reg-event-fx
  ::init-failure
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
