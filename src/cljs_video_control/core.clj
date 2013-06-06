(ns cljs-video-control.core
  (:use compojure.core)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [hiccup.page :as hp]
            [hiccup.util :as hu]))

(defn standard-head [title]
  [:head
    [:title title]
    [:meta  {:name "viewport"
             :content "width=device-width, initial-scale=1.0"}]
    (hp/include-css "http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css"
                    "css/bootstrap.min.css"
                    "css/style.css")
    (hp/include-js "http://code.jquery.com/jquery-1.9.1.js"
                   "http://code.jquery.com/ui/1.10.3/jquery-ui.js"
                   "http://underscorejs.org/underscore.js"
                   "http://backbonejs.org/backbone.js")])

(defn render-index []
  (hp/html5
   (standard-head "Index")

   [:body
    [:div.container
     [:h2 "Demo Pages"]

     [:ul
      [:li [:a {:href "demo-backbone"} "demo via backbone.js"]]
      [:li [:a {:href "demo-yui"} "demo via YUI"] " (deprecated)"]
      [:li [:a {:href "video-backbone"} "video control tests via backbone.js"]]
      [:li [:a {:href "video-framing"} "video frame selection tests"]]
      [:li [:a {:href "dragger"} "drag-and-drop example"]]]]]))

(defn render-demo-backbone []
  (hp/html5
   [:head
    (hp/include-js
     "http://code.jquery.com/jquery-1.8.2.min.js"
     "http://underscorejs.org/underscore.js"
     "http://backbonejs.org/backbone.js")]

   [:body
    [:h2 "backbone.js"]

    [:p [:button#clickable-event "Click to trigger an alert from basic Backbone event"]]
    [:p [:button#clickable-color "Click to change background color"]]

    (hp/include-js "js/demo_backbone.js")]))

(defn render-demo-yui []
  (hp/html5
   [:head
    (hp/include-js "http://yui.yahooapis.com/3.10.1/build/yui/yui-min.js")]

   [:body
    ;; Template for YUI templater:
    [:script {:type "text/template"}
     [:p#my-paraxxx "originalxxx"]]

    [:h2 "YUI"]

    [:div#myapp-app [:p#my-para "initial"]]

    [:p [:button#clickable-event "Click to trigger an alert from basic YUI event"]]

    (hp/include-js "js/demo_yui.js")]))

(defn render-video-backbone []
  (hp/html5
   [:head
    (hp/include-js
       "http://code.jquery.com/jquery-1.8.2.min.js"
       "http://underscorejs.org/underscore.js"
       "http://backbonejs.org/backbone.js")
    (hp/include-css "css/style.css")]
   [:body
    ;; Template for underscore.js:
    [:script#search_template {:type "text/template"}
     [:label "<%= search_label %>"]
     [:input#search_input {:type "text"}]
     [:input#search_button {:type "button" :value "Search"}]]

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
     [:button {:onclick "document._video.playbackRate-=0.1"} "playbackRate-=0.1"]

     [:button#bash "BASH"]]

    [:div [:p#status "init"]]

    [:div#search_container]

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

    (hp/include-js "js/video_framing.js"
                   "js/bootstrap.min.js")]))

(defn render-dragger
  "backbone/jQuery drag-and-drop example."
  []
  (hp/html5
   [:head
    (hp/include-css "http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css"
                    "css/dragger.css")
    (hp/include-js "http://code.jquery.com/jquery-1.9.1.js"
                   "http://code.jquery.com/ui/1.10.3/jquery-ui.js"
                   "http://underscorejs.org/underscore.js"
                   "http://backbonejs.org/backbone.js")]

   [:body
    [:div#main
     [:div#dr1.droppable "Drop 1"]
     [:div#dr2.droppable "Drop 2"]
     [:div#AAA.draggable "Drag me"]

     [:p.status "container paragraph"]]
    (hp/include-js "js/dragger.js")]))

(defroutes my-routes
  (GET "/" [] (render-index))
  (GET "/demo-backbone" [] (render-demo-backbone))
  (GET "/demo-yui" [] (render-demo-yui))
  (GET "/video-backbone" [] (render-video-backbone))
  (GET "/video-framing" [] (render-video-framing))
  (GET "/dragger" [] (render-dragger))
  (route/resources "/" {:root "public"}))

(def app
  (handler/site my-routes))
