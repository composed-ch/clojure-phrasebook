(defn min-sec [sec]
  (str (quot sec 60) "m"
       (mod sec 60) "s"))
