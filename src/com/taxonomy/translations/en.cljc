(ns com.taxonomy.translations.en)

(def taxonomy-ui
  {:missing-translation "Missing translation"
   :previous-page       "Previous page"
   :next-page           "Next page"})

(def product
  {})

(def end-user
  {:user-not-exists                   "User does not exists"
   :user-already-exists               "User already exists"
   :user-provides-different-passwords "User provides different password"
   :user-provides-wrong-password      "User provides wrong password"})

(def translations
  {:com.taxonomy.ui       taxonomy-ui
   :com.taxonomy.product  product
   :com.taxonomy.end-user end-user})
