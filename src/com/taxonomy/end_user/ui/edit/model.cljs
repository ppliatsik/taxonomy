(ns com.taxonomy.end-user.ui.edit.model
  (:require [re-frame.core :as rf]
            [clojure.string :as clj.str]
            [com.taxonomy.ui.routes :as routes]
            [com.taxonomy.end-user :as end-user]))

(def paths [:ui/forms ::end-user/edit :data])
(def data-path (rf/path [:ui/forms ::end-user/edit :data]))
(def metadata
  {:data-path paths})

(defn check-fields
  [data]
  (assoc data :correct-inputs
              (and (not (clj.str/blank? (:first-name data)))
                   (end-user/is-only-alphabetic-str (:first-name data))
                   (not (clj.str/blank? (:last-name data)))
                   (end-user/is-only-alphabetic-str (:last-name data))
                   (not (clj.str/blank? (:email data)))
                   (end-user/email-is-valid? (:email data)))))

(rf/reg-event-fx
  ::init
  [data-path]
  (fn [{:keys [db]} [_ username]]
    {:db (assoc db :username username
                   :can-edit false)
     :fx [[:dispatch [:ajax/get {:uri     (str "/api/users/" username)
                                 :success ::init-success
                                 :failure ::init-failure}]]]}))

(rf/reg-event-db
  ::init-success
  [data-path]
  (fn [db [_ response]]
    (assoc db :user response
              :first-name (:first-name response)
              :last-name (:last-name response)
              :email (:email response))))

(rf/reg-event-fx
  ::init-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::edit
  [data-path]
  (fn [{:keys [db]} _]
    (let [params (select-keys db [:first-name :last-name :email])]
      {:fx [[:dispatch [:ajax/put {:uri     (str "/api/users/" (:username db) "/update-info")
                                   :params  params
                                   :success ::edit-success
                                   :failure ::edit-failure}]]]})))

(rf/reg-event-fx
  ::edit-success
  [data-path]
  (fn [_ _]
    {:fx [[:url (routes/main-view)]
          [:dispatch [:ui/push-notification {:title :com.taxonomy.ui/success
                                             :body  ::end-user/user-edited
                                             :type  :success}]]]}))

(rf/reg-event-fx
  ::edit-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::send-activation-link
  [data-path]
  (fn [{:keys [db]} _]
    {:fx [[:dispatch [:ajax/put {:uri     (str "/api/users/" (:username db) "/resend-email-activation-account")
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

(rf/reg-event-fx
  ::activate
  [data-path]
  (fn [{:keys [db]} _]
    {:fx [[:dispatch [:ajax/put {:uri     (str "/api/users/" (:username db) "/activate")
                                 :success ::activate-success
                                 :failure ::activate-failure}]]]}))

(rf/reg-event-db
  ::activate-success
  [data-path]
  (fn [db [_ {:keys [payload]}]]
    (assoc db :user payload)))

(rf/reg-event-fx
  ::activate-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::deactivate
  [data-path]
  (fn [{:keys [db]} _]
    {:fx [[:dispatch [:ajax/put {:uri     (str "/api/users/" (:username db) "/deactivate")
                                 :success ::deactivate-success
                                 :failure ::deactivate-failure}]]]}))

(rf/reg-event-db
  ::deactivate-success
  [data-path]
  (fn [db [_ {:keys [payload]}]]
    (assoc db :user payload)))

(rf/reg-event-fx
  ::deactivate-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::delete
  [data-path]
  (fn [{:keys [db]} _]
    {:fx [[:dispatch [:ajax/delete {:uri     (str "/api/users/" (:username db))
                                    :success ::delete-success
                                    :failure ::delete-failure}]]]}))

(rf/reg-event-fx
  ::delete-success
  [data-path]
  (fn [_ _]
    {:fx [[:url (routes/users)]]}))

(rf/reg-event-fx
  ::delete-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::change-password
  [data-path]
  (fn [_ _]
    {:fx [[:url (routes/change-password)]]}))

(rf/reg-event-db
  ::toggle-edit
  [data-path]
  (fn [db _]
    (update db :can-edit not)))

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
