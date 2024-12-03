(ns com.taxonomy.product.ui.list.view
  (:require [re-frame.core :as rf]
            [clojure.spec.alpha :as s]
            [spec-tools.core :as st]
            [clojure.string :as clj.str]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.product :as product]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.product.ui.product-view :as product-view]
            [com.taxonomy.product.ui.list.model :as model]))

(defn- multi-weights
  [{:keys [products] :as model} lang product-key]
  (->> products
       (map #(get % product-key))
       (map (fn [data]
              (let [property (clj.str/replace (:property data) #"\s" "")]
                [:div.column.is-3
                 [:label.label.mb-0 {:htmlFor (str "non-functional-guarantees-w-" property)}
                  (str (trans/translate lang (keyword "com.taxonomy.product" product-key)) "-" (:property data))]
                 [:input.input {:key       (str "non-functional-guarantees-w-" property)
                                :value     (get-in model [product-key property])
                                :on-change (fn [e]
                                             (rf/dispatch [::model/set-multi-weight-input
                                                           product-key property (-> e .-target .-value)]))}]
                 (when-not (s/valid? ::product/weight
                                     (st/coerce ::product/weight (get-in model [product-key property]) st/string-transformer))
                   [:p.help.is-danger (trans/translate lang ::end-user/wrong-input)])])))
       (into [:div.columns])))

(defn- criteria-view
  [model lang login-user]
  [:div.box.has-background-success-light
   [:span.is-large
    (trans/translate lang ::product/choose-criteria)]
   [:div.columns
    [:div.column.is-3
     [:label.label.mb-0 {:htmlFor "name"}
      (trans/translate lang ::product/name)]
     [:input.input {:key       "name"
                    :value     (:name model)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-criterion
                                               :name "EQUAL" (-> e .-target .-value)]))}]]]])

(defn- weights-view
  [model lang]
  [:div.box.has-background-success-light
   [:span.is-large
    (trans/translate lang ::product/choose-weights)]
   [multi-weights model lang :non-functional-guarantees]
   [multi-weights model lang :restrictions]
   [:div.columns
    [:div.column.is-3
     [:label.label.mb-0 {:htmlFor "test-duration-w"}
      (trans/translate lang ::product/test-duration)]
     [:input.input {:key       "test-duration-w"
                    :value     (:test-duration model)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-input
                                               :test-duration (-> e .-target .-value)]))}]
     (when-not (s/valid? ::product/weight
                         (st/coerce ::product/weight (:test-duration model) st/string-transformer))
       [:p.help.is-danger (trans/translate lang ::end-user/wrong-input)])]]])

(defn- submit-buttons
  [{:keys [correct-inputs]} lang]
  [:div.columns
   [:div.column.is-4
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/match])
      :disabled false}
     [:span (trans/translate lang ::product/match)]]]
   [:div.column.is-4
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/classification])
      :disabled (not correct-inputs)}
     [:span (trans/translate lang ::product/classification)]]]
   [:div.column.is-4
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/discovery])
      :disabled (not correct-inputs)}
     [:span (trans/translate lang ::product/discovery)]]]])

(defn view []
  (let [login-user @(rf/subscribe [:ui/user])
        model      @(rf/subscribe [::model/ui-model])
        lang       (:language model)]
    [:article.box
     [ui.navbar/view]
     [criteria-view model lang login-user]
     [weights-view model lang]
     [submit-buttons model lang]
     [product-view/list-products model lang]]))
