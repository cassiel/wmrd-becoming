(ns models
  (:require [lib :as lib]))

(def SortableModel
  (.extend
   Backbone.Model
   (lib/JS> :initialize (fn [] (.log js/console "SortableModel initialised."))
            :defaults {:title "---"
                       :colour "#000000"})))

(def StoreModel
  (.extend
   Backbone.Model
   (lib/JS> :initialize (fn [] (.log js/console "StoreModel initialised."))
            :defaults {:title "///"
                       :colour "#000000"})))
