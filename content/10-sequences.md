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

### Compose Function

Write a function `compose` that expects a vector functions as its
argument. The function shall return another function expecting a
single argument that first applies the last given function, then
applies its result to the second-last given function, etc.

Hint: Use `last` and `butlast` to separate the last element from the
rest of a collection. If no functions are passed, just return the
`identity` function.

Test: `((compose [#(- % 1) #(* % 2) #(+ % 1)]) 5)` shall return `11`.

{{% expand title="Solution" %}}
```clojure
(defn compose [fns]
  (let [f (last fns)
        fns (butlast fns)]
    (cond
      (nil? f) identity
      (nil? fns) f
      :else (fn [x] ((compose fns) (f x))))))
```
{{% /expand %}}

### Sequence Count

Write a function `seq-count` that accepts a vector, a list, a set, or
a map, and returns the number of elements in the given collection.

Hint: Convert the collection to a sequence first and use `next` to
traverse the sequence.

Test: `(seq-count [])` shall return 0, `(seq-count [1 2 3])` shall
return 3, and `(seq-count [nil nil])` shall return 2.

{{% expand title="Solution" %}}
```clojure
(defn seq-count [coll]
  (loop [xs (seq coll)
         n 0]
    (if (nil? xs)
      n
      (recur (next xs) (+ n 1)))))
```
{{% /expand %}}

### Parse Numbers

Given a collection of numbers, booleans, strings, and elements of
other types, write a function `as-nums` that returns a sequence of
numbers as follows:

- Numbers shall be retained as they are,
- booleans shall be converted to `1` (`true`) or `0` (`false`),
- and strings shall be parsed to floating point numbers.

Strings that cannot be parsed as a number or elements of any other
type can be ignored and won't end up in the returned sequence.

Hint: Use `Float/parseFloat` to parse the strings. Handle a possible
`NumberFormatException`.

Test: `(as-nums [3 2.5 true true false "1.25" "hey" :ho nil 7])` shall
return `(3 2.5 1 1 0 1.25 7)`.

{{% expand title="Solution" %}}
```clojure
(defn try-parse [s]
  (try
    (Float/parseFloat s)
    (catch NumberFormatException e nil)))
    
(defn as-nums [coll]
  (loop [coll (seq coll)
         acc (seq [])]
    (let [x (last coll)
          xs (butlast coll)]
      (cond
        (nil? coll) acc
        (number? x) (recur xs (cons x acc))
        (boolean? x) (recur xs (cons (if x 1 0) acc))
        (string? x) (let [y (try-parse x)]
                      (recur xs (if y (cons y acc) acc)))
        :else (recur xs acc)))))
```
{{% /expand %}}

### Longest Songs

Given a vector of songs:

```clojure
(def songs [{:name "Pale Fire" :duration "4m17s"}
            {:name "Monument" :duration "6m34s"}
            {:name "The Ivory Gate of Dreams" :duration "21m58s"}
            {:name "Still Remains" :duration "16m8s"}
            {:name "The Ghosts of Home" :duration "10m31s"}
            {:name "Glass Houses" :duration "3m35s"}
            {:name "Guardian" :duration "7m33s"}
            {:name "Exodus" :duration "8m39s"}])
```

Write a function `longest` that accepts a positive numeric parameter
`n`, a collection of songs, and returns the `n` longest songs
formatted as a string.

Hint: Write a helper function `parse-duration` that converts the
duration into a number of seconds. Use regular expressions with
[`re-matcher`](https://clojuredocs.org/clojure.core/re-matcher) and
[`re-find`](https://clojuredocs.org/clojure.core/re-find) for that
purpose. Use `sort-by` and `reverse` to sort the result in descending
order, `take` to extract the first `n` elements, and `interpose` to
format the output.

Test: `(longest 3 songs)` shall return `"1) The Ivory Gate of Dreams |
2) Still Remains | 3) The Ghosts of Home"`.

{{% expand title="Solution" %}}
```clojure
(defn parse-duration [dur]
  (let [matcher (re-matcher #"^(\d+)m(\d+)s$" dur)
        results (re-find matcher)]
    (if (= (count results) 3)
      (+ (* (Integer/parseInt (get results 1)) 60)
         (Integer/parseInt (get results 2)))
      0)))

(defn longest [n songs]
  (let [with-secs (map #(assoc % :secs (parse-duration (:duration %))) songs)
        sorted (sort-by :secs with-secs)
        reversed (reverse sorted)
        top-n (take n reversed)]
    (loop [n 1
           songs top-n
           acc []]
      (if (empty? songs)
        (reduce #(str %1 %2) "" (interpose " | " acc))
        (recur (+ n 1) (rest songs) (conj acc (str n ") " (:name (first songs)))))))))
```
{{% /expand %}}

### Pointy Arrow Refactoring

Rrefactor the `longest` function from the last exercise using the
_pointy arrow_ operator `->>`

Hint: The `->>` operator takes the return value from a function, and
hands it down to the next function as its last parameter. The `range`
function creates a sequence starting from the first parameter
(inclusive) up to the second parameter (exclusive). Use `interleave`
and `partition` to combine two sequences.

Test: Same as in last exercise.

{{% expand title="Solution" %}}
```clojure
;; parse-duration as before

(defn longest [n songs]
  (->>
   songs
   (map #(assoc % :secs (parse-duration (:duration %))))
   (sort-by :secs)
   (reverse)
   (take n)
   (map #(:name %))
   (interleave (range 1 (+ n 1)))
   (partition 2)
   (map #(str (first %) ") " (second %)))
   (interpose " | ")
   (reduce #(str %1 %2) "")))
```
{{% /expand %}}