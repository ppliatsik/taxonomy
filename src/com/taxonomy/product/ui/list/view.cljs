(ns com.taxonomy.product.ui.list.view
  (:require [re-frame.core :as rf]
            [clojure.spec.alpha :as s]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.product :as product]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.product.ui.product-view :as product-view]
            [com.taxonomy.product.ui.list.model :as model]))

(defn- criteria-view
  [model lang]
  [:div.box.has-background-success-light
   [:span.is-large
    (trans/translate lang ::product/choose-criteria)]])

(defn- weights-view
  [model lang]
  [:div.box.has-background-success-light
   [:span.is-large
    (trans/translate lang ::product/choose-weights)]
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "charge-packets-w"}
      (trans/translate lang ::product/charge-packets)]
     [:input.input {:key       "charge-packets-w"
                    :value     (:charge-packets-w model)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-input
                                               :charge-packets-w (-> e .-target .-value)]))}]
     (when-not (s/valid? ::product/weight-spec (:charge-packets-w model))
       [:p.help.is-danger (trans/translate lang ::end-user/wrong-input)])]]
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "non-functional-guarantees-value-w"}
      (str (trans/translate lang ::product/non-functional-guarantees) "-" (trans/translate lang ::product/value))]
     [:input.input {:key       "non-functional-guarantees-value-w"
                    :value     (:non-functional-guarantees-value-w model)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-input
                                               :non-functional-guarantees-value-w (-> e .-target .-value)]))}]
     (when-not (s/valid? ::product/weight-spec (:non-functional-guarantees-value-w model))
       [:p.help.is-danger (trans/translate lang ::end-user/wrong-input)])]]
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "restrictions-value-w"}
      (str (trans/translate lang ::product/restrictions) "-" (trans/translate lang ::product/value))]
     [:input.input {:key       "restrictions-value-w"
                    :value     (:restrictions-value-w model)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-input
                                               :restrictions-value-w (-> e .-target .-value)]))}]
     (when-not (s/valid? ::product/weight-spec (:restrictions-value-w model))
       [:p.help.is-danger (trans/translate lang ::end-user/wrong-input)])]]
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "test-duration-w"}
      (trans/translate lang ::product/test-duration)]
     [:input.input {:key       "test-duration-w"
                    :value     (:test-duration-w model)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-input
                                               :test-duration-w (-> e .-target .-value)]))}]
     (when-not (s/valid? ::product/weight-spec (:test-duration-w model))
       [:p.help.is-danger (trans/translate lang ::end-user/wrong-input)])]]])

(defn- submit-buttons
  [{:keys [correct-inputs]} lang]
  [:div.columns
   [:div.column.is-4
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/match])
      :disabled false}
     [:span (trans/translate lang ::product/match)]]]
   [:div.column.is-4
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/classification])
      :disabled (not correct-inputs)}
     [:span (trans/translate lang ::product/classification)]]]
   [:div.column.is-4
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/discovery])
      :disabled (not correct-inputs)}
     [:span (trans/translate lang ::product/discovery)]]]])

(defn view []
  (let [model @(rf/subscribe [::model/ui-model])
        lang  (:language model)]
    [:article.box
     [ui.navbar/view]
     [criteria-view model lang]
     [weights-view model lang]
     [submit-buttons model lang]
     [product-view/list-products model lang]]))
