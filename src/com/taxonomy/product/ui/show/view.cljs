(ns com.taxonomy.product.ui.show.view
  (:require [re-frame.core :as rf]
            [clojure.string :as clj.str]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.ui.form :as form]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.product :as product]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.product.ui.show.model :as model]))

(defn- show-guarantees-restrictions
  [data lang]
  (->> data
       (map (fn [cur]
              [:div.column
               [:table.table.is-fullwidth.is-stripped.is-hoverable.vertical-align-middle.fixed
                [:tr
                 [:td (trans/translate lang ::product/property)]
                 [:td (:property cur)]]
                [:tr
                 [:td (trans/translate lang ::product/operator)]
                 [:td (:operator cur)]]
                [:tr
                 [:td (trans/translate lang ::product/value)]
                 [:td (:value cur)]]
                [:tr
                 [:td (trans/translate lang ::product/metric)]
                 [:td (:metric cur)]]
                [:tr
                 [:td (trans/translate lang ::product/direction-of-values)]
                 [:td (if (:direction-of-values cur)
                        (trans/translate lang :com.taxonomy.ui/positive)
                        (trans/translate lang :com.taxonomy.ui/negative))]]
                [:tr
                 [:td (trans/translate lang ::product/unit)]
                 [:td (:unit cur)]]]]))
       (into [:div.columns])))

