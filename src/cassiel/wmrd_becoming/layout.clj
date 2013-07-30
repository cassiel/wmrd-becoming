(ns cassiel.wmrd-becoming.layout
  (:require [hiccup.page :as hp]
            (cassiel.wmrd-becoming [manifest :as m]
                                   [css :as css])))

(defn url
  "Build a URL, using a `src` from `manifest`."
  [src path]
  (format "http://%s%s/%s"
          (:host src)
          (if-let [p (:port src)] (str ":" p) "")
          path))

(def server (partial url (:server m/CONFIG)))
(def asset (partial url (:assets m/CONFIG)))
(def field (partial url (:field m/CONFIG)))

(defn assets [shot frame-lo frame-hi]
  (let [dir-name (format "%s/shot_%s_%s_%s" (:shots-url-root m/CONFIG) shot frame-lo frame-hi)]
    {:thumb (asset (format "%s/image_half.jpg" dir-name))
     :video (asset (format "%s/imageList_all.mp4" dir-name))}))

(defn standard-head [title & css-stems]
  (as-> [:head
         [:title title]
         [:meta {:name "viewport"
                 :content "width=device-width, initial-scale=1.0"}]

         (hp/include-css "css/jquery-ui.css"
                         "css/bootstrap.min.css"
                         "css/bootstrap-responsive.min.css")]
        H

        (cons H (cons css/standard css-stems))

        (conj H (hp/include-js "js/jquery-1.9.1.js"
                               "js/jquery.lazyload.min.js"
                               "js/jquery-ui.js"
                               "js/underscore.js"
                               "js/backbone.js"))))

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
