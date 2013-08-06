(ns cassiel.wmrd-becoming.pages
  (:require [hiccup.page :as hp]
            (cassiel.wmrd-becoming [manifest :as m]
                                   [css :as css]
                                   [layout :as lx])))

(defn render-main []
  (hp/html5
   (lx/standard-head "" css/main)

   [:body
    ;; Template for each of the clip thumbnail containers:
    [:script#item-template {:type "text/template"}
     [:div.inner-box
      [:img.thumb {:src "img/grey.gif"}]
      [:div.thumb-ident [:p "<%= slug %>" [:br] "<%= timestamp %>"]]
      ;; Overlay translucent panel to mark shots which have been used. (Initially invisible.)
      [:div.thumb-cover]]]

    [:div.container
     [:div#top-margin]

     [:div.row
      [:div.span12
       [:div#vdiv
        (let [video-attrs {:preload "none"
                           :loop 1
                           ;;:poster "img/grey.gif"
                           }
              video-attrs (if (:autoplay m/CONFIG)
                            (assoc video-attrs :autoplay 1)
                            video-attrs)]
          [:video#video video-attrs
           [:source#mp4 {;;:src video
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
                    [4 [:button#upload.big-button "UPLOAD"]]
                    [2 [:button#do-2d.big-button "2D"]]
                    [2 [:button#pause-it.big-button "PAUSE"]]
                    [2 [:button#deuse.big-button "un-use"]])

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
