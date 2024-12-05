(ns com.taxonomy.product.ui.create.view
  (:require [re-frame.core :as rf]
            [clojure.string :as clj.str]
            [clojure.set :as clj.set]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.ui.form :as form]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.product :as product]
            [com.taxonomy.product.ui.create.model :as model]))

(defn- checkbox
  [model k lang]
  (let [id (name k)]
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor id}
      (trans/translate lang (keyword "com.taxonomy.product" (name k)))]
     [:input.checkbox.ml-3 {:type      "checkbox"
                            :checked   (get model k)
                            :on-change #(rf/dispatch [::model/set-input k (-> % .-target .-checked)])}]]))

(defn- input
  [model k lang]
  (let [id (name k)]
    [:div.column.is-6
     [:label.label.mb-0 {:htmlFor id}
      (trans/translate lang (keyword "com.taxonomy.product" (name k)))]
     [:input.input {:key       id
                    :value     (get model k)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-input k (-> e .-target .-value)]))}]]))

(defn- multi-dropdown-menu-view
  [{:keys [products-choices] :as model} k lang]
  (let [id     (name k)
        values (clj.set/difference (get products-choices k) (set (get model k)))
        value  (when-let [v (get model k)]
                 (clj.str/join v ","))]
    [:div.columns
     [:div.column.is-3
      [:label.label {:htmlFor id} (trans/translate lang (keyword "com.taxonomy.product" (name k)))]
      [:div.control
       [:span.select.is-fullwidth
        (into [:select {:id        id
                        :on-change (fn [e]
                                     (let [new-v (conj (get model k) (-> e .-target .-value))]
                                       (rf/dispatch [::model/set-input k (vec new-v)])))}
               [:option ""]]
              (map (fn [v]
                     [:option {:key v :value v} v])
                   values))]]]
     [:div.column.is-6
      [:input.input {:key       (str id "-in")
                     :value     value
                     :disabled  true}]]]))

(defn- multi-dropdown-line
  [property values line k idx lang]
  (let [id     (name k)
        values (clj.set/difference values (set (get line k)))
        value  (when-let [v (get line k)]
                 (clj.str/join v ","))]
    [:div.columns
     [:div.column.is-3
      [:label.label {:htmlFor id} (trans/translate lang (keyword "com.taxonomy.product" (name k)))]
      [:div.control
       [:span.select.is-fullwidth
        (into [:select {:id        id
                        :on-change (fn [e]
                                     (let [new-v (conj (get line k) (-> e .-target .-value))]
                                       (rf/dispatch [::model/set-line-field-input property idx k (vec new-v)])))}
               [:option ""]]
              (map (fn [v]
                     [:option {:key v :value v} v])
                   values))]]]
     [:div.column.is-6
      [:input.input {:key       (str id "-in")
                     :value     value
                     :disabled  true}]]]))

(defn- input-line
  [property value k idx lang]
  (let [id (name k)]
    [:div.column.is-3
     [:label.label.mb-0 {:htmlFor id}
      (trans/translate lang (keyword "com.taxonomy.product" (name k)))]
     [:input.input {:key       id
                    :value     value
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-line-field-input
                                               property idx k (-> e .-target .-value)]))}]]))

(defn- dropdown-line
  [property values line k idx lang]
  (let [id (name k)]
    [:div.column.is-3
     [:label.label {:htmlFor id} (trans/translate lang (keyword "com.taxonomy.product" (name k)))]
     [:div.control
      [:span.select.is-fullwidth
       (into [:select {:id        id
                       :value     (get line k)
                       :on-change (fn [e]
                                    (rf/dispatch [::model/set-line-field-input
                                                  property idx k (-> e .-target .-value)]))}
              [:option ""]]
             (map (fn [v]
                    [:option {:key v :value v} v])
                  values))]]]))

(defn- cost-model-line
  [{:keys [products-choices]} line idx lang]
  [:div.columns {:key (str "cost-model" idx)}
   [multi-dropdown-line :cost-model (:cost-model-types products-choices) line :cost-model-types idx lang]
   [input-line :cost-model (:charge-packets line) :charge-packets idx lang]
   [dropdown-line :cost-model (:time-charge-types products-choices) line :time-charge-type idx lang]
   [:div.column
    [:button.button.is-small.is-danger
     {:on-click (fn [_]
                  (rf/dispatch [::model/remove-line :cost-model idx]))}
     [form/icons {:icon     :minus
                  :icon-css "fa-2x"}]]]])

