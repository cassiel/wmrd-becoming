(ns collections
  (:require [models :as m]
            [lib :as lib]))

(def SortableCollection
  (.extend
   Backbone.Collection
   (lib/JS> :model m/SortableModel
            :url "/active"
            :initialize (fn [] (.log js/console "SortableCollection initialised.")))))

(def StoreCollection
  (.extend
   Backbone.Collection
   (lib/JS> :model m/StoreModel
            :url "/store"
            :initialize (fn [] (.log js/console "StoreCollection initialised.")))))
