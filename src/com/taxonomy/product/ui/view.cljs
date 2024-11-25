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
     [:div
      [:div.columns
       [:div.column.is-6
        [:a.button.is-link.m-1.is-fullwidth
         {:href (routes/products)}
         [:span (trans/translate lang ::product/discovery)]]]]
      (when (or (end-user/is-user? login-user)
                (end-user/is-admin? login-user))
        [:div
         [:div.columns
          [:div.column.is-6
           [:a.button.is-link.m-1.is-fullwidth
            {:href (routes/my-products)}
            [:span (trans/translate lang ::end-user/my-products)]]]]
         [:div.columns
          [:div.column.is-6
           [:a.button.is-link.m-1.is-fullwidth
            {:href (routes/create-product)}
            [:span (trans/translate lang ::end-user/create-product)]]]]])]]))
