(def jack {:income 4500 :balance 12942 :debt 120000})
(def jill {:income 3200 :balance 19172 :debt 250000})
(def jane {:income 4900 :balance 10342 :debt 100000})

(defn sum-by
  "Sums up the given property in args provided."
  [prop & args]
  (cond
    (empty? args) 0
    (not (contains? (first args) prop)) 0
    :else (+ (get (first args) prop) (apply sum-by prop (rest args)))))
