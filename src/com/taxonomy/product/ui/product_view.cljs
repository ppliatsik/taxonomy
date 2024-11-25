(ns com.taxonomy.product.ui.product-view
  (:require [com.taxonomy.translations :as trans]
            [com.taxonomy.product :as product]
            [com.taxonomy.ui.routes :as routes]))

(defn- table-row
  [{:keys [id name]}]
  [:tr {:key id}
   [:td.nowrap
    [:a {:href (routes/product {:id id})}
     name]]])

(defn list-products
  [{:keys [products]} lang]
  [:table.table.is-fullwidth.is-stripped.is-hoverable.vertical-align-middle.fixed
   [:thead.has-background-light
    [:tr
     [:td (trans/translate lang ::product/name)]]]
   [:tbody
    (map (fn [product]
           ^{:key (:id product)}
           [table-row product])
         products)]])
