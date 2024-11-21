(ns com.taxonomy.end-user.ui.change-password.model
  (:require [re-frame.core :as rf]
            [clojure.string :as clj.str]
            [com.taxonomy.ui.routes :as routes]
            [com.taxonomy.end-user :as end-user]))

(def paths [:ui/forms ::end-user/change-password :data])
(def data-path (rf/path [:ui/forms ::end-user/change-password :data]))
(def metadata
  {:data-path paths})

(defn check-fields
  [data]
  (assoc data :correct-inputs
              (and (and (not (clj.str/blank? (:old-password data)))
                        (end-user/password-is-valid? (:old-password data)))
                   (not (clj.str/blank? (:password data)))
                   (end-user/password-is-valid? (:password data))
                   (or (and (not (clj.str/blank? (:password-verification data)))
                            (end-user/username-is-valid? (:password-verification data)))
                       (= (:password-verification data) (:password data))))))

(rf/reg-event-fx
  ::init
  [data-path]
  (fn [{:keys [db]} [_ username]]
    {:db (assoc db :username username
                   :old-password nil
                   :password nil
                   :password-verification nil
                   :correct-inputs false)}))

(rf/reg-event-fx
  ::change-password
  [data-path]
  (fn [{:keys [db]} _]
    (let [params (select-keys db [:old-password :password :password-verification])]
      {:fx [[:dispatch [:ajax/put {:uri     (str "/api/users/" (:username db) "/change-password")
                                   :params  params
                                   :success ::change-password-success
                                   :failure ::change-password-failure}]]]})))

(rf/reg-event-fx
  ::change-password-success
  [data-path]
  (fn [_ _]
    {:fx [[:url (routes/users)]]}))

(rf/reg-event-fx
  ::change-password-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-db
  ::set-old-password
  [data-path]
  (fn [db [_ v]]
    (assoc db :old-password v)))

(rf/reg-event-db
  ::set-password
  [data-path]
  (fn [db [_ v]]
    (assoc db :password v)))

(rf/reg-event-db
  ::set-password-verification
  [data-path]
  (fn [db [_ v]]
    (assoc db :password-verification v)))

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
