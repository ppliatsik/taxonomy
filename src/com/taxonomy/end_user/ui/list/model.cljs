(ns com.taxonomy.end-user.ui.list.model
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.pagination :as pagination]
            [com.taxonomy.end-user :as end-user]))

(def paths [:ui/forms ::end-user/list :data])
(def data-path (rf/path [:ui/forms ::end-user/list :data]))
(def metadata
  {:data-path paths})

(rf/reg-event-fx
  ::init
  [data-path]
  (fn [{:keys [db]} [_ {:keys [limit offset q]}]]
    (let [limit        (or limit pagination/default-limit)
          offset       (or offset pagination/default-offset)
          current-page (-> offset (quot limit) inc)]
      {:db (-> db
               (assoc :q q)
               (assoc :limit limit)
               (assoc :offset offset)
               (assoc-in [:pagination :current-page] current-page))
       :fx [[:dispatch [:ajax/get {:uri     "/api/users"
                                   :success ::init-success
                                   :failure ::init-failure}]]]})))

(rf/reg-event-db
  ::init-success
  [data-path]
  (fn [db [_ {:keys [results pagination]}]]
    (let [current-page (-> db :pagination :current-page)]
      (assoc db :users results
                :pagination (pagination/set-pagination-data pagination current-page)))))

(rf/reg-event-fx
  ::init-failure
  [data-path]
  (fn [_ [_ {:keys [response]}]]
    {:fx [[:dispatch [:ui/push-notification {:title :com.taxonomy.ui/failure
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::set-current-page
  [data-path]
  (fn [{:keys [db]} [_ current-page]]
    (let [limit  (:limit db)
          offset (* limit (dec current-page))
          params (merge {:limit  limit
                         :offset offset}
                        (when-let [q (:q db)]
                          {:q q}))]
      {:db (assoc-in db [:pagination :current-page] current-page)
       :fx [[:dispatch [:ajax/get {:uri     "/api/users"
                                   :params  params
                                   :success ::init-success
                                   :failure ::init-failure}]]]})))

(rf/reg-event-fx
  ::search
  [data-path]
  (fn [{:keys [db]} _]
    (let [params {:limit  (:limit db)
                  :offset (:offset db)
                  :q      (:q db)}]
      {:fx [[:dispatch [:ajax/get {:uri     "/api/users"
                                   :params  params
                                   :success ::init-success
                                   :failure ::init-failure}]]]})))

(rf/reg-event-db
  ::set-q
  [data-path]
  (fn [db [_ v]]
    (assoc db :q v)))

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
