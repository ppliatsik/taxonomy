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
   :products-menu              "Μενού προϊόντων"
   :match                      "Ταίριασμα SecaaS προϊόντων"
   :classification             "Ταξινόμηση SecaaS προϊόντων"
   :discovery                  "Ανακάλυψη SecaaS προϊόντων"
   :product-published          "Το προϊόν δημοσιεύτηκε"
   :product-unpublished        "Το προϊόν από-δημοσιεύτηκε"
   :product-deleted            "Το προϊόν διαγράφηκε"
   :product-created            "Το προϊόν δημιουργήθηκε"
   :publish                    "Δημοσίευση"
   :unpublish                  "Από-δημοσίευση"
   :delete                     "Διαγραφή"
   :name                       "Όνομα"})

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
   :account-activation-problem        "Πρόβλημα στην ενεργοποίηση του λογαριασμού σας, παρακαλώ επικοινωνήστε με τον διαχειριστή"
   :password-changed                  "Ο κωδικός άλλαξε"
   :user-registered                   "Η εγγραφή ολοκληρώθηκε"
   :user-edited                       "Οι πληροφορίες του χρήστη άλλαξαν"})

(def translations
  {:com.taxonomy.ui       taxonomy-ui
   :com.taxonomy.product  product
   :com.taxonomy.end-user end-user})
