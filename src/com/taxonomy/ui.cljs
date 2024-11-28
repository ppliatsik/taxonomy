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
            [com.taxonomy.ui.auth :as auth]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.product :as product]

            [com.taxonomy.end-user.ui.login.view :as login.view]
            [com.taxonomy.end-user.ui.create.view :as user.create.view]
            [com.taxonomy.end-user.ui.list.view :as user.list.view]
            [com.taxonomy.end-user.ui.edit.view :as user.edit.view]
            [com.taxonomy.end-user.ui.change-password.view :as user.change-password]
            [com.taxonomy.end-user.ui.reset-password.view :as user.reset-password]
            [com.taxonomy.end-user.ui.email-activation.view :as user.email-activation]

            [com.taxonomy.product.ui.view :as product.view]
            [com.taxonomy.product.ui.create.view :as product.create.view]
            [com.taxonomy.product.ui.list.view :as product.list.view]
            [com.taxonomy.product.ui.show.view :as product.show.view]
            [com.taxonomy.product.ui.my-products.view :as my-products.view]))

(def languages
  [{:src      "images/gr.gif"
    :alt      "ελληνικά"
    :language :gr}
   {:src      "images/en.gif"
    :alt      "english"
    :language :en}])

(rf/reg-fx
  :page-title
  (fn [title]
    (set! window.document.title title)))

(rf/reg-fx
  :url
  (fn [url]
    (.assign window.location url)))

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

(defn language-selector
  [language]
  (let [l (->> languages
               (remove #(= language (:language %)))
               first)]
    [:div
     {:data-tooltip (trans/translate language :com.taxonomy.ui/select-language)}
     [:img.button.is-ghost.pl-1
      {:src      (:src l)
       :alt      (:alt l)
       :on-click #(rf/dispatch [::set-language (:language l)])}]]))

(defn main-view
  []
  (let [user @(rf/subscribe [:ui/user])
        lang @(rf/subscribe [:ui/language])]
    [:article.box
     [ui.navbar/view]
     [:div.icons-wrapper.columns.is-multiline
      [:div.column.is-6
       [:a.button.is-link.m-1.is-fullwidth
        {:href (routes/secaas-products-management)}
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
        (= :com.taxonomy.ui/main-view current-view) [main-view]
        (= :com.taxonomy.end-user/login current-view) [login.view/view]
        (= :com.taxonomy.end-user/register current-view) [user.create.view/view]
        (= :com.taxonomy.end-user/users current-view) [user.list.view/view]
        (= :com.taxonomy.end-user/user current-view) [user.edit.view/view]
        (= :com.taxonomy.end-user/change-password current-view) [user.change-password/view]
        (= :com.taxonomy.end-user/reset-password current-view) [user.reset-password/view]
        (= :com.taxonomy.end-user/email-activate-account current-view) [user.email-activation/view]
        (= :com.taxonomy.product/create-product current-view) [product.create.view/view]
        (= :com.taxonomy.product/products current-view) [product.list.view/view]
        (= :com.taxonomy.product/product current-view) [product.show.view/view]
        (= :com.taxonomy.product/my-products current-view) [my-products.view/view]
        (= :com.taxonomy.product/secaas-products-management current-view) [product.view/view]
        :else [main-view]))))

(defn app []
  (fn []
    (let [user @(rf/subscribe [:ui/user])
          lang @(rf/subscribe [:ui/language])]
      [:div
       [:div.container
        [:div.navbar-menu
         [:div.navbar-item
          {:style {:position "fixed"
                   :float    "right"}}
          [language-selector lang]]]
        (if user
          [:div.columns
           {:style {:float "right"}}
           [:div.column
            [:a.button
             {:href (routes/user {:username (:username user)})}
             [form/icons {:icon     :user
                          :icon-css "fa-2x"}]]]
           [:div.column
            [:button.button.is-info
             {:on-click #(rf/dispatch [::auth/logout])}
             [:span (trans/translate lang ::end-user/logout)]]]]
          [:div.columns
           {:style {:float "right"}}
           [:div.column
            [:a {:href (routes/login)}
             (trans/translate lang ::end-user/login)]]
           [:div.column
            [:a {:href (routes/register)}
             (trans/translate lang ::end-user/register)]]])
        [ui.notifications-bar/global-notifications-bar]
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
