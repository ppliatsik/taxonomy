(ns com.taxonomy.product.ui.list.view
  (:require [re-frame.core :as rf]
            [clojure.string :as clj.str]
            [clojure.set :as clj.set]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.ui.form :as form]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.product :as product]
            [com.taxonomy.product.ui.product-view :as product-view]
            [com.taxonomy.product.ui.list.model :as model]))

(defn- checkbox
  [model k lang]
  [:div.column.field.is-3
   [:div.label (trans/translate lang (keyword "com.taxonomy.product" (name k)))]
   [:input.checkbox.ml-1
    {:type      "checkbox"
     :disabled  false
     :checked   (get-in model [:criteria k :match-value])
     :on-change (fn [e]
                  (rf/dispatch [::model/set-criterion-value k (-> e .-target .-checked)]))}]])

(defn- checkbox-not
  [model k lang]
  [:div.column.field.is-3
   [:div.label (trans/translate lang ::product/not)]
   [:input.checkbox.ml-1
    {:type      "checkbox"
     :disabled  false
     :checked   (get-in model [:criteria k :not])
     :on-change (fn [e]
                  (rf/dispatch [::model/set-criterion-not k (-> e .-target .-checked)]))}]])

(defn- dropdown
  [model values k lang]
  (let [id     (name k)
        values (clj.set/difference values (set (get-in model [:criteria k :match-value])))]
    [:div.column.is-3
     [:label.label {:htmlFor id} (trans/translate lang (keyword "com.taxonomy.product" (name k)))]
     [:div.control
      [:span.select.is-fullwidth
       (into [:select {:id        id
                       :value     (get-in model [:criteria k :match-value])
                       :on-change (fn [e]
                                    (let [v (conj (vec (get-in model [:criteria k :match-value])) (-> e .-target .-value))]
                                      (rf/dispatch [::model/set-criterion-value k v])))}
              [:option ""]]
             (map (fn [v]
                    [:option {:key v :value v} v])
                  values))]]]))

(defn- dropdown-simple
  [model values k lang]
  (let [id (name k)]
    [:div.column.is-3
     [:label.label {:htmlFor id} (trans/translate lang (keyword "com.taxonomy.product" (name k)))]
     [:div.control
      [:span.select.is-fullwidth
       (into [:select {:id        id
                       :value     (get-in model [:criteria k :match-value])
                       :on-change (fn [e]
                                    (rf/dispatch [::model/set-criterion-value k (-> e .-target .-value)]))}
              [:option ""]]
             (map (fn [v]
                    [:option {:key v :value v} v])
                  values))]]]))

(defn- dropdown-values
  [model k lang]
  (let [id    (str (name k) "v")
        value (some->> (get-in model [:criteria k :match-value])
                       (clj.str/join ","))]
    [:div.column.is-3
     [:button.button.is-info
      {:on-click (fn [_]
                   (rf/dispatch [::model/clear-criterion-value k]))}
      [:span (trans/translate lang ::product/clear)]]
     [:input.input {:key      id
                    :value    value
                    :disabled true}]]))

(defn- dropdown-operators
  [{:keys [products-choices] :as model} k lang]
  (let [id (str "op-" (name k))]
    [:div.column
     {:className (if (or (= :security-mechanisms k)
                         (= :threats k))
                   "is-12"
                   "is-3")}
     [:label.label {:htmlFor id} (trans/translate lang ::product/operators)]
     [:div.control
      [:span.select.is-fullwidth
       (into [:select {:id        id
                       :value     (get-in model [:criteria k :operator])
                       :on-change (fn [e]
                                    (rf/dispatch [::model/set-criterion-operator k (-> e .-target .-value)]))}
              [:option ""]]
             (map (fn [v]
                    [:option {:key v :value v} v])
                  (:operators products-choices)))]]]))

