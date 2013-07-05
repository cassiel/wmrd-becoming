(ns cljs-video-control.ajax
  "Methods for AJAX interaction."
  (:require [ring.util [response :as resp]]
            [clj-http.client :as client]
            (cljs-video-control [manifest :as m]
                                [layout :as lx]))
  (:import (java.io File)))

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
            (let [a (lx/assets shot frame-lo frame-hi)]
              (assoc a :slug shot)))]

    (resp/response (sort-by :slug (map (comp make-item
                                             next
                                             (partial re-find #"(\d+)_(\d+)_(\d+)*")
                                             str)
                                       (seq (.listFiles (File. m/SHOTS-FILE-ROOT))))))))

(defn post-active
  "Post an item for the active sequence."
  [p]
  (println "PARAMS: " p)
  (resp/response {:id 999
                  :title "SAVED"}))

(defn upload
  [p]
  (let [s (lx/field "upload")]
    (println "SERVER " s " PARAMS " p)
    (client/post s {:form-params p})))