(defn- guarantees-restrictions-head
  [lang]
  [:div.columns
   [:div.column.is-2 [:b (trans/translate lang ::product/property)]]
   [:div.column.is-1 [:b (trans/translate lang ::product/operator)]]
   [:div.column.is-2 [:b (trans/translate lang ::product/value)]]
   [:div.column.is-2 [:b (trans/translate lang ::product/metric)]]
   [:div.column.is-1 [:b (trans/translate lang ::product/direction-of-values)]]
   [:div.column.is-2 [:b (trans/translate lang ::product/unit)]]])

(defn- guarantees-restrictions-line
  [property line idx lang]
  [:div.columns {:key (str (name property) idx)}
   [input-line property (:property line) :property idx lang]
   [input-line property (:operator line) :operator idx lang]
   [input-line property (:value line) :value idx lang]
   [input-line property (:metric line) :metric idx lang]
   [input-line property (:direction-of-values line) :direction-of-values idx lang]
   [input-line property (:unit line) :unit idx lang]
   [:div.column
    [:button.button.is-small.is-danger
     {:on-click (fn [_]
                  (rf/dispatch [::model/remove-line property idx]))}
     [form/icons {:icon     :minus
                  :icon-css "fa-2x"}]]]])

(defn- support-line
  [{:keys [products-choices]} line idx lang]
  [:div.columns {:key (str "support" idx)}
   [multi-dropdown-line :support (:support-types products-choices) line :support-types idx lang]
   [input-line :support (:support-daily-duration line) :support-daily-duration idx lang]
   [input-line :support (:support-package-number line) :support-package-number idx lang]
   [:div.column
    [:button.button.is-small.is-danger
     {:on-click (fn [_]
                  (rf/dispatch [::model/remove-line :support idx]))}
     [form/icons {:icon     :minus
                  :icon-css "fa-2x"}]]]])

