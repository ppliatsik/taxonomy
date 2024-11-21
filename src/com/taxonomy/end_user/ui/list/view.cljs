(ns com.taxonomy.end-user.ui.list.view
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.ui.form :as form]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.ui.routes :as routes]
            [com.taxonomy.end-user.ui.list.model :as model]))

(defn- table-row
  [{:keys [username first-name last-name email]}]
  [:tr {:key username}
   [:td.nowrap
    [:a {:href (routes/user {:username username})}
     username]]
   [:td first-name]
   [:td last-name]
   [:td email]])

(defn- list-users
  [{:keys [users]} lang]
  [:table.table.is-fullwidth.is-stripped.is-hoverable.vertical-align-middle.fixed
   [:thead.has-background-light
    [:tr
     [:td (trans/translate lang ::end-user/username)]
     [:td (trans/translate lang ::end-user/first-name)]
     [:td (trans/translate lang ::end-user/last-name)]
     [:td (trans/translate lang ::end-user/email)]]]
   [:tbody
    (map (fn [user]
           ^{:key (:username user)}
           [table-row user])
         users)]])

(defn- search
  [model]
  [:div.columns
   [:div.column.is-6
    [:input.input {:key       "email"
                   :value     (:q model)
                   :on-change (fn [e]
                                (rf/dispatch [::model/set-q
                                              (-> e .-target .-value)]))}]]
   [:div.column.is-6
    [:button.button.is-info
     {:on-click #(rf/dispatch [::model/search])}
     [form/icons {:icon     :search
                  :icon-css "fa-1x"}]]]])

(defn view []
  (let [model @(rf/subscribe [::model/ui-model])
        lang  (:language model)]
    [:article.box
     [ui.navbar/view]
     [search model]
     [list-users model lang]
     [form/pagination (:pagination model) lang ::model/set-current-page]]))
