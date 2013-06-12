(ns sortable
  "Sortables."
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
                           (.sortable (.$ me ".row"))

                           ;; Listen to the model:
                           (.listenTo me
                                      (.-model me)
                                      "change"
                                      (.-render me))

                           ;; Initial render:
                           (.render me)))

        :events { }

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
