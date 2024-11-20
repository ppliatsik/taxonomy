(ns com.taxonomy.end-user.ui.list.view
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.translations :as trans]
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

(defn view []
  (let [model @(rf/subscribe [::model/ui-model])
        lang  (:language model)]
    [:article.box
     [ui.navbar/view]
     [list-users model lang]]))
