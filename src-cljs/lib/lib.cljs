(ns lib
  "Generic library.")

(defn JS>
  "Inline key-value args to Javascript map."
  [& args]
  (clj->js (apply hash-map args)))

(defn on-model-and-view
  "Given `f` (which takes a model and a view, then optional args passed in by backbone's
   event machinery), return a function which wraps up the view as `this`, so that
   %1 is model, %2 is view."
  [f]
  (fn [& e] (this-as view (apply f (.-model view) view e))))

(defn getter [model]
  (fn [key] (.get model (name key))))

(defn logger [heading obj]
  (.log js/console (str "[| " heading " :: " (.keys js/_ obj) " |]")))
