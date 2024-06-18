(ns com.taxonomy.end-user
  (:require [clojure.spec.alpha :as s]))

(defn email-is-valid?
  [email]
  (re-matches #"^[^@]+@[^@]+$" email))

(s/def ::username string?)
(s/def ::password string?)
(s/def ::first-name string?)
(s/def ::last-name string?)
(s/def ::email (s/and string? email-is-valid?))
(s/def ::roles (s/coll-of string? :into []))

(s/def ::old-password string?)

(s/def ::limit (s/nilable nat-int?))
(s/def ::offset (s/nilable nat-int?))

(s/def ::create-user-request
  (s/keys :req-un [::username ::password ::first-name ::last-name ::email]))

(s/def ::update-user-info-request
  (s/keys :req-un [::first-name ::last-name ::email]))

(s/def ::change-user-password-request
  (s/keys :req-un [::password ::old-password]))

(s/def ::get-users-request
  (s/keys :opt-un [::limit ::offset]))
