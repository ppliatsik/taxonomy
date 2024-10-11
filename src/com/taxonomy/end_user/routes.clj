(ns com.taxonomy.end-user.routes
  (:require [com.taxonomy.end-user :as end-user]
            [com.taxonomy.end-user.api :as api]))

(def routes
  [["/login"
    {:swagger {:tags ["users"]}
     :name    ::end-user/login-user
     :post    {:summary    "Login user"
               :parameters {:body {:username ::end-user/username
                                   :password ::end-user/password}}
               :handler    api/login}}]

   ["/logout"
    {:swagger {:tags ["users"]}
     :name    ::end-user/logout-user
     :post    {:summary    "Logout user"
               :handler    api/logout}}]

   ["/email-activate-account"
    {:swagger {:tags ["users"]}
     :name    ::end-user/email-activate-account
     :post    {:summary    "Activate account by email verification"
               :parameters {:query {:token ::end-user/token}}
               :handler    api/email-activate-account}}]

   ["/users"
    [""
     {:swagger {:tags ["users"]}
      :name    ::end-user/users
      :post    {:summary    "Create user"
                :parameters {:body ::end-user/create-user-request}
                :handler    api/create-user}
      :get     {:summary    "Get all users"
                :parameters {:query ::end-user/get-users-request}
                :handler    api/get-users}}]
    ["/:username"
     {:swagger {:tags ["users"]}
      :name    ::end-user/user
      :get     {:summary    "Get user"
                :parameters {:path {:username ::end-user/username}}
                :handler    api/get-user}
      :delete  {:summary    "Delete user"
                :parameters {:path {:username ::end-user/username}}
                :handler    api/delete-user}}]
    ["/:username/activate"
     {:swagger {:tags ["users"]}
      :name    ::end-user/activate-user
      :put     {:summary    "Activate user"
                :parameters {:path {:username ::end-user/username}}
                :handler    api/activate-user}}]
    ["/:username/deactivate"
     {:swagger {:tags ["users"]}
      :name    ::end-user/deactivate-user
      :put     {:summary    "Deactivate user"
                :parameters {:path {:username ::end-user/username}}
                :handler    api/deactivate-user}}]
    ["/:username/resend-email-activation-account"
     {:swagger {:tags ["users"]}
      :name    ::end-user/resend-email-activation-account
      :post    {:summary    "Resend email activation account"
                :parameters {:path {:username ::end-user/username}}
                :handler    api/resend-email-activation-account}}]
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
                :parameters {:path {:username ::end-user/username}
                             :body ::end-user/update-user-info-request}
                :handler    api/update-user-info}}]]])
