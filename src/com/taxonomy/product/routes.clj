(ns com.taxonomy.product.routes
  (:require [com.taxonomy.product :as product]
            [com.taxonomy.product.api :as api]))

(def routes
  [["/products-match"
    {:swagger {:tags ["products"]}
     :name    ::product/products-match
     :post    {:summary    "Products match"
               :parameters {:body ::product/match-products-request}
               :handler    api/products-match}}]

   ["/products-classification"
    {:swagger {:tags ["products"]}
     :name    ::product/products-classification
     :post    {:summary    "Products classification"
               :parameters {:body ::product/classify-products-request}
               :handler    api/products-classification}}]

   ["/products-discovery"
    {:swagger {:tags ["products"]}
     :name    ::product/products-discovery
     :post    {:summary    "Products discovery"
               :parameters {:body ::product/discover-products-request}
               :handler    api/products-discovery}}]

   ["/my-products"
    {:swagger {:tags ["products"]}
     :name    ::product/my-products
     :get     {:summary "My products"
               :handler api/get-my-products}}]

   ["/products"
    [""
     {:swagger {:tags ["products"]}
      :name    ::product/products
      :post    {:summary    "Create product"
                :parameters {:body ::product/create-product-request}
                :handler    api/create-product}}]
    ["/:id"
     {:swagger {:tags ["products"]}
      :name    ::product/product
      :get     {:summary    "Get product"
                :parameters {:path {:id ::product/id}}
                :handler    api/get-product}
      :delete  {:summary    "Delete product"
                :parameters {:path {:id ::product/id}}
                :handler    api/delete-product}}]
    ["/:id/publish"
     {:swagger {:tags ["products"]}
      :name    ::product/publish
      :put     {:summary    "Publish product"
                :parameters {:path {:id ::product/id}}
                :handler    api/publish-product}}]
    ["/:id/unpublish"
     {:swagger {:tags ["products"]}
      :name    ::product/unpublish
      :put     {:summary    "Unpublish product"
                :parameters {:path {:id ::product/id}}
                :handler    api/unpublish-product}}]]])
