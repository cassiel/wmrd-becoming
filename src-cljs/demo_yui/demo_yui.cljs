(ns demo-yui
  "Demo using YUI. We'll try to assemble a complete Model/View here.")

(defn JS>
  "Inline key-value args to Javascript map."
  [& args]
  (clj->js (apply hash-map args)))

(defn main [Y]
  (let [all-gone (fn []
                   (this-as me (zero? (.get me "slices"))))
        eat-slice (fn []
                    (this-as me
                             (if (.allGone me)
                               (.fire me "error"
                                      (JS> :type "eat"
                                           :error "There isn't any pie left."))
                               (do
                                 (.set me "slices" (dec (.get me "slices")))
                                 (.log Y (str "You just ate some " (.get me "type") " pie."))))))
        model (-> (.-Base Y)
                  (.create "pieModel"
                           (.-Model Y)
                           []
                           (JS> :allGone all-gone
                                :eatSlice eat-slice)
                           (JS> :ATTRS {:slices {:value 6}
                                        :type {:value "apple"}})))

        pecanPie (new model (JS> :type "pecan"))]
    (.on pecanPie "error" (fn [e] (.log Y (.error e))))
    (.eatSlice pecanPie)
    (.log Y (.get pecanPie "slices"))))

(.use (js/YUI) "model" main)
