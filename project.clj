(defproject cljs-video-control "0.1.0-SNAPSHOT"
  :description "Example HTML5 video control via ClojureScript"
  :url "https://github.com/cassiel/cljs-video-control"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [jayq "2.3.0"]]
  :plugins [[lein-cljsbuild "0.3.2"]
            [lein-ring "0.8.5"]]

  :cljsbuild
  {:builds
   [{:source-paths ["src-cljs"]
     :compiler {:pretty-print true
                :output-to "resources/public/js/cljs.js"
                :optimizations :simple}}]}

  :ring {:port 3000
         :handler cljs-video-control.core/app})
