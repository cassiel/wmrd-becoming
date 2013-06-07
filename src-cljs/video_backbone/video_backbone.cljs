(ns video-backbone
  (:use [jayq.core :only [$]])
  (:require [jayq.core :as jq]
            [lib :as lib]))

;; Do this as a proper backbone.js model:

(def VideoSystem
  (.extend
   Backbone.Model
   (lib/JS>
    :initialize
    (fn [] nil)

    :load
    (fn [] (this-as me
                   (.load (.get me "video"))))

    :play
    (fn [] (this-as me
                   (.play (.get me "video"))
                   (.set me (lib/JS> :playing true))))

    :pause
    (fn [] (this-as me
                   (.pause (.get me "video"))
                   (.set me (lib/JS> :playing false))))

    :jump
    (fn [pos] (this-as me
                      (set! (.-currentTime (.get me "video")) pos)))

    :timeUpdate
    (fn [] (this-as me
                   (let [t (.-currentTime (.get me "video"))]
                     (.log js/console t)
                     (this-as me (.set me (lib/JS> :location t))))))

    ;; defaults can also be a function.
    :defaults {:playing false
               :location 0.0})))

;; The view (and, erm, controller):

(def VideoView
  (.extend
   Backbone.View
   (lib/JS> ;; Initialise by rendering the template into the DOM:
    :initialize
    (fn [] (this-as me
                   ;; Re-render on any model change:
                   (.listenTo me
                              (.-model me)
                              "change"
                              (.-render me))
                   ;; Initial render:
                   (.render me)))

    ;; Events generally go into the model.
    :events
    {"click #play" (lib/on-model-and-view #(.play %1))
     "click #load" (lib/on-model-and-view #(.load %1))
     "click #pause" (lib/on-model-and-view #(.pause %1))
     "click #jump10" (lib/on-model-and-view #(.jump %1 10))}

    :render
    (fn [] nil))))

(defn listen-timeupdate [model v]
  (.addEventListener v
                     "timeupdate"
                     (fn [e] (.timeUpdate model))
                     false))

;; This is rather nasty: we need to keep `model` and `view` accessible otherwise
;; the events set up in the view break. Same for the video object pulled from the DOM
;; as well, I expect.

;; It seems a bit daft that we have to attach the video object as a (trackable)
;; parameter rather than a plain Clojure parameter, but the structure setup is
;; obviously a little subtle.

(def STATE
  (let [v (.getElementById js/document "video")
        model (VideoSystem. (lib/JS> :video v))
        view (VideoView. (lib/JS> :el ".container"
                                  :model model))]
    (lib/JS> :video v
             :model model
             :view view)))

(defn go []
  (set! (.-_video js/document) (.-video STATE))
  (listen-timeupdate (.-model STATE)
                     (.-video STATE)))

(jq/document-ready go)
