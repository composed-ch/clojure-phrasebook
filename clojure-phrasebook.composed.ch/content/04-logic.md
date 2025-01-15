+++
title = "Logic"
weight = 4
+++

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
(divide 10 0)
;; "division failedjava.lang.ArithmeticException: Divide by zero"
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

### Range Check

Write a function `in-range?` that returns `true` when the argument `x` is within
the interval of parameters `a` (lower limit) and `b` (upper limit), and `false`
otherwise.

Hint: Use two conditions in conjunction—or a comparison operator using three
operands.

Test: `(in-range? 3 1 5)` shall return `true`, `(in-range 17 1 10)` shall return
`false`.

### Pythagorean Triplet

Write a function `pythagorean-triplet` that accepts three numbers `a`, `b`, and
`c`. If the condition `a²+b²=c²` holds true for the arguments provided, the
string `a²+b²=c²` shall be returned, with `a`, `b`, and `c` replaced by their
actual values. If the condition does not hold, the string `a²+b²≠c²` shall be
returned with the according replacements.

Hint: Use `Math/pow` to compute the exponents, and `str` to concatenate the
return value.

Test: `(pythagorean-triplet 3 4 5)` shall return `3²+4²=5²`, and
`(pythagorean-triplet 1 2 3)` shall return `1²+2²≠3²`.

### Quadratic Formula

Write a function `quadratic` that accepts parameters `a`, `b`, and `c`, and
returns the solutions to equations of the form `ax²+bx+c=0` using the quadratic
formula. The function shall return a vector of zero, one, or two elements.

Hint: Use `cond` and write a helper function `discriminant`. To turn `x` into a
negative number, write `(- x)`. Use `:else` instead of a floating point
comparison against `0.0`.

Test: `(quadratic 1 -4 3)` shall return `[3.0 1.0]`, `(quadratic 1 2 1)` shall
return `[-1]`, and `(quadratic 1 4 5)` shall return `[]`.

### State Machine

Write a function `next-editor-state` that accepts two parameters `state`
(`:clean-unsaved`, `:dirty-unsaved`, `:clean-saved`, `:dirty-saved`) and
`action` (`:edit`, `:save`), and returns the new state based on the following
transitions:

- A _clean unsaved_ state becomes _dirty unsaved_ when edited and _clean saved_
  when saved.
- A _dirty unsaved_ state remains when edited and becomes _clean saved_ when
  saved.
- A _clean saved_ state becomes _dirty saved_ when edited and remains when
  saved.
- A _dirty saved_ state remains when edited and becomes _clean saved_ when
  saved.

Hint: Use nested `case` checks.

Test: `(next-editor-state :dirty-unsaved :save)` returns `:clean-saved`, etc.

### Number Parsing

Write a function `parse-number` that accepts a string parameter `input` which
shall be parsed as an integer to be returned.

Hint: Use `Integer/parseInt` to parse the number. Catch a possible
`NumberFormatException`, in which case a new exception is to be thrown using
`ex-info`.

Test: `(parse-number "13")` shall return 13, and `(parse-number "foo")` shall
throw an exception.
