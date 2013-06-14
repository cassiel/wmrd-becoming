(ns models
  (:require [lib :as lib]))

(def SortableModel
  (.extend
   Backbone.Model
   (lib/JS> :initialize (fn [] (.log js/console "SortableModel initialised."))
            :defaults {:title "---" })))

(def StoreModel
  (.extend
   Backbone.Model
   (lib/JS> :initialize (fn [] (.log js/console "StoreModel initialised."))
            :defaults {:title "///" })))