(defn- items-list
  [property items event lang]
  (let [k-space (if (= :security-mechanisms property)
                  "com.taxonomy.security-mechanisms"
                  "com.taxonomy.threats")]
    [:div.control
     [:div.select.is-multiple.is-fullwidth
      [:select.has-background-grey-lighter
       {:multiple  true
        :size      8
        :on-change (fn [e]
                     (let [selected-options (-> (-> e .-target .-selectedOptions)
                                                (map (fn [option] (.-value option)))
                                                (into #{}))]
                       (rf/dispatch [event selected-options])))}
       (map-indexed (fn [i item]
                      [:option.m-1.has-background-warning-light.p-1
                       {:key   (str item i)
                        :value item}
                       (trans/translate lang (keyword k-space item))])
                    items)]]]))

(defn- security-threats-view
  [model property selected-property lang]
  (let [selected-items  (get model selected-property)
        available-items (clj.set/difference (set (get model property))
                                            (set selected-items))

        choices-to-add    (if (= :security-mechanisms property)
                            (:security-mechanism-to-add model)
                            (:threat-to-add model))
        choices-to-remove (if (= :security-mechanisms property)
                            (:security-mechanism-to-remove model)
                            (:threat-to-remove model))]
    [:div.columns
     [:div.columns
      [:div.column.is-6
       [:b (trans/translate lang (keyword "com.taxonomy.product" (name property)))]]]
     [:div.columns
      [:div.column.is-6
       [items-list property available-items ::model/select-smt-to-add lang]]
      [:div.column.is-2
       [:div.columns
        [:div.column
         [:button.button.is-small
          {:on-click (fn [_]
                       (rf/dispatch [::model/add-smt selected-property choices-to-add]))}
          [form/icons {:icon     :right
                       :icon-css "fa-2x"}]]]]
       [:div.columns
        [:div.column
         [:button.button.is-small
          {:on-click (fn [_]
                       (rf/dispatch [::model/remove-smt selected-property choices-to-remove]))}
          [form/icons {:icon     :left
                       :icon-css "fa-2x"}]]]]]
      [:div.column.is-6
       [items-list property selected-items ::model/select-smt-to-remove lang]]]]))

(defn- create-view
  [model lang]
  [:div
   [:div.columns
    [input model :name lang]]
   [:div.columns
    [input model :description lang]]
   [multi-dropdown-menu-view model :delivery-methods lang]
   [multi-dropdown-menu-view model :deployment-models lang]
   [multi-dropdown-menu-view model :product-categories lang]
   [:div.columns
    [:div.columns
     [:div.column.is-6
      [:b (trans/translate lang ::product/cost-model)]
      [:div.column
       [:button.button.is-small.is-success
        {:on-click (fn [_]
                     (rf/dispatch [::model/add-line :cost-model]))}
        [form/icons {:icon     :plus
                     :icon-css "fa-2x"}]]]]]
    [:div.columns
     [:div.column.is-2 [:b (trans/translate lang ::product/cost-model-types)]]
     [:div.column.is-1 [:b (trans/translate lang ::product/charge-packets)]]
     [:div.column.is-2 [:b (trans/translate lang ::product/time-charge-types)]]]
    (map-indexed (fn [idx line]
                   ^{:key (str "cost-model" idx)} [cost-model-line model line idx lang])
                 (:cost-model model))]
   [security-threats-view model :security-mechanisms :selected-security-mechanisms lang]
   [:div.columns
    [:div.columns
     [:div.column.is-6
      [:b (trans/translate lang ::product/non-functional-guarantees)]
      [:div.column
       [:button.button.is-small.is-success
        {:on-click (fn [_]
                     (rf/dispatch [::model/add-line :non-functional-guarantees]))}
        [form/icons {:icon     :plus
                     :icon-css "fa-2x"}]]]]]
    [guarantees-restrictions-head lang]
    (map-indexed (fn [idx line]
                   ^{:key (str "non-functional-guarantees" idx)} [guarantees-restrictions-line model line idx lang])
                 (:non-functional-guarantees model))]
   [multi-dropdown-menu-view model :protection-types lang]
   [multi-dropdown-menu-view model :security-properties lang]
   [multi-dropdown-menu-view model :protected-items lang]
   [security-threats-view model :threats :selected-threats lang]
   [:div.columns
    [:div.columns
     [:div.column.is-6
      [:b (trans/translate lang ::product/restrictions)]
      [:div.column
       [:button.button.is-small.is-success
        {:on-click (fn [_]
                     (rf/dispatch [::model/add-line :restrictions]))}
        [form/icons {:icon     :plus
                     :icon-css "fa-2x"}]]]]]
    [guarantees-restrictions-head lang]
    (map-indexed (fn [idx line]
                   ^{:key (str "restrictions" idx)} [guarantees-restrictions-line model line idx lang])
                 (:restrictions model))]
   [:div.columns
    [checkbox model :open-source lang]]
   [:div.columns
    [checkbox model :freely-available lang]]
   [:div.columns
    [checkbox model :test-version lang]]
   [:div.columns
    [input model :test-duration lang]]
   [multi-dropdown-menu-view model :product-interfaces lang]
   [:div.columns
    [input model :product-company lang]]
   [multi-dropdown-menu-view model :marketplaces lang]
   [:div.columns
    [:div.columns
     [:div.column.is-6
      [:b (trans/translate lang ::product/support)]
      [:div.column
       [:button.button.is-small.is-success
        {:on-click (fn [_]
                     (rf/dispatch [::model/add-line :support]))}
        [form/icons {:icon     :plus
                     :icon-css "fa-2x"}]]]]]
    [:div.columns
     [:div.column.is-2 [:b (trans/translate lang ::product/support-types)]]
     [:div.column.is-1 [:b (trans/translate lang ::product/support-daily-duration)]]
     [:div.column.is-2 [:b (trans/translate lang ::product/support-package-number)]]]
    (map-indexed (fn [idx line]
                   ^{:key (str "support" idx)} [support-line model line idx lang])
                 (:support model))]])

(defn- create-button
  [{:keys [correct-inputs]} lang]
  [:div.columns
   [:div.column.is-6
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/create])
      :disabled (not correct-inputs)}
     [:span (trans/translate lang ::product/create-product)]]]])

(defn view []
  (let [model @(rf/subscribe [::model/ui-model])
        lang  (:language model)]
    [:article.box
     [ui.navbar/view]
     [create-view model lang]
     [create-button model lang]]))
