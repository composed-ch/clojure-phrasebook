(def guitarists #{"Dave" "Adrian" "Janick"})
(def song {:title "Paschendale" :duration "8m28s"})
(disj (:solos (assoc song :solos guitarists)) "Janick")

