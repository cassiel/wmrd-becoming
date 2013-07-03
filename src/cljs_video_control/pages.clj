(ns cljs-video-control.pages
  (:require [hiccup.page :as hp]
            (cljs-video-control [manifest :as m]
                                [css :as css]
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
   (lx/standard-head "Video Control" css/main)

   [:body
    ;; Template for each of the clip thumbnail containers:
    [:script#item-template {:type "text/template"}
     [:div.inner-box
      [:img.thumb]]]

    [:div.container
     [:h2 "Video Control"]

     [:div.row
      [:div.span12
       [:div.vdiv
        [:video#video
         {:preload "none"
          :poster (lx/asset (str m/SHOTS-URL-ROOT "/shot_00002_00005871_00005943/image_half.jpg"))}
         [:source#mp4
          {:src (lx/asset (str m/SHOTS-URL-ROOT "/shot_00002_00005871_00005943/imageList_all.mp4"))
           :type "video/mp4"}]]]]]

     [:div {:style "height: 20px"}]

     [:div#clips
      (lx/format-row 12
                     [4 [:button#fetcher.v "FETCH"]])

      (lx/format-row 12
                     [12 [:div#storage]])]

     [:div {:style "height: 10px"}]

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
