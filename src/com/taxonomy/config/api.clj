(ns com.taxonomy.config.api
  (:require [com.taxonomy.http.http-response :as http-response]
            [com.taxonomy.product :as product]))

(defn get-security-mechanisms
  [{:keys [security-mechanisms] :as req}]
  (http-response/ok security-mechanisms))

(defn get-threats
  [{:keys [threats] :as req}]
  (http-response/ok threats))

(defn get-products-choices
  [req]
  (http-response/ok {:delivery-methods    product/delivery-methods-set
                     :deployment-models   product/deployment-models-set
                     :product-categories  product/product-categories-set
                     :cost-model-types    product/cost-model-types-set
                     :time-charge-types   product/time-charge-type-set
                     :operators           product/operator-types
                     :protection-types    product/protection-types-set
                     :security-properties product/security-properties-set
                     :protected-items     product/protected-items-set
                     :product-interfaces  product/product-interfaces-set
                     :marketplaces        product/marketplaces-set
                     :support-types       product/support-type-set
                     :logical-operators   product/logical-operators}))
