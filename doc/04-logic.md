# Logic

Execute code conditionally:

```clojure
(def active true)
(if active
  (println "active")) ; "active"
```

Run alternative code if a condition fails:

```clojure
(def active false)
(if active
  (println "active")
  (println "inactive")) ; "inactive"
```

Use `if` as an expression:

```clojure
(def active true)
(if active
  "active"
  "inactive") ; "active"
```

An `if` expression with no else branch returns `nil` if the condition fails:

```clojure
(def active false)
(if active "active") ; nil
```

Check for equality and inequality:

```clojure
(= (+ 2 3) (+ 1 4)) ; true
(not= (+ 3 5) 99) ; true
```

Compare numbers:

```clojure
(> 5 3) ; true
(< 3 5) ; true
(<= 4 3) ; false
(>= 2 3) ; false
```

Unlike infix operators, prefix operators allow for many operands:

```clojure
(= 5 (+ 2 3) (+ 1 2 2) (- 8 1 1 1)) ; true
(< 1 4 17 99 128) ; true
(< 1 4 99 17 128) ; false
```

Use type predicate functions to check whether or not an expression is of a
certain type:

```clojure
(number? 13) ; true
(number? "foo") ; false
(string? "bar") ; true
(string? 42) ; false
(keyword? :foobar) ; true
(keyword? "foobar") ; false
(vector? [1 3 5 7]) ; true
(vector? #{1 3 5 7}) ; false
(map? {:name "Jim" :instrument "Guitar"}) ; true
(map? [:name "Jim" :instrument "Guitar"]) ; false
```

Use logical expressions to combine conditions:

```clojure
(defn leap-year? [year]
  (or (and (= (mod year 4) 0)
           (not= (mod year 100) 0))
      (= (mod year 400) 0)))

(leap-year? 1984) ; true
(leap-year? 1999) ; false
(leap-year? 2000) ; true
(leap-year? 2004) ; true
(leap-year? 2100) ; false
```

Only the values `false` and `nil` are _falsy_ and evaluate to `false` in a
condition. All other values, including the number zero, the empty string, and
empty collections are _truthy_ and evaluate to `true`.

Group expressions to blocks:

```clojure
(defn divide-with-rest [x y]
  (if (= (mod x y) 0)
    (do
      (println x "is divisible by" y)
      (/ x y))
    (do
      (println x "is not divisible by" y)
      [(quot x y) (mod x y)])))

(divide-with-rest 10 5)
;; 10 is divisible by 5
;; 2
(divide-with-rest 10 3)
;; 10 is not divisible by 3
;; [3 1]
```

Execute a block conditionally:

```clojure
(defn divide [x y]
  (when (not= y 0)
    (println "divisible")
    (/ x y)))

(divide 10 3)
;; divisible
;; 10/3

(divide 10 0)
;; nil
```

Handle multiple conditions:

```clojure
(defn fizzbuzz [n]
  (cond
    (= (mod n 15) 0) "FizzBuzz"
    (= (mod n 3) 0) "Fizz"
    (= (mod n 5) 0) "Buzz"
    :else (str n)))

(fizzbuzz 2) ; "2"
(fizzbuzz 6) ; "Fizz"
(fizzbuzz 10) ; "Buzz"
(fizzbuzz 30) ; "FizzBuzz"
```

Compare an expression against multiple values:

```clojure
(defn day-name [d]
  (case d
    0 "Sunday"
    1 "Monday"
    2 "Tuesday"
    3 "Wednesday"
    4 "Thursday"
    5 "Friday"
    6 "Saturday"
    "unknown"))

(day-name 0) ; "Sunday"
(day-name 3) ; "Wednesday"
(day-name 8) ; "unknown"
```

Catch an exception:

```clojure
(defn divide [x y]
  (try
    (/ x y)
    (catch ArithmeticException e (str "division failed" e))))

(divide 10 5) ; 2
(divide 10 0) ; "division failedjava.lang.ArithmeticException: Divide by zero"
```

Throw an exception:

```clojure
(defn safe-divide [x y]
  (if (= y 0)
    (throw
      (ex-info "illegal division" {:dividend x :divisor y}))
    (/ x y)))

(safe-divide 10 2) ; 5
(safe-divide 10 0)
;; execution error (ExceptionInfo) at user/safe-divide (tmp.clj:4).
;; illegal division
```

Exceptions thrown by `ex-info` can be caught as `clojure.llang.ExceptionInfo`:

```clojure
(try
  (safe-divide 3 0)
  (catch clojure.lang.ExceptionInfo e
    (println "panic!")))
```

## Exercises

- if
    - write a function `range-check [x a b]`
    - check if a number within a given range
    - return `x in [a;b]` or `x not in [a;b]` as string
    - either use single operator `(<= a x b)` or a logical operator `(and (>= x a) (<= x b))`
- when
    - write a function `safe-divide [x y]`
    - safe divide (only divide if not by zero)
    - otherwise `nil` is returned
- cond
    - quadratic formula: `quadratic [a b c]` returning `[x1 x2]`, `[x1]`, or `[]` depending on the determinant
    - helper function: `discriminant [a b c]`
- case
    - implement a state machine `next-editor-state [state action]`
    - actions
        - edit
        - save
    - states
        - clean unsaved
            - edit: dirty unsaved
            - save: clean saved
        - dirty unsaved
            - edit: [same]
            - save: clean saved
        - clean saved
            - edit: dirty saved
            - save: [same]
        - dirty saved
            - edit: [same]
            - save: clean saved
- try/catch/throw
    - write a function `parse-number [input]`
    - Integer/parseInt with NumberFormatException
    - throw own exception if number cannot be parsed
