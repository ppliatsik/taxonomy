(ns ^:figwheel-hooks com.taxonomy.ui
  (:require [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [secretary.core :as sec]
            [goog.object]
            [com.taxonomy.ui.routes :as routes]
            [com.taxonomy.ui.breadcrumb :as ui.breadcrumb]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.ui.form :as form]
            [com.taxonomy.ui.notifications-bar :as ui.notifications-bar]))

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
  [:article.box
   [ui.navbar/view]
   [:div.icons-wrapper.columns.is-multiline
    ]])

(defn child-view []
  (fn []
    (let [current-view @(rf/subscribe [:ui/get-view])]
      (rf/dispatch [::ui.breadcrumb/set-breadcrumb current-view])
      (cond
        :else [main-view]))))

(defn app []
  (fn []
    [:div
     [:div.container
      [ui.notifications-bar/global-notifications-bar]
      [child-view]
      [:footer
       [:div.content.has-text-centered.mt-4
        [:div.content.has-text-centered.m-2.p-2
         [:span ""]]]]]]))

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
