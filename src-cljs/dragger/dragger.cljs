(ns dragger
  "Simple drag-and-drop example."
  (:use [jayq.core :only [$]])
  (:require [jayq.core :as jq]
            [lib :as lib]))

(def Model
  (.extend
   Backbone.Model
   (lib/JS> :initialize (fn [])

            :defaults {:my-value 42})))

(def View
  (.extend
   Backbone.View
   (lib/JS> :initialize
        (fn [] (this-as me
                       ;; jQuery UI setup (we set up listeners in the events hash):
                       (.draggable (.$ me ".row .draggable")
                                   (lib/JS> :containment (.-$el me)
                                            :helper "clone"
                                            :opacity 0.7))
                       (let [targets (.$ me ".row div table tbody tr .droppable-skel")]
                         (.droppable targets
                                     (lib/JS> :tolerance "pointer"
                                              :activeClass "drop-active"
                                              :hoverClass "drop-hover"))
                         (.droppable targets "disable"))

                       ;; Listen to the model:
                       (.listenTo me
                                  (.-model me)
                                  "change"
                                  (.-render me))

                       ;; Initial render:
                       (.render me)))

        :events {"drag .draggable" (fn [] (.log js/console "drag"))

                 "dragstart .draggable"
                 (fn []
                   (.log js/console "dragstart")
                   ;; Activate a calculated selection of the droppable slots, by turning on their
                   ;; droppable style.
                   (this-as me
                            (let [targets (nth (.$ me ".droppable-skel") 6)]
                              (.addClass targets "droppable")
                              (.droppable targets "enable"))))

                 "dragstop .draggable"
                 (fn []
                   (.log js/console "dragstop")
                   (this-as me
                            (let [targets (.$ me ".droppable-skel")]
                              (.removeClass targets "droppable")
                              (.droppable targets "disable"))))

                 "dropactivate .droppable" (fn [] (.log js/console "dropactivate"))

                 "drop .droppable" (fn [e ui]
                                     (.log js/console (str "e=" (.keys js/_ e)))
                                     (.log js/console (str "ui=" (.keys js/_ ui))))}

        :render
        (fn [] (this-as me
                       (.log js/console "rendering..."))))))

(def STATE (let [model (Model.)
                 view (View. (lib/JS> :el "#main-enclosure"
                                      :model model))]
             (lib/JS> :model model
                      :view view)))

(defn go [] nil)

(jq/document-ready go)
