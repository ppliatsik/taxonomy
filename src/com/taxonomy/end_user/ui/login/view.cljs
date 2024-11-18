(ns com.taxonomy.end-user.ui.login.view
  (:require [re-frame.core :as rf]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.end-user.ui.login.model :as model]))

(defn view []
  (let [model @(rf/subscribe [::model/form-data])
        lang  @(rf/subscribe [:ui/language])]
    [:article.box
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
                                                 (-> e .-target .-value)]))}]]]
     [:div.columns
      [:div.column.is-6
       [:button.button.is-info
        {:on-click #(rf/dispatch [::model/login])}
        [:span (trans/translate lang ::end-user/login)]]]]]))
