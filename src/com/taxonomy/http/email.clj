(ns com.taxonomy.http.email
  (:require [postal.core :as mail]))

(defn send
  [{:keys [to subject body]}]
  (mail/send-message {:host "mail.taxonomy.com"}
                     {:from    "admin@taxonomy.com"
                      :to      to
                      :subject subject
                      :body    body}))
