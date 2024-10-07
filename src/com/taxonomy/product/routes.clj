(ns com.taxonomy.product.routes
  (:require [com.taxonomy.product :as product]
            [com.taxonomy.product.api :as api]))

(def routes
  [["/products-match"
    {:swagger {:tags ["products"]}
     :name    ::product/products-match
     :get     {:summary    "Products match"
               :parameters {:query ::product/get-products-request}
               :handler    api/products-match}}]

   ["/products-classification"
    {:swagger {:tags ["products"]}
     :name    ::product/products-classification
     :get     {:summary    "Products classification"
               :parameters {:query ::product/get-products-request}
               :handler    api/products-classification}}]

   ["/products-discovery"
    {:swagger {:tags ["products"]}
     :name    ::product/products-discovery
     :get     {:summary    "Products discovery"
               :parameters {:query ::product/get-products-request}
               :handler    api/products-discovery}}]

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
