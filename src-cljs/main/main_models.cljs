(ns main-models
  (:use [jayq.core :only [$]])
  (:require [lib :as lib]))

(def VIDEOS {:sintel ["http://media.w3.org/2010/05/sintel/poster.png"
                      "http://media.w3.org/2010/05/sintel/trailer.mp4"]
             :bunny ["http://media.w3.org/2010/05/bunny/poster.png"
                     "http://media.w3.org/2010/05/bunny/trailer.mp4"]
             :movie ["http://media.w3.org/2010/05/bunny/poster.png"
                     "http://192.168.2.3:3000/peach.mp4"]
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

(def Clip
  (.extend
   Backbone.Model
   (lib/JS> :initialize (fn [] (.log js/console "Clip model initialized."))
            :defaults {:title "BOGUS"
                       :image "XXBOGUS"
                       :colour [0 0 0]})))

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
