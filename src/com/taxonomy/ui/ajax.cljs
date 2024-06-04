(ns com.taxonomy.ui.ajax
  (:require [ajax.edn]
            [ajax.json]
            [day8.re-frame.http-fx]
            [re-frame.core :as rf]))

(rf/reg-event-fx
  :ajax/post
  (fn [_ [_ {:keys [uri params success failure timeout response-format] :as m}]]
    {:http-xhrio
     (merge
       {:method           :post
        :uri              uri
        :params           params
        :with-credentials true
        :timeout          (or timeout 20000)
        :format           (ajax.edn/edn-request-format)
        :response-format  (or response-format (ajax.edn/edn-response-format))
        :on-failure       (if failure [failure] [::http-post-failure])}
       (when success {:on-success (if (coll? success) success [success])}))}))

(rf/reg-event-fx
  :ajax/get
  (fn [_ [_ {:keys [uri params response-format success failure timeout vec-strategy] :as options}]]
    {:http-xhrio
     (merge {:method           :get
             :uri              uri
             :params           params
             :with-credentials true
             :timeout          (or timeout 20000)
             :format           (ajax.edn/edn-request-format)
             :response-format  (or response-format (ajax.edn/edn-response-format))}
            (when vec-strategy
              {:vec-strategy vec-strategy})
            (if failure
              {:on-failure (if (coll? failure) failure [failure])}
              {:on-failure [::http-get-failure]})
            (when success {:on-success (if (coll? success) success [success])}))}))

(rf/reg-event-fx
  :ajax/put
  (fn [_ [_ {:keys [uri params success failure timeout] :as m}]]
    {:http-xhrio
     (merge
       {:method           :put
        :uri              uri
        :params           params
        :with-credentials true
        :timeout          (or timeout 20000)
        :format           (ajax.edn/edn-request-format)
        :response-format  (ajax.edn/edn-response-format)}
       (if failure
         {:on-failure [failure]}
         {:on-failure [::http-put-failure]})
       (when success {:on-success (if (coll? success) success [success])}))}))

(rf/reg-event-fx
  :ajax/delete
  (fn [_ [_ {:keys [uri params success failure timeout] :as m}]]
    {:http-xhrio
     (merge
       {:method           :delete
        :uri              uri
        :params           params
        :with-credentials true
        :timeout          (or timeout 20000)
        :format           (ajax.edn/edn-request-format)
        :response-format  (ajax.edn/edn-response-format)}
       (if failure
         {:on-failure [failure]}
         {:on-failure [::http-delete-failure]})
       (when success {:on-success (if (coll? success) success [success])}))}))

(rf/reg-event-fx
  :ajax/sent-file
  (fn [_ [_ {:keys [uri body success failure timeout response-format] :as m}]]
    {:http-xhrio
     (merge
       {:method           :post
        :uri              uri
        :body             body
        :with-credentials true
        :response-format  (or response-format (ajax.json/json-response-format {:keywords? true}))
        :timeout          (or timeout 20000)}
       (if failure
         {:on-failure [failure]}
         {:on-failure [::http-post-failure]})
       (when success {:on-success (if (coll? success) success [success])}))}))
