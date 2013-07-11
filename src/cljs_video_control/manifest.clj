(ns cljs-video-control.manifest)

(def MACBOOK-LOCAL
  {:server {:host "localhost"
            :port 3000}
   :assets {:host "localhost"}
   :field {:host "192.168.2.9"
           :port 8080}
   :shots-file-root "/Users/nick/Sites/shots"
   :shots-url-root "~nick/shots"
   :autoplay true
   :do-upload false})

(def CONFIG MACBOOK-LOCAL)
