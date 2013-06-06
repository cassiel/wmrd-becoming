(defn foo [& args] (apply hash-map args))

(foo :A 3 :B 4)

(name :A)
(name "A")
