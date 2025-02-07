+++
title = "State"
weight = 18
+++

Use an _atom_ to advance state in a thread-safe manner:

```clojure
(def counter (atom 0))

(defn update [step]
  (swap! counter #(+ % step))
  (deref counter))

(update 3) ; 3
(update 5) ; 8

(deref counter) ; 8
@counter ; 8
```

The function passed to `swap!` shall be referentially transparent (or
"pure"), because it is re-evaluated if the atom's underlying value
changed between reading and updating it.

Use an _agent_ if the atomic update is accompanied by a side-effect
that only shall be executed once:

```clojure
(def counter (agent 0))

(defn update [step]
  (send
   counter
   (fn [c]
     (println "update counter from" c "by" step)
     (+ c step)))
  (deref counter))

(update 3)
;; update counter from 0 by 3
;; 0

(update 5)
;; update counter from 3 by 5
;; 3

@counter ; 8
```

The function passed to `send` is executed asynchronuously, and the
value is only updated later.

Use `ref` to advance the state of multiple items in a single
transaction:

```clojure
(def consumed (ref []))
(def spent (ref 0.0))

(defn consume [product price]
  (dosync
   (alter consumed #(conj % product))
   (alter spent #(+ % price))))

(consume "beer" 5.25) ; 5.25
(consume "cheese" 7.95) ; 13.2
@consumed ; ["beer" "cheese"]
@spent ; 13.2
```

How to pick between a var, an atom, an agent, and a ref?

- Use vars for values that do not change.
- Use atoms when advancing state using an update function without any side-effect.
- Use agents when the update function involves side-effects.
- Use refs when updating multiple values together in a transaction.

Before using refs, consider aggregating the values to a map wrapped by an atom.

## Exercises

### Memoized Fibonacci

Define a var `fib-cache` that holds and atom to a map. Write a
function `fib-mem` that expects a parameter `n`. The function shall
compute the nth Fibonacci number, but cache the results in
`fib-cache`.

Hint: Only compute the result if it's not to be found in the
cache. After calculating the result, update `fib-cache` using `swap!`.

Test: `(fib-mem 60)` shall return `2504730781961` and finish within
reasonable time, i.e. `(time (fib-mem 60))` shall finish within the
magnitude of 100 milliseconds.