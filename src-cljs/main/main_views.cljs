(ns main-views
  (:use [jayq.core :only [$]])
  (:require [lib :as lib]
            [cljs-video-control.manifest :as m]))

(defn draw-curtains
  [view]
  (let [v (.$ view "#video")
        d (.$ view "#draggable")
        video-width (.width v)
        frame-width (.width d)
        l-width (- (.-left (.position d))
                   (.-left (.position v)))
        r-width (- video-width frame-width l-width)
        left-curtain (.$ view "#curtainL")
        right-curtain (.$ view "#curtainR")]
    (.width left-curtain l-width)
    (.width right-curtain r-width)
    (.position left-curtain (lib/JS> :my "right top"
                                     :at "left top"
                                     :of d))
    (.position right-curtain (lib/JS> :my "left top"
                                      :at "right top"
                                      :of d))))

(defn position-frame
  "Position the selection frame at `pos` (normalised, 0.0..1.0) in the video frame."
  [view pos]
  (let [m (.-model view)
        mg (lib/getter m)
        mapped-pos (+ (* pos (mg :keyEndPosition))
                      (* (- 1 pos) (mg :keyStartPosition)))
        v (.$ view "#video")
        d (.$ view "#draggable")
        video-width (.width v)
        frame-width (.width d)
        tl-pixel-pos (int (* mapped-pos (- video-width frame-width)))]
    (.position d (lib/JS> :my "left top"
                          :at (format "left+%d top" tl-pixel-pos)
                          :of v))
    (draw-curtains view)))

