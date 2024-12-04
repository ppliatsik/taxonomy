(ns com.taxonomy.ui.util)

(defn get-all-keys
  [m]
  (reduce (fn [acc [k v]]
            (if (map? v)
              (apply conj acc (name k) (get-all-keys v))
              (conj acc (name k))))
          []
          m))
