(ns com.taxonomy.product.ui.view
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.ui.routes :as routes]
            [com.taxonomy.product :as product]
            [com.taxonomy.end-user :as end-user]))

(defn view []
  (let [login-user @(rf/subscribe [:ui/user])
        lang       @(rf/subscribe [:ui/language])]
    [:article.box
     [ui.navbar/view]
     [:div.columns.is-centered
      [:div.column.is-4
       [:a.button.is-link.m-1.is-fullwidth
        {:href (routes/products)}
        [:span (trans/translate lang ::product/discovery)]]]
     (when (or (end-user/is-user? login-user)
               (end-user/is-admin? login-user))
       [:div.columns
        {:style {:margin-top "0.01%"}}
        [:div.column.is-8
         {:style {:margin-left "2%"}}
         [:a.button.is-link.m-1.is-fullwidth
          {:href (routes/my-products)}
          [:span (trans/translate lang ::product/my-products)]]]
        [:div.column.is-8
         [:a.button.is-link.m-1.is-fullwidth
          {:href (routes/create-product)}
          [:span (trans/translate lang ::product/create-product)]]]])]]))
