+++
title = "More Capable Functions"
weight = 5
+++

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

## Exercises

### Acceleration and Speed

Write a multi-arity function `end-speed` to compute the speed given an
acceleration `a` in `m/s²`, a time indication `t` in `s`, and—optionally—a
starting speed `v0` in `m/s`. Document the function with the formula in a
docstring.

Hint: Use the formula `v=v0+at`. The lower-arity function shall call the
higher-arity function.

Test: `(end-speed 2 10)` shall return `20`, and `(end-speed 2 10 7.5)` shall
return `27.5`.

### Property Summation

Given the following maps of personal financial data:

```clojure
(def jack {:income 4500 :balance 12942 :debt 120000})
(def jill {:income 3200 :balance 19172 :debt 250000})
(def jane {:income 4900 :balance 10342 :debt 100000})
```

Write a recursive function `(sum-by [prop & args])` that sums up the given
property `prop` in the provided data. Document the function using a docstring.

Hint: Return 0 if the property isn't found in the data, or of no arguments are
provided. Use `apply` for the recursive call.

Test: `(sum-by :income jack jill)` shall return `7700`, and `(sum-by :debt jack
jill jane)` shall return `470000`.

### Bonus Calculation

Given the following payroll data:

```clojure
(def dilbert {:salary 120000 :position "Engineer"})
(def wally {:salary 90000 :position "Engineer"})
(def topper {:salary 150000 :position "Sales" :revenue 2750000})
(def ashok {:salary 36000 :position "Intern"})
(def boss {:salary 500000 :position "Manager"})
```

Write a multimethod `bonus` that calculates each employee's bonus based on the
following rules:

1. Engineers earning more than `100000` get a bonus of 10% of their salary.
   Engineers earning up to `100000` get a bonus of 15% of their salary.
2. Sales people get a bonus of 10% of their salary and 1% of their revenue.
3. Interns get a fixed bonus of `2000`.
4. All other employees get a bonus of `0`.

Hint: The `dispatch` function shall differentiate between low- and high-earning
engineers. Do not explicitly return `:default` from the dispatch method for all
other employees.

Test: `(bonus dilbert)` shall return `12000.0`, `(bonus wally)` shall return
`13500.0`, `(bonus topper)` shall return `42500.0`, and `(bonus ashok)` shall
return `2000.0`

### Fibonacci Numbers

Write a tail-recursive function `(fib [n])` that computes the nth Fibonacci
number.

Hint: The nth Fibonacci number is defined as the sum of its two predecessors.
The first two Fibonacci numbers are `1` and `1`. Use `loop` and `recur` to
implement the function.

Test: `(fib 0)` and `(fib 1)` shall return `1`, `(fib 10)` shall return `89`,
and `(fib 45)` shall return `1836311903`—and finish within within milliseconds
(use `(time (fib 45))` to check).

### Blob Eats Blob

Given the following data:

```clojure
(def red {:weight 80 :strength 50})
(def blue {:weight 90 :strength 40})
(def green {:weight 70 :strength 35})
(def black {:weight 0 :strength 0})
```

Write a function `(merge-blobs [a b])` which simulates a fight between the two
blobs `a` and `b`. The blob with the higher strength eats up the blog with the
lower strength, thereby gaining the defeated blob's weight and 10% of its
strength. Test for the following conditions:

- Preconditions: Both blobs have a positive weight and strength.
- Postcondition: The returned weight is higher than the weight of any given
  blob.

Hint: Use `:pre` and `:post` to enforce the conditions.

Test: `(merge-blobs red blue)` shall return `{:weight 170 :strength 54}`, and
`(merge-blobs green black)` shall throw an assertion error.
