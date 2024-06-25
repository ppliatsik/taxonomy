(ns com.taxonomy.end-user
  (:require [clojure.spec.alpha :as s]))

(defn is-only-alphabetic-str
  [s]
  (re-find #"^[a-zA-Z]+$" s))

(defn username-is-valid?
  [username]
  (and (is-only-alphabetic-str (-> username first str))
       (re-find #"^[a-zA-Z_]+$" username)))

(defn password-is-valid?
  "Minimum eight and maximum 10 characters, at least one uppercase letter,
  one lowercase letter, one number and one special character"
  [password]
  (re-find #"^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{8,}$"
           password))

(defn email-is-valid?
  [email]
  (re-matches #"^[^@]+@[^@]+$" email))

(s/def ::token string?)

(s/def ::role
  (s/and string? #{"admin" "user"}))

(s/def ::username (s/and string? username-is-valid?))
(s/def ::password (s/and string? password-is-valid?))
(s/def ::first-name (s/and string? is-only-alphabetic-str))
(s/def ::last-name (s/and string? is-only-alphabetic-str))
(s/def ::email (s/and string? email-is-valid?))
(s/def ::roles (s/coll-of ::role :into []))

(s/def ::password-verification (s/and string? password-is-valid?))
(s/def ::old-password (s/and string? password-is-valid?))

(s/def ::limit (s/nilable nat-int?))
(s/def ::offset (s/nilable nat-int?))

(s/def ::create-user-request
  (s/keys :req-un [::username ::password ::first-name ::last-name ::email ::password-verification]))

(s/def ::update-user-info-request
  (s/keys :req-un [::first-name ::last-name ::email]))

(s/def ::change-user-password-request
  (s/keys :req-un [::password ::old-password ::password-verification]))

(s/def ::get-users-request
  (s/keys :opt-un [::limit ::offset]))
