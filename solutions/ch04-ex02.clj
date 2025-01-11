(defn pythagorean-triplet [a b c]
  (if (= (+ (Math/pow a 2) (Math/pow b 2)) (Math/pow c 2))
    (str a "²+" b "²=" c "²")
    (str a "²+" b "²≠" c "²")))

