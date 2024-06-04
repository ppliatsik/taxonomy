(ns com.taxonomy.ui.pagination)

(def default-limit 10)
(def default-offset 0)

(defn set-pagination-data
  [{:keys [total limit offset]
    :or {:limit default-limit :offset default-offset}
    :as pagination}
   current-page]
  {:first-page   1
   :last-page    (.ceil js/Math (/ total limit))
   :current-page (or current-page 1)
   :limit        limit
   :offset       offset})
