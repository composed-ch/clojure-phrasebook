(defn format-duration [duration]
  (let [hours (quot duration 3600)
        duration (mod duration 3600)
        minutes (quot duration 60)
        duration (mod duration 60)
        seconds duration
        result (if (> hours 0) (str hours "h") "")
        result (str result (if (> minutes 0) (str minutes "m") ""))
        result (str result (if (> seconds 0) (str seconds "s") ""))]
    result))
