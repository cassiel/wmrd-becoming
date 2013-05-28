(ns video
  (:use [jayq.core :only [$]])
  (:require [jayq.core :as jq]))

; ALERT ON CLICK
; Rewrite of http://backbonejs.org/#Events
(def o {})

(.extend js/_ o Backbone.Events)

(.on o "alert"
     (fn [msg] (js/alert msg)))

(.on o "play"
     (fn [] (.play (.-_video js/document))))

(jq/bind ($ "#bash")
         :click
         (fn [e] (.trigger o "play")))
