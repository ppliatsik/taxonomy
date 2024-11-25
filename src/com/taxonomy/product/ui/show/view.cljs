(ns com.taxonomy.product.ui.show.view
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.product :as product]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.product.ui.show.model :as model]))

(defn- control-buttons
  [login-user {:keys [product]} lang]
  (when product
    [:div.columns
     (when (end-user/is-current-user? login-user (:created-by product))
       [:div.column.is-2
        [:button.button.is-info
         {:on-click #(rf/dispatch [::model/publish])}
         [:span (trans/translate lang ::product/publish)]]
        [:button.button.is-info
         {:on-click #(rf/dispatch [::model/unpublish])}
         [:span (trans/translate lang ::product/unpublish)]]])
     (when (or (end-user/is-admin? login-user)
               (end-user/is-current-user? login-user (:created-by product)))
       [:div.column.is-2
        [:button.button.is-danger
         {:on-click #(rf/dispatch [::model/delete])}
         [:span (trans/translate lang ::product/delete)]]])]))

(defn- product-view
  [{:keys [product]} lang]
  (when product
    ))

(defn view []
  (let [login-user @(rf/subscribe [:ui/user])
        model      @(rf/subscribe [::model/ui-model])
        lang       (:language model)]
    [:article.box
     [ui.navbar/view]
     [control-buttons login-user model lang]
     [product-view model lang]]))
