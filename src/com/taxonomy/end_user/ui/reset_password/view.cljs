(ns com.taxonomy.end-user.ui.reset-password.view
  (:require [re-frame.core :as rf]
            [clojure.string :as clj.str]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.end-user.ui.reset-password.model :as model]))

(defn- email-field
  [model lang]
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
      [:p.help.is-danger (trans/translate lang ::end-user/wrong-input)])]])

(defn- submit-button
  [{:keys [correct-inputs]} lang]
  [:div.columns
   [:div.column.is-6
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/reset-password])
      :disabled (not correct-inputs)}
     [:span (trans/translate lang :com.taxonomy.ui/submit)]]]])

(defn view []
  (let [model @(rf/subscribe [::model/ui-model])
        lang  (:language model)]
    [:article.box
     [ui.navbar/view]
     [email-field model lang]
     [submit-button model lang]]))
