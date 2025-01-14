(def fr {:name "France" :population 68373433 :hdi 0.91 :gdp 4.359e12})
(def it {:name "Italy" :population 58968501 :hdi 0.906 :gdp 2.376e12})
(def ng {:name "Nigeria" :population 230842743 :hdi 0.548 :gdp 0.252738e12})
(def bd {:name "Bangladesh" :population 174655977 :hdi 0.670 :gdp 1.801e12})

(defn wealthy? [country]
  (>= (/ (:gdp country) (:population country)) 25000))

(defn populous? [country]
  (>= (:population country) 100000000))

(defn developed? [country]
  (>= (:hdi country) 0.75))

(def first-world? (every-pred wealthy? (complement populous?) developed?))

