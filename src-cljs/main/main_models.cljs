(ns main-models
  (:use [jayq.core :only [$]])
  (:require [lib :as lib]))

(defn- switch-video
  [model thumb video]
  (let [v (.get model "video")]
    (.setAttribute v "poster" thumb)
    (.setAttribute (.get model "mp4src") "src" video)
      ;; Turn off the sound for the test movies:
    (set! (.-muted v) true)))

(def Clip
  (.extend
   Backbone.Model
   (lib/JS> :initialize
            (fn [] (this-as me
                           (.log js/console "Clip model initialized.")))

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
    (fn [thumb video]
      (this-as me
               (switch-video me thumb video)
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

    :durationUpdate
    (fn [] (this-as me
                   (let [t (.-duration (.get me "video"))]
                     (this-as me (.set me (lib/JS> :duration t))))))

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
