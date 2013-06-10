(ns cljs-video-control.css
  (:require [hiccup.core :as c]))

(defn- css-rule [rule]
  (let [sels (reverse (rest (reverse rule)))
        props (last rule)]
    (str (apply str (interpose " " (map name sels)))
         "{" (apply str (map #(str (name (key %)) ": " (val %) ";") props)) "}\n")))

(defn- css
  [& rules]
  (c/html [:style {:type "text/css"}
           (apply str (map css-rule rules))]))

(def standard
  (css [:#video {:width "400px"
                 :height "225px"}]
       [:.vdiv {:width "400px"
                :margin-left "auto"
                :margin-right "auto"}]
       [:.v {:width "100%"}]
       [:#draggable {:background "#FFF"
                     :opacity 0.25}]
       [:input#search_input {:width "100%"}]))

(def dragger
  (css [:.draggable {;; :height "50px"
                     :background "#CCC"}]
       [:td {:height "50px"}]
       ;; When dragging, the clone doesn't have a width:
       [:.ui-draggable-dragging {:width "60px"}]
       [:.droppable {;;:height "50px"
                     :background "#000"
                     :color "#FFF"}]
       [:.drop-active {:color "#44F"}]
       [:.drop-hover {:background "#44A"}]))

(def other
  (css [:.foo {:height "50px"}]))
