(ns video-hybrid
  "Video control with MVC framework in backbone.js and an enclosing wrapper in YUI
   (which we might need for the drag-and-drop plugins)."
  (:use [jayq.core :only [$]])
  (:require [jayq.core :as jq]))

(defn JS>
  "Inline key-value args to Javascript map."
  [& args]
  (clj->js (apply hash-map args)))

;; Do this as a proper backbone.js model:

(defn main [Y]
  (let [VideoSystem
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
                         :location 0.0}))

        video-system (VideoSystem.)

        ;; The view (and, erm, controller):

        VideoView
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
                           (.html template)))))))

        ;; Build a view: it takes a model instance as argument.

        video-view (VideoView. (JS> :el ($ :#search_container)
                                    :model video-system))

        ;; Pull out the video element from the document:
        v (.getElementById js/document "video")]

    (.addEventListener v
                       "timeupdate"
                       (fn [e] (.timeUpdate video-system (.-currentTime v)))
                       false)

    (set! (.-_video js/document) v)))

(.use (js/YUI) "node" main)
