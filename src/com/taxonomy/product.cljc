(ns com.taxonomy.product
  (:require [clojure.spec.alpha :as s]))

(s/def ::id string?)
(s/def ::name string?)
(s/def ::published boolean?)
(s/def ::description (s/nilable string?))
(s/def ::created-by string?)

(def delivery-methods-set #{"SAAS" "IAAS" "DAAS" "CAAS"})
(s/def ::delivery-method (s/and string? delivery-methods-set))
(s/def ::delivery-methods (s/nilable (s/coll-of ::delivery-methods :kind vector? :min-count 0)))

(def deployment-models-set #{"PUBLIC" "PRIVATE" "HYBRID"})
(s/def ::deployment-model (s/and string? deployment-models-set))
(s/def ::deployment-models (s/nilable (s/coll-of ::deployment-model :kind vector? :min-count 0)))

(def product-categories-set #{"SIEM" "BCDR" "INTRUSION_MANAGEMENT" "IAM" "DLP" "ENCRYPTION"
                              "EMAIL_SECURITY" "NETWORK_SECURITY" "SECURITY_ASSESSMENT" "WEB_SECURITY"})
(s/def ::product-category (s/and string? product-categories-set))
(s/def ::product-categories	(s/nilable (s/coll-of ::product-category :kind vector? :min-count 0)))

(def cost-model-types-set #{"PER_USER" "PER_FEATURE" "PER_SUB_FEATURE" "UNKNOWN"
                            "PER_WORKLOAD" "PER_HARDWARE_RESOURCE" "PER_PACKET" "PER_NODE"})
(s/def ::cost-model-type (s/and string? cost-model-types-set))
(s/def ::cost-model-types (s/coll-of ::cost-model-type :kind vector? :min-count 0))
(s/def ::charge-packets nat-int?)
(def time-charge-type-set #{"MONTHLY" "QUARTERLY" "YEARLY"})
(s/def ::time-charge-type (s/and string? time-charge-type-set))
(s/def ::cost-model-map
  (s/keys :opt-un [::cost-model-types ::charge-packets ::time-charge-type]))
(s/def ::cost-model	(s/nilable (s/coll-of ::cost-model-map :kind vector? :min-count 0)))

(s/def ::security-mechanism keyword?)
(s/def ::security-mechanisms (s/nilable (s/coll-of ::security-mechanism :kind vector? :min-count 0)))

(s/def ::property	string?)
(def operator-types #{"LESS_THAN" "LESS_EQUAL_THAN" "GREATER_THAN" "GREATER_EQUAL_THAN"
                      "EQUAL" "DIFFERENT" "SUBSET" "SUPERSET" "INCLUDES" "NON_INCLUDES"})
