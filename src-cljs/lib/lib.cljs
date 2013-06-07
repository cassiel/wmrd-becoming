(ns lib
  "Generic library.")

(defn JS>
  "Inline key-value args to Javascript map."
  [& args]
  (clj->js (apply hash-map args)))

(defn on-model-and-view
  "Given `f` (which takes a model and a view, then optional extra args), return a function
   which wraps up the view as `this`."
  [f & args]
  #(this-as view (apply f (.-model view) view args)))
