(ns com.taxonomy.product.ui.show.model
  (:require [re-frame.core :as rf]
            [com.taxonomy.product :as product]))

(def paths [:ui/forms ::product/show :data])
(def data-path (rf/path [:ui/forms ::product/show :data]))
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
