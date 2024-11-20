(ns com.taxonomy.ui.routes
  (:require [secretary.core :as sec]
            [re-frame.core :as rf]
            [goog.events :as events])
  (:import [goog History]
           [goog.history EventType]))

(defn history
  []
  (doto (History.)
    (events/listen EventType.NAVIGATE
                   (fn [event]
                     (let [fragment-url (.-token event)]
                       (sec/dispatch! fragment-url))))
    (.setEnabled true)))

(defn setup-app-routes
  []
  (sec/set-config! :prefix "#")

  (sec/defroute login "/login" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.end-user/login])
                (rf/dispatch [:com.taxonomy.end-user.ui.login.model/init]))

  (sec/defroute register "/register" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.end-user/register])
                (rf/dispatch [:com.taxonomy.end-user.ui.create.model/init]))

  (sec/defroute users "/users" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.end-user/users])
                (rf/dispatch [:com.taxonomy.end-user.ui.list.model/init]))

  (sec/defroute user "/users/:username" [username]
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.end-user/user])
                (rf/dispatch [:com.taxonomy.ui.breadcrumb/set-breadcrumb-params {:username username}])
                (rf/dispatch [:com.taxonomy.end-user.ui.edit.model/init username]))

  (sec/defroute change-password "/users/:username/change-password" [username]
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.end-user/change-password])
                (rf/dispatch [:com.taxonomy.ui.breadcrumb/set-breadcrumb-params {:username username}])
                (rf/dispatch [:com.taxonomy.end-user.ui.change-password.model/init username]))

  (sec/defroute reset-password "/reset-password" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.end-user/reset-password])
                (rf/dispatch [:com.taxonomy.end-user.ui.reset-password.model/init]))

  (sec/defroute create-product "/create-product" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.product/create-product]))

  (sec/defroute products "/products" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.product/products]))

  (sec/defroute product "/product/:id" [id]
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.product/product])
                (rf/dispatch [:com.taxonomy.ui.breadcrumb/set-breadcrumb-params {:product-id id}]))

  (sec/defroute main-view "/" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.ui/main-view]))

  (sec/defroute "*" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.ui/main-view]))

  (history))
