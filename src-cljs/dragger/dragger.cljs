(ns dragger
  (:use [jayq.core :only [$]])
  (:require [jayq.core :as jq]))

(defn JS>
  "Inline key-value args to Javascript map."
  [& args]
  (clj->js (apply hash-map args)))

;; Direct jQuery UI setup:
;;(.draggable ($ "#draggable"))
;;(.droppable ($ "#droppable") (JS> :drop (fn [] (js/alert "dropped"))
;;                                  :tolerance "pointer"))

;; MVC-based setup:

(def Model
  (.extend
   Backbone.Model
   (JS> :initialize (fn [])

        :defaults {:my-value 42})))

(def model (Model.))

(def View
  (.extend
   Backbone.View
   (JS> :initialize
        (fn [] (this-as me
                       ;; jQuery UI setup:
                       (.draggable (.$ me ".draggable"))
                       (.droppable (.$ me ".droppable")
                                   (JS> :tolerance "pointer"))

                       ;; Listen to the model:
                       (.listenTo me
                                  (.-model me)
                                  "change"
                                  (.-render me))

                       ;; Initial render:
                       (.render me)))

        :events {"drag .draggable" (fn [] (.log js/console "drag"))
                 "drop .droppable" (fn [] (.log js/console "drop"))}

        :render
        (fn [] (this-as me
                       (.log js/console "rendering...")
                       (-> (.$ me ".status")
                           (.html "BOG"))))
        )))

(def view (View. (JS> :el "#main"
                      :model model)))
