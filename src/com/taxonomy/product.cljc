(ns com.taxonomy.product
  (:require [clojure.spec.alpha :as s]))

(s/def ::id nat-int?)
(s/def ::name string?)

(def delivery-methods-set #{"SaaS" "IaaS" "DaaS" "CaaS" "VMM"})
(s/def ::delivery-method (s/and string? delivery-methods-set))
(s/def ::delivery-methods (s/coll-of ::delivery-methods :kind vector? :min-count 0))

(def layout-models-set #{"public" "private" "hybrid"})
(s/def ::layout-model (s/and string? layout-models-set))
(s/def ::layout-models (s/coll-of ::layout-model :kind vector? :min-count 0))

(def product-categories-set #{"SIEM" "BCDR" "IDS" "IAM" "DLP" "crypto" "email-security"
                              "network-security" "security-evaluation" "web-security"})
(s/def ::product-category (s/and string? product-categories-set))
(s/def ::product-categories (s/coll-of ::product-category :kind vector? :min-count 0))

(def protection-types-set #{"proactive" "detective" "reactive"})
(s/def ::protection-type (s/and string? protection-types-set))
(s/def ::protection-types (s/coll-of ::protection-type :kind vector? :min-count 0))

(def security-features-set #{"integrity" "availability" "non-repudiation" "confidentiality"
                             "privacy" "authentication" "authorization" "access-control"})
(s/def ::security-feature (s/and string? security-features-set))
(s/def ::security-features (s/coll-of ::security-feature :kind vector? :min-count 0))

(def protected-items-set #{"data" "everything" "network" "infrastructure" "applications" "hosts"
                           "VMs" "containers" "databases" "files" "vpc" ""})
(s/def ::protected-item (s/and string? protected-items-set))
(s/def ::protected-items (s/coll-of ::protected-item :kind vector? :min-count 0))

(def product-usages-set #{"Rest-API" "Cli" "Web-GUI"})
(s/def ::product-usage (s/and string? product-usages-set))
(s/def ::product-usages (s/coll-of ::product-usage :kind vector? :min-count 0))

(s/def ::product-company string?)

(def marketplaces-set #{"AWS" "GCP" "Azure" "Google"})
(s/def ::marketplace (s/nilable (s/and string? marketplaces-set)))
(s/def ::marketplaces (s/coll-of ::marketplace :kind vector? :min-count 0))

(def version-types-set #{"non-free" "free-without-restrictions"
                         "free-with-restrictions" "free-unlimited"})
(def free-version-type-set #{"days" "months" "users" "devices"})
(s/def ::version-type (s/and string? version-types-set))
(s/def ::free-version-counter (s/nilable (s/or zero? pos-int?)))
(s/def ::free-version-type (s/nilable (s/and string? free-version-type-set)))
(s/def ::free-version-details
  (s/map-of ::free-version-type ::free-version-counter))

(def cost-model-types-set #{"per-users" "per-features" "per-sub-features" "unknown" "per-work-load"})
(s/def ::cost-model-type (s/and string? cost-model-types-set))
(s/def ::cost-model-types (s/coll-of ::cost-model-type :kind vector? :min-count 0))
(s/def ::cost-model-packets (s/nilable pos-int?))
(s/def ::cost-model-time (s/nilable (s/coll-of pos-int?)))
(s/def ::cost-model-time-type (s/nilable (s/and string? #{"monthly" "yearly"})))
(s/def ::cost-model-charge (s/map-of ::cost-model-time ::cost-model-time-type))
(s/def ::minimum-cost
  #?(:clj (s/and decimal? #(>= % 0.0M)))
  #?(:cljs (s/and number? #(>= % 0.0))))

(s/def ::implemented-security-mechanisms string?)
(s/def ::non-functional-guarantees string?)
(s/def ::threats-faced string?)
(s/def ::restrictions (s/nilable string?))

(def product-support-type-set #{"phone" "email" "faq" "documentation" "portal" "video"
                                "twitter" "troubleshooting-center" "software-versions"
                                "chat" "forum" "tickets" "community" "notifications" "blog"})
(s/def ::product-support-type (s/and string? product-support-type-set))
(s/def ::product-support-packets (s/nilable pos-int?))
(s/def ::product-support-time (s/nilable pos-int?))

(s/def ::create-product-request
  (s/keys :req-un [::name ::delivery-methods ::layout-models ::product-categories ::protection-types
                   ::security-features ::protected-items ::product-usages ::product-company
                   ::marketplaces ::version-type ::cost-model]
          :opt-un [::free-version-details ::cost-model-types ::cost-model-packets ::cost-model-charge
                   ::minimum-cost ::product-support-type ::product-support-packets ::product-support-time]))

(s/def ::get-products-request
  (s/keys :opt-un []))
