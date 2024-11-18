(ns com.taxonomy.translations.en)

(def taxonomy-ui
  {:missing-translation "Missing translation"
   :previous-page       "Previous page"
   :next-page           "Next page"
   :success             "Success"
   :failure             "Failure"})

(def product
  {:product-not-exists         "Product does not exists"
   :product-already-exists     "Product already exists"
   :secaas-products-management "SecaaS products management"})

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
   :register                          "Register"
   :username                          "Username"
   :password                          "Password"
   :users-management                  "Users management"})

(def translations
  {:com.taxonomy.ui       taxonomy-ui
   :com.taxonomy.product  product
   :com.taxonomy.end-user end-user})
