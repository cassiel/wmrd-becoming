(ns range-slider
  "Model and view for a jQuery range slider."
  (:use [jayq.core :only [$]])
  (:require [jayq.core :as jq]
            [lib :as lib]))

;; Simple model.

(def Model
  (.extend Backbone.Model
           (lib/JS> :initialize
                    (fn [] nil)

                    :range
                    (fn [[min max]]
                      (this-as me (.set me (lib/JS> :min min
                                                    :max max))))

                    :defaults {:min 0
                               :max 0})))


;; View, attached to the main container div.

(def View
  (.extend Backbone.View
           (lib/JS>
            :initialize
            (fn [] (this-as me
                           (let [range-slider (.$ me "#range_slider")]
                             (set! (.-$range-slider me) range-slider)
                             (set! (.-$min me) (.$ me "#min"))
                             (set! (.-$max me) (.$ me "#max"))
                             (.slider range-slider
                                      (lib/JS> :range true)))

                           ;; Listen to the model:
                           (.listenTo me
                                      (.-model me)
                                      "change"
                                      (.-render me))

                           ;; Initial render:
                           (.render me)))

            :events
            {"slidechange #range_slider"
             (lib/on-model-and-view #(.range %1 (.slider (.-$range-slider %2) "values")))}

            :render
            (lib/on-model-and-view (fn [m v]
                                     (.html (.-$min v) (.get m "min"))
                                     (.html (.-$max v) (.get m "max")))))))

(def STATE
  (let [model (Model.)
        view (View. (lib/JS> :el ".container"
                             :model model))]
    (lib/JS> :model model
             :view view)))

(defn go [] nil)
(jq/document-ready go)
