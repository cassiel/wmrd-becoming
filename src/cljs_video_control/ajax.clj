(ns cljs-video-control.ajax
  "Methods for AJAX interaction."
  (:require [ring.util [response :as resp]]))

(defn get-store
  "Get clip selection from the store."
  []
  (letfn [(make-item [img]
            {:title (str "F" (rand-int 100))
             :image img
             :colour (repeatedly 3 #(rand-int 256))})]
    (resp/response (map make-item ["P1030301" "P1030382" "P1030388" "P1030043"
                                   "P1030419" "P1030422" "P1030434" "P1030541"
                                   "P1030581" "P1030610" "P1030643" "P1030681"]))))

(defn get-clips
  "Get clip list (eventually this'll be scrollable by 'bank')."
  []
  (letfn [(make-item [[shot frame-lo frame-hi]]
            (let [dir-name (format "shot_%s_%s_%s" shot frame-lo frame-hi)
                  thumb (format "http://localhost:8000/%s/image_half.jpg" dir-name)
                  video (format "http://localhost:8000/%s/imageList_all.mp4" dir-name)]
              {:slug shot
               :thumb thumb
               :video video}))]

    (resp/response (map make-item [["00000" "00005531" "00005695"]
                                   ["00001" "00005699" "00005867"]
                                   ["00002" "00005871" "00005943"]
                                   ["00003" "00005947" "00006097"]
                                   ["00004" "00006101" "00006149"]
                                   ["00005" "00006153" "00006257"]
                                   ["00006" "00006261" "00006349"]
                                   ["00007" "00006353" "00006519"]
                                   ["00008" "00006523" "00007069"]
                                   ["00009" "00007073" "00007107"]]))))

(defn post-active
  "Post an item for the active sequence."
  [p]
  (println "PARAMS: " p)
  (resp/response {:id 999
                  :title "SAVED"}))
