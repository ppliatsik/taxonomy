(ns com.taxonomy.http.email
  (:require [clojure.tools.logging :as log])
  (:import [java.util Properties]
           [javax.mail Message$RecipientType Session]
           [javax.mail.internet InternetAddress MimeMessage]))

(defn send-email
  [email-host {:keys [to subject body]}]
  (try
    (let [props (Properties.)
          _     (.put props "mail.smtp.host" (:host email-host))
          _     (.put props "mail.smtp.port" (:port email-host))
          _     (.put props "mail.smtp.user" (:user email-host))
          _     (.put props "mail.smtp.password" (:pass email-host))
          _     (.put props "mail.from" (:user email-host))
          _     (.put props "mail.smtp.starttls.enable" "true")
          _     (.put props "mail.smtp.auth" "true")

          session (Session/getInstance props nil)

          msg (MimeMessage. session)
          _   (.setRecipient msg Message$RecipientType/TO (InternetAddress. to))
          _   (.setSubject msg subject)
          _   (.setText msg body)

          trans (.getTransport session "smtp")]
      (.connect trans (:host email-host) (:user email-host) (:pass email-host))
      (.sendMessage trans msg (.getAllRecipients msg))
      (.close trans))
    (catch Exception ex
      (log/error "Cannot send email." (.getMessage ex)))))
