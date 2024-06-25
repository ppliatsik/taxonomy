(ns com.taxonomy.translations.gr)

(def taxonomy-ui
  {:missing-translation "Λείπει η μετάφραση"
   :previous-page       "Προηγούμενη σελίδα"
   :next-page           "Επόμενη σελίδα"})

(def product
  {:product-not-exists     "Το προϊόν δεν υπάρχει"
   :product-already-exists "Το προϊόν υπάρχει ήδη"})

(def end-user
  {:user-not-exists                   "Ο χρήστης δεν υπάρχει"
   :user-already-exists               "Ο χρήστης υπάρχει ήδη"
   :user-provides-different-passwords "Ο χρήστης έδωσε διαφορετικούς κωδικούς"
   :user-provides-wrong-password      "Ο χρήστης έδωσε λάθος κωδικό"
   :invalid-token                     "Μη έγκυρο token"})

(def translations
  {:com.taxonomy.ui       taxonomy-ui
   :com.taxonomy.product  product
   :com.taxonomy.end-user end-user})
