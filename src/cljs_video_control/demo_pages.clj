(ns cljs-video-control.demo-pages
  (:require [hiccup.page :as hp]
            (cljs-video-control [css :as css]
                                [layout :as lx])))

(defn render-demo-backbone []
  (hp/html5
   (lx/standard-head "Backbone Demo")

   [:body
    [:div.container
     [:h2 "backbone.js"]

     [:p [:button#clickable-event "Click to trigger an alert from basic Backbone event"]]
     [:p [:button#clickable-color "Click to change background color"]]]

    (hp/include-js "js/demo_backbone.js")]))

(defn search-template []
  (hp/html5
   (lx/standard-head "Search Template")

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

    (hp/include-js "js/video_backbone.js")]))

(defn render-video-framing []
  (hp/html5
   (lx/standard-head "Video Framing")

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
   (lx/standard-head "Range Slider")

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
   (lx/standard-head "Drag and Drop" css/dragger)

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
   (lx/standard-head "Sortable" css/sortable)

   [:body
    [:script#item-template-OLD {:type "text/template"}
     [:div.inner-box
      [:div.tt "<%= title %>"]
      [:div.bb
       [:button.add-me [:i.icon-plus-sign]]
       [:button.del-me [:i.icon-trash]]]]]

    [:script#item-template {:type "text/template"}
     [:div.inner-box
      [:img.thumb]            ; Can't use template on attributes?
      [:div.tt "<%= title %>"
       [:button.add-me [:i.icon-plus-sign]]
       [:button.del-me [:i.icon-trash]]]]]

    [:div.container
     [:h2 "Sortable"]

     [:div#main-enclosure
      (lx/format-row 12
                     [6 [:div.outer-box]])

      [:div {:style "height: 20px"}]

      [:div#store
       (lx/format-row 12
                      [12 [:div.storage]])

       [:div {:style "height: 20px"}]

       (lx/format-row 12
                      [2 [:button#populate.main-button "Populate"]]
                      [2 [:button#fetcher.main-button "Fetch"]])]]]

    (hp/include-js "js/sortable.js")]))
