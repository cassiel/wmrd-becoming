(ns cassiel.wmrd-becoming.manifest)

(def MACBOOK-LOCAL
  {:server {:host "localhost"
            :port 3000}
   :assets {:host "localhost"}
   :field {:host "localhost"
           :port 8080}
   ;; File root is the parent of the URL root (since we need `shotList.txt` server-side).
   :shots-file-root "/Users/nick/Sites"
   :shots-url-root "~nick/shots"
   :autoplay true
   :do-upload false})

(def MACBOOK-LINKSYS
  {:server {:host "localhost"
            :port 3000}
   :assets {:host "localhost"}
   :field {:host "becoming1.lan"
           :port 8080}
   ;; File root is the parent of the URL root (since we need `shotList.txt` server-side).
   :shots-file-root "/Users/nick/Sites"
   :shots-url-root "~nick/shots"
   :autoplay true
   :do-upload false})

(def MACBOOK-SERVER
  {:server {:host "sultanahmet.lan"
            :port 3000}
   :assets {:host "sultanahmet.lan"}
   :field {:host "192.168.2.9"
           :port 8080}
   :shots-file-root "/Users/nick/Sites"
   :shots-url-root "~nick/shots"
   :autoplay true
   :do-upload false})

(def BECOMING1-LOCAL
  {:server {:host "localhost"
            :port 3000}
   :assets {:host "localhost"}
   :field {:host "192.168.2.9"
           :port 8080}
   :shots-file-root "/home/nick/public_html"
   :shots-url-root "~nick/shots"
   :autoplay true
   :do-upload false})

(def BECOMING1-LOCAL-LINUX
  {:server {:host "localhost"
            :port 3000}
   :assets {:host "localhost"}
   :field {:host "becoming1.lan"
           :port 8080}
   :shots-file-root "/home/nick/public_html"
   :shots-url-root "~nick/shots"
   :autoplay true
   :do-upload false})

(def BECOMING1
  {:server {:host "becoming1.lan"
            :port 3000}
   :assets {:host "becoming1.lan"}
   :field {:host "becoming1.lan"
           :port 8080}
   :shots-file-root "/home/nick/public_html"
   :shots-url-root "~nick/shots"
   :autoplay true
   :do-upload true})

(def CONFIG BECOMING1)
