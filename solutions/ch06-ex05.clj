(def pliers {:price 7.55 :stock 23 :value 173.65 :revenue 0.0})
(def hammers {:price 3.95 :stock 10 :value 39.50 :revenue 0.0})
(def nails {:price 0.05 :stock 1974 :value 98.70 :revenue 0.0})

(defn sell [quantity item]
  (update (update (update item
                          :stock
                          #(- % quantity))
                  :value
                  #(- % (* quantity (:price item))))
          :revenue
          #(+ % (* quantity (:price item)))))

