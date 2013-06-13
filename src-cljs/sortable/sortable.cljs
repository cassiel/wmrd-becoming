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
            :defaults {:title "---" })))

;; A collection for the sortables.

(def SortableCollection
  (.extend
   Backbone.Collection
   (lib/JS> :model SortableModel
            :initialize (fn [] (.log js/console "SortableConnection initialised.")))))

(def ItemView
  (.extend
   Backbone.View
   (lib/JS>
    :initialize
    (fn [] (.log js/console "ItemView initialised."))

    ;; Pull template from page, cache as fn:
    :template
    (.template js/_
               (.html ($ "#item-template")))

    ;; Render by replacing our HTML with a template rendered with the model properties
    ;; (specifically "title"). Return ourself.
    :render
    (fn []
      (this-as me
               (.html
                (.-$el me)
                (.template me (.toJSON (.-model me))))
               me)))))

(def TopLevelView
  (.extend
   Backbone.View
   (lib/JS> :initialize
            (fn [] (this-as me
                           (.sortable (.$ me ".outer-box, .storage")
                                      (lib/JS> :items ".inner-box, .lower-box"
                                               :connectWith ".connected-sortable"
                                               :containment "#main-enclosure"))

                           ;; "change" event from the collection:
                           (.listenTo me
                                      (.-collection me)
                                      "change"
                                      (fn [] (.log js/console "collection change")))

                           ;; "add" event from the collection:
                           (.listenTo me
                                      (.-collection me)
                                      "add"
                                      (fn [item]
                                        (let [view (ItemView. (lib/JS> :model item))
                                              el (.-el (.render view))]
                                          (.append ($ ".outer-box") el))))
                           ;; Initial render:
                           (.render me)))

            :events {"sortupdate .outer-box"
                     (fn [e ui]
                       (lib/logger "update: e" e)
                       (lib/logger "update: ui" ui)
                       (.log js/console "item id" (.-id (first (.-item ui))))
                       (this-as me
                                (doseq [x (.sortable (.$ me ".outer-box")
                                                     "toArray")]
                                  (.log js/console (str "item.id " x)))))

                     "sortupdate .storage"
                     (fn [e ui] nil)}

            :render
            (fn [] (this-as me
                           (.log js/console (str "rendering at length..."
                                                 (.-length (.-collection me)))))))))

(def STATE (let [collection (SortableCollection.)
                 view (TopLevelView. (lib/JS> :el "#main-enclosure"
                                              :collection collection))]
             (lib/JS> :collection collection
                      :top-view view)))

(defn go []
  (.add (.-collection STATE)
        (lib/JS> :title "Robot")))

(jq/document-ready go)
