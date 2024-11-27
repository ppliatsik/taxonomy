(ns com.taxonomy.end-user.ui.edit.view
  (:require [re-frame.core :as rf]
            [clojure.string :as clj.str]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.ui.form :as form]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.ui.routes :as routes]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.end-user.ui.edit.model :as model]))

(defn- control-buttons
  [login-user {:keys [user username]} lang]
  [:div.columns
   [:div.column.is-2
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/toggle-edit])}
     [:span (trans/translate lang ::end-user/edit)]]]
   [:div.column.is-2
    {:style {:margin-left "-2.5%"}}
    [:a.button.is-link.is-info
     {:href (routes/change-password {:username username})}
     [:span (trans/translate lang ::end-user/change-password)]]]
   (when (end-user/is-admin? login-user)
     (if (:active user)
       [:div.column.is-2
        [:button.button.is-info
         {:on-click #(rf/dispatch [::model/deactivate])}
         [:span (trans/translate lang ::end-user/deactivate)]]]
       [:div.columns
        {:style {:margin-top "0.3%"}}
        [:div.column.is-4
         [:button.button.is-info
          {:on-click #(rf/dispatch [::model/activate])}
          [:span (trans/translate lang ::end-user/activate)]]]
        [:div.column.is-2
         [:button.button.is-info
          {:on-click #(rf/dispatch [::model/send-activation-link])}
          [:span (trans/translate lang ::end-user/send-activate-link)]]]]))
   (when (or (end-user/is-admin? login-user)
             (end-user/is-current-user? login-user username))
     [:div.column.is-2
      [:button.button.is-danger
       {:on-click #(rf/dispatch [::model/show-delete-confirmation-box])}
       [:span (trans/translate lang ::end-user/delete)]]])])

(defn- user-fields
  [{:keys [user can-edit] :as model} lang]
  [:div
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "username"}
      (trans/translate lang ::end-user/username)]
     [:input.input {:key       "username"
                    :value     (:username user)
                    :disabled  true
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-username
                                               (-> e .-target .-value)]))}]]]
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "first-name"}
      (trans/translate lang ::end-user/first-name)]
     [:input.input {:key       "first-name"
                    :value     (:first-name model)
                    :disabled  (not can-edit)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-first-name
                                               (-> e .-target .-value)]))}]
     (when (and (not (clj.str/blank? (:first-name model)))
                (not (end-user/is-only-alphabetic-str (:first-name model))))
       [:p.help.is-danger (trans/translate lang ::end-user/wrong-input)])]]
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "last-name"}
      (trans/translate lang ::end-user/last-name)]
     [:input.input {:key       "last-name"
                    :value     (:last-name model)
                    :disabled  (not can-edit)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-last-name
                                               (-> e .-target .-value)]))}]
     (when (and (not (clj.str/blank? (:last-name model)))
                (not (end-user/is-only-alphabetic-str (:last-name model))))
       [:p.help.is-danger (trans/translate lang ::end-user/wrong-input)])]]
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "email"}
      (trans/translate lang ::end-user/email)]
     [:input.input {:key       "email"
                    :value     (:email model)
                    :disabled  (not can-edit)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-email
                                               (-> e .-target .-value)]))}]
     (when (and (not (clj.str/blank? (:email model)))
                (not (end-user/email-is-valid? (:email model))))
       [:p.help.is-danger (trans/translate lang ::end-user/wrong-input)])]]])

(defn- submit-button
  [{:keys [can-edit correct-inputs]} lang]
  [:div.columns
   [:div.column.is-6
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/edit])
      :disabled (or (not can-edit) (not correct-inputs))}
     [:span (trans/translate lang :com.taxonomy.ui/submit)]]]])

(defn view []
  (let [login-user @(rf/subscribe [:ui/user])
        model      @(rf/subscribe [::model/ui-model])
        lang       (:language model)]
    [:article.box
     [ui.navbar/view]
     [control-buttons login-user model lang]
     [user-fields model lang]
     [submit-button model lang]
     (when-not (:hide-delete-confirmation-box model)
       (form/delete-confirmation-dialog ::model/delete ::model/hide-delete-confirmation-box lang))]))
