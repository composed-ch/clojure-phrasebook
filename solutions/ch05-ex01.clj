(defn end-speed
  "Calculates the end-speed as v=v0+at."
  ([a t] (end-speed a t 0))
  ([a t v0] (+ v0 (* a t))))
