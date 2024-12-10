(ns com.taxonomy.product.ui.show.view
  (:require [re-frame.core :as rf]
            [clojure.string :as clj.str]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.ui.form :as form]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.ui.util :as util]
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
                 [:td.nowrap (trans/translate lang ::product/property)]
                 [:td.nowrap (:property cur)]]
                [:tr
                 [:td.nowrap (trans/translate lang ::product/operator)]
                 [:td.nowrap (:operator cur)]]
                [:tr
                 [:td.nowrap (trans/translate lang ::product/value)]
                 [:td.nowrap (:value cur)]]
                [:tr
                 [:td.nowrap (trans/translate lang ::product/metric)]
                 [:td.nowrap (:metric cur)]]
                [:tr
                 [:td.nowrap (trans/translate lang ::product/direction-of-values)]
                 [:td.nowrap (if (:direction-of-values cur)
                               (trans/translate lang :com.taxonomy.ui/positive)
                               (trans/translate lang :com.taxonomy.ui/negative))]]
                [:tr
                 [:td.nowrap (trans/translate lang ::product/unit)]
                 [:td.nowrap (:unit cur)]]]]))
       (into [:div.columns])))

(defn- product-view
  [{:keys [product]} lang]
  (when product
    [:table.table.is-fullwidth.is-stripped.is-hoverable.vertical-align-middle.fixed
     [:tr
      [:td.nowrap (trans/translate lang ::product/name)]
      [:td.nowrap (:name product)]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/published)]
      [:td.nowrap (if (:published product)
             (trans/translate lang :com.taxonomy.ui/yes)
             (trans/translate lang :com.taxonomy.ui/no))]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/description)]
      [:td.nowrap (:description product)]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/creator)]
      [:td.nowrap (:creator product)]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/delivery-methods)]
      [:td.nowrap (clj.str/join "," (:delivery-methods product))]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/deployment-models)]
      [:td.nowrap (clj.str/join "," (:deployment-models product))]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/product-categories)]
      [:td.nowrap (clj.str/join "," (:product-categories product))]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/cost-model)]
      [:td.nowrap (->> (:cost-model product)
                       (map (fn [cm]
                              [:div.column
                               [:table.table.is-fullwidth.is-stripped.is-hoverable.vertical-align-middle.fixed
                                [:tr
                                 [:td.nowrap (trans/translate lang ::product/cost-model-types)]
                                 [:td.nowrap (clj.str/join "," (:cost-model-types cm))]]
                                [:tr
                                 [:td.nowrap (trans/translate lang ::product/charge-packets)]
                                 [:td.nowrap (:charge-packets cm)]]
                                [:tr
                                 [:td.nowrap (trans/translate lang ::product/time-charge-types)]
                                 [:td.nowrap (clj.str/join "," (:time-charge-types cm))]]]]))
                       (into [:div.columns]))]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/security-mechanisms)]
      [:td.nowrap (as-> (:security-mechanisms product) $
                        (map #(trans/translate lang (keyword "com.taxonomy.security-mechanisms" %)) $)
                        (clj.str/join ",\n" $))]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/non-functional-guarantees)]
      [:td.nowrap [show-guarantees-restrictions (:non-functional-guarantees product) lang]]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/protection-types)]
      [:td.nowrap (clj.str/join "," (:protection-types product))]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/security-properties)]
      [:td.nowrap (clj.str/join "," (:security-properties product))]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/protected-items)]
      [:td.nowrap (clj.str/join "," (:protected-items product))]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/threats)]
      [:td.nowrap (as-> (:threats product) $
                        (map #(trans/translate lang (keyword "com.taxonomy.threats" %)) $)
                        (clj.str/join ",\n" $))]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/restrictions)]
      [:td.nowrap [show-guarantees-restrictions (:restrictions product) lang]]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/open-source)]
      [:td.nowrap (if (:open-source product)
                    (trans/translate lang :com.taxonomy.ui/yes)
                    (trans/translate lang :com.taxonomy.ui/no))]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/freely-available)]
      [:td.nowrap (if (:freely-available product)
                    (trans/translate lang :com.taxonomy.ui/yes)
                    (trans/translate lang :com.taxonomy.ui/no))]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/test-version)]
      [:td.nowrap (if (:test-version product)
                    (trans/translate lang :com.taxonomy.ui/yes)
                    (trans/translate lang :com.taxonomy.ui/no))]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/test-duration)]
      [:td.nowrap (when (:test-duration product)
                    (str (:test-duration product) " " (trans/translate lang ::product/days)))]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/product-interfaces)]
      [:td.nowrap (clj.str/join "," (:product-interfaces product))]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/product-company)]
      [:td.nowrap (:product-company product)]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/marketplaces)]
      [:td.nowrap (clj.str/join "," (:marketplaces product))]]
     [:tr
      [:td.nowrap (trans/translate lang ::product/support)]
      [:td.nowrap (->> (:support product)
                       (map (fn [sup]
                              [:div.column
                               [:table.table.is-fullwidth.is-stripped.is-hoverable.vertical-align-middle.fixed
                                [:tr
                                 [:td.nowrap (trans/translate lang ::product/support-types)]
                                 [:td.nowrap (clj.str/join "," (:support-types sup))]]
                                [:tr
                                 [:td.nowrap (trans/translate lang ::product/support-daily-duration)]
                                 [:td.nowrap (:support-daily-duration sup)]]
                                [:tr
                                 [:td.nowrap (trans/translate lang ::product/support-package-number)]
                                 [:td.nowrap (:support-package-number sup)]]]]))
                       (into [:div.columns]))]]]))

(defn- control-buttons
  [login-user {:keys [product]} lang]
  (when product
    [:div.columns
     (when (end-user/is-current-user? login-user (:creator product))
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
               (end-user/is-current-user? login-user (:creator product)))
       [:div.column.is-2
        {:style {:margin-left "1.5%"
                 :margin-top  "0.9%"}}
        [:button.button.is-danger
         {:on-click #(rf/dispatch [::model/show-delete-confirmation-box])}
         [:span (trans/translate lang ::product/delete)]]])]))

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
