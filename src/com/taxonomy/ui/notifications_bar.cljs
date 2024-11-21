(ns com.taxonomy.ui.notifications-bar
  (:require [re-frame.core :as rf]
            [com.taxonomy.translations :as trans]))

(def notification-types {:error   {:className "is-danger"}
                         :warning {:className "is-warning"}
                         :success {:className "is-success"
                                   :timeout   5000}})

(rf/reg-sub
  :ui/notifications
  (fn [db _]
    (:notifications db)))

(rf/reg-event-fx
  :ui/push-notification
  (fn [{:keys [db]} [_ {:keys [type] :as notification}]]
    (let [type      (get (-> notification-types keys set) type :warning)
          timeout   (get-in notification-types [type :timeout])
          className (get-in notification-types [type :className])
          uuid      (str (random-uuid))
          msg       (-> notification
                        (assoc :uuid       uuid
                               :disposable true
                               :timeout    timeout
                               :className  className
                               :type       type))]
      (merge {:db (update db :notifications conj msg)}
             (when timeout
               {:dispatch-later [{:ms       timeout
                                  :dispatch [:ui/pop-notification msg]}]})))))

(rf/reg-event-db
  :ui/pop-notification
  (fn [db [_ notification]]
    (update db :notifications (fn [n]
                                (->> n
                                     (remove #(= notification %))
                                     vec)))))

(defn global-notifications-bar
  []
  (let [notifications @(rf/subscribe [:ui/notifications])
        lang          @(rf/subscribe [:ui/language])]
    (when (seq notifications)
      (into [:aside]
            (map (fn [{:keys [uuid title body error className disposable] :as n}]
                   [:div.message {:key       uuid
                                  :className className}
                    [:div.message-header
                     [:p title]
                     (when disposable
                       [:button.delete {:aria-label "delete"
                                        :on-click   (fn [_]
                                                      (rf/dispatch [:ui/pop-notification n]))}])]
                    (when body
                      [:div.message-body (trans/translate lang body)
                       (when error
                         [:p error])])]))
            notifications))))
