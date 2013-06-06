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

  :resource-paths ["resources" "_dyn-resources"]

  :aliases {"go" ["ring" "server-headless"]
            "once" ["cljsbuild" "once"]
            "auto" ["cljsbuild" "auto"]}

  :cljsbuild
  {:builds
   [{:source-paths ["src-cljs/demo_backbone"]
     :compiler {:pretty-print true
                :output-to "_dyn-resources/public/js/demo_backbone.js"
                :optimizations :simple}}
    {:source-paths ["src-cljs/dragger"]
     :compiler {:pretty-print true
                :output-to "_dyn-resources/public/js/dragger.js"
                :optimizations :simple}}
    {:source-paths ["src-cljs/search_template"]
     :compiler {:pretty-print true
                :output-to "_dyn-resources/public/js/search_template.js"
                :optimizations :simple}}
    {:source-paths ["src-cljs/demo_yui"]
     :compiler {:pretty-print true
                :output-to "_dyn-resources/public/js/demo_yui.js"
                :optimizations :simple}}
    {:source-paths ["src-cljs/video_backbone"]
     :compiler {:pretty-print true
                :output-to "_dyn-resources/public/js/video_backbone.js"
                :optimizations :simple}}
    {:source-paths ["src-cljs/video_framing"]
     :compiler {:pretty-print true
                :output-to "_dyn-resources/public/js/video_framing.js"
                :optimizations :simple}}]}

  :ring {:port 3000
         :handler cljs-video-control.core/app})
