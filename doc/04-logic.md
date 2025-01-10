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

```clojure
```

```clojure
```

```clojure
```

```clojure
```

```clojure
```

```clojure
```

```clojure
```

```clojure
```

```clojure
```

```clojure
```

```clojure
```

```clojure
```

```clojure
```

```clojure
```

```clojure
```

```clojure
```

```clojure
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
