+++
title = "Macros"
weight = 20
+++

> [!IMPORTANT]
>
> Macros extend the language. Only use them if the problem cannot be solved
> with other language constructs.

Considering repetitive code for a three-start rating system:

```clojure
(defn display [stars]
  (case stars
    3 "***"
    2 "**"
    1 "*"
    ""))

(defn verbalize [stars]
  (case stars
    3 (println "good")
    2 (println "mediocre")
    1 (println "bad")
    (println "unknown")))
```

Write a macro using _syntax quoting_ to insert junks code into a template:

```clojure
(defmacro three-star-rating [stars three two one unknown]
  `(case ~stars
     3 ~three
     2 ~two
     1 ~one
     ~unknown))
```

The `` ` `` (backtick) quotes a template. The `~` character inserts the following identifier.

Rewrite the repetitive code using the macro:

```clojure
(defn display [stars]
  (three-star-rating stars "***" "**" "*" ""))

(defn verbalize [stars]
  (three-star-rating
   stars
   (println "good")
   (println "mediocre")
   (println "bad")
   (println "unknown")))
```

Expand a macro to inspect the generated code:

```clojure
(macroexpand-1 '(three-star-rating 2 :yay :meh :nah :pff))
;; (clojure.core/case 2 3 :yay 2 :meh 1 :nah :pff)
```

Notice that `macroexpand-1` expands macros _once_. The `macroexpand`
function expands the code recursively until all macros are expanded,
including (`case`, `cond`, etc.).

## Exercises

### Unless

Write a macro `unless` that accepts both a condition and some code,
and, unlike `when`, only executes the code if the condition does _not_
hold true.

Hint: Define the macro in terms of `when` and `not`.

Test: `(unless (> 1 0) "math is not broken")` shall return `nil`, and
`(unless (> 0 1) "math is ok")` shall return `"math is ok"`.

{{% expand title="Solution" %}}
```clojure
(defmacro unless [cond code]
  `(when (not ~cond)
     ~code))
```
{{% /expand %}}

### Arithmetic If

Write a macro `arithmetic-if` that expects an integer `n`, and three
code parameters `pos`, `zero`, and `neg`, which are to be executed if
`n` is positive, zero, or negative, respectively.

Hint: Write a template using `cond`.

Test: `(arithmetic-if 3 :+ :0 :-)` shall return `:+`, `(arithmetic-if
0 :+ :0 :-)` shall return `:0`, and `(arithmetic-if -7 :+ :0 :-)`
shall return `:-`.

{{% expand title="Solution" %}}
```clojure
(defmacro arithmetic-if [n pos zero neg]
  `(cond
     (> ~n 0) ~pos
     (= ~n 0) ~zero
     :else ~neg))
```
{{% /expand %}}

### Maybe

Write a macro `maybe` that expects two parameters: `code` to be
executed, and a probability `p` of execution. The higher the
probability, the more likely the code is executed.

Hint: Use `rand` to generate a random number between `0.0` and
`1.0`. Execute the code if the random number is smaller than or equal
to the given probability.

Test: `(maybe (println "*") 0.9)` should more likely output a `*`
character than `(maybe (print "*") 0.1)`.

{{% expand title="Solution" %}}
```clojure
(defmacro maybe [code p]
  `(when (<= (rand) ~p)
     ~code))
```
{{% /expand %}}
