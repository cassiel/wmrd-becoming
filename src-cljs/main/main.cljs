(ns main
  (:use [jayq.core :only [$]])
  (:require [main-views :as v]
            [main-models :as m]
            [main-collections :as c]
            [jayq.core :as jq]
            [lib :as lib]))

(def STATE
  (let [v (.getElementById js/document "video")
        model (m/VideoSystem. (lib/JS> :video v))
        clips (c/ClipCollection.)
        main-view (v/VideoView. (lib/JS> :el ".container"
                                         :model model))]
    (lib/JS> :video v
             :model model
             :collection clips
             :main-view main-view
             :clip-set-view (v/ClipSetView. (lib/JS> :collection clips
                                                     :model model)))))

(defn listen [model v]
  (.addEventListener v
                     "timeupdate"
                     (fn [e] (.timeUpdate model))
                     false)
  (.addEventListener v
                     "play"
                     (fn [e] (.playUpdate model true))
                     false)
  (.addEventListener v
                     "pause"
                     (fn [e] (.playUpdate model false))
                     false))

(defn go []
  (let [m (.-model STATE)
        v (.-video STATE)]
    (set! (.-_video js/document) v)       ; temporary, for debugging.
    (listen m v)))

(jq/document-ready go)