(def VideoView
  (.extend
   Backbone.View
   (lib/JS>
    :el "#vdiv"

    :initialize
    (fn [] (this-as me
                   ;; Hold onto some text fields:
                   (set! (.-$duration me) (.$ me "#duration"))
                   (set! (.-$location me) (.$ me "#location"))
                   (set! (.-$status me)   (.$ me "#status"))
                   (set! (.-$dragging me) (.$ me "#dragging"))

                   ;; Plant the jQuery for the MP4 (within our main el) into the model.
                   ;; (This is rather nasty: should probably do it via global "$".)
                   (.set (.-model me) (lib/JS> "mp4src" (first (.$ me "#mp4"))))

                   ;; Set up draggable cover frame:
                   (let [v (.$ me "#video")
                         d (.$ me "#draggable")
                         video-width (.width v)]
                     (.draggable d (lib/JS> :containment v
                                            :start (fn [ev ui] (.dragging (.-model me) true))
                                            :stop  (fn [ev ui] (.dragging (.-model me) false))

                                            :drag  (fn [ev ui]
                                                     ;; frame-width done here because it's not
                                                     ;; set at initialization time (or rather, it's
                                                     ;; the default width as per CSS).
                                                     (let [frame-width (.width (.$ me "#draggable"))
                                                           pixel-pos (-> ui (.-position) (.-left))]
                                                       (.dragPosition (.-model me)
                                                                      pixel-pos
                                                                      (/ pixel-pos
                                                                         (- video-width frame-width)))))))
                     (.height d (.height v))
                     (.width d (Math/floor (* (.height d) (/ 9 16))))
                     (position-frame me 0))

                   (let [m (.-model me)
                         mg (lib/getter m)]
                     ;; Re-render on any change to playback location (or duration, on load):
                     (.listenTo me
                                m
                                "change:duration"
                                (.-render me))

                     (.listenTo me
                                m
                                "change:location"
                                (.-render me))

                     (.listenTo me
                                m
                                "change:pixelDragPosition"
                                (fn [] (draw-curtains me)))

                     (.listenTo me
                                m
                                "change:normLocation"
                                #_ (fn []
                                  (.log js/console (str ">> normalised location "
                                                        (mg :normLocation))))
                                (.-render me))

                     (.listenTo me
                                m
                                "change:liveSecondHalf"
                                (.-render me))

                     (.listenTo me
                                m
                                "change:trapSecondHalf"
                                (.-render me))

                     (.listenTo me
                                m
                                "change:dragging"
                                (fn [] (.log js/console (str "Dragging: " (mg :dragging)))))

                     (.listenTo me
                                m
                                "change:status"
                                (fn []
                                  (.log js/console (str "need restart? s=" (mg :status)))
                                  ;; This hack for HTML5 mobile (instead of looping) - seems to be a lost cause.
                                  #_ (when-not (mg :status)
                                    (doseq [x [0 0.01]] (.jump m x))
                                    (.play m)
                                    #_ (js/setTimeout #(.load m) 2000))
                                  (.render me))))

                   ;; Set top margin.

                   (.height (.$ me "#top-margin") (int (/ (- (.-height js/screen)
                                                             (.height (.-$el me))) 2)))

                   ;; Initial render:
                   (.render me)))

    ;; Events generally go into the model.
    :events
    {"click #play" (lib/on-model-and-view #(.play %1))
     "click #load" (lib/on-model-and-view #(.load %1))
     "click #pause" (lib/on-model-and-view #(.pause %1))
     "click #jump10" (lib/on-model-and-view #(.jump %1 10))

     "click #upload" (fn [] (this-as me (.upload (.-syncModel (.-options me))
                                                (.-model me))))}

    :render
    (lib/on-model-and-view (fn [m v]
                             (let [mg (lib/getter m)]
                               (do
                                 (.html (.-$duration v) (mg :duration))
                                 (.html (.-$location v) (mg :location))
                                 (.html (.-$dragging v) (format "LIVE=%s TRAP=%s POS=%f KS=%f KE=%f"
                                                                (mg :liveSecondHalf)
                                                                (mg :trapSecondHalf)
                                                                (mg :normedDragPosition)
                                                                (mg :keyStartPosition)
                                                                (mg :keyEndPosition)))

                                 (when-not (mg :dragging)
                                   (position-frame v (mg :normLocation))

                                   (.css (.$ v "#draggable h2") "display" "none")

                                   (if (mg :liveSecondHalf)
                                     (.css (.$ v "#draggable h2.secondHalf") "display" "block")
                                     (.css (.$ v "#draggable h2.firstHalf") "display" "block")))

                                 (.html (.-$status v)
                                        (if (mg :status)
                                          "playing" "paused")))))))))

(def ClipView
  (.extend
   Backbone.View
   (lib/JS>
    :initialize
    (fn []
      (this-as me
               (.log js/console (str "ClipView initialised, opts=" (.keys js/_ (.-options me))))
               (.listenTo me
                          (.-model me)
                          "change:selected"
                          (let [mg (lib/getter (.-model me))]
                            (fn []
                              (let [p (.$ me "div.thumb-ident > p")]
                                (.fadeTo p "fast" (if (mg :selected) 1.0 m/SLUG-OPACITY))))))))

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
               (let [mg (lib/getter (.-model me))]
                 (.log js/console (str "LOAD!: "
                                       (mg :slug)
                                       ", video model "
                                       (.-videoModel (.-options me))))

                 (.select (.-videoModel (.-options me))
                          (.-model me)
                          (mg :slug)
                          (mg :thumb)
                          (mg :video)))))

    ;; Render by replacing our HTML (initially an anonymous `div`) with a template
    ;; rendered with the model properties.
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
               #_ (.attr (.$ me ".thumb")
                      "src"
                      (.get (.-model me) "thumb"))

               (.attr (.$ me ".thumb")
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
                                        (fn [] (.log js/console "collection event SYNC")
                                          ;; Set the scrollable storage width to allow all clips,
                                          ;; each with left and right margin:
                                          (let [storage-width (format "%dpx"
                                                                      (* (+ m/THUMB-WIDTH (* 2 m/THUMB-MARGIN))
                                                                         (.-length coll)))]
                                            (.log js/console (str "storage width " storage-width))
                                            (.width (.$ me "#storage") storage-width))

                                          (.lazyload (.$ me "img.thumb")
                                                     (lib/JS> :event "BANG"
                                                              :effect "fadeIn"
                                                              :container (.$ me "#storage")))
                                          (js/setTimeout #(.trigger (.$ me "img.thumb") "BANG") 1000)))

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
