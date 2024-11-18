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
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.end-user/register]))

  (sec/defroute users "/users" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.end-user/users]))

  (sec/defroute user "/users/:username" [username]
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.end-user/user])
                (rf/dispatch [:gt.podman.ui.breadcrumb/set-breadcrumb-params {:username username}]))

  (sec/defroute create-product "/create-product" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.product/create-product]))

  (sec/defroute products "/products" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.product/products]))

  (sec/defroute product "/product/:id" [id]
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.product/product])
                (rf/dispatch [:gt.podman.ui.breadcrumb/set-breadcrumb-params {:product-id id}]))

  (sec/defroute main-view "/" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.ui/main-view]))

  (sec/defroute "*" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.ui/main-view]))

  (history))
