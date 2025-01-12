# More Capable Functions

Create a _multi-arity_ function:

```clojure
(defn interest
  ([balance rate] (* balance (/ rate 100)))
  ([balance rate days] (* balance (/ rate 100) (/ days 360))))

(interest 17543.55 1.25) ; 219.294375
(interest 17543.55 1.25 180) ; 109.6471875
```

It is common for lower-arity functions to call higher-arity functions by filling
in arguments with default values to share an implementation:

```clojure
(defn interest
  ([balance rate] (interest balance rate 360))
  ([balance rate days] (* balance (/ rate 100) (/ days 360))))

(interest 9456.93 0.75) ; 70.926975
(interest 9456.93 0.75 180) ; 35.4634875
```

Write a _variadic function_ by accepting a variable number of arguments:

```clojure
(defn pizza-price [basic & extras]
  (+ basic (count extras)))

(pizza-price 17) ; 17
(pizza-price 17 "Salami") ; 18
(pizza-price 17 "Salami" "Extra Cheese" "Olives") ; 20
```

Write a _multimethod_ that dispatches the function call to its proper
implementation depending on the arguments provided:

```clojure
(defn dispatch [shape]
  (cond
    (and (contains? shape :height) (contains? shape :width)) :rectangle
    (contains? shape :side) :square
    (contains? shape :radius) :circle))

(defmulti area dispatch)

(defmethod area :rectangle [r]
  (* (:height r) (:width r)))

(defmethod area :square [s]
  (* (:side s) (:side s)))

(defmethod area :circle [c]
  (* (Math/pow (:radius c) 2) Math/PI))

(defmethod area :default [x]
  (ex-info "unknown shape" {:shape x}))

(area {:width 3 :height 4}) ; 12
(area {:side 3}) ; 9
(area {:radius 5}) ; 78.53981633974483
(area {:h 7 :a 10 :c 4}) ; Exception
```

An argument not accounted for in the `dispatch` function is resolved to
`:default`.

Write a recursive function:

```clojure
(defn sum [xs]
  (if (empty? xs)
    0
    (+ (first xs) (sum (rest xs)))))

(sum []) ; 0
(sum [1 2 3]) ; 6
(sum [1 2 3 4 5]) ; 15
```

Write a _tail-recursive_ function:

```clojure
(defn sum
  ([xs] (sum xs 0))
  ([xs acc]
   (if (empty? xs)
     acc
     (sum (rest xs) (+ (first xs) acc)))))
```

Enable _tail-call optimization_ by replacing the recursive function call with
`recur`:

```clojure
(defn sum
  ([xs] (sum xs 0))
  ([xs acc]
   (if (empty? xs)
     acc
     (recur (rest xs) (+ (first xs) acc))))) ; NOTE: recur instead of sum
```

Refactor tail recursion as a _loop_:

```clojure
(defn sum [xs]
  (loop [xs xs
         acc 0]
    (if (empty? xs)
      acc
      (recur (rest xs) (+ (first xs) acc)))))
```

Document values and function using a _docstring_:

```clojure
(def VAT "value added tax rate in percent" 8.1)

(defn hypot
  "Calculates the hypothenuse given the rectangular triangle's legs a and b."
  [a b]
  (Math/sqrt (+ (Math/pow a 2) (Math/pow b 2))))

(hypot 3 4) ; 5.0
```

Retrieve a docstring programmatically:

```clojure
(doc VAT) ; value added tax rate in percent

(doc hypot) 
;; Calculates the hypothenuse given the rectangular triangle's legs a and b.
```

Provide validations of arguments and the return value using _pre_ and _post
conditions_, respectively:

```clojure
(defn hypot [a b]
  {:pre [(> a 0) (> b 0)]
   :post [(> % a) (> % b)]}
  (Math/sqrt (+ (Math/pow a 2) (Math/pow b 2))))

(hypot 3 4) ; 5.0
(hypot 3 0) ; Assert failed: (> b 0)
```

The return value is available as `%` in the post condition.

