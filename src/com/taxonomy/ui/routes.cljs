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

  (sec/defroute email-activate-account "/email-activate-account" [query-params]
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.end-user/email-activate-account])
                (rf/dispatch [:com.taxonomy.end-user.ui.email-activation.model/init query-params]))

  (sec/defroute create-product "/create-product" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.product/create-product])
                (rf/dispatch [:com.taxonomy.product.ui.create.model/init]))

  (sec/defroute products "/products" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.product/products])
                (rf/dispatch [:com.taxonomy.product.ui.list.model/init]))

  (sec/defroute product "/product/:id" [id]
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.product/product])
                (rf/dispatch [:com.taxonomy.ui.breadcrumb/set-breadcrumb-params {:id id}])
                (rf/dispatch [:com.taxonomy.product.ui.show.model/init id]))

  (sec/defroute my-products "/my-products" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.product/my-products])
                (rf/dispatch [:com.taxonomy.product.ui.my-products.model/init]))

  (sec/defroute secaas-products-management "/secaas-products-management" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.product/secaas-products-management]))

  (sec/defroute main-view "/" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.ui/main-view]))

  (sec/defroute "*" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.ui/main-view]))

  (history))
