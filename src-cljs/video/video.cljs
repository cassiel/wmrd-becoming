(ns video
  (:use [jayq.core :only [$]])
  (:require [jayq.core :as jq]))

; ALERT ON CLICK
; Rewrite of http://backbonejs.org/#Events
(def o {})

(.extend js/_ o Backbone.Events)

(.on o "alert"
     (fn [msg] (js/alert msg)))

(.on o "play"
     (fn [] (.play (.-_video js/document))))

#_ (jq/bind ($ "#bash")
         :click
         (fn [e] (.trigger o "play")))

;; Do this as a proper backbone.js model:

(def VideoSystem
  (.extend
   Backbone.Model
   (clj->js {:play
             (fn [] (this-as me (.set me (clj->js {:playing true}))))

             :stop
             (fn [] (this-as me (.set me (clj->js {:playing false}))))

             :timeUpdate
             (fn [t] (this-as me (.set me (js-obj "timeUpdate" t))))})))

(def video-system (VideoSystem.))

(.on video-system "change:playing"
     (fn [model playing]
       (jq/inner ($ :#status)
                 (str "playing: " playing))))

(.on video-system "change:timeUpdate"
     (fn [model t]
       (.log js/console t)))

(jq/bind ($ "#bash")
         :click
         (fn [e] (.play video-system)))

;; Set listeners on the video object, triggering model calls into backbone:

(defn listen-timeupdate [v]
  (.addEventListener v
                     "timeupdate"
                     (fn [e] (.timeUpdate video-system (.-currentTime v)))
                     false))

(jq/document-ready (fn []
                     (let [v (.getElementById js/document "video")]
                       (listen-timeupdate v)
                       (set! (.-_video js/document) v))))
