(ns com.taxonomy.util
  (:require [clojure.string :as clj.str]))

(def password-lower-chars
  [\a \b \c \d \e \f \g \h \i \j \k \l \m
   \n \o \p \q \r \s \t \u \v \w \x \y \z])

(def password-capital-chars
  [\A \B \C \D \E \F \G \H \I \J \K \L \M
   \N \O \P \Q \R \S \T \U \V \W \X \Y \Z])

(def password-symbols
  [\! \# \$ \% \& \* \? \@ \_])

(def password-numbers
  [\0 \1 \2 \3 \4 \5 \6 \7 \8 \9])

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
   (let [remain (mod n 4)
         nums   (into [] (for [i (range 4)]
                           (if (= 3 i)
                             (+ (quot n 4) remain)
                             (quot n 4))))

         password (str (apply str (take (quot n (first nums)) (repeatedly #(rand-nth password-lower-chars))))
                       (apply str (take (quot n (second nums)) (repeatedly #(rand-nth password-capital-chars))))
                       (apply str (take (quot n (nth nums 2)) (repeatedly #(rand-nth password-symbols))))
                       (apply str (take (quot n (last nums)) (repeatedly #(rand-nth password-numbers)))))]
     (-> password
         vec
         shuffle
         clj.str/join)))
  ([]
   (create-password 16)))
