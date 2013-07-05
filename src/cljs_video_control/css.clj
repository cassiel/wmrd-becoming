(ns cljs-video-control.css
  (:require [hiccup.core :as c]
            (cljs-video-control [manifest :as m])))

(defn- css-rule [rule]
  (let [sels (reverse (rest (reverse rule)))
        props (last rule)]
    (str (apply str (interpose " " (map name sels)))
         "{" (apply str (map #(str (name (key %)) ": " (val %) ";") props)) "}\n")))

(defn- css
  [& rules]
  (c/html [:style {:type "text/css"}
           (apply str (map css-rule rules))]))

(defn- video-aspect [props width]
  (assoc props
    :width (str width "px")
    :height (str (int (/ width m/ASPECT)) "px")))

(def standard
  (css [:#vdiv (video-aspect {:margin-left "auto"
                              :margin-right "auto"} m/GALLEY-WIDTH)]
       [:#video (video-aspect { } m/GALLEY-WIDTH)]
       [:.v {:width "100%"}]
       [:#draggable {:background "#FFF"
                     :opacity 0}]
       ["#draggable h2.firstHalf, #draggable h2.secondHalf" {:display "none"}]
       [:#draggable.ui-draggable-dragging {:background "#FAA"}]
       [:input#search_input {:width "100%"}]))

(def main
  (css [:body {:background "#111"}]
       [:.box (video-aspect {:float "left"
                             :margin "0 5px"} m/THUMB-WIDTH)]
       [:#viewport {:height "100px"
                    :width "100%"
                    :overflow "scroll"
                    :-webkit-overflow-scrolling "touch"}]
       ["#clips div.row div" {:height (:height (video-aspect { } m/THUMB-WIDTH))
                              ;;:width "1230px"
                              }]
       [:#storage {:height "0px"
                   :width "100000px"}]
       ["#curtainL, #curtainR" (video-aspect {:background "#111"
                                              :opacity 0.5} m/GALLEY-WIDTH)]
       [:#curtainL {:border-right "1px solid #888"}]
       [:#curtainR {:border-left "1px solid #888"}]
       [:#debug {:display "none"}]))

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