(s/def ::operator	(s/and string? operator-types))
(s/def ::value
  #?(:clj decimal?)
  #?(:cljs number?))
(s/def ::metric	(s/nilable string?))
(s/def ::direction-of-values (s/nilable boolean?))
(s/def ::unit (s/nilable string?))

(s/def ::non-functional-guarantees-map
  (s/keys :opt-un [::property ::operator ::value ::metric ::direction-of-values ::unit]))
(s/def ::non-functional-guarantees (s/nilable (s/coll-of ::non-functional-guarantees-map :kind vector? :min-count 0)))

(def protection-types-set #{"PROACTIVE" "DETECTIVE" "REACTIVE"})
(s/def ::protection-type (s/and string? protection-types-set))
(s/def ::protection-types	(s/nilable (s/coll-of ::protection-type :kind vector? :min-count 0)))

(def security-properties-set #{"INTEGRITY" "AVAILABILITY" "NON_REPUDIATION" "CONFIDENTIALITY" "PRIVACY"})
(s/def ::security-property (s/and string? security-properties-set))
(s/def ::security-properties (s/nilable (s/coll-of ::security-property :kind vector? :min-count 0)))

(def protected-items-set #{"DATA" "EVERYTHING" "NETWORK" "INFRASTRUCTURE" "APPLICATIONS" "ENDPOINT_DEVICE"
                           "VMs" "CONTAINERS" "DATABASES" "FILES" "VPC" "SERVICES" "HYPERVISOR"
                           "CONTAINER_ORCHESTRATOR" "OS" "DEVICES"})
(s/def ::protected-item (s/and string? protected-items-set))
(s/def ::protected-items (s/nilable (s/coll-of ::protected-item :kind vector? :min-count 0)))

(s/def ::threat keyword?)
(s/def ::threats (s/nilable (s/coll-of ::threat :kind vector? :min-count 0)))

(s/def ::restrictions-map
  (s/keys :opt-un [::property ::operator ::value ::metric ::direction-of-values ::unit]))
(s/def ::restrictions (s/nilable (s/coll-of ::restrictions-map :kind vector? :min-count 0)))

(s/def ::open-source (s/nilable boolean?))
(s/def ::freely-available (s/nilable boolean?))
(s/def ::test-version (s/nilable boolean?))
(s/def ::test-duration (s/nilable nat-int?))

(def product-interfaces-set #{"Rest_API" "CLI" "Web_UI" "GUI"})
(s/def ::product-interface (s/and string? product-interfaces-set))
(s/def ::product-interfaces	(s/nilable (s/coll-of ::product-interface :kind vector? :min-count 0)))

(s/def ::product-company string?)

(def marketplaces-set #{"AWS" "GCP" "AZURE" "SEARCH_ENGINE"})
(s/def ::marketplace (s/and string? marketplaces-set))
(s/def ::marketplaces (s/nilable (s/coll-of ::marketplace :kind vector? :min-count 0)))

(def support-type-set #{"PHONE" "EMAIL" "FAQ_DOCUMENTATION" "PORTAL" "VIDEO" "TWITTER" "TROUBLESHOOTING_CENTER"
                        "SOFTWARE_VERSIONS" "CHAT" "FORUM" "TICKETS" "COMMUNITY" "NOTIFICATIONS" "BLOG"})
(s/def ::support-type (s/and string? support-type-set))
(s/def ::support-types (s/coll-of ::support-type :kind vector? :min-count 0))
(s/def ::support-daily-duration integer?)
(s/def ::support-package-number integer?)
(s/def ::support-map
  (s/keys :opt-un [::support-types ::support-daily-duration ::support-package-number]))
(s/def ::support (s/nilable (s/coll-of ::support-map :kind vector? :min-count 0)))

(s/def ::create-product-request
  (s/keys :req-un [::name ::description ::delivery-methods ::deployment-models ::product-categories
                   ::cost-model ::security-mechanisms ::non-functional-guarantees ::protection-types
                   ::security-properties ::protected-items ::threats ::restrictions ::open-source
                   ::freely-available ::test-version ::test-duration ::product-interfaces
                   ::product-company ::marketplaces ::support]))

(s/def ::weight
  #?(:clj (s/nilable (s/and decimal? #(>= % 0.0M) #(<= % 1.0M))))
  #?(:cljs (s/nilable (s/and number? #(>= % 0.0) #(<= % 1.0)))))

(s/def ::weight-map
  (s/map-of ::property ::weight))

(s/def ::non-functional-guarantees-w (s/nilable ::weight-map))
(s/def ::restrictions-w (s/nilable ::weight-map))
(s/def ::test-duration-w ::weight)

(s/def ::weights
  (s/keys :opt-un [::non-functional-guarantees-w ::restrictions-w ::test-duration-w]))

(s/def ::ids (s/nilable (s/coll-of ::id :kind vector? :min-count 0)))

(s/def ::classify-products-request
  (s/keys :req-un [::weights]
          :opt-un [::ids]))

(def logical-operators #{"AND" "OR" "XOR"})
(s/def ::logical-operator (s/and string? logical-operators))
(s/def ::not (s/nilable boolean?))
(s/def ::match-value
  (s/or :string string?
        :number number?
        :vector vector?))
(s/def ::property-name keyword?)
(s/def ::criterion
  (s/keys :req-un [::property-name ::match-value]
          :opt-un [::operator ::not]))

(s/def ::criteria (s/nilable (s/coll-of ::criterion :kind vector? :min-count 0)))

(s/def ::match-products-request
  (s/keys :req-un [::criteria]
          :opt-un [::logical-operator]))

(s/def ::discover-products-request
  (s/keys :req-un [::weights ::criteria]
          :opt-un [::logical-operator]))

(def ->operator
  {"LESS_THAN"          "<"
   "LESS_EQUAL_THAN"    "<="
   "GREATER_THAN"       ">"
   "GREATER_EQUAL_THAN" ">="
   "EQUAL"              "="
   "DIFFERENT"          "<>"
   "SUBSET"             "subset"
   "SUPERSET"           "superset"
   "INCLUDES"           "includes"
   "NON_INCLUDES"       "non-includes"})

(defn normalize-params
  [params user-info]
  (let [logical-operator (if (not user-info)
                           "AND"
                           (as-> params $
                                 (filter #(= :logical-operator (:property-name %)) $)
                                 (first $)
                                 (:match-value $)
                                 (or $ "AND")))
        params           (->> params
                              (filter #(not= :logical-operator (:property-name %)))
                              (map (fn [{:keys [operator] :as property}]
                                     (assoc property :operator (get ->operator operator "=")))))]
    {:logical-operator logical-operator
     :params           params}))
