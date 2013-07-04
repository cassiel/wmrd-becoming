(ns cljs-video-control.layout
  (:require [hiccup.page :as hp]
            (cljs-video-control [manifest :as m]
                                [css :as css])))

(defn url
  "Build a URL, using a `src` from `manifest`."
  [src path]
  (format "http://%s%s/%s"
          (:host src)
          (if-let [p (:port src)] (str ":" p) "")
          path))

(def server (partial url m/SERVER))
(def asset (partial url m/ASSETS))
(def field (partial url m/FIELD))

(defn assets [shot frame-lo frame-hi]
  (let [dir-name (format "%s/shot_%s_%s_%s" m/SHOTS-URL-ROOT shot frame-lo frame-hi)]
    {:thumb (asset (format "%s/image_half.jpg" dir-name))
     :video (asset (format "%s/imageList_all.mp4" dir-name))}))

(defn standard-head [title & css-stems]
  (as-> [:head
         [:title title]
         [:meta {:name "viewport"
                 :content "width=device-width, initial-scale=1.0"}]
         (hp/include-css "http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css"
                         "css/bootstrap.min.css")]
        H

        (cons H (cons css/standard css-stems))

        (conj H (hp/include-js "http://code.jquery.com/jquery-1.9.1.js"
                               "js/jquery.lazyload.min.js"
                               "http://code.jquery.com/ui/1.10.3/jquery-ui.js"
                               "http://underscorejs.org/underscore.js"
                               "http://backbonejs.org/backbone.js"))))

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
