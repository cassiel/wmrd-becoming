(ns cljs-video-control.core
  (:use compojure.core)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [hiccup.page :as hp])
  (:require (cljs-video-control [css :as css]
                                [layout :as lx])))

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
                               "http://code.jquery.com/ui/1.10.3/jquery-ui.js"
                               "http://underscorejs.org/underscore.js"
                               "http://backbonejs.org/backbone.js"))))

(defn render-index []
  (hp/html5
   (standard-head "Index" css/other)

   [:body
    [:div.container
     [:h2 "Demo Pages"]

     [:ul
      [:li [:a {:href "demo-backbone"}
            "demo: simple examples via backbone.js"]]

      [:li [:a {:href "video-backbone"}
            "video control tests"]]

      [:li [:a {:href "video-framing"}
            "video frame selection tests"]]

      [:li [:a {:href "range-slider"}
            "a simple range slider"]]

      [:li [:a {:href "search-template"}
            "a simple search and templating test"]]

      [:li [:a {:href "dragger"}
            "a simple drag-and-drop example"]]

      [:li [:a {:href "sortable"}
            "sortable items by dragging"]]]]]))

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
     [:div.row
      [:div.span1.offset3 [:label "<%= search_label %>"]]
      [:div.span4 [:input#search_input {:type "text"}]]
      [:div.span1 [:input#search_button {:type "button" :value "Search"}]]]]

    [:div.container
     [:h2 "Search Example"]
     [:div#search_container]]

    (hp/include-js "js/search_template.js")]))

(defn render-video-backbone []
  (hp/html5
   (standard-head "Video Control")

   [:body
    [:div.container
     [:h2 "Video Control"]

     [:div.row
      [:div.span12
       [:div.vdiv
        [:video#video
         {:controls 1
          :preload "none"
          :poster "http://media.w3.org/2010/05/sintel/poster.png"}
         [:source#mp4
          {:src "http://media.w3.org/2010/05/sintel/trailer.mp4"
           :type "video/mp4"}]]]]]

     #_ [:div#buttons
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

     [:div {:style "height: 20px"}]

     (lx/format-row 12
                    ;; slight hack: the button text is an index to the videos in the JS.
                    [2 [:button#select.v "sintel"]]
                    [2 [:button#select.v "bunny"]]
                    [2 [:button#select.v "movie"]]
                    [2 [:button#select.v "video"]])

     [:div {:style "height: 10px"}]

     (lx/format-row 12
                    [2 [:button#play.v "PLAY"]]
                    [2 [:button#pause.v "PAUSE"]]
                    [2 [:button#jump10.v "JUMP10"]]
                    [2 [:div]])

     [:div {:style "height: 10px"}]

     (lx/format-row 12
                    [6 [:table.table.table-bordered.table-condensed
                        [:thead [:tr [:th "Location"] [:th "Status"]]]
                        [:tbody [:tr [:td#location "---"] [:td#status "---"]]]]])]

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
   (standard-head "Drag and Drop" css/dragger)

   [:body
    [:div.container
     [:h2 "Drag and Drop"]

     [:div#main-enclosure
      [:div.row
       [:div.span10.offset1
        [:table.table.table-bordered.table-condensed
         ;; Make the TDs selectively droppable in the JS.
         [:tbody [:tr (for [i (range 10)] [:td.droppable-skel i])]]]]]

      [:div.row
       (for [i (range 6)] [(if (zero? i)
                             :div.span1.offset3
                             :div.span1)
                           ;; Make the entire tables draggable:
                           [:table.table.table-bordered.table-condensed.draggable
                            [:tbody [:tr [:td "X"]]]]])]]]

    (hp/include-js "js/dragger.js")]))

(defn render-sortable
  "backbone/jQuery sortable example."
  []
  (hp/html5
   (standard-head "Sortable" css/sortable)

   [:body
    [:script#item-template {:type "text/template"}
     [:div.inner-box "<%= title %>"]]

    [:div.container
     [:h2 "Sortable"]

     [:div#main-enclosure
      (lx/format-row 12
                     [6 [:div.outer-box.connected-sortable
                         #_ (for [i (range 3)] [:div.inner-box (inc i)])]])

      [:div {:style "height: 20px"}]

      (lx/format-row 12
                     [8 [:div.storage.connected-sortable
                         (for [i (range 12)] [:div.lower-box (+ 101 i)])]])]]

    (hp/include-js "js/sortable.js")]))

(defroutes my-routes
  (GET "/" [] (render-index))
  (GET "/demo-backbone" [] (render-demo-backbone))
  (GET "/video-backbone" [] (render-video-backbone))
  (GET "/video-framing" [] (render-video-framing))
  (GET "/range-slider" [] (render-range-slider))
  (GET "/search-template" [] (search-template))
  (GET "/dragger" [] (render-dragger))
  (GET "/sortable" [] (render-sortable))
  (route/resources "/" {:root "public"}))

(def app
  (handler/site my-routes))
