(ns com.taxonomy.util
  (:require [clojure.string :as clj.str]))

(def password-chars
  [\! \# \$ \% \& \0 \1 \2 \3 \4 \5 \6 \7 \8 \9 \* \? \@
   \A \B \C \D \E \F \G \H \I \J \K \L \M \N \O \P \Q \R
   \S \T \U \V \W \X \Y \Z \a \b \c \d \e \f \g \h \i \j
   \k \l \m \n \o \p \q \r \s \t \u \v \w \x \y \z])

(defn keyword->string
  "keyword -> string keeping qualified part"
  [k]
  (let [str-key (str k)]
    (if (clj.str/starts-with? str-key ":")
      (subs str-key 1)
      str-key)))

(defn string->md5
  [^String s]
  (let [algorithm (java.security.MessageDigest/getInstance "MD5")
        raw       (.digest algorithm (.getBytes s))]
    (format "%032x" (BigInteger. 1 raw))))

(defn create-password
  ([n]
   (apply str (take n (repeatedly #(rand-nth password-chars)))))
  ([]
   (create-password 15)))
