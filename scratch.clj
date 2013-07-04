(ns user
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

(next
 (re-find #"(\d+)_(\d+)_(\d+)*" "shot_00000_00005531_00005695"))

(name :A)
