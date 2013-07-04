(ns main-views
  (:use [jayq.core :only [$]])
  (:require [lib :as lib]))

(defn position-frame
  "Position the selection frame at `pos` (normalised, 0.0..1.0) in the video frame."
  [view pos]
  (let [v (.$ view "#video")
        d (.$ view "#draggable")]
    (.position d (lib/JS> :my "left top"
                          :at (format "left+%d top" pos)
                          :of v))))

(def VideoView
  (.extend
   Backbone.View
   (lib/JS>
    :initialize
    (fn [] (this-as me
                   ;; Hold onto some text fields:
                   (set! (.-$duration me) (.$ me "#duration"))
                   (set! (.-$location me) (.$ me "#location"))
                   (set! (.-$status me) (.$ me "#status"))

                   ;; Plant the jQuery for the MP4 (within our main el) into the model.
                   ;; (This is rather nasty: should probably do it via global "$".)
                   (.set (.-model me) (lib/JS> "mp4src" (first (.$ me "#mp4"))))

                   ;; Set up draggable cover frame:
                   (let [v (.$ me "#video")
                         d (.$ me "#draggable")]
                     (.draggable d (lib/JS> :containment v))
                     (.height d (.height v))
                     (.width d (Math/floor (* (.height d) (/ 9 16))))
                     (position-frame me 0)
                     #_ (.position d (lib/JS> :my "left top"
                                           :at "left top"
                                           :of (.$ me "#video"))))

                   ;; Re-render on any change to playback location (or duration, on load):
                   (.listenTo me
                              (.-model me)
                              "change:duration"
                              (.-render me))

                   (.listenTo me
                              (.-model me)
                              "change:location"
                              (.-render me))

                   (let [m (.-model me)]
                     (.listenTo me
                                m
                                "change:status"
                                (fn []
                                  (.log js/console (str "need restart? s=" (.get (.-model me) "status")))
                                  ;; This hack for HTML5 mobile (instead of looping) - seems to be a lost cause.
                                  #_ (when-not (.get m "status")
                                    (doseq [x [0 0.01]] (.jump m x))
                                    (.play m)
                                    #_ (js/setTimeout #(.load m) 2000))
                                  (.render me))))

                   ;; Initial render:
                   (.render me)))

    ;; Events generally go into the model.
    :events
    {"click #play" (lib/on-model-and-view #(.play %1))
     "click #load" (lib/on-model-and-view #(.load %1))
     "click #pause" (lib/on-model-and-view #(.pause %1))
     "click #jump10" (lib/on-model-and-view #(.jump %1 10))}

    :render
    (lib/on-model-and-view (fn [m v]
                             (do
                               (.html (.-$duration v) (.get m "duration"))
                               (.html (.-$location v) (.get m "location"))
                               (position-frame v (* 50 (.get m "location")))
                               (.html (.-$status v)
                                      (if (.get m "status")
                                        "playing" "paused"))))))))

(def ClipView
  (.extend
   Backbone.View
   (lib/JS>
    :initialize
    (fn []
      (this-as me
               (.log js/console (str "ClipView initialised, opts=" (.keys js/_ (.-options me))))))

    ;; Pull template from page, cache as fn:
    :template
    (.template js/_
               (.html ($ "#item-template")))

    :events {"click .inner-box"
             :doLoad}

    :doLoad
    ;; add this item (from the store) to the active sequence.
    (fn []
      (this-as me
               (.log js/console (str "LOAD!: "
                                     (.get (.-model me) "slug")
                                     ", video model "
                                     (.-videoModel (.-options me))))

               (.select (.-videoModel (.-options me))
                        (.get (.-model me) "thumb")
                        (.get (.-model me) "video"))))

    ;; Render by replacing our HTML (initially an anonymous `div`) with a template
    ;; rendered with the model properties (specifically "title" and "colour").
    ;; (We replace the HTML of our `div`, so the template `div` is nested.)
    ;; Return ourself.
    :render
    (fn []
      (this-as me
               (.html (.-$el me)
                      (.template me (.toJSON (.-model me))))
               ;; CSS class needed for sortable interaction etc - added to our
               ;; root `div`.
               (.addClass (.-$el me) "box")
               (.attr (.$ me ".thumb")
                      "src"
                      (.get (.-model me) "thumb"))

               #_ (.attr (.$ me ".thumb")
                      "data-original"
                      (.get (.-model me) "thumb"))

               me)))))

(defn add-item
  "Add an item to the view, synthesising a DOM element for it."
  [me model video-model]
  (let [clip-view (ClipView. (lib/JS> :model model
                                      :videoModel video-model))
        el (.-el (.render clip-view))]
    (.append (.$ me "#storage") el)))

(def ClipSetView
  ^{:doc "View of the set of clips currently available for loading/cueing."}
  (.extend
   Backbone.View
   (lib/JS> :el "#clips"

            :initialize
            (fn [] (this-as me
                           (let [coll (.-collection me)
                                 video-model (.-model me)]
                             (.listenTo me
                                        coll
                                        "change"
                                        (fn [] (.log js/console (str  "collection change, len="
                                                                     (.-length coll)))))

                             (.listenTo me
                                        coll
                                        "reset"
                                        (fn [items]
                                          (.log js/console (str  "collection reset, len="
                                                                 (.-length coll)
                                                                 " i am "
                                                                 (.keys js/_ video-model)))
                                          (.remove (.$ me ".box"))    ; Remove multiple?
                                          (doseq [i (.-models items)] (add-item me i video-model))))

                             (.listenTo me
                                        coll
                                        "add"
                                        (fn [] (.log js/console (str  "collection add, len="
                                                                     (.-length coll)))))

                             (.listenTo me
                                        coll
                                        "sync"
                                        (fn [] (.log js/console "collection event SYNC")))

                             (.render me))))

            :events {"click #fetcher"
                     :doFetch}

            :doFetch
            (fn []
              (this-as me
                       ;; When fetching, reset the entire collection, since we'll
                       ;; generally be replacing everything.
                       (.fetch (.-collection me)
                               (lib/JS> :reset true

                                        :success
                                        (fn [] (.log js/console "fetched"))

                                        :error
                                        (fn [coll resp opts]
                                          (js/alert resp))))))

            :render
            (fn [] (this-as me
                           (.log js/console (str "rendering clip set at length "
                                                 (.-length (.-collection me)))))))))
