(ns cljs-video-control.manifest)

(def SERVER {:host "192.168.1.111"
             :port 3000})

(def ASSETS {:host "192.168.1.111"})

(def FIELD {:host "192.168.2.9"
            :port 8080})

(def SHOTS-FILE-ROOT "/home/nick/public_html/shots")

(def SHOTS-URL-ROOT "~nick/shots")

(def GALLEY-WIDTH 1170)

(def THUMB-WIDTH 300)

(def SLUG-OPACITY 0.5)                  ; Fades between this and 1.0.

(def ASPECT (/ 24 10))

(def SPLASH-ASSET ["00000" "00005531" "00005695"])
