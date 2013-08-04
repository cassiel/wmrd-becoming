(ns cassiel.wmrd-becoming.persist
  "Simple persistence of some clip state."
  (:require [cassiel.wmrd-becoming [manifest :as m]]
            [clj-yaml.core :as yaml])
  (:import (java.io FileNotFoundException)))

(def STATE-FILE (str (System/getProperty "user.home") "/becoming-state.yml"))

;; Some YAML input (such as maps with integer keys) appears busted, so we need to convert
;; to/from a list of pairs:

(defn load-state []
  (try
    (apply hash-map (flatten (yaml/parse-string (slurp STATE-FILE))))
    (catch FileNotFoundException _ { })))

(defn store-state [state]
  (spit STATE-FILE (yaml/generate-string (seq state))))

(defn mark-used [state clip]
  (assoc state clip {:used-at (java.util.Date.)}))

(defn used? [state clip]
  (contains? state clip))
