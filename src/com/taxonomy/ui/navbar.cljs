(ns com.taxonomy.ui.navbar
  (:require [re-frame.core :as rf]
            [com.taxonomy.ui.breadcrumb :as ui.breadcrumb]))

(defn view []
  (let [breadcrumbs @(rf/subscribe [:ui/breadcrumbs])
        language    @(rf/subscribe [:ui/language])]
    [:div.has-background-warning-light.p-0.m-2
     [ui.breadcrumb/breadcrumb-list breadcrumbs language]]))
