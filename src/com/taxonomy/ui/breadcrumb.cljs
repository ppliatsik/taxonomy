(ns com.taxonomy.ui.breadcrumb
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.routes :as routes]))

(defn breadcrumbs-routes
  [route params]
  (let [routes {:com.taxonomy.ui/main-view {:name-route   "Home"
                                            :icon-route   "fas fa-home"
                                            :href         routes/main-view
                                            :parent-route nil}}

        default-route  (get routes :com.taxonomy.ui/main-view)
        returned-route (get routes route default-route)]
    (update returned-route :href #(when % (apply % [params])))))

(defn get-routes
  [current-view params]
  (let [route  (breadcrumbs-routes current-view params)
        parent (:parent-route route)]
    (if-not parent
      [route]
      (conj (get-routes parent params) route))))

(rf/reg-event-fx
  ::set-breadcrumb
  (fn [{:keys [db]} [_ current-view]]
    (let [params (get-in db [:ui/breadcrumbs :params])]
      {:db         (assoc-in db [:ui/breadcrumbs :routes] (get-routes current-view params))
       :page-title (:name-route (breadcrumbs-routes current-view params))})))

(rf/reg-event-db
  ::set-breadcrumb-params
  (fn [db [_ breadcrumb-params]]
    (-> db
        (assoc-in [:ui/breadcrumbs :params] breadcrumb-params))))

(rf/reg-sub
  :ui/breadcrumbs
  (fn [db _]
    (get db :ui/breadcrumbs)))

(defn breadcrumb-li
  [{:keys [icon-route name-route href] :as route}]
  [:li {:key name-route}
   [:a
    {:href href}
    [:span (if icon-route
             [:i.icon {:class icon-route}]
             name-route)]]])

(defn breadcrumb-list
  [{:keys [routes] :as breadcrumbs}]
  (let [butlast-routes (butlast routes)
        last-route     (last routes)]
    [:nav.breadcrumb.m-0.p-1
     [:ul
      (concat
        (map #(breadcrumb-li %) butlast-routes)
        [[:li {:key last-route}
          [:a {:style {:color "black"}}
           [:span
            (if (:icon-route last-route)
              [:i.icon {:class (:icon-route last-route)}]
              (:name-route last-route))]]]])]]))
