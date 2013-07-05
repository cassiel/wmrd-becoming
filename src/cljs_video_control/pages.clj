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
   (lx/standard-head "" css/main)

   [:body
    ;; Template for each of the clip thumbnail containers:
    [:script#item-template {:type "text/template"}
     [:div.inner-box
      [:img.thumb {:src "img/grey.gif"}]
      [:div.thumb-ident [:p "<%= slug %>"]]]]

    [:div.container
     [:div#top-margin]

     [:div.row
      [:div.span12
       [:div#vdiv
        (let [{:keys [thumb video]} (apply lx/assets m/SPLASH-ASSET)
              video-attrs {:preload "none"
                           :loop 1
                           :poster thumb}
              video-attrs (if m/AUTOPLAY
                            (assoc video-attrs :autoplay 1)
                            video-attrs)]
          [:video#video video-attrs
           [:source#mp4 {:src video
                         :type "video/mp4"}]])

        [:div#curtainL]
        [:div#draggable
         [:h2.firstHalf "START"]
         [:h2.secondHalf "END"]]
        [:div#curtainR]]]]

     [:div {:style "height: 20px"}]

     [:div#clips
      (lx/format-row 12
                     [12 [:div#viewport [:div#storage]]])]

     [:div {:style "height: 10px"}]

     (lx/format-row 12
                    [4 [:button#upload.v "UPLOAD"]])

     [:div {:style "height: 10px"}]

     [:div#debug
      (lx/format-row 12
                     [6 [:table.table.table-bordered.table-condensed
                         [:thead [:tr
                                  [:th "Duration"]
                                  [:th "Location"]
                                  [:th "Status"]
                                  [:th "Dragging"]]]
                         [:tbody [:tr
                                  [:td#duration "---"]
                                  [:td#location "---"]
                                  [:td#status "---"]
                                  [:td#dragging "---"]]]]])]]

    (hp/include-js "js/main.js")]))
