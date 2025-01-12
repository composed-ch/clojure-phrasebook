(def red {:weight 80 :strength 50})
(def blue {:weight 90 :strength 40})
(def green {:weight 70 :strength 35})
(def black {:weight 0 :strength 0})

(defn merge-blobs [a b]
  {:pre [(> (:weight a) 0)
         (> (:weight b) 0)
         (> (:strength a) 0)
         (> (:strength b) 0)]
   :post [(> (:weight %) (:weight a))
          (> (:weight %) (:weight b))]}
  (if (> (:strength a) (:strength b))
    {:weight (+ (:weight a) (:weight b))
     :strength (+ (:strength a) (* 0.1 (:strength b)))}
    {:weight (+ (:weight a) (:weight b))
     :strength (+ (* 0.1 (:strength b)) (:strength a))}))


