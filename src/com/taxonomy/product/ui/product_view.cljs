(ns com.taxonomy.product.ui.product-view
  (:require [clojure.string :as clj.str]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.product :as product]
            [com.taxonomy.ui.routes :as routes]))

(defn- table-row
  [{:keys [id name created-by delivery-methods deployment-models
           product-categories product-company marketplaces]}]
  [:tr {:key id}
   [:td.nowrap
    [:a {:href (routes/product {:id id})}
     name]]
   [:td.nowrap created-by]
   [:td.nowrap (clj.str/join delivery-methods ",")]
   [:td.nowrap (clj.str/join deployment-models ",")]
   [:td.nowrap (clj.str/join product-categories ",")]
   [:td.nowrap (clj.str/join product-company ",")]
   [:td.nowrap (clj.str/join marketplaces ",")]])

(defn list-products
  [{:keys [products]} lang]
  [:table.table.is-fullwidth.is-stripped.is-hoverable.vertical-align-middle.fixed
   [:thead.has-background-light
    [:tr
     [:td (trans/translate lang ::product/name)]
     [:td (trans/translate lang ::product/created-by)]
     [:td (trans/translate lang ::product/delivery-methods)]
     [:td (trans/translate lang ::product/deployment-models)]
     [:td (trans/translate lang ::product/product-categories)]
     [:td (trans/translate lang ::product/product-company)]
     [:td (trans/translate lang ::product/marketplaces)]]]
   [:tbody
    (map (fn [product]
           ^{:key (:id product)}
           [table-row product])
         products)]])
