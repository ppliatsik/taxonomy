(ns ^:figwheel-hooks com.taxonomy.ui
  (:require [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [secretary.core :as sec]
            [goog.object]
            [com.taxonomy.ui.routes :as routes]
            [com.taxonomy.ui.breadcrumb :as ui.breadcrumb]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.ui.form :as form]
            [com.taxonomy.ui.notifications-bar :as ui.notifications-bar]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.product :as product]
            [com.taxonomy.end-user.ui.login.view :as login.view]))

(rf/reg-fx
  :page-title
  (fn [title]
    (set! window.document.title title)))

(rf/reg-event-db
  ::set-view
  (fn [db [_ view]]
    (assoc db :ui/current-view view)))

(rf/reg-sub
  :ui/get-view
  (fn [db _]
    (:ui/current-view db)))

(rf/reg-event-db
  ::set-language
  (fn [db [_ language]]
    (assoc db :language (or language :gr))))

(rf/reg-sub
  :ui/language
  (fn [db _]
    (get db :language)))

(defn main-view
  []
  (let [user @(rf/subscribe [:ui/user])
        lang @(rf/subscribe [:ui/language])]
    [:article.box
     [:div.icons-wrapper.columns.is-multiline
      [:div.column.is-6
       [:a.button.is-link.m-1.is-fullwidth
        {:href (routes/products)}
        [:span (trans/translate lang ::product/secaas-products-management)]]]
      (when (end-user/is-admin? user)
        [:div.column.is-6
         [:a.button.is-link.m-1.is-fullwidth
          {:href (routes/users)}
          [:span (trans/translate lang ::end-user/users-management)]]])]]))

(defn child-view []
  (fn []
    (let [current-view @(rf/subscribe [:ui/get-view])]
      (rf/dispatch [::ui.breadcrumb/set-breadcrumb current-view])
      (cond
        (= :com.taxonomy.end-user/login current-view) [login.view/view]
        (= :com.taxonomy.end-user/register current-view) [login.view/view]
        (= :com.taxonomy.end-user/users current-view) [login.view/view]
        (= :com.taxonomy.end-user/user current-view) [login.view/view]
        (= :com.taxonomy.end-user/create-product current-view) [login.view/view]
        (= :com.taxonomy.end-user/products current-view) [login.view/view]
        (= :com.taxonomy.end-user/product current-view) [login.view/view]
        :else [main-view]))))

(defn app []
  (fn []
    (let [user @(rf/subscribe [:ui/user])
          lang @(rf/subscribe [:ui/language])]
      [:div
       [:div.container
        (if user
          [:div
           {:style {:float "right"}}
           [:a.button
            {:href (routes/user {:username (:username user)})}
            [form/icons {:icon     :user
                         :icon-css "fa-2x"}]]]
          [:div.columns
           {:style {:float "right"}}
           [:div.column
            [:a {:href (routes/login)}
             (trans/translate lang ::end-user/login)]]
           [:div.column
            [:a {:href (routes/register)}
             (trans/translate lang ::end-user/register)]]])
        [ui.notifications-bar/global-notifications-bar]
        [ui.navbar/view]
        [child-view]
        [:footer
         [:div.content.has-text-centered.mt-4
          [:div.content.has-text-centered.m-2.p-2
           [:span "Master Thesis of Panagiotis Pliatsikas, 2024 - 2025"]]]]]])))

(rf/reg-fx
  ::visit-current-url
  (fn [_]
    (sec/dispatch! (-> js/window .-location .-hash (subs 1)))))

(rf/reg-event-fx
  ::load-success
  (fn [_ _]
    {:dispatch-n         [[::set-language]]
     ::visit-current-url []}))

(defn main-app
  "Mount to root element"
  []
  (rf/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [(fn [] [app])] root-el)))

(defn init
  []
  (routes/setup-app-routes)
  (rf/dispatch-sync [::load-success])
  (main-app))

(defn ^:after-load re-render []
  (main-app))

(defonce start-up (do (init) true))
