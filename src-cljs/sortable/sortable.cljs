(ns sortable
  "Sortables."
  (:use [jayq.core :only [$]])
  (:require [jayq.core :as jq]
            [lib :as lib]))

;; A model for each item in the list of sortables.

(def SortableModel
  (.extend
   Backbone.Model
   (lib/JS> :initialize (fn [] (.log js/console "SortableModel initialised."))
            :defaults { })))

;; A collection for the sortables.

(def SortableCollection
  (.extend
   Backbone.Collection
   (lib/JS> :model SortableModel
            :initialize (fn [] (.log js/console "SortableConnection initialised.")))))

(def TopLevelView
  (.extend
   Backbone.View
   ))

(def TopLevelView
  (.extend
   Backbone.View
   (lib/JS> :initialize
            (fn [] (this-as me
                           (.sortable (.$ me ".outer-box, .storage")
                                      (lib/JS> :items ".inner-box, .lower-box"
                                               :connectWith ".connected-sortable"
                                               :containment "#main-enclosure"))

                           ;; Listen to the collection:
                           (.listenTo me
                                      (.-collection me)
                                      "change"
                                      (.-render me))

                           ;; Initial render:
                           (.render me)))

            :events { }

            :render
            (fn [] (this-as me
                           (.log js/console "rendering..."))))))

(def STATE (let [collection (SortableCollection.)
                 view (TopLevelView. (lib/JS> :el "#main-enclosure"
                                              :collection collection))]
             (lib/JS> :collection collection
                      :top-view view)))

(defn go [] nil)

(jq/document-ready go)
