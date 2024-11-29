(ns com.taxonomy.translations.gr)

(def taxonomy-ui
  {:missing-translation                "Λείπει η μετάφραση"
   :previous-page                      "Προηγούμενη σελίδα"
   :next-page                          "Επόμενη σελίδα"
   :success                            "Επιτυχία"
   :failure                            "Αποτυχία"
   :submit                             "Υποβολή"
   :main-view                          "Taxonomy"
   :yes                                "Ναι"
   :no                                 "Όχι"
   :delete-dialog-box                  "Αίτημα διαγραφής"
   :delete-dialog-box-confirmation-msg "Είστε σίγουρος/η ότι θέλετε να προχωρήσετε με τη διαγραφή;"
   :select-language                    "Επιλογή γλώσσας"})

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
   :discovery                  "Ανακάλυψη SecaaS προϊόντων"
   :product-published          "Το προϊόν δημοσιεύτηκε"
   :product-unpublished        "Το προϊόν από-δημοσιεύτηκε"
   :product-deleted            "Το προϊόν διαγράφηκε"
   :product-created            "Το προϊόν δημιουργήθηκε"
   :publish                    "Δημοσίευση"
   :unpublish                  "Από-δημοσίευση"
   :delete                     "Διαγραφή"
   :choose-criteria            "Επιλέξτε κριτήρια για ταίριασμα SecaaS προϊόντων"
   :choose-weights             "Επιλέξτε βάρη για ταξινόμηση των SecaaS προϊόντων"
   :name                       "Όνομα"
   :delivery-methods           "Τρόποι παράδοσης"
   :deployment-models          "Μοντέλο διάταξης"
   :product-categories         "Κατηγορία προϊόντος"
   :product-company            "Εταιρία προϊόντος"
   :marketplaces               "Σημεία αγοράς"})

