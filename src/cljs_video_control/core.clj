(ns cljs-video-control.core
  (:use compojure.core)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            (ring.middleware [json :as json])
            (cljs-video-control [ajax :as ajax]
                                [pages :as pages])))

(defn json-test []
  {:body
   [{:A 1
     :B [2 3]}
    :C :D 55 6 []]})

(defn simple-logging-middleware [app]
  (fn [req]
    (doseq [[k v] req]
      (println (format "%20s: %s" k v)))
    (app req)))

(defroutes my-routes
  ;; HTML pages
  (GET "/" [] (pages/render-index))
  (GET "/demo-backbone" [] (pages/render-demo-backbone))
  (GET "/video-backbone" [] (pages/render-video-backbone))
  (GET "/video-framing" [] (pages/render-video-framing))
  (GET "/range-slider" [] (pages/render-range-slider))
  (GET "/search-template" [] (pages/search-template))
  (GET "/dragger" [] (pages/render-dragger))
  (GET "/sortable" [] (pages/render-sortable))

  ;; JSON testing:
  (GET "/json-test" [] (json-test))

  ;; Model interaction:
  (GET "/store" [] (ajax/get-store))
  (POST "/active" {p :params} (ajax/post-active p))

  ;; Misc:
  (route/resources "/" {:root "public"})
  (route/not-found "page not found"))

(def app
  (-> (handler/site my-routes)
      (json/wrap-json-params)
      (json/wrap-json-response)
      (simple-logging-middleware)))
