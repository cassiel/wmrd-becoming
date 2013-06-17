(ns cljs-video-control.ajax
  "Methods for AJAX interaction.")

(defn get-store
  "Get clip selection from the store."
  []
  {:body [{:title (str "F" (rand-int 100)) :colour [0 0 0]}
          {:title (str "F" (rand-int 100)) :colour [0 0 255]}]})

(defn post-active
  "Post an item for the active sequence."
  [p]
  (println "PARAMS: " p)
  {:body {:id 999
          :title "SAVED"}})
