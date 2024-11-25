(ns com.taxonomy.product.ui.my-products.view
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.product.ui.product-view :as product-view]
            [com.taxonomy.product.ui.my-products.model :as model]))

(defn view []
  (let [model @(rf/subscribe [::model/ui-model])
        lang  (:language model)]
    [:article.box
     [ui.navbar/view]
     [product-view/list-products model lang]]))
