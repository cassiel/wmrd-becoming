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
       [:.drop-active {:background "#22A"}]
       [:.drop-hover {:background "#88A"}]))

(def sortable
  (css [:.outer-box {:float "left"}]
       [:.storage {:float "left"}]
       [".outer-box, .storage" {:background "#EEE"
                                :width "100%"
                                :height "148px"
                                }]
       [:.inner-box {:background "#CCC"}]
       [:.lower-box {:background "#BBB"}]
       [".inner-box, .lower-box" {:float "left"
                                  :height "60px"
                                  :width "60px"
                                  :margin "5px"
                                  :border "2px solid #AAA"
                                  :text-align "center"}]))

(def other
  (css [:.foo {:height "50px"}]))
