(ns com.taxonomy.http.http-response)

(defn response
  ([status body]         (response status body {}))
  ([status body headers] {:status status :body body :headers headers}))

(def ok (partial response 200))
(def invalid (partial response 400))
(def not-found (partial response 404))
(def server-error (partial response 500))

(defn one-or-404
  ([res]
   (one-or-404 res :not-found))
  ([res not-found-result]
   (if res
     (ok res)
     (not-found {:result not-found-result}))))
