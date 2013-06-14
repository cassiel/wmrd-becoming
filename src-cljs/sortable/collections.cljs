(ns collections
  (:require [models :as m]
            [lib :as lib]))

(def SortableCollection
  (.extend
   Backbone.Collection
   (lib/JS> :model m/SortableModel
            :initialize (fn [] (.log js/console "SortableConnection initialised.")))))

(def StoreCollection
  (.extend
   Backbone.Collection
   (lib/JS> :model m/StoreModel
            :initialize (fn [] (.log js/console "StoreConnection initialised.")))))
