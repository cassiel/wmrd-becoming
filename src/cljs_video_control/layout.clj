(ns cljs-video-control.layout)

(defn format-row
  "Format a set of divs into a row. The args pair span with data.
   TODO: add additional id/class fragments?"
  [& items]
  (let [total (reduce + (map first items))
        offset (int (/ (- 12 total) 2))
        mk-tag (fn [i width] (keyword (if (zero? i)
                                       (str "div.span" width ".offset" offset)
                                       (str "div.span" width))))
        [c items]
        (reduce (fn [[i lst] [width content]]
                  [(inc i) (cons [(mk-tag i width) content] lst)])
                [0 nil]
                items)]

    [:div.row (reverse items)]))
