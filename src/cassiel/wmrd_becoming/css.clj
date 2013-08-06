(ns cassiel.wmrd-becoming.css
  (:require [hiccup.core :as c]
            (cassiel.wmrd-becoming [manifest :as m]
                                   [style :as st])))

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
    :height (str (int (/ width st/ASPECT)) "px")))

(def standard
  (css [:#vdiv (video-aspect {:margin-left "auto"
                              :margin-right "auto"
                              :background "#333"} st/GALLEY-WIDTH)]
       [:#video (video-aspect { } st/GALLEY-WIDTH)]
       [:.v {:width "100%"}]
       [:.big-button {:background (st/BUTTON-ACTIVE 0)
                      :padding "8px 13px"
                      :width "100%"
                      :color "#AAA"
                      :font-family "'Gill Sans', sans-serif"
                      :font-size "17px"
                      :border-radius "4px"
                      :-webkit-border-radius "4px"
                      :border (st/BUTTON-OUTLINE false)}]
       [:#draggable {:background "#FFF"
                     :opacity 0}]
       ["#draggable h2.firstHalf, #draggable h2.secondHalf" {:display "none"}]
       [:#draggable.ui-draggable-dragging {:background "#FAA"}]
       [:input#search_input {:width "100%"}]))

(def main
  (css [:body {:background "#111"}]
       [:.box (video-aspect {:float "left"
                             :margin "0 5px"} st/THUMB-WIDTH)]
       [:img.thumb (video-aspect {:float "left"
                                  :margin (format "0 %dpx" st/THUMB-MARGIN)} st/THUMB-WIDTH)]
       [:div.inner-box {:position "relative"}]
       [:div.thumb-ident {:position "absolute"
                          :top "0.5em"
                          :right "0.5em"
                         ;; :height "2em"
                          }]
       [:div.thumb-cover (video-aspect {:margin (format "0 %dpx" st/THUMB-MARGIN)
                                        :background "#FF0"
                                        :opacity (st/USED-OPACITY false)}
                                       st/THUMB-WIDTH)]
       ["div.thumb-ident p" {:color (st/SHOT-EXPIRED false)
                             :opacity (st/SELECTED-OPACITY false)
                             :text-shadow "#000 0 0 0.5em"
                             :font-family "'Gill Sans', sans-serif"
                             :font-size "16pt"}]
       [:#viewport {:height (:height (video-aspect { } st/THUMB-WIDTH))
                    :width "100%"
                    :overflow-x "scroll"
                    :overflow-y "hidden"
                    :-webkit-overflow-scrolling "touch"}]
       ["#clips > div.row > div" {:height (:height (video-aspect { } st/THUMB-WIDTH))}]
       [:#storage {:height "0px"
                   ;;:width "100000px"
                   }]
       ["#curtainL, #curtainR" (video-aspect {:background "#111"
                                              :opacity 0.0} st/GALLEY-WIDTH)]
       [:#curtainL {:border-right (st/CURTAIN-TRIM-HIGHLIGHT false)}]
       [:#curtainR {:border-left (st/CURTAIN-TRIM-HIGHLIGHT false)}]
       [:button#upload {:opacity 0.0}]
       [:button#deuse {:opacity 0.0}]
       [:#debug {:display "none"}]))
