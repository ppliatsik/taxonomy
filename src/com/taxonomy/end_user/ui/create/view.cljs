(ns com.taxonomy.end-user.ui.create.view
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.translations :as trans]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.end-user.ui.create.model :as model]))

(defn view []
  (let [model @(rf/subscribe [::model/ui-model])
        lang  (:language model)]
    [:article.box
     [:div.columns
      [ui.navbar/view]]]))