(defn- control-buttons
  [login-user {:keys [product]} lang]
  (when product
    [:div.columns
     (when (end-user/is-current-user? login-user (:created-by product))
       [:div.columns
        [:div.column.is-4
         {:style {:margin-right "12%"
                  :margin-top   "7%"}}
         [:button.button.is-info
          {:on-click #(rf/dispatch [::model/publish])}
          [:span (trans/translate lang ::product/publish)]]]
        [:div.column.is-4
         {:style {:margin-top "7%"}}
         [:button.button.is-info
          {:on-click #(rf/dispatch [::model/unpublish])}
          [:span (trans/translate lang ::product/unpublish)]]]])
     (when (or (end-user/is-admin? login-user)
               (end-user/is-current-user? login-user (:created-by product)))
       [:div.column.is-2
        {:style {:margin-left "1.5%"
                 :margin-top  "0.9%"}}
        [:button.button.is-danger
         {:on-click #(rf/dispatch [::model/show-delete-confirmation-box])}
         [:span (trans/translate lang ::product/delete)]]])]))

(defn- product-view
  [{:keys [product]} lang]
  (when product
    [:table.table.is-fullwidth.is-stripped.is-hoverable.vertical-align-middle.fixed
     [:tr
      [:td (trans/translate lang ::product/name)]
      [:td (:name product)]]
     [:tr
      [:td (trans/translate lang ::product/published)]
      [:td (if (:published product)
             (trans/translate lang :com.taxonomy.ui/yes)
             (trans/translate lang :com.taxonomy.ui/no))]]
     [:tr
      [:td (trans/translate lang ::product/description)]
      [:td (:description product)]]
     [:tr
      [:td (trans/translate lang ::product/created-by)]
      [:td (:created-by product)]]
     [:tr
      [:td (trans/translate lang ::product/delivery-methods)]
      [:td (clj.str/join (:delivery-methods product) ",")]]
     [:tr
      [:td (trans/translate lang ::product/deployment-models)]
      [:td (clj.str/join (:deployment-models product) ",")]]
     [:tr
      [:td (trans/translate lang ::product/product-categories)]
      [:td (clj.str/join (:product-categories product) ",")]]
     [:tr
      [:td (trans/translate lang ::product/cost-model)]
      [:td (->> (:cost-model product)
                (map (fn [cm]
                       [:div.column
                        [:table.table.is-fullwidth.is-stripped.is-hoverable.vertical-align-middle.fixed
                         [:tr
                          [:td (trans/translate lang ::product/cost-model-types)]
                          [:td (clj.str/join (:cost-model-types cm) ",")]]
                         [:tr
                          [:td (trans/translate lang ::product/charge-packets)]
                          [:td (:charge-packets cm)]]
                         [:tr
                          [:td (trans/translate lang ::product/time-charge-types)]
                          [:td (clj.str/join (:time-charge-types cm) ",")]]]]))
                (into [:div.columns]))]]
     [:tr
      [:td (trans/translate lang ::product/security-mechanisms)]
      [:td (as-> (model/get-all-keys (:security-mechanisms product)) $
                 (map #(trans/translate lang (keyword "com.taxonomy.security-mechanisms" %)))
                 (clj.str/join "\n" $))]]
     [:tr
      [:td (trans/translate lang ::product/non-functional-guarantees)]
      [:td [show-guarantees-restrictions (:non-functional-guarantees product) lang]]]
     [:tr
      [:td (trans/translate lang ::product/protection-types)]
      [:td (clj.str/join (:product-categories product) ",")]]
     [:tr
      [:td (trans/translate lang ::product/security-properties)]
      [:td (clj.str/join (:security-properties product) ",")]]
     [:tr
      [:td (trans/translate lang ::product/protected-items)]
      [:td (clj.str/join (:protected-items product) ",")]]
     [:tr
      [:td (trans/translate lang ::product/threats)]
      [:td (as-> (model/get-all-keys (:threats product)) $
                 (map #(trans/translate lang (keyword "com.taxonomy.threats" %)))
                 (clj.str/join "\n" $))]]
     [:tr
      [:td (trans/translate lang ::product/restrictions)]
      [:td [show-guarantees-restrictions (:restrictions product) lang]]]
     [:tr
      [:td (trans/translate lang ::product/open-source)]
      [:td (if (:open-source product)
             (trans/translate lang :com.taxonomy.ui/yes)
             (trans/translate lang :com.taxonomy.ui/no))]]
     [:tr
      [:td (trans/translate lang ::product/freely-available)]
      [:td (if (:freely-available product)
             (trans/translate lang :com.taxonomy.ui/yes)
             (trans/translate lang :com.taxonomy.ui/no))]]
     [:tr
      [:td (trans/translate lang ::product/test-version)]
      [:td (if (:test-version product)
             (trans/translate lang :com.taxonomy.ui/yes)
             (trans/translate lang :com.taxonomy.ui/no))]]
     [:tr
      [:td (trans/translate lang ::product/test-duration)]
      [:td (when (:test-duration product)
             (str (:test-duration product) " " (trans/translate lang ::product/days)))]]
     [:tr
      [:td (trans/translate lang ::product/product-interfaces)]
      [:td (clj.str/join (:product-interfaces product) ",")]]
     [:tr
      [:td (trans/translate lang ::product/product-company)]
      [:td (:product-company product)]]
     [:tr
      [:td (trans/translate lang ::product/marketplaces)]
      [:td (clj.str/join (:marketplaces product) ",")]]
     [:tr
      [:td (trans/translate lang ::product/support)]
      [:td (->> (:support product)
                (map (fn [sup]
                       [:div.column
                        [:table.table.is-fullwidth.is-stripped.is-hoverable.vertical-align-middle.fixed
                         [:tr
                          [:td (trans/translate lang ::product/support-types)]
                          [:td (clj.str/join (:support-types sup) ",")]]
                         [:tr
                          [:td (trans/translate lang ::product/support-daily-duration)]
                          [:td (:support-daily-duration sup)]]
                         [:tr
                          [:td (trans/translate lang ::product/support-package-number)]
                          [:td (:support-package-number sup)]]]]))
                (into [:div.columns]))]]]))

(defn view []
  (let [login-user @(rf/subscribe [:ui/user])
        model      @(rf/subscribe [::model/ui-model])
        lang       (:language model)]
    [:article.box
     [ui.navbar/view]
     [control-buttons login-user model lang]
     [product-view model lang]
     (when-not (:hide-delete-confirmation-box model)
       (form/delete-confirmation-dialog ::model/delete ::model/hide-delete-confirmation-box lang))]))
