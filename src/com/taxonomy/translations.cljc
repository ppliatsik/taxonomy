(ns com.taxonomy.translations
  (:require [tongue.core :as tongue]
            [com.taxonomy.translations.en :as en]
            [com.taxonomy.translations.gr :as gr]))

(def dicts
  {:tongue/fallback :gr
   :en              en/translations
   :gr              gr/translations
   :el              gr/translations})

(let [dictionary (tongue/build-translate dicts)]
  (defn tr
    [language key]
    (dictionary (or language :gr) (or key :com.taxonomy.ui/missing-translation))))
