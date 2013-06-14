(ns views
  (:use [jayq.core :only [$]])
  (:require [lib :as lib]))

;; A model for each item in the list of sortables.

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

    :events {"click .add-me"
             :addClick}

    :addClick
    (fn []
      (this-as me
               (.log js/console (str "item add on " (.keys js/_ me)))
               (.add (.-sortableColl (.-options me))
                     (.clone (.-model me)))))


    ;; Render by replacing our HTML with a template rendered with the model properties
    ;; (specifically "title"). Return ourself.
    :render
    (fn []
      (this-as me
               (.html
                (.-$el me)
                (.template me (.toJSON (.-model me))))
               me)))))

(def BotLevelView
  (.extend
   Backbone.View
   (lib.JS> :el "#store"

            :initialize
            (fn [] (this-as me
                           (let [coll (.-collection me)
                                 sortable-coll (.-sortableCollection (.-options me))]
                             ;; "add" event from the collection. Create a new view
                             ;; over a copy of the added model.
                             (.listenTo me
                                        coll
                                        "add"
                                        (fn [item]
                                          (.log js/console "add triggered in BotLevelView.")
                                          (let [view (ItemView. (lib/JS> :model item
                                                                         :sortableColl
                                                                         sortable-coll))
                                                el (.-el (.render view))]
                                            (.append (.$ me ".storage") el)))))

                           (.render me)))

            :events {"click .test-add"
                     :testAdd}


            :testAdd
            (fn [e]
              (.preventDefault e)
              (this-as me (.add (.-collection me)
                                (lib/JS> :title "Added (Bot)"))))



            :render
            (fn [] (this-as me
                           (.log js/console (str "rendering BotLevel at length..."
                                                 (.-length (.-collection me))))
                           me)))))

(def TopLevelView
  (.extend
   Backbone.View
   (lib/JS> :el "#main-enclosure"

            :initialize
            (fn [] (this-as me
                           (let [coll (.-collection me)]
                             (.sortable (.$ me ".outer-box")
                                        (lib/JS> :items ".inner-box"
                                                 :containment "#main-enclosure"))

                             ;; "change" event from the collection:
                             (.listenTo me
                                        coll
                                        "change"
                                        (fn [] (.log js/console "collection change")))

                             ;; "add" event from the collection. Create a new view
                             ;; over a copy of the added model.
                             (.listenTo me
                                        coll
                                        "add"
                                        (fn [item]
                                          (.log js.console "add triggered in TopLevelView")
                                          (let [view (ItemView. (lib/JS> :model (.clone item)
                                                                         :sortableModel coll))
                                                el (.-el (.render view))]
                                            (.append (.$ me ".outer-box") el))))

                             (.listenTo me
                                        coll
                                        "reset"
                                        (.log js/console "collection reset!")))

                           ;; Initial render:
                           (.render me)))

            :events {"sortupdate .outer-box"
                     :updateOuter

                     "sortupdate .storage"
                     :updateStorage}

            :updateOuter
            (fn [e ui]
              #_ (lib/logger "update: e" e)
              #_ (lib/logger "update: ui" ui)
              ;; dragged id:
              #_ (.log js/console "item id" (.-id (first (.-item ui))))
              (this-as me
                       (doseq [x (.sortable (.$ me ".outer-box")
                                            "toArray")]
                         (.log js/console (str "item.id " x))))
              ;; Update collection to contain items in new order, without
              ;; firing the view.
              #_ (this-as me
                       (.set (.-collection me)
                             models
                             (lib/JS> :add false
                                      :remove false
                                      :merge false)))
              )

            :updateStorage
            (fn [e ui] nil)

            :testAdd
            (fn [e]
              (.preventDefault e)
              (this-as me (.add (.-collection me)
                                (lib/JS> :title "Added (Top)"))))

            :render
            (fn [] (this-as me
                           (.log js/console (str "rendering at length..."
                                                 (.-length (.-collection me))))
                           me)))))
