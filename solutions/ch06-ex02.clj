(def fr {:name "France" :population 68373433 :hdi 0.91 :gdp 4.359e12})
(def it {:name "Italy" :population 58968501 :hdi 0.906 :gdp 2.376e12})
(def ng {:name "Nigeria" :population 230842743 :hdi 0.548 :gdp 0.252738e12})
(def bd {:name "Bangladesh" :population 174655977 :hdi 0.670 :gdp 1.801e12})

(defn wealthy? [threshold country]
  (>= (/ (:gdp country) (:population country)) threshold))

(defn populous? [threshold country]
  (>= (:population country) threshold))

(defn developed? [threshold country]
  (>= (:hdi country) threshold))

(def first-world? (every-pred (partial wealthy? 25000)
                              (complement (partial populous? 100000000))
                              (partial developed? 0.75)))

