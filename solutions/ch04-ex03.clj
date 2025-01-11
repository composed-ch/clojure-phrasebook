(defn discriminant [a b c]
  (- (Math/pow b 2) (* 4 a c)))

(defn quadratic [a b c]
  (cond
    (> (discriminant a b c) 0)
    [(/ (+ (- b) (Math/sqrt (discriminant a b c))) (* 2 a))
     (/ (- (- b) (Math/sqrt (discriminant a b c))) (* 2 a))]
    (< (discriminant a b c) 0) []
    :else [(/ (- b) (* 2 a))]))
