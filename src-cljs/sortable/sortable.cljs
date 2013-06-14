(ns sortable
  "Sortables."

  (:require [collections :as c]
            [views :as v]
            [jayq.core :as jq]
            [lib :as lib]))

(def STATE (let [top-collection (c/SortableCollection.)
                 bot-collection (c/StoreCollection.)
                 top-view (v/TopLevelView. (lib/JS> :collection top-collection))
                 bot-view (v/BotLevelView. (lib/JS> :collection bot-collection
                                                    :sortableCollection top-collection))]
             (lib/JS> :topCollection top-collection
                      :botCollection bot-collection
                      :topView top-view
                      :botView bot-view)))

(defn go []
  #_ (.add (.-botCollection STATE)
        (lib/JS> {:title "go"})))

(jq/document-ready go)
