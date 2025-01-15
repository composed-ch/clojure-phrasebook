# Let

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

### Formatting Durations

Write a function `format-duration` that, given a duration in seconds, returns a
string of the following format: `XhYmZs`, with `X` indicating hours, `Y`
indicating minutes, and `Z` indicating seconds.

Hint: Use `quot` and `mod` for the calculation and `let` for bindings. Omit any
indications equal to `0` (e.g. `5m13s` or `1h30m` instead of `0h5m13s` or
`1h30m0s`, respectively).

Test: `(format-duration 4321)` shall return `1h12m1s`, `(format-duration 3600)`
shall return `1h`, and `(format-duration 62)` shall return `1m2s`.
