(ns video-backbone
  (:use [jayq.core :only [$]])
  (:require [jayq.core :as jq]
            [lib :as lib]))

(def VIDEOS {:sintel ["http://media.w3.org/2010/05/sintel/poster.png"
                      "http://media.w3.org/2010/05/sintel/trailer.mp4"]
             :bunny ["http://media.w3.org/2010/05/bunny/poster.png"
                     "http://media.w3.org/2010/05/bunny/trailer.mp4"]
             :movie ["http://media.w3.org/2010/05/bunny/poster.png"
                     "http://media.w3.org/2010/05/bunny/movie.mp4"]
             :video ["http://media.w3.org/2010/05/video/poster.png"
                     "http://media.w3.org/2010/05/video/movie_300.mp4"]})

(defn- switch-video
  [model mp4src tag]
  (let [v (.get model "video")
        [poster-name video-name] (VIDEOS (keyword tag))]
    (.setAttribute v "poster" poster-name)
    (.setAttribute mp4src "src" video-name)
      ;; Turn off the sound for the test movies:
    (set! (.-muted v) true)))

;; Do this as a proper backbone.js model:

(def VideoSystem
  (.extend
   Backbone.Model
   (lib/JS>
    :initialize
    (fn [] nil)

    :select
    (fn [tag]
      (.log js/console (str "select " tag))
      (this-as me
               (switch-video me (.get me "mp4src") tag)
               (.load me)))

    :load
    (fn [] (this-as me
                   (.load (.get me "video"))))

    :play
    (fn [] (this-as me
                   (.play (.get me "video"))))

    :pause
    (fn [] (this-as me
                   (.pause (.get me "video"))))

    :jump
    (fn [pos] (this-as me
                      (set! (.-currentTime (.get me "video")) pos)))

    :timeUpdate
    (fn [] (this-as me
                   (let [t (.-currentTime (.get me "video"))]
                     (this-as me (.set me (lib/JS> :location t))))))

    :playUpdate
    (fn [state]
      (.log js/console (str "playUpdate: " state))
      (this-as me (.set me (lib/JS> :status state))))

    ;; defaults can also be a function.
    :defaults {:status false
               :location 0.0})))

;; The view (and, erm, controller):

(def VideoView
  (.extend
   Backbone.View
   (lib/JS> ;; Initialise by rendering the template into the DOM:
    :initialize
    (fn [] (this-as me
                   (set! (.-$location me) (.$ me "#location"))
                   (set! (.-$status me) (.$ me "#status"))
                   ;; Plant the jQuery for the MP4 (within our main el) into the model.
                   ;; (This is rather nasty: should probably do it via global "$".)
                   (.set (.-model me) (lib/JS> "mp4src" (first (.$ me "#mp4"))))
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

     "click #select"
     (lib/on-model-and-view
      (fn [m v ev]
        (let [text (.-innerText (.-currentTarget ev))
              tag (keyword text)]
          (.select m tag))))

     "click #load" (lib/on-model-and-view #(.load %1))
     "click #pause" (lib/on-model-and-view #(.pause %1))
     "click #jump10" (lib/on-model-and-view #(.jump %1 10))}

    :render
    (lib/on-model-and-view (fn [m v]
                             (do
                               (.html (.-$location v) (.get m "location"))
                               (.html (.-$status v)
                                      (if (.get m "status")
                                        "playing" "paused"))))))))

(defn listen [model v]
  (.addEventListener v
                     "timeupdate"
                     (fn [e] (.timeUpdate model))
                     false)
  (.addEventListener v
                     "play"
                     (fn [e] (.playUpdate model true))
                     false)
  (.addEventListener v
                     "pause"
                     (fn [e] (.playUpdate model false))
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
  (let [m (.-model STATE)
        v (.-video STATE)]
    (set! (.-_video js/document) v)       ; temporary, for debugging.
    (listen m v)))

(jq/document-ready go)
