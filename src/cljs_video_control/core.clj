(ns cljs-video-control.core
  (:use compojure.core)
  (:require (compojure [route :as route]
                       [handler :as handler])
            [ring.util [response :as resp]]
            (ring.middleware [json :as json])
            (cljs-video-control [ajax :as ajax]
                                [pages :as pages]
                                [demo-pages :as dp])))

(defn json-test []
  (resp/response
   [{:A 1
     :B [2 3]}
    :C :D 55 6 []]))

(defn simple-logging-middleware [app]
  (fn [req]
    (doseq [[k v] req]
      (println (format "%20s: %s" k v)))
    (app req)))

(defroutes my-routes
  ;; HTML pages
  (GET "/" [] (pages/render-index))
  (GET "/demo-backbone" [] (dp/render-demo-backbone))
  (GET "/video-backbone" [] (dp/render-video-backbone))
  (GET "/video-framing" [] (dp/render-video-framing))
  (GET "/range-slider" [] (dp/render-range-slider))
  (GET "/search-template" [] (dp/search-template))
  (GET "/dragger" [] (dp/render-dragger))
  (GET "/sortable" [] (dp/render-sortable))
  (GET "/composite" [] (pages/render-composite))

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
