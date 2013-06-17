(ns cljs-video-control.ajax
  "Methods for AJAX interaction.")

(defn get-store
  "Get clip selection from the store."
  []
  (letfn [(make-item [img]
            {:title (str "F" (rand-int 100))
             :image img
             :colour (repeatedly 3 #(rand-int 256))})]
    {:body (map make-item ["P1030301" "P1030382" "P1030388" "P1030043"
                           "P1030419" "P1030422" "P1030434" "P1030541"
                           "P1030581" "P1030610" "P1030643" "P1030681"])}))

(defn post-active
  "Post an item for the active sequence."
  [p]
  (println "PARAMS: " p)
  {:body {:id 999
          :title "SAVED"}})
