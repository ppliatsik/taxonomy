(ns com.taxonomy.translations.gr)

(def taxonomy-ui
  {:missing-translation "Λείπει η μετάφραση"
   :previous-page       "Προηγούμενη σελίδα"
   :next-page           "Επόμενη σελίδα"})

(def product
  {})

(def end-user
  {:user-not-exists                   "Ο χρήστης δεν υπάρχει"
   :user-already-exists               "Ο χρήστης υπάρχει ήδη"
   :user-provides-different-passwords "Ο χρήστης έδωσε διαφορετικούς κωδικούς"
   :user-provides-wrong-password      "Ο χρήστης έδωσε λάθος κωδικό"})

(def translations
  {:com.taxonomy.ui       taxonomy-ui
   :com.taxonomy.product  product
   :com.taxonomy.end-user end-user})
