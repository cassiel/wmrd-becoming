(ns search-template
  (:use [jayq.core :only [$]])
  (:require [jayq.core :as jq]))

(defn JS>
  "Inline key-value args to Javascript map."
  [& args]
  (clj->js (apply hash-map args)))

;; Do this as a proper backbone.js model:

(def SearchSystem
  (.extend
   Backbone.Model
   (JS> :initialize
        (fn [] nil)

        :defaults { })))

(def search-system (SearchSystem.))

;; The view (and, erm, controller):

(def SearchView
  (.extend
   Backbone.View
   (JS> ;; Initialise by rendering the template into the DOM:
    :initialize
    (fn [] (this-as me
                   ;; Re-render on any model change:
                   (.listenTo me
                              (.-model me)
                              "change"
                              (.-render me))
                   ;; Initial render:
                   (.render me)))

    :events {"click input[type=button]" :doSearch}

    :doSearch
    (fn [] (js/alert (str "Search for " (.val ($ "#search_input")))))

    :render
    ;; Compile the template using underscore
    (fn []
      (this-as me
               (let [template (.template js/_
                                         (.html ($ "#search_template"))
                                         (JS> :search_label "FOO"))]
                 (-> (.-$el me)
                     (.html template))))))))

;; Build a view: it takes a model instance as argument.

(def search-view (SearchView. (JS> :el "#search_container"
                                   :model search-system)))
