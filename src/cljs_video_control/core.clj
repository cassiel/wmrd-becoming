(ns cljs-video-control.core
  (:use compojure.core)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [hiccup.core :as h]
            [hiccup.page :as hp]))

(defn render-body []
  (hp/html5
    [:head]
    [:body
     [:button#clickable-event "Click to trigger an alert from basic Backbone event"]
     [:button#clickable-color "Click to change background color"]
     (hp/include-js
       "http://code.jquery.com/jquery-1.8.2.min.js"
       "http://underscorejs.org/underscore.js"
       "http://backbonejs.org/backbone.js"
       "js/cljs.js")
     ]))


(defroutes my-routes
  (GET "/" [] (render-body))
  (route/resources "/" {:root "public"}))

(def app
  (handler/site my-routes))
