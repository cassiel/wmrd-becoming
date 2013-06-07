(ns cljs-video-control.core
  (:use compojure.core)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [hiccup.core :as c]
            [hiccup.page :as hp]
            [hiccup.util :as hu]))


(defn css-rule [rule]
  (let [sels (reverse (rest (reverse rule)))
        props (last rule)]
    (str (apply str (interpose " " (map name sels)))
         "{" (apply str (map #(str (name (key %)) ": " (val %) ";") props)) "}\n")))

(defn css
  [& rules]
  (c/html [:style {:type "text/css"}
           (apply str (map css-rule rules))]))

(def CSS-ENTRIES
  {:standard [[:#video {:height "240px"
                        :top "50px"
                        :left "50px"}]
              [:#draggable {:background "#FFF"
                            ;;:opacity 0.9
                            }]]

   :dragger [[:.draggable {;; :height "50px"
                           :background "#CCC"}]
             [:td {:height "50px"
                      }]
             ;; When dragging, the clone doesn't have a width:
             [:.ui-draggable-dragging {:width "60px"}]
             [:.droppable {;;:height "50px"
                           :background "#999"
                           :color "#FFF"}]
             [:.drop-active {:color "#44F"}]
             [:.drop-hover {:background "#44A"}]]

   :other [[:.foo {:height "50px"}]]})

(defn standard-head [title & css-stems]
  (as-> [:head
         [:title title]
         [:meta {:name "viewport"
                 :content "width=device-width, initial-scale=1.0"}]
         (hp/include-css "http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css"
                         "css/bootstrap.min.css")]
        H

        (vec (reduce (fn [l x] (conj l (apply css (get CSS-ENTRIES x))))
                     H
                     (cons :standard css-stems)))

        (conj H (hp/include-js "http://code.jquery.com/jquery-1.9.1.js"
                               "http://code.jquery.com/ui/1.10.3/jquery-ui.js"
                               "http://underscorejs.org/underscore.js"
                               "http://backbonejs.org/backbone.js"))))

(defn render-index []
  (hp/html5
   (standard-head "Index" :other)

   [:body
    [:div.container
     [:h2 "Demo Pages"]

     [:ul
      [:li [:a {:href "demo-backbone"} "demo via backbone.js"]]
      [:li [:a {:href "video-backbone"} "video control tests via backbone.js"]]
      [:li [:a {:href "video-framing"} "video frame selection tests"]]
      [:li [:a {:href "range-slider"} "simple range slider"]]
      [:li [:a {:href "search-template"} "search-template"]]
      [:li [:a {:href "dragger"} "drag-and-drop example"]]]]]))

(defn render-demo-backbone []
  (hp/html5
   (standard-head "Backbone Demo")

   [:body
    [:div.container
     [:h2 "backbone.js"]

     [:p [:button#clickable-event "Click to trigger an alert from basic Backbone event"]]
     [:p [:button#clickable-color "Click to change background color"]]]

    (hp/include-js "js/demo_backbone.js")]))

(defn search-template []
  (hp/html5
   (standard-head "Search Template")

   [:body
    ;; Template for underscore.js:
    [:script#search_template {:type "text/template"}
     [:label "<%= search_label %>"]
     [:input#search_input {:type "text"}]
     [:input#search_button {:type "button" :value "Search"}]]

    [:div.container
     [:div#search_container]]

    (hp/include-js "js/search_template.js")]))

(defn render-video-backbone []
  (hp/html5
   (standard-head "Video Control")

   [:body
    [:div.container
     [:h2 "Video Control"]
     [:div
      [:video#video
       {:controls 1
        :preload "none"
        :poster "http://media.w3.org/2010/05/sintel/poster.png"}
       [:source#mp4
        {:src "http://media.w3.org/2010/05/sintel/trailer.mp4"
         :type "video/mp4"}]]]

     [:div#buttons
      [:button {:onclick "document._video.load()"} "load()"]
      [:button {:onclick "document._video.play()"} "play()"]
      [:button {:onclick "document._video.pause()"} "pause()"]
      [:button {:onclick "document._video.currentTime+=10"} "currentTime+=10"]
      [:button {:onclick "document._video.currentTime-=10"} "currentTime-=10"]
      [:button {:onclick "document._video.currentTime=50"} "currentTime=50"]
      [:button {:onclick "document._video.playbackRate++"} "playbackRate++"]
      [:button {:onclick "document._video.playbackRate--"} "playbackRate--"]
      [:button {:onclick "document._video.playbackRate+=0.1"} "playbackRate+=0.1"]
      [:button {:onclick "document._video.playbackRate-=0.1"} "playbackRate-=0.1"]]

     [:div
      [:button#load "LOAD"]
      [:button#play "PLAY"]
      [:button#pause "PAUSE"]
      [:button#jump10 "JUMP10"]]]

    (hp/include-js "js/video_backbone.js")]))

(defn render-video-framing []
  (hp/html5
   (standard-head "Video Framing")

   [:body
    [:div.container
     [:h2 "Draggable Frame"]
     [:video#video
      {:controls 1
       :preload "none"
       :poster "http://media.w3.org/2010/05/sintel/poster.png"}
      [:source#mp4
       {:src "http://media.w3.org/2010/05/sintel/trailer.mp4"
        :type "video/mp4"}]]

     [:div#draggable]]

    (hp/include-js "js/video_framing.js")]))

(defn render-range-slider []
  (hp/html5
   (standard-head "Range Slider")

   [:body
    [:div.container
     [:h2 "Range Slider"]

     [:div#range_slider]

     [:div {:style "height: 20px"}]

     [:div.row
      [:div.span2.offset5 [:table.table.table-bordered.table-condensed
                           [:thead [:tr [:th "Min"] [:th "Max"]]]
                           [:tbody [:tr [:td#min "min"] [:td#max "max"]]]]]]]

    (hp/include-js "js/range_slider.js")]))

(defn render-dragger
  "backbone/jQuery drag-and-drop example."
  []
  (hp/html5
   (standard-head "Drag and Drop" :dragger)

   [:body
    [:div.container
     [:h2 "Drag and Drop"]

     [:div#main-enclosure
      [:div.row
       [:div.span10.offset1
        [:table.table.table-bordered.table-condensed
         ;; Make the TDs droppable:
         [:tbody [:tr (for [i (range 10)] [:td.droppable i])]]]]]

      [:div.row
       (for [i (range 6)] [(if (zero? i)
                             :div.span1.offset3
                             :div.span1)
                           ;; Make the entire tables draggable:
                           [:table.table.table-bordered.table-condensed.draggable
                            [:tbody [:tr [:td "X"]]]]])]]]

    (hp/include-js "js/dragger.js")]))

(defroutes my-routes
  (GET "/" [] (render-index))
  (GET "/demo-backbone" [] (render-demo-backbone))
  (GET "/video-backbone" [] (render-video-backbone))
  (GET "/video-framing" [] (render-video-framing))
  (GET "/range-slider" [] (render-range-slider))
  (GET "/search-template" [] (search-template))
  (GET "/dragger" [] (render-dragger))
  (route/resources "/" {:root "public"}))

(def app
  (handler/site my-routes))
