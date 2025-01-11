;; Solution using a conjunction of two conditions.
(defn in-range? [x a b]
  (and (>= x a) (<= x b)))

;; Solution using an operator with three operands.
(defn in-range? [x a b]
  (<= a x b))
