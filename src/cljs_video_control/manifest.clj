(ns cljs-video-control.manifest)

(def SERVER {:host "192.168.2.3"
             :port 3000})

(def ASSETS {:host "192.168.2.3"})

(def FIELD {:host "192.168.2.9"
            :port 8080})

(def SHOTS-FILE-ROOT "/home/nick/public_html/shots")

(def SHOTS-URL-ROOT "~nick/shots")

(def ASPECT (/ 24 10))

(def GALLEY-WIDTH 1170)
(def THUMB-WIDTH 300)
(def THUMB-MARGIN 5)

(def BUTTON-OUTLINE {false "1px solid #444"
                     true "1px solid #888"})

(def SLUG-OPACITY 0.5)                  ; Fades between this and 1.0.

(def AUTOPLAY true)

(def DO-UPLOAD true)
