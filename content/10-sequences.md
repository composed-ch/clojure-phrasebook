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

Sort and reverse a collection:

```clojure
(sort [9 4 6 2 1]) ; (1 2 4 6 9)
(reverse [2 4 8 16]) ; (16 8 4 2)
```

Partition a collection into smaller junks:

```clojure
(partition 2 [0 1 2 3 4 5]) ; ((0 1) (2 3) (4 5))
```

Weave two collections together:

```clojure
(def odd [1 3 5 7])
(def even [2 4 6 8])
(interleave odd even) ; (1 2 3 4 5 6 7 8)
```

Insert a separator in between elements:

```clojure
(interpose "," [1 2 3 4]) ; (1 "," 2 "," 3 "," 4)
```

Those functions turn a collection into a sequence first. Such
collections are said to be _seqable_, i.e. they can be turned into a
sequence.

Filter a collection using a predicate:

```clojure
(filter #(= (mod % 2) 0) [0 1 2 3 4 5]) ; (0 2 4)
```

Check whether or not a predicate holds true for at least one element:

```clojure
(some #(= (mod % 2) 0) [0 1 2 3 4 5]) ; true
(some #(= ( mod % 2) 0) [1 3 5 7]) ; nil
```

Transform the elements of a collection:

```clojure
(map #(* % 2) [0 1 2 3]) ; (0 2 4 6)
```

Compose functions:

```clojure
(def twice-the-successor (comp #(* % 2) #(+ % 1)))
(twice-the-successor 3) ; 8
(map twice-the-successor [0 1 2 3]) ; (2 4 6 8)
```

Executing `((comp f g) x)` is equivalent to `(f (g x))`.

Process a collection in a loop:

```clojure
(for [x [1 2 3]] (* x 2)) ; (2 4 6)
```

Combine the elements of a collection to a single value:

```clojure
(reduce (fn [acc x] (* acc x)) [1 2 3 4]) ; 24
```

Provide an initial _accumulator_ value (instead of using the first
element):

```clojure
(reduce (fn [acc x]
          (if (= (mod x 2) 0)
            (assoc acc :even (+ (:even acc) 1))
            (assoc acc :odd (+ (:odd acc) 1))))
        {:even 0 :odd 0}
        [0 1 2 3 4 8 16]) ; {:even 5, :odd 2}
```

The `{:even 0 :odd 0}` map (second argument) becomes the initial
`acc`.

## Exercises

### TODO: Write your own `compose` function.

### Sequence Count

Write a function `seq-count` that accepts a vector, a list, a set, or
a map, and returns the number of elements in that collection.

Hint: Convert the collection to a sequence first and use `next` to
traverse the sequence.

Test: `(seq-count [])` shall return 0, `(seq-count [1 2 3])` shall
return 3, and `(seq-count [nil nil])` shall return 2.

### Parse Numbers

Given a collection of numbers and strings, the latter of which could
represent a number, write a function `as-nums` that returns a sequence
of numbers. If a string cannot be parsed as a number, return 0 in its
place.

Hint: Use `Float/parseFloat` to parse the strings. Handle a possible
`NumberFormatException` by returning 0.0.

Test: TODO

### TODO: Longest Songs

TODO: vector of songs with durations (3m15s, 7m15s, etc.), find and
display the longest three, formatted nicely.

### TODO: Pointy Arrow Refactoring

TODO: refactor the last exercise using the `->>` operator.