(def end-user
  {:user-not-exists                   "Ο χρήστης δεν υπάρχει"
   :user-already-exists               "Ο χρήστης υπάρχει ήδη"
   :user-already-active               "Ο χρήστης είναι ήδη ενεργός"
   :user-already-inactive             "Ο χρήστης είναι ήδη ανενεργός"
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

(def security-mechanisms
  {:threat-detection                                                 "Ανίχνευση απειλών"
   :threat-detection-based-on-behavior                               "Ανίχνευση απειλών με βάση τη συμπεριφορά"
   :threat-detection-based-on-ai                                     "Ανίχνευση απειλών με βάση τη ΤΝ"
   :threat-detection-based-on-logs                                   "Ανίχνευση απειλών με βάση αρχεία καταγραφής"
   :threat-detection-based-on-third-party-data                       "Ανίχνευση απειλών με βάση δεδομένα τρίτων (ευφυΐα απειλών)"
   :threat-detection-based-on-adapted-queries                        "Ανίχνευση απειλών με βάση προσαρμοσμένα ερωτήματα"
   :threat-detection-in-real-time                                    "Ανίχνευση απειλών σε πραγματικό χρόνο"
   :threat-detection-in-real-time-in-logs                            "Ανίχνευση απειλών σε πραγματικό χρόνο σε αρχεία καταγραφής"
   :threat-detection-in-real-time-based-on-machine-learning          "Ανίχνευση απειλών σε πραγματικό χρόνο με βάση τη μηχανική μάθηση"
   :threat-detection-in-real-time-based-on-rules                     "Ανίχνευση απειλών σε πραγματικό χρόνο με βάση κανόνες"
   :threat-detection-in-real-time-based-on-rules-correlation         "Ανίχνευση απειλών σε πραγματικό χρόνο με βάση κανόνες συσχέτισης"
   :threat-detection-in-real-time-based-on-rules-threshold-detection "Ανίχνευση απειλών σε πραγματικό χρόνο με βάση κανόνες ανίχνευσης κατωφλιού"
   :threat-detection-in-real-time-based-on-rules-anomaly-detection   "Ανίχνευση απειλών σε πραγματικό χρόνο με βάση κανόνες ανίχνευσης ανωμαλιών"

   :response                                   "Απάντηση"
   :individual-response                        "Μεμονωμένη απάντηση"
   :apply-security-fixes                       "Εφαρμογή επιδιορθώσεων ασφάλειας"
   :recovery-from-backups                      "Ανάνηψη μέσω αντιγράφων ασφαλείας"
   :kill-malicious-processes                   "Θανάτωση κακόβουλων διαδικασιών"
   :user-logout                                "Αποσύνδεση χρηστών"
   :revoke-user-permissions                    "Ανάκληση αδειών χρήστη"
   :disable-user-account                       "Απενεργοποίηση λογαριασμού χρήστη"
   :generate-alerts                            "Παραγωγή ειδοποιήσεων"
   :generate-rich-alerts                       "Παραγωγή εμπλουτισμένων ειδοποιήσεων"
   :malware-quarantine                         "Καραντίνα κακόβουλου λογισμικού"
   :packet-expulsion                           "Αποπομπή πακέτων"
   :apply-requests-flow-restriction-techniques "Εφαρμογή τεχνικών περιορισμού ροής αιτήσεων"
   :regulation-of-firewall-rules               "Ρύθμιση κανόνων τείχους προστασίας"
   :activation-of-multi-factor-authentication  "Ενεργοποίηση αυθεντικοποίησης πολλαπλών παραγόντων"
   :network-segmentation                       "Τμηματοποίηση δικτύου"
   :isolation                                  "Απομόνωση"
   :isolation-of-ip                            "Απομόνωση IP διευθύνσεων"
   :isolation-of-network                       "Απομόνωση δικτύου"
   :isolation-of-infected-machines             "Απομόνωση μολυσμένων μηχανημάτων"
   :isolation-of-usb-devices                   "Απομόνωση συσκευών USB"
   :investigation-of-an-incident               "Διερεύνηση συμβάντος"
   :data-integrity-checks                      "Έλεγχοι ακεραιότητας δεδομένων"
   :backup-encryption                          "Κρυπτογράφηση αντιγράφων ασφαλείας"
   :complex-response                           "Σύνθετη απάντηση"
   :non-automated-response                     "Μη αυτοματοποιημένη απάντηση"
   :apply-incident-response-plan               "Εφαρμογή σχεδίου απόκρισης συμβάντων"
   :automated-response                         "Αυτοματοποιημένη απάντηση"
   :execution-of-workflows                     "Εκτέλεση ροών εργασίας"

   :risk-management                                                                "Διαχείριση κινδύνου"
   :asset-discovery                                                                "Ανακάλυψη περιουσιακών στοιχείων"
   :identifying-common-threats-or-attacks-within-the-framework-miter-attack        "Εντοπισμός κοινών απειλών ή επιθέσεων εντός του πλαισίου MITER ATT&CK®"
   :gathering-and-correlating-suspected-or-confirmed-threats-with-incident-data    "Συγκέντρωση και συσχέτιση ύποπτων ή επιβεβαιωμένων απειλών με δεδομένα συμβάντων"
   :vulnerabilities-detection                                                      "Ανίχνευση τρωτοτήτων"
   :quantification-prioritisation-of-potential-risks                               "Ποσοτικοποίηση και ιεράρχηση πιθανών κινδύνων"
   :quantification-prioritisation-of-potential-risks-from-network-traffic-patterns "Ποσοτικοποίηση και ιεράρχηση πιθανών κινδύνων από μοτίβα κίνησης δικτύου"
   :risk-rating                                                                    "Βαθμολόγηση κινδύνου"
   :monitoring                                                                     "Παρακολούθηση"
   :combined-security-and-network-monitoring                                       "Συνδυασμένη παρακολούθηση ασφάλειας και λειτουργίας δικτύου"
   :monitoring-device-and-software-configuration-changes                           "Παρακολούθηση αλλαγών διαμόρφωσης συσκευής και λογισμικού"

   :data-and-events-management                         "Διαχείριση δεδομένων και συμβάντων"
   :logs-management                                    "Διαχείριση αρχείων καταγραφής"
   :insert-logs                                        "Εισαγωγή αρχείων καταγραφής"
   :collect-logs                                       "Συλλογή αρχείων καταγραφής"
   :normalization-logs                                 "Κανονικοποίηση αρχείων καταγραφής"
   :enrichment-logs                                    "Εμπλουτισμός αρχείων καταγραφής"
   :storage-logs                                       "Αποθήκευση αρχείων καταγραφής"
   :analyze-logs                                       "Ανάλυση αρχείων καταγραφής"
   :monitor-logs                                       "Παρακολούθηση αρχείων καταγραφής"
   :digital-signature-logs                             "Ψηφιακή υπογραφή αρχείων καταγραφής"
   :encrypt-logs                                       "Κρυπτογράφηση αρχείων καταγραφής"
   :incidents-management                               "Διαχείριση συμβάντων"
   :collect-incidents                                  "Συλλογή συμβάντων"
   :collection-of-third-party-security-incidents       "Συλλογή ειδοποιήσεων ασφάλειας τρίτων μερών"
   :enrichment-incidents                               "Εμπλουτισμός συμβάντων"
   :enrichment-incidents-with-threat-information-flows "Εμπλουτισμός συμβάντων με ροές πληροφοριών απειλών"
   :correlation-incidents                              "Συσχετισμός συμβάντων"
   :correlation-of-distributed-incidents               "Συσχετισμός κατανεμημένων συμβάντων"
   :storage-incidents                                  "Αποθήκευση συμβάντων"
   :long-term-storage-incidents                        "Μακροπρόθεσμη αποθήκευση συμβάντων"
   :data-management                                    "Διαχείριση δεδομένων"
   :historical-data-management                         "Διαχείριση ιστορικών δεδομένων"
   :collect-historical-data                            "Συλλογή ιστορικών δεδομένων"
   :edit-historical-data                               "Επεξεργασία ιστορικών δεδομένων"
   :analyze-historical-data                            "Ανάλυση ιστορικών δεδομένων"
   :storage-historical-data                            "Αποθήκευση ιστορικών δεδομένων"
   :smart-data-management                              "Διαχείριση έξυπνων δεδομένων"
   :analyze-smart-data                                 "Ανάλυση έξυπνων δεδομένων"
   :correlate-smart-data                               "Συσχετισμός έξυπνων δεδομένων"
   :search-data                                        "Αναζήτηση δεδομένων"
   :search-for-normalised-data                         "Αναζήτηση κανονικοποιημένων δεδομένων"
   :raw-data-search                                    "Αναζήτηση ακατέργαστων δεδομένων"
   :collect-data                                       "Συλλογή δεδομένων"
   :environmental-data-collection                      "Συλλογή δεδομένων περιβάλλοντος"
   :edit-data                                          "Επεξεργασία δεδομένων"
   :analyze-data                                       "Ανάλυση δεδομένων"
   :storage-data                                       "Αποθήκευση δεδομένων"
   :data-visualization                                 "Οπτικοποίηση δεδομένων"
   :update-data                                        "Ενημέρωση δεδομένων"
   :continuous-threat-intelligence-updates             "Συνεχείς ενημερώσεις πληροφοριών απειλών"

   :reports-compliance              "Αναφορές και Συμμόρφωση"
   :reports                         "Αναφορές"
   :normalised-data-reports         "Αναφορές κανονικοποιημένων δεδομένων"
   :raw-data-reports                "Αναφορές σε ακατέργαστα δεδομένα"
   :compliance-reports              "Αναφορές συμμόρφωσης"
   :customisable-compliance-reports "Προσαρμόσιμες αναφορές συμμόρφωσης"
   :prepared-compliance-reports     "Προπαρασκευασμένες αναφορές συμμόρφωσης"
   :support-compliance-regulations  "Υποστήριξη κανονισμών συμμόρφωσης"
   :basel-II                        "Basel II"
   :ffiec                           "FFIEC"
   :support-compliance-laws         "Υποστήριξη νόμων συμμόρφωσης"
   :bill-198                        "Bill 198"
   :ferpa                           "FERPA"
   :fisma                           "FISMA"
   :glba                            "GLBA"
   :hipaa                           "HIPAA"
   :sox                             "SOX"
   :support-compliance-guidance     "Υποστήριξη οδηγιών συμμόρφωσης"
   :gpg13                           "GPG13"
   :nispom                          "NISPOM"
   :ssae                            "SSAE"
   :disa-stig                       "DISA STIG"
   :nist-csf                        "NIST CSF"
   :support-compliance-to-standards "Υποστήριξη/συμμόρφωση σε πρότυπα"
   :iso-27002                       "ISO 27002"
   :nerc-cip                        "NERC CIP"
   :pci-dss                         "PCI DSS"

   :integrations                                  "Ενσωματώσεις"
   :integrations-with-other-components-or-systems "Ενσωματώσεις με άλλα συστατικά ή συστήματα"
   :network                                       "δίκτυο"
   :identity-providers                            "πάροχοι ταυτότητας"
   :endpoints                                     "τελικά σημεία"
   :applications                                  "εφαρμογές"
   :cloud-services                                "υπηρεσίες νέφους"
   :iaas                                          "IaaS"
   :paas                                          "PaaS"
   :saas                                          "SaaS"
   :chat-systems                                  "συστήματα συνομιλίας"
   :recovery-tools                                "εργαλεία αποκατάστασης"
   :security-ecosystems                           "οικοσυστήματα ασφάλειας"

   :other-functions                                                      "Άλλες λειτουργίες (ασφάλειας)"
   :tool-boards                                                          "Πίνακες εργαλείων"
   :predefined-tool-tables                                               "Προκαθορισμένοι πίνακες εργαλείων"
   :customizable-tool-tables                                             "Προσαρμόσιμοι πίνακες εργαλείων"
   :rules-management                                                     "Διαχείριση κανόνων"
   :rules-adaptation                                                     "Προσαρμογή κανόνων"
   :rules-validation                                                     "Επικύρωση κανόνων"
   :rules-visualisation                                                  "Οπτικοποίηση κανόνων"
   :rules-grouping                                                       "Ομαδοποίηση κανόνων"
   :provision-of-content-packages                                        "Παροχή πακέτων περιεχομένου"
   :team-collaboration                                                   "Συνεργασία ομάδων"
   :diary-management-for-ongoing-compliance-investigations-and-forensics "Διαχείριση ημερολογίου για συνεχείς έρευνες συμμόρφωσης και forensics"})

(def threats
  {:unintentional-damage            "Ακούσια βλάβη/απώλεια πληροφοριών ή περιουσιακών στοιχείων ΠΣ"
   :damage-due-to-human-error       "Βλάβη από ανθρώπινο λάθος"
   :damage-due-to-software-error    "Βλάβη από λάθος λογισμικού"
   :damage-due-to-physical-disaster "Βλάβη από φυσική καταστρογή"
   :damage-due-to-power-outages     "Βλάβη από διακοπές ρεύματος"

   :failures-malfunction                  "Αποτυχίες/ δυσλειτουργία"
   :failures-of-devices-or-systems        "Αποτυχία συσκευών ή συστημάτων"
   :failures-or-disruption-of-main-supply "Αποτυχία ή διακοπή της κύριας παροχής"

   :data-threats   "Απειλές δεδομένων"
   :data-lock-in   "Κλείδωμα δεδομένων"
   :data-remanence "Επαναφορά δεδομένων"
   :data-loss      "Απώλεια δεδομένων"
   :data-breach    "Παραβίαση δεδομένων"

   :application-threats        "Απειλές εφαρμογών"
   :injection                  "Έγχυση"
   :sql-injection              "Έγχυση SQL"
   :command-injection          "Έγχυση εντολών"
   :xss                        "XSS"
   :csrf                       "CSRF"
   :xml-wrapper-attacks        "Επιθέσεις περιτυλίγματος XML"
   :bad-software-configuration "Κακή διαμόρφωση λογισμικού"
   :backdoor-debugging-options "Backdoor/επιλογές εντοπισμού σφαλμάτων"

   :nefarious-activity-abuse              "Κακόβουλη δραστηριότητα/ κατάχρηση"
   :targeted-attacks                      "Στοχευμένες επιθέσεις (APTs κτλ.)"
   :priviledge-exploitation               "Εκμετάλλευση προνομίων"
   :abuse-of-authorizations               "Κατάχρηση εξουσιοδοτήσεων"
   :priviledge-escalation                 "Κλιμάκωση προνομίων"
   :compromising-confidential-information "Παραβίαση εμπιστευτικών πληροφοριών"
   :information-modification              "Τροποποίηση πληροφοριών"
   :information-loss                      "Απώλεια πληροφοριών"
   :information-leakage                   "Διαρροή πληροφοριών"
   :mitm                                  "MitM"
   :eavesdropping                         "Υποκλοπές"
   :account-hijacking                     "Υποκλοπή λογαριασμού"
   :identity-theft                        "Κλοπή ταυτότητας"
   :dos                                   "DOS"
   :ddos                                  "DDOS"
   :flood-attack                          "Επίθεση πλημμύρας"
   :protocol-based-attack                 "Επίθεση βάσει πρωτοκόλλου"
   :buffer-overflow-attack                "Επίθεση υπερχείλισης buffer"
   :malformed-packet-attack               "Επίθεση με κακοσχηματισμένα πακέτα"
   :rejection-attack                      "Επίθεση αντανάκλασης"
   :amplification-attack                  "Επίθεση ενίσχυσης"
   :malicious-code-software-activity      "Κακόβουλος κώδικας/ λογισμικό/ δραστηριότητα"
   :viruses                               "Ιοί"
   :trojan-horses                         "Δούρειοι ίπποι"
   :ransomware                            "Λογισμικό λύτρων"
   :worms                                 "Σκουλήκια"
   :rootkits                              "Rootkits"
   :social-engineering                    "Κοινωνική μηχανική"
   :baiting                               "Δόλωμα"
   :impersonation                         "Πλαστοπροσωπία"
   :phishing-attacks                      "Επιθέσεις phishing"
   :quid-pro-quo                          "Quid pro quo"
   :popup-windows                         "Αναδυόμενα παράθυρα"
   :scareware                             "Λογισμικό τρόμου"
   :pretexting                            "Pretexting"
   :multimedia-peripheral-masquerading    "Μεταμφίεση πολυμέσων/περιφερειακών συσκευών"
   :typosquatting                         "Τυποποίηση"
   :receive-of-unsolicited-e-mail         "Λήψη ανεπιθύμητων e-mail"

   :physical-attack                  "Φυσική επίθεση"
   :theft                            "Κλοπή"
   :fraud                            "Απάτη"
   :sabotage                         "Δολιοφθορά"
   :vandalism                        "Βανδαλισμός"
   :damage-from-the-warfare          "Ζημιές από τις πολεμικές επιχειρήσεις"
   :coercion-extortion-or-corruption "Εξαναγκασμός, εκβιασμός ή διαφθορά"
   :terrorists-attack                "Επίθεση από τρομοκράτες"
   :piggybacking-tailgating          "Piggybacking/tailgating"
   :dumpster-diving                  "Dumpster diving"
   :shoulder-surfing                 "Shoulder surfing"})

(def translations
  {:com.taxonomy.ui                  taxonomy-ui
   :com.taxonomy.product             product
   :com.taxonomy.end-user            end-user
   :com.taxonomy.security-mechanisms security-mechanisms
   :com.taxonomy.threats             threats})
