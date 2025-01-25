+++
title = "Lazy Sequences"
weight = 11
+++

Create a lazy sequence by repeating a value:

```clojure
(def chorus (repeat "oh"))
```

Such a sequence is lazy by providing its values on demand, and it is
unbounded by providing values forever:

```clojure
(first chorus) ; "oh"
(nth chorus 10) ; "oh"
(nth chorus 1000) ; "oh"
```

Retrieve the first `n` elements of a sequence:

```clojure
(take 3 chorus) ; ("oh" "oh" "oh")
```

Repeat the items of a sequence:

```clojure
(def verse (cycle ["oh" "ah" "yeah"]))
(take 7 verse) ; ("oh" "ah" "yeah" "oh" "ah" "yeah" "oh")
```

Not the sequence itself, but its items are repeated in a cycle.

Create a sequence based on a starting value and a successor function:

```clojure
(def counter (iterate inc 1))
(take 5 counter) ; (1 2 3 4 5)
```

The generated sequence is immutable and stateless:

```clojure
(first counter) ; 1
(first counter) ; 1
(take 5 counter) ; (1 2 3 4 5)
(take 3 counter) ; (1 2 3)
```

Many functions on sequences are lazy:

```clojure
(take 5 (map #(* 10 %) (iterate inc 1))) ; (10 20 30 40 50)
(take 3 (filter #(= (mod % 2) 0) (iterate inc 1))) ; (2 4 6)
```

The values are only mapped and filtered as needed.

While many functions on sequences are lazy, some like `count` and
`reduce` are _eager_.

Create a lazy sequence explicitly from another (non-lazy) sequence:

```clojure
(def nums (lazy-seq [1 2 3]))
(take 2 nums) ; (1 2)
(take 3 nums) ; (1 2 3)
```

Limit the REPL output size when dealing with infinite sequences:

```clojure
(set! *print-length* 5)
(println (iterate inc 1)) ; (1 2 3 4 5 ...)
```

Realize a lazy sequence, enforcing its computation:

```clojure
(defn chatty-inc [n]
  (println "incrementing" n)
  (inc n))

(def counter (iterate chatty-inc 1)) ; no output
(def first-5 (take 5 counter)) ; still no output
(doall first-5)
;; incrementing 1
;; incrementing 2
;; incrementing 3
;; incrementing 4
;; incrementing 5
;; (1 2 3 4 5)
```

Process the elements of a lazy sequence one by one:

```clojure
(defn decorate [xs]
  (doseq [x xs]
    (println "<[" x "]>")))

(decorate (take 3 (iterate inc 1)))
;; <[ 1 ]>
;; <[ 2 ]>
;; <[ 3 ]>
```

Write a function that produces a lazy sequence:

```clojure
(defn my-iterate [f x]
  (let [next (f x)]
    (cons x (lazy-seq (my-iterate f next)))))

(take 5 (my-iterate inc 1)) ; (1 2 3 4 5)
(take 5 (my-iterate #(* % 2) 1)) ; (1 2 4 8 16)
```

## Exercises

### Lazy Fibonacci Numbers

Write a function `lazy-fib` that returns a lazy sequence of Fibonacci
numbers.

Hint: Use `cons` and `lazy-seq` with a recursive function call to
produce the next element. A multi-arity function (with zero and two
arguments) is a good option to provide the initial two Fibonacci
numbers.

Test: `(take 10 (fib-stream))` shall return `(1 1 2 3 5 8 13 21 34
55)`, and `(nth (fib-stream) 45)` shall return `1836311903`.

{{% expand title="Solution" %}}
```clojure
(defn lazy-fib
  ([] (lazy-fib 1 1))
  ([a b] (cons a (lazy-seq (lazy-fib b (+ a b))))))
```
{{% /expand %}}

### Lazy Prime Numbers

Write a function `lazy-primes` that returns a lazy sequence of prime
numbers. Write a predicate function `is-prime` that checks whether or
not the given argument is a prime number.

Hint: Use `cons` and `lazy-seq` again. Use the functions `take-while`
and `some` to create and process the candidates for the divisibility
test in the `is-prime` predicate.

Test: `(take 10 (lazy-primes))` shall return `(2 3 5 7 11 13 17 19 23
29)`.

{{% expand title="Solution" %}}
```clojure
(defn is-prime [x]
  (if (< x 2)
    false
    (let [ys (take-while #(<= % (/ x 2)) (iterate inc 2))]
      (not (some #(= (mod x %) 0) ys)))))

(defn lazy-primes
  ([] (lazy-primes 2))
  ([x] (if (is-prime x)
         (cons x (lazy-seq (lazy-primes (inc x))))
         (lazy-seq (lazy-primes (inc x))))))
```
{{% /expand %}}

### Process File Line by Line

Write a function `sum-up-to` that reads lines from a text file, tries
to parse them as numbers, sums those numbers up until a given limit is
reached, writes the processed numeric lines into a output text file,
and returns the sum. Both `source` and `target` paths and the `limit`
are given as parameters.

Hint: Use `with-open`, `clojure.java.io/reader`,
`clojure.java.io/writer`, and `line-seq` for (lazy) file line by line
processing. If the lines are exhausted before the limit is reached,
the current sum should be returned.

Test: Given the following file `lines.txt`:

```plain
1
foo
2
bar
3
qux
4
baz
5
bum
6
wah
7
```

`(sum-up-to lines.txt result.txt 16)` shall return `15` and produce a
file `result.txt` with the following content:

```plain
1
2
3
4
5
```

{{% expand title="Solution" %}}
```clojure
(defn try-parse [s]
  (try (Integer/parseInt s)
       (catch NumberFormatException e nil)))

(defn sum-up-to [source sink limit]
  (with-open [r (clojure.java.io/reader source)
              w (clojure.java.io/writer sink)]
    (loop [lines (line-seq r)
           acc 0]
      (let [line (first lines)
            x (try-parse line)]
        (if (nil? line)
          acc
          (if x
            (let [y (if x (+ acc x) acc)]
              (if (> y limit)
                acc
                (do
                  (.write w (str x "\n"))
                  (recur (rest lines) y))))
            (recur (rest lines) acc)))))))
```
{{% /expand %}}