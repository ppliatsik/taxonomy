(ns com.taxonomy.end-user.ui.edit.model
  (:require [re-frame.core :as rf]
            [com.taxonomy.end-user :as end-user]))

(def paths [:ui/forms ::end-user/edit :data])
(def data-path (rf/path [:ui/forms ::end-user/edit :data]))
(def metadata
  {:data-path paths})

(rf/reg-sub
  ::form-data
  (fn [db _]
    (get-in db paths)))

(rf/reg-sub
  ::ui-model
  :<- [::form-data]
  :<- [:ui/language]
  (fn [[data language] _]
    (-> metadata
        (assoc :language language)
        (merge data))))
