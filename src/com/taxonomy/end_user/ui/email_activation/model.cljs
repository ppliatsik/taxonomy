(ns com.taxonomy.end-user.ui.email-activation.model
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.routes :as routes]
            [com.taxonomy.end-user :as end-user]))

(def paths [:ui/forms ::end-user/email-activation :data])
(def data-path (rf/path [:ui/forms ::end-user/email-activation :data]))
(def metadata
  {:data-path paths})

(rf/reg-event-fx
  ::init
  [data-path]
  (fn [_ [_ {:keys [token]}]]
    {:fx [[:dispatch [:ajax/put {:uri     (str "/api/email-activate-account")
                                 :params  {:token token}
                                 :success ::init-success
                                 :failure ::init-failure}]]]}))

(rf/reg-event-db
  ::init-success
  [data-path]
  (fn [_ _]
    {:fx [[:url (routes/main-view)]
          [:dispatch [:ui/push-notification {:title :com.taxonomy.ui/success
                                             :body  ::end-user/account-activated
                                             :type  :success}]]]}))

(rf/reg-event-fx
  ::init-failure
  [data-path]
  (fn [_ _]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  ::end-user/account-activation-problem
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
