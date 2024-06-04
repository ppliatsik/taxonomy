(ns migratedb
  (:require migration))

(defn do-migrate-init [action]
      (when (= action "init")
            (migration/init))
      (if (= action "rollback")
        (migration/rollback)
        (migration/migrate)))

(defn -main [& args]
      (let [action (first args)]
           (do-migrate-init action)))
