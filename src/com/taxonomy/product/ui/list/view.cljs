(ns com.taxonomy.product.ui.list.view
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.product :as product]
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
    (trans/translate lang ::product/choose-weights)]])

(defn- submit-buttons
  [lang]
  [:div.columns
   [:div.column.is-4
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/match])}
     [:span (trans/translate lang ::product/match)]]]
   [:div.column.is-4
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/classification])}
     [:span (trans/translate lang ::product/classification)]]]
   [:div.column.is-4
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/discovery])}
     [:span (trans/translate lang ::product/discovery)]]]])

(defn view []
  (let [model @(rf/subscribe [::model/ui-model])
        lang  (:language model)]
    [:article.box
     [ui.navbar/view]
     [criteria-view model lang]
     [weights-view model lang]
     [submit-buttons lang]
     [product-view/list-products model lang]]))
