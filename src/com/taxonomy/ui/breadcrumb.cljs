(ns com.taxonomy.ui.breadcrumb
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.routes :as routes]
            [com.taxonomy.translations :as trans]))

(defn breadcrumbs-routes
  [route params]
  (let [routes {:com.taxonomy.ui/main-view {:name-route   :com.taxonomy.ui/main-view
                                            :icon-route   "fas fa-home"
                                            :href         routes/main-view
                                            :parent-route nil}

                :com.taxonomy.end-user/login {:name-route   :com.taxonomy.end-user/login
                                              :href         routes/login
                                              :parent-route :com.taxonomy.ui/main-view}

                :com.taxonomy.end-user/register {:name-route   :com.taxonomy.end-user/register
                                                 :href         routes/register
                                                 :parent-route :com.taxonomy.ui/main-view}

                :com.taxonomy.end-user/users {:name-route   :com.taxonomy.end-user/users
                                              :href         routes/users
                                              :parent-route :com.taxonomy.ui/main-view}

                :com.taxonomy.end-user/user {:name-route   :com.taxonomy.end-user/user
                                             :href         routes/user
                                             :parent-route :com.taxonomy.end-user/list-view}

                :com.taxonomy.end-user/change-password {:name-route   :com.taxonomy.end-user/change-password
                                                        :href         routes/change-password
                                                        :parent-route :com.taxonomy.ui/main-view}

                :com.taxonomy.end-user/reset-password {:name-route   :com.taxonomy.end-user/reset-password
                                                       :href         routes/reset-password
                                                       :parent-route :com.taxonomy.ui/main-view}

                :com.taxonomy.product/create-product {:name-route   :com.taxonomy.product/create-product
                                                      :href         routes/create-product
                                                      :parent-route :com.taxonomy.product/products-menu}

                :com.taxonomy.product/products {:name-route   :com.taxonomy.product/products
                                                :href         routes/products
                                                :parent-route :com.taxonomy.product/products-menu}

                :com.taxonomy.product/product {:name-route   :com.taxonomy.product/product
                                               :href         routes/product
                                               :parent-route :com.taxonomy.product/products-menu}

                :com.taxonomy.product/my-products {:name-route   :com.taxonomy.product/my-products
                                                   :href         routes/my-products
                                                   :parent-route :com.taxonomy.product/products-menu}

                :com.taxonomy.product/products-menu {:name-route   :com.taxonomy.product/products-menu
                                                     :href         routes/products-menu
                                                     :parent-route :com.taxonomy.ui/main-view}}

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
    (let [params   (get-in db [:ui/breadcrumbs :params])
          language (-> db :ui/region :language)]
      {:db         (assoc-in db [:ui/breadcrumbs :routes] (get-routes current-view params))
       :page-title (trans/translate language (:name-route (breadcrumbs-routes current-view params)))})))

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
  [{:keys [icon-route name-route href] :as route} language]
  [:li {:key name-route}
   [:a
    {:href href}
    [:span (if icon-route
             [:i.icon {:class icon-route}]
             (trans/translate language name-route))]]])

(defn breadcrumb-list
  [{:keys [routes] :as breadcrumbs} language]
  (let [butlast-routes (butlast routes)
        last-route     (last routes)]
    [:nav.breadcrumb.m-0.p-1
     [:ul
      (concat
        (map #(breadcrumb-li % language) butlast-routes)
        [[:li {:key last-route}
          [:a {:style {:color "black"}}
           [:span
            (if (:icon-route last-route)
              [:i.icon {:class (:icon-route last-route)}]
              (trans/translate language (:name-route last-route)))]]]])]]))
