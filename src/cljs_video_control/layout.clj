(ns cljs-video-control.layout)

(defn format-row
  "Format a set of divs centred into a row. The args are each [span, data].
   TODO: add additional id/class fragments?"
  [row-width & items]
  (let [total (reduce + (map first items))
        offset (int (/ (- row-width total) 2))
        mk-tag (fn [i width] (keyword (if (zero? i)
                                       (str "div.span" width ".offset" offset)
                                       (str "div.span" width))))
        [c items] (reduce (fn [[i lst] [width content]]
                            [(inc i) (cons [(mk-tag i width) content] lst)])
                          [0 nil]
                          items)
        remaining (- row-width total offset)
        items (if (pos? remaining)
                (cons [(keyword (str "div.span" remaining))] items)
                items)]

    [:div.row (reverse items)]))
