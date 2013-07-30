(ns main-models
  (:use [jayq.core :only [$]])
  (:require [lib :as lib]
            [cassiel.wmrd-becoming.manifest :as m]))

(defn- switch-video
  [model slug thumb video]
  (let [mg (lib/getter model)
        v (mg :video)]
    (.each (mg :collection)
           (fn [clip] (.set clip (lib/JS> :selected false))))
    (.setAttribute v "poster" thumb)
    (.setAttribute (mg :mp4src) "src" video)
    ;; Turn off the sound for the test movies:
    (set! (.-muted v) true)
    ;; Reset editing state associated with the current video (before loading):
    (.set model (lib/JS> :slug slug
                         :keyStartPosition 0.5
                         :keyEndPosition 0.5
                         :dragEnabled false))))

(def Clip
  (.extend
   Backbone.Model
   (lib/JS> :initialize
            (fn [] (this-as me
                           (.log js/console "Clip model initialized.")))

            :defaults {:selected false})))

(def VideoSystem
  (.extend
   Backbone.Model
   (lib/JS>
    :initialize
    (fn [] nil)

    :select
    (fn [clip slug thumb video]
      (this-as me
               (switch-video me slug thumb video)
               (.set clip (lib/JS> :selected true))
               (.set me (lib/JS> :dirty true))
               (.load me)))

    ;; Manual dragging of the selection frame. Drag start/stop.
    :dragging
    (fn [how] (this-as me
                      (.set me (lib/JS> :dragging how))
                      (when-not how     ; Release drag:
                        (let [key-name (if (.get me "trapSecondHalf") :keyEndPosition :keyStartPosition)]
                          (.set me (lib/JS> :trapSecondHalf (.get me "liveSecondHalf")
                                            key-name (.get me "normedDragPosition")
                                            :dirty true))))))

    ;; Actual dragging.
    :dragPosition
    (fn [pixel-pos scaled-pos] (this-as me (.set me (lib/JS> :pixelDragPosition pixel-pos
                                                            :normedDragPosition scaled-pos))))

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

    :loadStart
    (fn [] #_ (this-as me (.set me (lib/JS> :dragEnabled true))))

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
      (this-as me
               (.set me (lib/JS> :status state))
               (if state (this-as me (.set me (lib/JS> :dragEnabled true))))))

    ;; defaults can also be a function.
    :defaults {:slug "---"
               :status false
               :location 0.0
               :duration 0.0
               :keyStartPosition 0.5
               :keyEndPosition 0.5
               :normLocation 0.0
               :liveSecondHalf false    ; Tracks video position.
               :trapSecondHalf false    ; Trapped when frame drag begins, reset on release.
               :dragEnabled false
               :dragging false
               :normedDragPosition 0.0
               :pixelDragPosition 0
               :dirty false})))

;; Model purely for sending video selection details to Field, via AJAX. (We need
;; other models for config and mode.)

(def Upload
  (.extend
   Backbone.Model
   (lib/JS>
    :url "/upload"

    :upload
    (fn [main-model]
      (this-as me (.save me
                         (lib/JS> :slug (.get main-model "slug")
                                  :keyStartPosition (.get main-model "keyStartPosition")
                                  :keyEndPosition (.get main-model "keyEndPosition"))
                         (lib/JS> :success (fn [] (.log js/console "Upload OK")
                                             (.set main-model (lib/JS> :dirty false)))
                                  :error (fn [ev resp opts]
                                           (.log js/console ev)
                                           (js/alert (.keys js/_ resp))
                                           (.log js/console resp))))))

    :defaults {:slug "---"
               :keyStartPosition 0.5
               :keyEndPosition 0.5})))

(def Config
  (.extend
   Backbone.Model
   (lib/JS>
    :url "/config"

    :flip3d
    (fn []
      (this-as me
               (.save me
                      (lib/JS> :in3d (- 1 (.get me "in3d")))
                      (lib/JS> :success (fn [model, response, options] (.log js/console response))
                               :error (fn [model, xhr, options] (js/alert "FAIL"))))))

    :defaults {:in3d 1})))

(def Mode
  (.extend
   Backbone.Model
   (lib/JS>
    :url "/mode"

    :flipRunning
    (fn []
      (this-as me
               (.save me
                      (lib/JS> :running (- 1 (.get me "running"))))))

    :defaults {:running 1})))
