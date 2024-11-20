(ns com.taxonomy.end-user.ui.reset-password.model
  (:require [re-frame.core :as rf]
            [clojure.string :as clj.str]
            [com.taxonomy.ui.routes :as routes]
            [com.taxonomy.end-user :as end-user]))

(def paths [:ui/forms ::end-user/reset-password :data])
(def data-path (rf/path [:ui/forms ::end-user/reset-password :data]))
(def metadata
  {:data-path paths})

(defn check-fields
  [data]
  (assoc data :correct-inputs
              (and (not (clj.str/blank? (:email data)))
                   (end-user/email-is-valid? (:email data)))))

(rf/reg-event-fx
  ::init
  [data-path]
  (fn [{:keys [db]} _]
    {:db (assoc db :email nil
                   :correct-inputs false)}))

(rf/reg-event-fx
  ::reset-password
  [data-path]
  (fn [{:keys [db]} _]
    {:fx [[:dispatch [:ajax/put {:uri     (str "/api/reset-password")
                                 :params  {:email (:email db)}
                                 :success ::reset-password-success
                                 :failure ::reset-password-failure}]]]}))

(rf/reg-event-fx
  ::reset-password-success
  [data-path]
  (fn [_ _]
    {:fx [[:url (routes/main-view)]]}))

(rf/reg-event-fx
  ::reset-password-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-db
  ::set-email
  [data-path]
  (fn [db [_ v]]
    (assoc db :email v)))

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
