(ns cassiel.wmrd-becoming.core
  (:use compojure.core)
  (:require (compojure [route :as route]
                       [handler :as handler])
            [ring.util [response :as resp]]
            (ring.middleware [json :as json])
            (cassiel.wmrd-becoming [ajax :as ajax]
                                   [pages :as pages])))

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
  (GET "/" [] (pages/render-main))

  ;; JSON testing:
  (GET "/json-test" [] (json-test))

  ;; Model interaction:
  (GET "/store" [] (ajax/get-store))
  (GET "/clips" [] (ajax/get-clips))
  (POST "/upload" {p :params} (ajax/upload p))
  (POST "/deuse" {p :params} (ajax/deuse p))
  (POST "/config" {p :params} (ajax/config p))
  (POST "/mode" {p :params} (ajax/mode p))
  (POST "/active" {p :params} (ajax/post-active p))

  ;; Misc:
  (route/resources "/" {:root "public"})
  (route/not-found "page not found"))

(def app
  (-> (handler/site my-routes)
      (json/wrap-json-params)
      (json/wrap-json-response)
      (simple-logging-middleware)))
