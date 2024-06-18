(ns com.taxonomy.end-user.routes
  (:require [com.taxonomy.http.middleware :as mw]
            [com.taxonomy.end-user :as end-user]
            [com.taxonomy.end-user.api :as api]))

(def routes
  [["/login"
    {:swagger {:tags ["users"]}
     :name    ::end-user/login-user
     :post    {:summary    "Login user"
               :parameters {:path {:username ::end-user/username}
                            :body {:password ::end-user/password}}
               :handler    api/login}}]

   ["/users"
    [""
     {:swagger {:tags ["users"]}
      :name    ::end-user/users
      :post    {:summary    "Create user"
                :middleware [mw/coerce-user-creation-body]
                :handler    api/create-user}
      :get     {:summary    "Get all users"
                :parameters {:query ::end-user/get-users-request}
                :handler    api/get-users}}]
    ["/:username/activate"
     {:swagger {:tags ["users"]}
      :name    ::end-user/activate-user
      :put     {:summary    "Activate user"
                :parameters {:path {:username ::end-user/username}}
                :handler    api/activate-user}}]
    ["/:username/change-password"
     {:swagger {:tags ["users"]}
      :name    ::end-user/change-user-password
      :put     {:summary    "Change user password"
                :parameters {:path {:username ::end-user/username}
                             :body ::end-user/change-user-password-request}
                :handler    api/change-user-password}}]
    ["/:username/reset-password"
     {:swagger {:tags ["users"]}
      :name    ::end-user/reset-user-password
      :put     {:summary    "Reset user password"
                :parameters {:path {:username ::end-user/username}
                             :body {:email ::end-user/email}}
                :handler    api/reset-user-password}}]
    ["/:username/update-info"
     {:swagger {:tags ["users"]}
      :name    ::end-user/update-user-info
      :put     {:summary    "Update user info"
                :middleware [mw/coerce-user-update-body]
                :parameters {:path {:username ::end-user/username}
                             :body ::end-user/update-user-info-request}
                :handler    api/update-user-info}}]]])
