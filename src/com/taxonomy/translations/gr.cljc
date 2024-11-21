(ns com.taxonomy.translations.gr)

(def taxonomy-ui
  {:missing-translation "Λείπει η μετάφραση"
   :previous-page       "Προηγούμενη σελίδα"
   :next-page           "Επόμενη σελίδα"
   :success             "Επιτυχία"
   :failure             "Αποτυχία"
   :submit              "Υποβολή"
   :main-view           "Taxonomy"})

(def product
  {:product-not-exists         "Το προϊόν δεν υπάρχει"
   :product-already-exists     "Το προϊόν υπάρχει ήδη"
   :secaas-products-management "Διαχείριση προϊόντων SecaaS"
   :create-product             "Δημιουργία προϊόντος SecaaS"
   :products                   "Προϊόντα SecaaS"
   :product                    "Προϊόν SecaaS"
   :my-products                "Τα προϊόντα SecaaS μου"
   :match                      "Ταίριασμα SecaaS προϊόντων"
   :classification             "Ταξινόμηση SecaaS προϊόντων"
   :discover                   "Ανακάλυψη SecaaS προϊόντων"})

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
   :password-verification             "Επαλήθευση κωδικού"
   :first-name                        "Όνομα"
   :last-name                         "Επώνυμο"
   :email                             "Email"
   :old-password                      "Παλιός κωδικός"
   :users-management                  "Διαχείριση χρηστών"
   :edit                              "Επεξεργασία"
   :change-password                   "Αλλαγή κωδικού"
   :activate                          "Ενεργοποίηση"
   :deactivate                        "Απενεργοποίηση"
   :delete                            "Διαγραφή"
   :reset-password                    "Επαναφορά κωδικού"
   :wrong-input                       "Λάθος είσοδος"
   :new-password-sent                 "Ένας νέος κωδικός στάλθηκε στο email σας"
   :activate-mail-link                "Email στάλθηκε στον χρήστη για να ενεργοποιήσει τον λογαριασμό του"
   :send-activate-link                "Αποστολή συνδέσμου ενεργοποίησης"
   :account-activated                 "Ο λογαριασμός ενεργοποιήθηκε"
   :account-activation-problem        "Πρόβλημα στην ενεργοποίηση του λογαριασμού σας, παρακαλώ επικοινωνήστε με τον διαχειριστή"})

(def translations
  {:com.taxonomy.ui       taxonomy-ui
   :com.taxonomy.product  product
   :com.taxonomy.end-user end-user})
