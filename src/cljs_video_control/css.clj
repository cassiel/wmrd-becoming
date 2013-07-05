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
                              :margin-right "auto"
                              :background "#333"} m/GALLEY-WIDTH)]
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
       [:img.thumb (video-aspect {:float "left"
                                  :margin (format "0 %dpx" m/THUMB-MARGIN)} m/THUMB-WIDTH)]
       [:div.inner-box {:position "relative"}]
       [:div.thumb-ident {:position "absolute"
                          :top "0.5em"
                          :right "0.5em"
                         ;; :height "2em"
                          }]
       ["div.thumb-ident p" {:color "#FFF"
                             :opacity m/SLUG-OPACITY
                             :text-shadow "#000 0 0 0.5em"
                             :font-family "'Gill Sans', sans-serif"
                             :font-size "16pt"}]
       [:#viewport {:height (:height (video-aspect { } m/THUMB-WIDTH))
                    :width "100%"
                    :overflow "scroll"
                    :-webkit-overflow-scrolling "touch"}]
       ["#clips > div.row > div" {:height (:height (video-aspect { } m/THUMB-WIDTH))}]
       [:#storage {:height "0px"
                   ;;:width "100000px"
                   }]
       ["#curtainL, #curtainR" (video-aspect {:background "#111"
                                              :opacity 0.0} m/GALLEY-WIDTH)]
       [:#curtainL {:border-right "1px solid #888"}]
       [:#curtainR {:border-left "1px solid #888"}]
       [:button#upload {:opacity 0.0
                        :background "-webkit-linear-gradient(top, #333 0%, #111 100%)"
                        :padding "8px 13px"
                        :color "#AAA"
                        :font-family "'Gill Sans', sans-serif"
                        :font-size "17px"
                        :border-radius "4px"
                        :-webkit-border-radius "4px"
                        :border (m/BUTTON-OUTLINE false)}]
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
