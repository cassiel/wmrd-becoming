(ns main-models
  (:use [jayq.core :only [$]])
  (:require [lib :as lib]))

(defn- switch-video
  [model thumb video]
  (let [v (.get model "video")]
    (.setAttribute v "poster" thumb)
    (.setAttribute (.get model "mp4src") "src" video)
    ;; Turn off the sound for the test movies:
    (set! (.-muted v) true)
    ;; Reset editing state associated with the current video:
    (.set me (lib/JS> :keyStartPosition 0.0
                      :keyEndPosition 1.0))))

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

    ;; Manual dragging of the selection frame. Drag start/stop.
    :dragging
    (fn [how] (this-as me
                      (.set me (lib/JS> :dragging how))
                      (when-not how     ; Release drag:
                        (let [key-name (if (.get me "trapSecondHalf") :keyEndPosition :keyStartPosition)]
                          (.set me (lib/JS> :trapSecondHalf (.get me "liveSecondHalf")
                                            key-name (.get me "dragPosition")))))))

    ;; Actual dragging.
    :dragPosition
    (fn [pos] (this-as me (.set me (lib/JS> :dragPosition pos))))

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
                   (let [t (.-duration (.get me "video"))
                         n (if (pos? t) (/ (.get me "location") t) 0.0)]
                     (this-as me
                              (.set me (lib/JS> :duration t
                                                :normLocation n
                                                :liveSecondHalf (> n 0.5)))
                              (when-not (.get me "dragging")
                                (.set me (lib/JS> :trapSecondHalf (.get me "liveSecondHalf"))))))))

    :timeUpdate
    (fn [] (this-as me
                   (let [t (.-currentTime (.get me "video"))
                         d (.get me "duration")
                         n (if (pos? d) (/ t d) 0.0)]
                     (this-as me
                              (.set me (lib/JS> :location t
                                                :normLocation n
                                                :liveSecondHalf (> n 0.5)))
                              (when-not (.get me "dragging")
                                (.set me (lib/JS> :trapSecondHalf (.get me "liveSecondHalf"))))))))

    :playUpdate
    (fn [state]
      (.log js/console (str "playUpdate: " state))
      (this-as me (.set me (lib/JS> :status state))))

    ;; defaults can also be a function.
    :defaults {:status false
               :location 0.0
               :duration 0.0
               :keyStartPosition 0.0
               :keyEndPosition 1.0
               :normLocation 0.0
               :liveSecondHalf false    ; Tracks video position.
               :trapSecondHalf false    ; Trapped when frame drag begins, reset on release.
               :dragging false
               :dragPosition 0})))
