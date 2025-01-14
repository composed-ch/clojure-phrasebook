(defn transform [x trans]
  (if (empty? trans)
    x
    (transform ((first trans) x) (rest trans))))
