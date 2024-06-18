(ns com.taxonomy.util
  (:require [clojure.string :as clj.str]))

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
  []
  )