(defn- items-list
  [model k items add? lang]
  (let [k-space (if (= :security-mechanisms k)
                  "com.taxonomy.security-mechanisms"
                  "com.taxonomy.threats")]
    [:div.control
     [:div.select.is-multiple.is-fullwidth
      [:select.has-background-grey-lighter
       {:multiple  true
        :size      8
        :on-change (fn [e]
                     (let [selected-options (->> (-> e .-target .-selectedOptions)
                                                 (map (fn [option] (.-value option)))
                                                 (into #{}))
                           old-values       (set (get-in model [:criteria k :match-value]))
                           new-values       (if add?
                                              (concat old-values selected-options)
                                              (clj.set/difference old-values selected-options))]
                       (rf/dispatch [::model/set-criterion-value k (vec new-values)])))}
       (map-indexed (fn [i item]
                      [:option.m-1.has-background-warning-light.p-1
                       {:key   (str item i)
                        :value item}
                       (trans/translate lang (keyword k-space item))])
                    items)]]]))

(defn- security-threats-view
  [model login-user k lang]
  (let [selected-items  (get-in model [:criteria k :match-value])
        available-items (clj.set/difference (set (get model k))
                                            (set selected-items))]
    [:div.columns
     [:div.columns
      [:div.column.is-6
       [:b (trans/translate lang (keyword "com.taxonomy.product" (name k)))]]]
     [:div.columns
      [:div.column.is-5
       [items-list model k available-items true lang]]
      [:div.column.is-5
       [items-list model k selected-items false lang]]]
     [:div.columns
      {:style {:margin-left "2%"}}
      (when login-user
        [dropdown-operators model k lang])]]))

(defn- input
  [model k lang]
  [:div.column.is-3
   [:label.label.mb-0 {:htmlFor (name k)}
    (trans/translate lang (keyword "com.taxonomy.product" (name k)))]
   [:input.input {:key       (name k)
                  :value     (get-in model [:criteria k :match-value])
                  :on-change (fn [e]
                               (rf/dispatch [::model/set-criterion-value k (-> e .-target .-value)]))}]])

(defn- criteria-view
  [model lang login-user]
  [:div.box.has-background-success-light
   [:span.is-large
    (trans/translate lang ::product/choose-criteria)]
   (when login-user
     [:div.columns
      [dropdown-simple model (-> model :products-choices :logical-operators) :logical-operators nil lang]])
   [:div.columns
    [input model :name lang]]
   [:div.columns
    [dropdown model (-> model :products-choices :delivery-methods) :delivery-methods lang]
    [dropdown-values model :delivery-methods lang]
    (when login-user
      [dropdown-operators model :delivery-methods lang])
    (when login-user
      [checkbox-not model :delivery-methods lang])]
   [:div.columns
    [dropdown model (-> model :products-choices :deployment-models) :deployment-models lang]
    [dropdown-values model :deployment-models lang]
    (when login-user
      [dropdown-operators model :deployment-models lang])
    (when login-user
      [checkbox-not model :deployment-models lang])]
   [:div.columns
    [dropdown model (-> model :products-choices :product-categories) :product-categories lang]
    [dropdown-values model :product-categories lang]
    (when login-user
      [dropdown-operators model :product-categories lang])
    (when login-user
      [checkbox-not model :product-categories lang])]
   [:div.columns
    [dropdown model (-> model :products-choices :cost-model-types) :cost-model-types lang]
    [dropdown-values model :cost-model-types lang]]
   [:div.columns
    [input model :charge-packets lang]]
   [:div.columns
    [dropdown model (-> model :products-choices :time-charge-types) :time-charge-types lang]
    [dropdown-values model :time-charge-types lang]]
   [security-threats-view model login-user :security-mechanisms lang]
   [:div.columns
    [input model :nfg-property lang]]
   [:div.columns
    [input model :nfg-value lang]]
   [:div.columns
    [input model :nfg-metric lang]]
   [:div.columns
    [dropdown model (-> model :products-choices :protection-types) :protection-types lang]
    [dropdown-values model :protection-types lang]
    (when login-user
      [dropdown-operators model :protection-types lang])
    (when login-user
      [checkbox-not model :protection-types lang])]
   [security-threats-view model login-user :threats lang]
   [:div.columns
    [input model :res-property lang]]
   [:div.columns
    [input model :res-value lang]]
   [:div.columns
    [input model :res-metric lang]]
   [:div.columns
    [checkbox model :open-source lang]]
   [:div.columns
    [checkbox model :freely-available lang]]
   [:div.columns
    [checkbox model :test-version lang]]
   [:div.columns
    [input model :test-duration lang]]
   [:div.columns
    [dropdown model (-> model :products-choices :product-interfaces) :product-interfaces lang]
    [dropdown-values model :product-interfaces lang]
    (when login-user
      [dropdown-operators model :product-interfaces lang])
    (when login-user
      [checkbox-not model :product-interfaces lang])]
   [:div.columns
    [dropdown model (-> model :products-choices :marketplaces) :marketplaces lang]
    [dropdown-values model :marketplaces lang]
    (when login-user
      [dropdown-operators model :marketplaces lang])
    (when login-user
      [checkbox-not model :marketplaces lang])]
   [:div.columns
    [dropdown model (-> model :products-choices :support-types) :support-types lang]
    [dropdown-values model :support-types lang]]
   [:div.columns
    [input model :support-daily-duration lang]]
   [:div.columns
    [input model :support-package-number lang]]])

(defn- multi-weights
  [{:keys [products] :as model} lang product-key]
  (->> products
       (mapcat #(get % product-key))
       (map :property)
       set
       (map (fn [data]
              (let [property (clj.str/replace data #"\s" "")]
                [:div.column.is-3
                 [:label.label.mb-0 {:htmlFor (str "non-functional-guarantees-w-" property)}
                  (str (trans/translate lang (keyword "com.taxonomy.product" product-key)) "-" property)]
                 [:input.input {:key       (str "non-functional-guarantees-w-" property)
                                :value     (get-in model [:weights product-key property])
                                :on-change (fn [e]
                                             (rf/dispatch [::model/set-multi-weight-input
                                                           product-key property (-> e .-target .-value)]))}]])))
       (into [:div.columns])))

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
                    :value     (-> model :weights :test-duration)
                    :on-change (fn [e]
                                 (rf/dispatch [::model/set-input
                                               :test-duration (-> e .-target .-value)]))}]]]])

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
