(ns com.taxonomy.product
  (:require [clojure.spec.alpha :as s]))

(s/def ::limit (s/nilable nat-int?))
(s/def ::offset (s/nilable nat-int?))
