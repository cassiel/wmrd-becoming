(ns video-backbone
  (:use [jayq.core :only [$]])
  (:require [jayq.core :as jq]))

(defn JS>
  "Inline key-value args to Javascript map."
  [& args]
  (clj->js (apply hash-map args)))

;; Do this as a proper backbone.js model:

(def VideoSystem
  (.extend
   Backbone.Model
   (JS> :initialize
        (fn [] nil)

        :load
        (fn [] (this-as me
                       (.load (.get me "video"))))

        :play
        (fn [] (this-as me
                       (.play (.get me "video"))
                       (.set me (JS> :playing true))))

        :pause
        (fn [] (this-as me
                       (.pause (.get me "video"))
                       (.set me (JS> :playing false))))

        :jump
        (fn [pos] (this-as me
                          (set! (.-currentTime (.get me "video")) pos)))

        :timeUpdate
        (fn [] (this-as me
                       (let [t (.-currentTime (.get me "video"))]
                         (.log js/console t)
                         (this-as me (.set me (JS> :location t))))))

        ;; defaults can also be a function.
        :defaults {:playing false
                   :location 0.0})))

(defn on-model
  "Given `f` (which takes a model), return a function which wraps up the view as `this`."
  [f & args]
  #(this-as view (apply f (.-model view) args)))

;; The view (and, erm, controller):

(def VideoView
  (.extend
   Backbone.View
   (JS> ;; Initialise by rendering the template into the DOM:
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
    {"click #play" (on-model #(.play %))
     "click #load" (on-model #(.load %))
     "click #pause" (on-model #(.pause %))
     "click #jump10" (on-model #(.jump % 10))}

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
        model (VideoSystem. (JS> :video v))
        view (VideoView. (JS> :el ".container"
                              :model model))]
    (JS> :video v
         :model model
         :view view)))

(defn go []
  (set! (.-_video js/document) (.-video STATE))
  (listen-timeupdate (.-model STATE)
                     (.-video STATE)))

(jq/document-ready go)
