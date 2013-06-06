(ns video-framing
  (:use [jayq.core :only [$]])
  (:require [jayq.core :as jq]))

(defn JS>
  "Inline key-value args to Javascript map."
  [& args]
  (clj->js (apply hash-map args)))

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
                       (let [v (.$ me "#video")
                             d (.$ me "#draggable")]
                         ;; jQuery UI setup (we set up listeners in the events hash):
                         (.draggable d (JS> :containment v))
                         (.height d (.height v))
                         (.width d (Math/floor (* (.height d) (/ 9 16))))
                         (.position d
                                    (JS> :my "left top"
                                         :at "left top"
                                         :of (.$ me "#video"))))


                       ;; Listen to the model:
                       (.listenTo me
                                  (.-model me)
                                  "change"
                                  (.-render me))

                       ;; Initial render:
                       (.render me)))

        :events {"drag #draggable" (fn [] (.log js/console "drag"))
                 "dragstart #draggable" (fn [] (.log js/console "dragstart"))
                 "dragstop #draggable" (fn [] (.log js/console "dragstop"))}

        :render
        (fn [] (this-as me
                       (.log js/console "rendering..."))))))

(def view (View. (JS> :el ".container"
                      :model model)))
