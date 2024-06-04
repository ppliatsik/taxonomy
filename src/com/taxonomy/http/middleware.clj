(ns com.taxonomy.http.middleware)

(defn inject-system
  [system]
  (fn [h]
    (fn [req]
      (h (merge req system)))))
