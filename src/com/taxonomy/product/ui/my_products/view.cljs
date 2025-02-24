(ns com.taxonomy.product.ui.my-products.view
  (:require [re-frame.core :as rf]
            [clojure.string :as clj.str]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.ui.routes :as routes]
            [com.taxonomy.ui.form :as form]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.product :as product]
            [com.taxonomy.product.ui.my-products.model :as model]))

(defn- table-row
  [{:keys [id name creator delivery-methods deployment-models
           product-categories product-company marketplaces] :as p}
   lang]
  [:tr {:key id}
   [:td.nowrap
    [:a {:href (routes/product {:id id})}
     name]]
   [:td.nowrap creator]
   [:td.nowrap (clj.str/join "," delivery-methods)]
   [:td.nowrap (clj.str/join "," deployment-models)]
   [:td.nowrap (clj.str/join "," product-categories)]
   [:td.nowrap product-company]
   [:td.nowrap (clj.str/join "," marketplaces)]
   [:td.nowrap
    [:button.button.is-danger
     {:on-click #(rf/dispatch [::model/show-delete-confirmation-box id])}
     [:span (trans/translate lang ::product/delete)]]]])

(defn list-products
  [{:keys [products]} lang]
  [:table.table.is-fullwidth.is-stripped.is-hoverable.vertical-align-middle.fixed
   [:thead.has-background-light
    [:tr
     [:td (trans/translate lang ::product/name)]
     [:td (trans/translate lang ::product/creator)]
     [:td (trans/translate lang ::product/delivery-methods)]
     [:td (trans/translate lang ::product/deployment-models)]
     [:td (trans/translate lang ::product/product-categories)]
     [:td (trans/translate lang ::product/product-company)]
     [:td (trans/translate lang ::product/marketplaces)]
     [:td (trans/translate lang ::product/delete)]]]
   [:tbody
    (map (fn [product]
           ^{:key (:id product)}
           [table-row product lang])
         products)]])

(defn view []
  (let [model @(rf/subscribe [::model/ui-model])
        lang  (:language model)]
    [:article.box
     [ui.navbar/view]
     [list-products model lang]
     (when-not (:hide-delete-confirmation-box model)
       (form/delete-confirmation-dialog ::model/delete ::model/hide-delete-confirmation-box lang))]))
