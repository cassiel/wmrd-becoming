(ns main-collections
  (:require [main-models :as m]
            [lib :as lib]))

(def ClipCollection
  ^{:doc "Collection of thumbnailed video clips."}
  (.extend
   Backbone.Collection
   (lib/JS> :model m/Clip
            :url "/clips"
            :initialize (fn [] (.log js/console "ClipCollection initialised.")))))
