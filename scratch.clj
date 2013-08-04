(ns user
  (:require (cassiel.wmrd-becoming [manifest :as m]
                                   [layout :as lx]
                                   [persist :as ps])
            [clj-http.client :as client]
            [clj-yaml.core :as yaml])
  (:import (java.io File)))

(defn foo [& args] (apply hash-map args))

(foo :A 3 :B 4)

(name :A)
(name "A")

(defn format-it [& items]
  (let [total (reduce + (map first items))
        offset (int (/ (- 12 total) 2))
        mk-tag (fn [i width] (keyword (if (and (zero? i)
                                              (pos? offset))
                                       (str "div.span" width ".offset" offset)
                                       (str "div.span" width))))
        [c items]
        (reduce (fn [[i lst] [width content]]
                  [(inc i) (cons [(mk-tag i width) content] lst)])
                [0 nil]
                items)]
    (reverse items)))

(format-it [2 "A"]
           [2 "B"]
           [2 "C"]
           [2 "D"])


(map (comp next
           (partial re-find #"(\d+)_(\d+)_(\d+)*")
           str)
     (seq (.listFiles (File. "/home/nick/Desktop/shots-some"))))

(re-find #"(\d+)_(\d+)_(\d+)*" "shot_00000_00005531_00005695")

(next
 (re-find #"(\d+)_(\d+)_(\d+)*" "shot_00000_00005531_00005695"))

(name :A)

(map (fn [line] (next (re-find #"(\d+)\s+(\d+)" line) ))
     (line-seq
      (clojure.java.io/reader
       (File. "/Users/nick/Sites/shotList.txt"))))

(reduce
 (fn [r line] (let [[clip ts] (next (re-find #"(\d+)\s+(\d+)" line))]
               (assoc r (Integer/parseInt clip) (Integer/parseInt ts))))
 { }
 (line-seq
      (clojure.java.io/reader
       (File. "/Users/nick/Sites/shotList.txt"))))

(File.
 (.getParentFile (File. (:shots-file-root m/CONFIG)))
 "shotList.txt")

;;--- Testing configuration web requests.

(client/post
 (lx/field "upload")
 {:form-params {:slug "00010"
                :keyStartPosition "0.5"
                :keyEndPosition "0.5"}})

(client/post
 (lx/field "config")
 {:form-params {:in3d "1"}})

(client/post
 (lx/field "mode")
 {:form-params {:running "1"}})

;; YAML persistence.

(slurp m/STATE-FILE)

(ps/load-state)

(apply hash-map (flatten (ps/load-state)))

(class (first (next (first (ps/load-state)))))

(ps/mark-used {} 42)

(ps/store-state (ps/mark-used {} 42))

(ps/store-state {23 "A" 40 "B"})

(yaml/parse-string (yaml/generate-string {46 "MAYBE" 52 "DEFINITELY"}))

(yaml/generate-string {46 "MAYBE" 52 "DEFINITELY"})


(seq {1 "A" 3 "B"})
