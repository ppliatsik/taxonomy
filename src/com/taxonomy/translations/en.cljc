(ns com.taxonomy.translations.en)

(def taxonomy-ui
  {:missing-translation "Missing translation"
   :previous-page       "Previous page"
   :next-page           "Next page"
   :success             "Success"
   :failure             "Failure"
   :submit              "Submit"
   :main-view           "Taxonomy"})

(def product
  {:product-not-exists         "Product does not exists"
   :product-already-exists     "Product already exists"
   :secaas-products-management "SecaaS products management"
   :create-product             "Create SecaaS product"
   :products                   "SecaaS products"
   :product                    "SecaaS product"
   :my-products                "My SecaaS products"
   :match                      "Match SecaaS products"
   :classification             "Classify SecaaS products"
   :discover                   "Discover SecaaS products"})

(def end-user
  {:user-not-exists                   "User does not exists"
   :user-already-exists               "User already exists"
   :user-already-active               "User already active"
   :user-provides-different-passwords "User provides different password"
   :user-provides-wrong-password      "User provides wrong password"
   :user-is-not-login                 "User is not logged in"
   :wrong-credentials                 "Wrong credentials"
   :user-is-inactive                  "User is inactive"
   :user-deactivated                  "User has been deactivated"
   :invalid-user                      "Invalid user"
   :invalid-token                     "Invalid token"
   :valid-token-exists                "Valid token exists"
   :login                             "Login"
   :logout                            "Logout"
   :register                          "Register"
   :users                             "Users"
   :user                              "User"
   :username                          "Username"
   :password                          "Password"
   :password-verification             "Verify password"
   :first-name                        "First name"
   :last-name                         "Last name"
   :email                             "Email"
   :old-password                      "Old password"
   :users-management                  "Users management"
   :edit                              "Edit"
   :change-password                   "Change password"
   :activate                          "Activate"
   :deactivate                        "Deactivate"
   :delete                            "Delete"
   :reset-password                    "Reset password"
   :wrong-input                       "Wrong input"})

(def translations
  {:com.taxonomy.ui       taxonomy-ui
   :com.taxonomy.product  product
   :com.taxonomy.end-user end-user})
