(ns main-views
  (:use [jayq.core :only [$]])
  (:require [lib :as lib]))

(def VideoView
  (.extend
   Backbone.View
   (lib/JS>
    :initialize
    (fn [] (this-as me
                   ;; Hold onto some text fields:
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

(def ClipView
  (.extend
   Backbone.View
   (lib/JS>
    :initialize
    (fn [] (.log js/console "ClipView initialised"))

    ;; Pull template from page, cache as fn:
    :template
    (.template js/_
               (.html ($ "#item-template")))

    :events {"click .add-me"
             :addClick}

    :addClick
    ;; add this item (from the store) to the active sequence.
    (fn [] (.log js/console "addClick"))

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
  [me model]
  (let [coll (.-collection me)
        clip-view (ClipView. (lib/JS> :model model))
        el (.-el (.render clip-view))]
    (.append (.$ me "#storage") el)))

(def ClipSetView
  ^{:doc "View of the set of clips currently available for loading/cueing."}
  (.extend
   Backbone.View
   (lib/JS> :el "#clips"

            :initialize
            (fn [] (this-as me
                           (let [coll (.-collection me)]
                             (.listenTo me
                                        coll
                                        "change"
                                        (fn [] (.log js/console (str  "collection change, len="
                                                                     (.-length coll)))))

                             (.listenTo me
                                        coll
                                        "reset"
                                        (fn [items] (.log js/console (str  "collection reset, len="
                                                                          (.-length coll)))
                                          (.remove (.$ me ".box"))    ; Remove multiple?
                                          (doseq [i (.-models items)] (add-item me i))))

                             (.listenTo me
                                        coll
                                        "add"
                                        (fn [] (.log js/console (str  "collection add, len="
                                                                     (.-length coll)))))

                             (.listenTo me
                                        coll
                                        "sync"
                                        (fn [] (js/alert "collection event SYNC")))

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
