(ns com.taxonomy.product.ui.create.view
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.product :as product]
            [com.taxonomy.product.ui.create.model :as model]))

(defn- create-view
  [model lang]
  )

(defn- create-button
  [lang]
  [:div.columns
   [:div.column.is-6
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/create])
      :disabled false}
     [:span (trans/translate lang ::product/create-product)]]]])

(defn view []
  (let [model @(rf/subscribe [::model/ui-model])
        lang  (:language model)]
    [:article.box
     [ui.navbar/view]
     [create-view model lang]
     [create-button lang]]))
