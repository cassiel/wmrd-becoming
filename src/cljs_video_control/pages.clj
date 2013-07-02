(ns cljs-video-control.pages
  (:require [hiccup.page :as hp]
            (cljs-video-control [css :as css]
                                [layout :as lx])))

(defn render-index []
  (hp/html5
   (lx/standard-head "Index" css/other)

   [:body
    [:div.container
     [:h2 "Pages"]

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
            "sortable items by dragging"]]

      [:li [:a {:href "main"}
            "main"]]]]]))

(defn render-main []
  (hp/html5
   (lx/standard-head "Video Control")

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

    (hp/include-js "js/main.js")]))
