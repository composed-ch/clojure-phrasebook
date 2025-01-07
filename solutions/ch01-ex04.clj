(defn degree [rad]
  (/ (* 180 rad) Math/PI))

(defn arc-min-sec [rad]
  (str (int (degree rad)) "°"
       (int (quot (mod (degree rad) 1) (/ 1 60))) "'"))
