(ns com.taxonomy.translations.en)

(def taxonomy-ui
  {:missing-translation "Missing translation"
   :previous-page       "Previous page"
   :next-page           "Next page"
   :success             "Success"
   :failure             "Failure"})

(def product
  {:product-not-exists     "Product does not exists"
   :product-already-exists "Product already exists"})

(def end-user
  {:user-not-exists                   "User does not exists"
   :user-already-exists               "User already exists"
   :user-already-active               "User already active"
   :user-provides-different-passwords "User provides different password"
   :user-provides-wrong-password      "User provides wrong password"
   :wrong-credentials                 "Wrong credentials"
   :user-is-inactive                  "User is inactive"
   :user-deactivated                  "User has been deactivated"
   :invalid-user                      "Invalid user"
   :invalid-token                     "Invalid token"
   :valid-token-exists                "Valid token exists"})

(def translations
  {:com.taxonomy.ui       taxonomy-ui
   :com.taxonomy.product  product
   :com.taxonomy.end-user end-user})
