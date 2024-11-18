(ns com.taxonomy.end-user.ui.login.model
  (:require [re-frame.core :as rf]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.ui.auth :as auth]))

(def paths [:ui/forms ::end-user/login :data])
(def data-path (rf/path [:ui/forms ::end-user/login :data]))
(def metadata
  {:data-path paths})

(rf/reg-event-fx
  ::init
  [data-path]
  (fn [{:keys [db]} _]
    {:db (assoc db :username nil
                   :password nil)}))

(rf/reg-event-fx
  ::login
  [data-path]
  (fn [{:keys [db]} _]
    (let [username (:username db)
          password (:password db)]
      {:fx [[:dispatch [::auth/login username password]]]})))

(rf/reg-event-db
  ::set-username
  [data-path]
  (fn [db [_ v]]
    (assoc db :username v)))

(rf/reg-event-db
  ::set-password
  [data-path]
  (fn [db [_ v]]
    (assoc db :password v)))

(rf/reg-sub
  ::form-data
  (fn [db _]
    (get-in db paths)))

(rf/reg-sub
  ::ui-model
  :<- [::form-data]
  (fn [[data] _]
    (-> metadata
        (merge data))))
