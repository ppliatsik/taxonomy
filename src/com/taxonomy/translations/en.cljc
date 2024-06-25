(ns com.taxonomy.translations.en)

(def taxonomy-ui
  {:missing-translation "Missing translation"
   :previous-page       "Previous page"
   :next-page           "Next page"})

(def product
  {:product-not-exists     "Product does not exists"
   :product-already-exists "Product already exists"})

(def end-user
  {:user-not-exists                   "User does not exists"
   :user-already-exists               "User already exists"
   :user-provides-different-passwords "User provides different password"
   :user-provides-wrong-password      "User provides wrong password"
   :invalid-token                     "Invalid token"})

(def translations
  {:com.taxonomy.ui       taxonomy-ui
   :com.taxonomy.product  product
   :com.taxonomy.end-user end-user})
