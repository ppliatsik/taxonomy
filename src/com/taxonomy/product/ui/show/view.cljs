(ns com.taxonomy.product.ui.show.view
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.ui.form :as form]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.product :as product]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.product.ui.show.model :as model]))

(defn- control-buttons
  [login-user {:keys [product]} lang]
  (when product
    [:div.columns
     (when (end-user/is-current-user? login-user (:created-by product))
       [:div.columns
        [:div.column.is-4
         {:style {:margin-right "12%"
                  :margin-top   "7%"}}
         [:button.button.is-info
          {:on-click #(rf/dispatch [::model/publish])}
          [:span (trans/translate lang ::product/publish)]]]
        [:div.column.is-4
         {:style {:margin-top "7%"}}
         [:button.button.is-info
          {:on-click #(rf/dispatch [::model/unpublish])}
          [:span (trans/translate lang ::product/unpublish)]]]])
     (when (or (end-user/is-admin? login-user)
               (end-user/is-current-user? login-user (:created-by product)))
       [:div.column.is-2
        {:style {:margin-left "1.5%"
                 :margin-top  "0.9%"}}
        [:button.button.is-danger
         {:on-click #(rf/dispatch [::model/show-delete-confirmation-box])}
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
     [product-view model lang]
     (when-not (:hide-delete-confirmation-box model)
       (form/delete-confirmation-dialog ::model/delete ::model/hide-delete-confirmation-box lang))]))
