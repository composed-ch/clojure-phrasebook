(defn fib [n]
  (loop [a 1
         b 1
         i n]
    (if (= i 0)
      a
      (recur b (+ a b) (- i 1)))))
