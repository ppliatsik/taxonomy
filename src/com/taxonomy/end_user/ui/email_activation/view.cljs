(ns com.taxonomy.end-user.ui.email-activation.view
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.navbar :as ui.navbar]
            [com.taxonomy.end-user.ui.email-activation.model :as model]))

(defn view []
  (let [model @(rf/subscribe [::model/ui-model])
        lang  (:language model)]
    [:article.box
     [ui.navbar/view]]))
