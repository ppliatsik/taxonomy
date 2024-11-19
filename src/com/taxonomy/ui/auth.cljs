(ns com.taxonomy.ui.auth
  (:require [re-frame.core :as rf]
            [reagent.cookies :as cookies]
            [com.taxonomy.ui.ajax]
            [com.taxonomy.ui.routes :as routes]))

(rf/reg-sub
  :ui/user
  (fn [db _]
    (get db :ui/user)))

(rf/reg-fx
  :ui/cookie
  (fn [cookies]
    (doseq [[k v] cookies]
      (cookies/set! (str k) v))))

(rf/reg-event-fx
  ::login
  (fn [_ [_ username password]]
    (when (and username password)
      {:fx [[:dispatch [:ajax/post {:uri     "/api/login"
                                    :params  {:username username
                                              :password password}
                                    :success ::login-success
                                    :failure ::login-failure}]]]})))

(rf/reg-event-fx
  ::login-success
  (fn [{:keys [db]} [_ {:keys [payload]}]]
    (let [user-info (update payload :roles set)]
      {:ui/cookie {"X-Auth-Token" (:token user-info)}
       :db        (assoc db :ui/user (dissoc user-info :token)
                            :token   (:token user-info))
       :fx        [[:url (routes/main-view)]]})))

(rf/reg-event-fx
  ::login-failure
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::logout
  (fn [_ _]
    {:fx [[:dispatch [:ajax/post {:uri     "/api/logout"
                                  :success ::logout-success
                                  :failure ::logout-failure}]]]}))

(rf/reg-event-fx
  ::logout-success
  (fn [_ _]
    (cookies/clear!)
    {:db {}
     :fx [[:url (routes/main-view)]]}))

(rf/reg-event-fx
  ::logout-failure
  (fn [_ [_ {:keys [response]}]]
    {:db {}
     :fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))
