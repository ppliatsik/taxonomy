(ns com.taxonomy.ui.routes
  (:require [secretary.core :as sec]
            [re-frame.core :as rf]
            [goog.events :as events])
  (:import [goog History]
           [goog.history EventType]))

(defn history
  []
  (doto (History.)
    (events/listen EventType.NAVIGATE
                   (fn [event]
                     (let [fragment-url (.-token event)]
                       (sec/dispatch! fragment-url))))
    (.setEnabled true)))

(defn setup-app-routes
  []
  (sec/set-config! :prefix "#")

  (sec/defroute main-view "/" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.ui/main-view]))

  (sec/defroute "*" []
                (rf/dispatch [:com.taxonomy.ui/set-view :com.taxonomy.ui/main-view]))

  (history))
