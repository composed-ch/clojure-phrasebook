(defn print-angle [degrees]
  (let [hours (quot degrees 1)
        decimals1 (mod degrees 1)
        minutes (quot decimals1 (/ 1 60))
        decimals2 (mod decimals1 (/ 1 60))
        seconds (quot decimals2 (/ 1 3600))
        result (if (> hours 0) (str (int hours) "°") "")
        result (str result (if (> minutes 0) (str (int minutes) "'") ""))
        result (str result (if (> seconds 0) (str (int seconds) "\"") ""))]
    (println result)))
