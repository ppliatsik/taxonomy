(ns com.taxonomy.end-user.ui.list.model
  (:require [re-frame.core :as rf]
            [com.taxonomy.end-user :as end-user]))

(def paths [:ui/forms ::end-user/list :data])
(def data-path (rf/path [:ui/forms ::end-user/list :data]))
(def metadata
  {:data-path paths})

(rf/reg-event-fx
  ::init
  [data-path]
  (fn [_ _]
    {:fx [[:dispatch [:ajax/get {:uri     "/api/users"
                                 :success ::init-success
                                 :failure ::init-failure}]]]}))

(rf/reg-event-db
  ::init-success
  [data-path]
  (fn [db [_ response]]
    (assoc db :users response)))

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
