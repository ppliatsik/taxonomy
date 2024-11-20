(ns com.taxonomy.end-user.ui.create.view
  (:require [re-frame.core :as rf]
            [clojure.string :as clj.str]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.end-user.ui.create.model :as model]))

(defn- user-fields
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
                                               (-> e .-target .-value)]))}]
     (when (and (not (clj.str/blank? (:username model)))
                (not (end-user/username-is-valid? (:username model))))
       [:p.help.is-danger (trans/translate lang ::end-user/wrong-input)])]]
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "password"}
      (trans/translate lang ::end-user/password)]
     [:input.input {:key       "password"
                    :type      :password
                    :value     (:password model)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-password
                                               (-> e .-target .-value)]))}]
     (when (and (not (clj.str/blank? (:password model)))
                (not (end-user/password-is-valid? (:password model))))
       [:p.help.is-danger (trans/translate lang ::end-user/wrong-input)])]]
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "password-verification"}
      (trans/translate lang ::end-user/password-verification)]
     [:input.input {:key       "password-verification"
                    :type      :password
                    :value     (:password-verification model)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-password-verification
                                               (-> e .-target .-value)]))}]
     (when (or (and (not (clj.str/blank? (:password-verification model)))
                    (not (end-user/password-is-valid? (:password-verification model))))
               (not= (:password-verification model) (:password model)))
       [:p.help.is-danger (trans/translate lang ::end-user/wrong-input)])]]
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "first-name"}
      (trans/translate lang ::end-user/first-name)]
     [:input.input {:key       "first-name"
                    :value     (:first-name model)
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
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-email
                                               (-> e .-target .-value)]))}]
     (when (and (not (clj.str/blank? (:email model)))
                (not (end-user/email-is-valid? (:email model))))
       [:p.help.is-danger (trans/translate lang ::end-user/wrong-input)])]]])

(defn- register-button
  [{:keys [correct-inputs]} lang]
  [:div.columns
   [:div.column.is-6
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/register])
      :disabled (not correct-inputs)}
     [:span (trans/translate lang ::end-user/register)]]]])

(defn view []
  (let [model @(rf/subscribe [::model/ui-model])
        lang  (:language model)]
    [:article.box
     [ui.navbar/view]
     [user-fields model lang]
     [register-button model lang]]))
