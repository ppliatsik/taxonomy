(ns com.taxonomy.end-user.ui.create.model
  (:require [re-frame.core :as rf]
            [clojure.string :as clj.str]
            [com.taxonomy.ui.routes :as routes]
            [com.taxonomy.end-user :as end-user]))

(def paths [:ui/forms ::end-user/create :data])
(def data-path (rf/path [:ui/forms ::end-user/create :data]))
(def metadata
  {:data-path paths})

(defn check-fields
  [data]
  (assoc data :correct-inputs
              (and (not (clj.str/blank? (:username data)))
                   (end-user/username-is-valid? (:username data))
                   (not (clj.str/blank? (:password data)))
                   (end-user/password-is-valid? (:password data))
                   (or (and (not (clj.str/blank? (:password-verification data)))
                            (end-user/username-is-valid? (:password-verification data)))
                       (= (:password-verification data) (:password data)))
                   (not (clj.str/blank? (:first-name data)))
                   (end-user/is-only-alphabetic-str (:first-name data))
                   (not (clj.str/blank? (:last-name data)))
                   (end-user/is-only-alphabetic-str (:last-name data))
                   (not (clj.str/blank? (:email data)))
                   (end-user/email-is-valid? (:email data)))))

(rf/reg-event-fx
  ::init
  [data-path]
  (fn [_ _]
    {:db {:correct-inputs false}}))

(rf/reg-event-fx
  ::register
  [data-path]
  (fn [{:keys [db]} _]
    (let [params (select-keys db [:username :password :password-verification
                                  :first-name :last-name :email])]
      {:fx [[:dispatch [:ajax/post {:uri     "/api/users"
                                    :params  params
                                    :success ::register-success
                                    :failure ::register-failure}]]]})))

(rf/reg-event-fx
  ::register-success
  (fn [_ _]
    {:fx [[:dispatch [::send-activation-link]]]}))

(rf/reg-event-fx
  ::register-failure
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::send-activation-link
  [data-path]
  (fn [{:keys [db]} _]
    {:fx [[:url (routes/main-view)]
          [:dispatch [:ajax/put {:uri     (str "/api/users/" (:username db) "/resend-email-activation-account")
                                 :success ::send-activation-link-success
                                 :failure ::send-activation-link-failure}]]]}))
(rf/reg-event-fx
  ::send-activation-link-success
  [data-path]
  (fn [_ _]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/success
                                             :body  ::end-user/activate-mail-link
                                             :type  :success}]]]}))

(rf/reg-event-fx
  ::send-activation-link-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

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

(rf/reg-event-db
  ::set-password-verification
  [data-path]
  (fn [db [_ v]]
    (assoc db :password-verification v)))

(rf/reg-event-db
  ::set-first-name
  [data-path]
  (fn [db [_ v]]
    (assoc db :first-name v)))

(rf/reg-event-db
  ::set-last-name
  [data-path]
  (fn [db [_ v]]
    (assoc db :last-name v)))

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
