(defproject eu.cassiel/wmrd-becoming "0.1.0-SNAPSHOT"
  :description "Control front-end for Becoming"
  :url "https://github.com/cassiel/wmrd-becoming"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [jayq "2.3.0"]
                 [hiccup "1.0.4"]       ; Needed for nrepl.
                 [clj-yaml "0.4.0"]
                 [ring/ring-json "0.2.0"]
                 [clj-http "0.7.4"]]
  :plugins [[lein-cljsbuild "0.3.2"]
            [lein-ring "0.8.5"]]

  :resource-paths ["resources" "_dyn-resources"]

  :aliases {"go" ["ring" "server-headless"]
            "once" ["cljsbuild" "once"]
            "auto" ["cljsbuild" "auto"]}

  :cljsbuild
  {:crossovers [cassiel.wmrd-becoming.manifest
                cassiel.wmrd-becoming.style]
   :crossover-path "_crossover-cljs"

   :builds
   [{:source-paths ["src-cljs/lib" "src-cljs/main"]
     :compiler {:pretty-print true
                :output-to "_dyn-resources/public/js/main.js"
                :optimizations :simple}}]}

  :ring {:port 3000
         :handler cassiel.wmrd-becoming.core/app})
