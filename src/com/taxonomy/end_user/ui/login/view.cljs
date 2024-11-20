(ns com.taxonomy.end-user.ui.login.view
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.ui.routes :as routes]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.end-user.ui.login.model :as model]))

(defn- fields
  [model lang]
  [:div
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "username"}
      (trans/translate lang ::end-user/username)]
     [:input.input {:key       "username"
                    :value     (:username model)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-username
                                               (-> e .-target .-value)]))}]]]
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "password"}
      (trans/translate lang ::end-user/password)]
     [:input.input {:key       "password"
                    :type      :password
                    :value     (:password model)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-password
                                               (-> e .-target .-value)]))}]]]])

(defn- login-button
  [lang]
  [:div.columns
   [:div.column.is-1
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/login])}
     [:span (trans/translate lang ::end-user/login)]]]
   [:div.column.is-6
    [:a {:href (routes/reset-password)}
     (trans/translate lang ::end-user/reset-password)]]])

(defn view []
  (let [model @(rf/subscribe [::model/ui-model])
        lang  (:language model)]
    [:article.box
     [ui.navbar/view]
     [fields model lang]
     [login-button lang]]))
