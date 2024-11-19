(ns com.taxonomy.translations.gr)

(def taxonomy-ui
  {:missing-translation "Λείπει η μετάφραση"
   :previous-page       "Προηγούμενη σελίδα"
   :next-page           "Επόμενη σελίδα"
   :success             "Επιτυχία"
   :failure             "Αποτυχία"})

(def product
  {:product-not-exists         "Το προϊόν δεν υπάρχει"
   :product-already-exists     "Το προϊόν υπάρχει ήδη"
   :secaas-products-management "Διαχείριση προϊόντων SecaaS"
   :create-product             "Δημιουργία προϊόντος SecaaS"
   :products                   "Προϊόντα SecaaS"
   :product                    "Προϊόν SecaaS"})

(def end-user
  {:user-not-exists                   "Ο χρήστης δεν υπάρχει"
   :user-already-exists               "Ο χρήστης υπάρχει ήδη"
   :user-already-active               "Ο χρήστης είναι ήδη ενεργός"
   :user-provides-different-passwords "Ο χρήστης έδωσε διαφορετικούς κωδικούς"
   :user-provides-wrong-password      "Ο χρήστης έδωσε λάθος κωδικό"
   :user-is-not-login                 "Ο χρήστης δεν έχει συνδεθεί"
   :wrong-credentials                 "Λάθος διαπιστευτήρια"
   :user-is-inactive                  "Ο χρήστης δεν είναι ενεργός"
   :user-deactivated                  "Ο χρήστης απενεργοποιήθηκε"
   :invalid-user                      "Μη έγκυρος χρήστης"
   :invalid-token                     "Μη έγκυρο token"
   :valid-token-exists                "Υπάρχει έγκυρο token"
   :login                             "Σύνδεση"
   :logout                            "Αποσύνδεση"
   :register                          "Εγγραφή"
   :users                             "Χρήστες"
   :user                              "Χρήστης"
   :username                          "Όνομα Χρήστη"
   :password                          "Κωδικός"
   :users-management                  "Διαχείριση χρηστών"})

(def translations
  {:com.taxonomy.ui       taxonomy-ui
   :com.taxonomy.product  product
   :com.taxonomy.end-user end-user})
