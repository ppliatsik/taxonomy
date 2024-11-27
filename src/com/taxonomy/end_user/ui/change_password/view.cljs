(ns com.taxonomy.end-user.ui.change-password.view
  (:require [re-frame.core :as rf]
            [clojure.string :as clj.str]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.end-user.ui.change-password.model :as model]))

(defn- password-fields
  [model lang]
  [:div
   [:div
    [:label.label.mb-0 {:htmlFor "old-password"}
     (trans/translate lang ::end-user/old-password)]
    [:input.input {:key       "old-password"
                   :type      :password
                   :value     (:old-password model)
                   :on-change (fn [e]
                                (rf/dispatch [::model/set-old-password
                                              (-> e .-target .-value)]))}]
    (when (and (not (clj.str/blank? (:old-password model)))
               (not (end-user/password-is-valid? (:old-password model))))
      [:p.help.is-danger (trans/translate lang ::end-user/wrong-input)])]
   [:div
    {:style {:margin-top "2%"}}
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
      [:p.help.is-danger (trans/translate lang ::end-user/wrong-input)])]
   [:div
    {:style {:margin-top "2%"}}
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
      [:p.help.is-danger (trans/translate lang ::end-user/wrong-input)])]])

(defn- submit-button
  [{:keys [correct-inputs]} lang]
  [:div.columns
   {:style {:margin-top "2%"}}
   [:div.column.is-6
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/change-password])
      :disabled (not correct-inputs)}
     [:span (trans/translate lang :com.taxonomy.ui/submit)]]]])

(defn view []
  (let [model @(rf/subscribe [::model/ui-model])
        lang  (:language model)]
    [:article.box
     [ui.navbar/view]
     [password-fields model lang]
     [submit-button model lang]]))
