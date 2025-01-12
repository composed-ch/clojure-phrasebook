(def dilbert {:salary 120000 :position "Engineer"})
(def wally {:salary 90000 :position "Engineer"})
(def topper {:salary 150000 :position "Sales" :revenue 2750000})
(def ashok {:salary 36000 :position "Intern"})
(def boss {:salary 500000 :position "Manager"})

(defn dispatch [employee]
  (cond
    (= "Engineer" (:position employee))
    (if (> (:salary employee) 100000) :engineer-high :engineer-low)
    (= "Sales" (:position employee)) :sales
    (= "Intern" (:position employee)) :intern))

(defmulti bonus dispatch)

(defmethod bonus :engineer-high [employee]
  (* 0.1 (:salary employee)))

(defmethod bonus :engineer-low [employee]
  (* 0.15 (:salary employee)))

(defmethod bonus :sales [employee]
  (+ (* 0.1 (:salary employee))
     (* 0.01 (:revenue employee))))

(defmethod bonus :intern [employee] 2000.0)

(defmethod bonus :default [_] 0.0)
