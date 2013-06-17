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
       [".outer-box, .storage" {:background-color "#EEE"
                                :width "100%"
                                :height "200px"}]
       ["button.add-me, button.del-me" {:padding 0}]
       ;; "Remove" buttons for items in the active box:
       [".outer-box .inner-box .add-me" {:display "none"}]
       [".storage .inner-box .del-me" {:display "none"}]
       [:div.tt {:text-align "center"
                 :margin-left "auto"
                 :margin-right "auto"
                 :font-size "16px"
                 :color "#FFFFFF"}]
       [:.box {:float "left"
               :width "120px"
               :margin "5px"}]
       [:.inner-box {:height "100%"
                     :border "2px solid #AAA"
                     :text-align "center"}]
       [:.main-button {:width "100%"}]))

(def other
  (css [:.foo {:height "50px"}]))
