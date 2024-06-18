(ns com.taxonomy.translations.gr)

(def taxonomy-ui
  {:missing-translation "Λείπει η μετάφραση"
   :previous-page       "Προηγούμενη σελίδα"
   :next-page           "Επόμενη σελίδα"})

(def product
  {})

(def end-user
  {:user-not-exists     "Ο χρήστης δεν υπάρχει"
   :user-already-exists "Ο χρήστης υπάρχει ήδη"})

(def translations
  {:com.taxonomy.ui       taxonomy-ui
   :com.taxonomy.product  product
   :com.taxonomy.end-user end-user})
