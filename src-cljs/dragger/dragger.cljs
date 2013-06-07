(ns dragger
  (:use [jayq.core :only [$]])
  (:require [jayq.core :as jq]
            [lib :as lib]))

(def Model
  (.extend
   Backbone.Model
   (lib/JS> :initialize (fn [])

            :defaults {:my-value 42})))

(def model (Model.))

(def View
  (.extend
   Backbone.View
   (lib/JS> :initialize
        (fn [] (this-as me
                       ;; jQuery UI setup (we set up listeners in the events hash):
                       (.draggable (.$ me ".row .draggable")
                                   (lib/JS> :helper "clone"
                                            :opacity 0.7
                                            ))
                       (.droppable (.$ me ".row div table tbody tr .droppable")
                                   (lib/JS> :tolerance "pointer"
                                            :activeClass "drop-active"
                                            :hoverClass "drop-hover"))

                       ;; Listen to the model:
                       (.listenTo me
                                  (.-model me)
                                  "change"
                                  (.-render me))

                       ;; Initial render:
                       (.render me)))

        :events {"drag .draggable" (fn [] (.log js/console "drag"))
                 "dragstart .draggable" (fn [] (.log js/console "dragstart"))
                 "dragstop .draggable" (fn [] (.log js/console "dragstop"))
                 "dropactivate .droppable" (fn [] (.log js/console "dropactivate"))
                 "drop .droppable" (fn [e ui]
                                     (.log js/console (str "t=" (.-id (.-target e))))
                                     (.log js/console (str "d=" (.-id (first (.-draggable ui))))))}

        :render
        (fn [] (this-as me
                       (.log js/console "rendering..."))))))

(def view (View. (lib/JS> :el "#main-enclosure"
                          :model model)))
