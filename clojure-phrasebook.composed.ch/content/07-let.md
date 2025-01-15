+++
title = "Let"
weight = 7
+++

Use _bindings_ to give a name to local variables:

```clojure
(defn total-costs [unit-price quantity]
  (let [good-costs (* unit-price quantity)
        freight-costs 8.5]
    (if (< good-costs 50)
      (+ good-costs freight-costs)
      good-costs)))

(total-costs 9.99 5) ; 58.45
(total-costs 10.99 5) ; 54.95
```

Earlier bindings are available to compute later bindings:

```clojure
(defn total-costs [unit-price quantity]
  (let [good-costs (* unit-price quantity)
        freight-costs 8.5
        discount (* good-costs 0.01)
        discounted-costs (- good-costs discount)]
    (if (< discounted-costs 50)
      (+ discounted-costs freight-costs)
      discounted-costs)))

(total-costs 10.0 5) ; 58.0
(total-costs 10.0 6) ; 59.4
```

Bindings are available in functions returned from `let`:

```clojure
(def interest-rates {:alice 2.5 :bob 1.5})
(defn interest-func [lender]
  (let [default-rate 5.0
        lender-rate (get interest-rates lender)
        actual-rate (if (nil? lender-rate) default-rate lender-rate)]
    (fn [amount]
      (* amount (/ actual-rate 100)))))

(def alice-interest (interest-func :alice))
(def bob-interest (interest-func :bob))
(def shlomo-interest (interest-func :shlomo))

(bob-interest 10000) ; 150.0
(alice-interest 10000) ; 250.0
(shlomo-interest 10000) ; 500.0
```

Use conditional bindings with `if-let`:

```clojure
(def credit {:alice 5.15 :bob 2.95})
(defn checkout [total customer]
  (if-let [deduction (get credit customer)]
    (- total deduction)
    total))

(checkout 100 :alice) ; 94.85
(checkout 100 :bob) ; 97.05
(checkout 100 :joe) ; 100
```

The binding is only available if th expression is truthy.

If returning `nil` in the alternative case is sufficient, use `when-let`:

```clojure
(def gifts {:alice "purse" :bob "hat"})
(defn handout [customer]
  (when-let [gift (get gifts customer)]
    (str "You get a " gift ".")))

(handout :alice) ; "You get a purse."
(handout :bob) ; "You get a hat."
(handout :chaim) ; nil
```

## Exercises

### Temporary Values

Rewrite the solution for exercise _Map Update Function_ from the last chapter
with `let` bindings so that the code becomes more readable.

Hint: Bind the result of each `update` operation to a new variable.

Test: Same as in original exercise.

{{% expand title="Solution" %}}
```clojure
(def pliers {:price 7.55 :stock 23 :value 173.65 :revenue 0.0})
(def hammers {:price 3.95 :stock 10 :value 39.50 :revenue 0.0})
(def nails {:price 0.05 :stock 1974 :value 98.70 :revenue 0.0})

(defn sell [quantity item]
  (let [value (* quantity (:price item))
        reduced-stock (update item :stock #(- % quantity))
        reduced-value (update reduced-stock :value #(- % value))
        increased-revenue (update reduced-value :revenue #(+ % value))]
    increased-revenue))
```
{{% /expand %}}

### Formatting Durations

Write a function `format-duration` that, given a duration in seconds, returns a
string of the following format: `XhYmZs`, with `X` indicating hours, `Y`
indicating minutes, and `Z` indicating seconds.

Hint: Use `quot` and `mod` for the calculation and `let` for bindings. Omit any
indications equal to `0` (e.g. `5m13s` or `1h30m` instead of `0h5m13s` or
`1h30m0s`, respectively).

Test: `(format-duration 4321)` shall return `1h12m1s`, `(format-duration 3600)`
shall return `1h`, and `(format-duration 62)` shall return `1m2s`.

{{% expand title="Solution" %}}
```clojure
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
```
{{% /expand %}}

### Arcminutes and Arcseconds

Write a function `print-angle` that expects a decimal angle in degrees, e.g.
`32.8451` and converts the decimal portion to arcminutes and arcseconds (i.e.
`1/60` and `1/36000` parts of a whole degree). Print the angle formatted as
`x°y'z"`.

Hint: Consider the hints from the last exercise.

Test: `(print-angle 32.8451)` shall print `32°50'42"`.

{{% expand title="Solution" %}}
```clojure
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
```
{{% /expand %}}
