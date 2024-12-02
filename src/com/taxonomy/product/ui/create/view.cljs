(ns com.taxonomy.product.ui.create.view
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.product :as product]
            [com.taxonomy.security-mechanisms :as security-mechanisms]
            [com.taxonomy.threats :as threats]
            [com.taxonomy.product.ui.create.model :as model]))

(defn- create-view
  [model lang]
  [:div
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "name"}
      (trans/translate lang ::product/name)]
     [:input.input {:key       "name"
                    :value     (:name model)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-input
                                               :name (-> e .-target .-value)]))}]]]
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "description"}
      (trans/translate lang ::product/description)]
     [:input.input {:key       "description"
                    :value     (:description model)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-input
                                               :description (-> e .-target .-value)]))}]]]
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "open-source"}
      (trans/translate lang ::product/open-source)]
     [:input.checkbox.ml-3 {:type      "checkbox"
                            :checked   (:open-source model)
                            :on-change #(rf/dispatch [::model/set-input
                                                      :open-source (-> % .-target .-checked)])}]]]
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "freely-available"}
      (trans/translate lang ::product/freely-available)]
     [:input.checkbox.ml-3 {:type      "checkbox"
                            :checked   (:freely-available model)
                            :on-change #(rf/dispatch [::model/set-input
                                                      :freely-available (-> % .-target .-checked)])}]]]
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "test-version"}
      (trans/translate lang ::product/test-version)]
     [:input.checkbox.ml-3 {:type      "checkbox"
                            :checked   (:test-version model)
                            :on-change #(rf/dispatch [::model/set-input
                                                      :test-version (-> % .-target .-checked)])}]]]
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "test-duration"}
      (trans/translate lang ::product/test-duration)]
     [:input.input {:key       "test-duration"
                    :value     (:test-duration model)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-input
                                               :test-duration (-> e .-target .-value)]))}]]]
   [:div.columns
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor "product-company"}
      (trans/translate lang ::product/product-company)]
     [:input.input {:key       "product-company"
                    :value     (:product-company model)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-input
                                               :product-company (-> e .-target .-value)]))}]]]])

(defn- create-button
  [{:keys [correct-inputs]} lang]
  [:div.columns
   [:div.column.is-6
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/create])
      :disabled (not correct-inputs)}
     [:span (trans/translate lang ::product/create-product)]]]])

(defn view []
  (let [model @(rf/subscribe [::model/ui-model])
        lang  (:language model)]
    [:article.box
     [ui.navbar/view]
     [create-view model lang]
     [create-button model lang]]))
