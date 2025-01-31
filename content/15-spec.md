+++
title = "Spec"
weight = 15
+++

Extend `:dependencies` in `project.clj` to use `clojure.spec` (still alpha):

```clojure
:dependencies [[org.clojure/clojure "1.11.1"]
               [org.clojure/clojure "1.12.0"]]
```

Use `clojure.spec` from the REPL:

```clojure
(require '[clojure.spec.alpha :as s])
```

Write a _spec_ using a predicate function:

```clojure
(s/valid? number? 37) ; true
(s/valid? number? :foo) ; false
```

Combine predicates using _and_ logic:

```clojure
(def positive-number (s/and number? #(> % 0)))
(s/valid? positive-number 9) ; true
(s/valid? positive-number 0) ; false
```

Combine predicates using _or_ logic:

```clojure
(def number-or-string (s/or :numeric number? :stringy string?))
(s/valid? number-or-string 13) ; true
(s/valid? number-or-string "99") ; true
(s/valid? number-or-string :gotcha) ; false
```

The additional keyword preceding the predicate function is required for feedback.