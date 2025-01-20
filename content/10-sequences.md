+++
title = "Sequences"
weight = 10
+++

Create a _sequence_ from a collection:

```clojure
(seq [1 2 3]) ; (1 2 3)
(seq '("foo" "bar" "qux")) ; ("foo" "bar" "qux")
(seq #{1 2 4}) ; (1 4 2)
(seq {:a 3 :b 7 :c 5}) ; ([:a 3] [:b 7] [:c 5])
```

Despite the round parentheses in the output, sequences are _not_ lists.

Create a sequence from an _empty_ colleection:

```clojure
(seq []) ; nil
(seq '()) ; nil
(seq #{}) ; nil
(seq {}) ; nil
```

Returning `nil` (falsy) instead of an empty sequence (truthy) is
useful in conditions.

Get the first element, all _but_ the first element, and the rest of a
sequence:

```clojure
(first (seq [1 2 3])) ; 1
(next (seq [1 2 3])) ; (2 3)
(rest (seq [1 2 3])) ; (2 3)
```

Applied to an empty sequence, both `first` and `next` return `nil`
(truthy), whereas `rest` returns an empty sequence (falsy):

```clojure
(first (seq [])) ; nil
(next (seq [])) ; nil
(rest (seq [])) ; ()
```

Add a new element to the front of a square:

```clojure
(cons 0 (seq [1 2 3])) ; (0 1 2 3)
```

TODO: summarize the rest of the chapter

## Exercises

### Sequence Count

Write a function `seq-count` that accepts a vector, a list, a set, or
a map, and returns the number of elements in that collection.

Hint: Convert the collection to a sequence first and use `next` to
traverse the sequence.

Test: `(seq-count [])` shall return 0, `(seq-count [1 2 3])` shall
return 3, and `(seq-count [nil nil])` shall return 2.

TODO: more exercises