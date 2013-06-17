(ns views
  (:use [jayq.core :only [$]])
  (:require [lib :as lib]))

;; A model for each item in the list of sortables.

(def ItemView
  (.extend
   Backbone.View
   (lib/JS>
    :initialize
    (fn [] (.log js/console "ItemView initialised"))

    ;; Pull template from page, cache as fn:
    :template
    (.template js/_
               (.html ($ "#item-template")))

    :events {"click .add-me"
             :addClick

             "click .del-me"
             :delClick}

    :addClick
    ;; add this item (from the store) to the active sequence.
    (fn []
      (this-as me
               (.log js/console (str "item add on " (.keys js/_ me)))
               (lib/logger "create"
                           (.create (.-sortableColl (.-options me))
                                    (.clone (.-model me))))))

    :delClick
    ;; delete this item (from the sequence).
    (fn []
      (this-as me
               (let [m (.-model me)]
                 (lib/logger "remove attempt" m)
                 (.remove (.-sortableColl (.-options me)) m)
                 ;; HTTP destroy on `m`.
                 )))

    ;; Render by replacing our HTML (initially an anonymous `div`) with a template
    ;; rendered with the model properties (specifically "title" and "colour").
    ;; (We replace the HTML of our `div`, so the template `div` is nested.)
    ;; Return ourself.
    :render
    (fn []
      (this-as me
               (.html (.-$el me)
                      (.template me (.toJSON (.-model me))))
               ;; CSS class needed for sortable interaction etc - added to our
               ;; root `div`.
               (.addClass (.-$el me) "box")
               (.css (.$ me ".inner-box")
                     "background-color"
                     (let [[r g b] (.get (.-model me) "colour")]
                       (format "rgb(%d, %d, %d)" r g b)))
               me)))))

(defn add-item
  "Add an item to the view, synthesising a DOM element for it."
  [me model]
  (let [coll (.-collection me)
        sortable-coll (.-sortableCollection (.-options me))
        view (ItemView. (lib/JS> :model model
                                 :sortableColl sortable-coll))
        el (.-el (.render view))]
    (.append (.$ me ".storage") el)))

(def BotLevelView
  (.extend
   Backbone.View
   (lib.JS> :el "#store"

            :initialize
            (fn [] (this-as me
                           (let [coll (.-collection me)]
                             ;; `add` event from the collection. Create a new view
                             ;; over a copy of the added model. (Not sure we actually
                             ;; see `add` for the bottom-level collections.)
                             (.listenTo me
                                        coll
                                        "add"
                                        (partial add-item me))

                             ;; `reset` for replacing the entire selection from the
                             ;; server. (There's no point doing that incrementally.)
                             (.listenTo me
                                        coll
                                        "reset"
                                        (fn [items]
                                          (.remove (.$ me ".box"))
                                          (doseq [i (.-models items)] (add-item me i)))))

                           (.render me)))

            :events {"click #populate"
                     :populate

                     "click #fetcher"
                     :doFetch}

            :populate
            (fn [e]
              (.preventDefault e)
              (this-as me
                       (.reset (.-collection me))
                       (doseq [i (range 10)]
                         (let [rgb (repeatedly 3 #(rand-int 256))]
                           (.add (.-collection me)
                                 (lib/JS> :title (str (inc i))
                                          :colour rgb))))))

            :doFetch
            (fn []
              (this-as me
                       ;; When fetching, reset the entire collection, since we'll
                       ;; generally be replacing everything.
                       (.fetch (.-collection me)
                               (lib/JS> :reset true

                                        :success
                                        (fn [] (.log js/console "fetched"))

                                        :error
                                        (fn [coll resp opts]
                                          (js/alert resp))))))

            :render
            ;; No actual rendering code (for now): we just listen to model changes and
            ;; alter the DOM directly there.
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
                                        (lib/JS> :items ".box"
                                                 :containment "#main-enclosure"
                                                 :tolerance "pointer"))

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
                                        "remove"
                                        (fn [] (js/alert "REMOVE")))

                             (.listenTo me
                                        coll
                                        "reset"
                                        (.log js/console "collection reset!"))

                             (.listenTo me
                                        coll
                                        "error"
                                        (fn [model xhr options]
                                          (lib/logger "collection event ERROR" xhr)
                                          (.log js/console (.-statusText xhr))))

                             (.listenTo me
                                        coll
                                        "sync"
                                        (fn [] (js/alert "collection event SYNC"))))

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

            :render
            (fn [] (this-as me
                           (.log js/console (str "rendering at length..."
                                                 (.-length (.-collection me))))
                           me)))))
