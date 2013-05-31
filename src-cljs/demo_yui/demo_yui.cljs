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
        MODEL (-> (.-Base Y)
                  (.create "pieModel"
                           (.-Model Y)
                           []
                           (JS> :allGone all-gone
                                :eatSlice eat-slice)
                           (JS> :ATTRS {:slices {:value 6}
                                        :type {:value "apple"}})))

        pecanPie (new MODEL (JS> :type "pecan"))

        VIEW (-> (.-Base Y)
                 (.create "pieView"
                          (.-View Y)
                          []

                          (JS> :events {".eat" {:click "eatSlice"}}
                               :template (str "{slices} slice(s) of {type} pie remaining. "
                                              "<button class='eat'>Eat a Slice!</button>")

                               :initializer
                               (fn []
                                 (this-as me (let [model (.get me "model")]
                                               (.after model "change" (.-render me) me)
                                               (.after model "destroy" (.-destroy me) me))))

                               :render
                               (fn []
                                 (this-as me
                                          (let [container (.get me "xcontainer")
                                                _1 (.log Y container)
                                                para (.one container "#my-para")
                                                _2 (.log Y (str "para=" para))
                                                html "<em>Bog!</em>"
                                                ]
                                            (.setHTML para html)

                                            me)))

                               :eatSlice
                               (fn [e] (this-as me (.eatSlice (.get me "model")))))

                          (JS> :ATTRS
                               {:xcontainer {:value (.one Y "#myapp-app")}})))

        pieView (new VIEW (JS> :model pecanPie))]

    (.render pieView)

    (.on pecanPie "slicesChange" (fn [e] (.log Y "slicesChange")))
    (.on pecanPie "error" (fn [e] (.log Y (.error e))))
    (.eatSlice pecanPie)
    (.log Y (.get pecanPie "slices"))))

(.use (js/YUI) "model" "view" main)
