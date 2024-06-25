(ns com.taxonomy.product
  (:require [clojure.spec.alpha :as s]))

(s/def ::id nat-int?)

(s/def ::limit (s/nilable nat-int?))
(s/def ::offset (s/nilable nat-int?))

(s/def ::create-product-request
  (s/keys :req-un []))

(s/def ::get-products-request
  (s/keys :opt-un [::limit ::offset]))
