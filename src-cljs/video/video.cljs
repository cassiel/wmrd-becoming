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

(defn JS>
  "Inline key-value args to Javascript map."
  [& args]
  (clj->js (apply hash-map args)))

;; Do this as a proper backbone.js model:

(def VideoSystem
  (.extend
   Backbone.Model
   (JS> :initialize
        (fn [] #_ (this-as me
                       (.on me "change:playing"
                            (fn [model playing]
                              (jq/inner ($ :#status)
                                        (str "playing: " playing))))
                       )
          #_ (this-as me (.on me "change:timeUpdate"
                              (fn [model t]
                                (.log js/console t)))))

        :play
        (fn [] (this-as me (.set me (JS> :playing true))))

        :stop
        (fn [] (this-as me (.set me (JS> :playing false))))

        :timeUpdate
        (fn [t] (this-as me (.set me (JS> :location t))))

        ;; defaults can also be a function.
        :defaults {:playing false
                   :location 0.0})))

(def video-system (VideoSystem.))

;; The view (and, erm, controller):

(def VideoView
  (.extend
   Backbone.View
   (JS> ;; Initialise by rendering the template into the DOM:
    :initialize
    (fn [] (this-as me
                   ;; Re-render on any model change.
                   (.listenTo me
                              (.-model me)
                              "change"
                              (.-render me))
                   (.render me)))

    :events {"click input[type=button]" :doSearch}

    :doSearch
    (fn [] (js/alert (str "Search for " (.val ($ :#search_input)))))

    :render
    ;; Compile the template using underscore
    (fn []
      (this-as me
               (let [location (.get (.-model me) "location")
                     template (.template js/_
                                         (.html ($ :#search_template))
                                         (JS> :search_label location))]
                 (-> (.-$el me)
                     (.html template))))))))

;; Build a view: it takes a model instance as argument.

(def video-view (VideoView. (JS> :el ($ :#search_container)
                                 :model video-system)))

#_ (.on video-system "change:playing"
     (fn [model playing]
       (jq/inner ($ :#status)
                 (str "playing: " playing))))

#_ (.on video-system "change:timeUpdate"
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
