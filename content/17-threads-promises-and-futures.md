+++
title = "Threads, Promises, and Futures"
weight = 17
+++

> [!INFO]
> Clojure functions implement Java's `Runnable`
> interface. Clojure vars live in thread-local storage.

Create a thread, execute a function in it, and wait for it to finish:

```clojure
(defn hello-goodbye []
  (println "hello")
  (println "goodbye"))

(def thread (Thread. hello-goodbye))

(.start thread)
(.join thread)

;; hello
;; goodbye
```

Same, but with multiple threads and parametrized output functions for
distinction:

```clojure
(defn hello-goodbye-fn [name]
  (fn []
    (println "hello from" name)
    (Thread/sleep 1000)
    (println "goodbye from" name)))

(def thread-a (Thread. (hello-goodbye-fn "a")))
(def thread-b (Thread. (hello-goodbye-fn "b")))

(.start thread-a)
(.start thread-b)
(.join thread-a)
(.join thread-b)

;; hello from a
;; hello from b
;; goodbye from a
;; goodbye from b
```

Same, but with many threads:

```clojure
(def threads (map #(Thread. (hello-goodbye-fn (str %))) (take 5 (iterate inc 1))))
(map (memfn start) threads)
(map (memfn join) threads)
```

Deliver a result through a _promise_:

```clojure
(def result (promise))
(deliver result 42)

(deref result) ; 42
@result ; 42
```

`@result` is syntactic sugar for `(deref result)`. A value can only be
delivered once, but derefed multiple times.

```clojure
(deliver result 99)
@result ; still 42
```

Use a promise to communicate between threads:

```clojure
(defn sum-prom [from to]
  (let [n (inc (- to from))
        result (promise)
        thread (Thread. #(deliver result (reduce + (take n (iterate inc from)))))]
    (.start thread)
    result))

(deref (sum-from-to 1 1e6)) ; 500000500000
@(sum-prom 1 1e6) ; 500000500000
```

Simplify concurrent code using a _future_, which is a promise with its
own thread:

```clojure
(defn sum-fut [from to]
  (let [n (inc (- to from))
        f (future (reduce + (take n (iterate inc from))))]
    f))

(deref (sum-fut 1 1e6)) ; 500000500000
@(sum-fut 1 1e6) ; 500000500000
```

Use a timeout to deliver a fallback value if a computation takes too long:

```clojure
(defn sum-from-to [from to]
  (let [n (inc (- to from))]
    (reduce + (take n (iterate inc from)))))

(def f (future (sum-from-to 1 1e6)))
(deref f 500 "computation took too long") ; 500000500000

(def f (future (sum-from-to 1 1e7)))
(deref f 500 "computation took too long") ; "computation took too long"
```

## Exercises

### Countdown

Write a function `countdown` that expects a `name` and a number `n`
from which to count down to zero. Print the name and the number, which
is to be counted down, and then pause for a second. Continue printing
until the number reached zero.

Then write a function `launch-rockets` that expects a number `n` (the
number of rockets to be launched) and `sec` (the number of seconds
from which to count down to zero). Create one thread per rocket,
i.e. `n` threads, which execute the `contdown` function with the name
`rocket X`, where `X` is a number from `1` to `n`. Start those threads.

Hint: Use `(locking *out* (println OUTPUT))` to make the output in
`countdown` thread-safe.

Test: `(launch-rockets 3 4)` shall produce the following (non-deterministic) output:

```plain
rocket 1: 4
rocket 2: 4
rocket 3: 4
rocket 1: 3
rocket 2: 3
rocket 3: 3
rocket 1: 2
rocket 2: 2
rocket 3: 2
rocket 1: 1
rocket 2: 1
rocket 3: 1
rocket 1: 0
rocket 3: 0
rocket 2: 0
```

{{% expand title="Solution" %}}
```clojure
(defn countdown [name n]
  (loop [i n]
    (locking *out*
      (println (str name ": " i)))
    (Thread/sleep 1000)
    (when (> i 0)
      (recur (dec i)))))

(defn launch-rockets [n sec]
  (let [func (fn [s] #(countdown s sec))
        rockets (map #(str "rocket " %) (take n (iterate inc 1)))
        threads (map #(Thread. (func %)) rockets)]
    (map (memfn start) threads)))
```
{{% /expand %}}

### Map in Parallel

Write a function `factor` that performs prime factorization without
any promises or futures and calculates the prime factors
synchronuously.

Then write a function `factors` that accepts a sequence of numbers,
which shall be factorized using the `factor` function just written,
but doing so in parallel.

Hint: [Integer factorization
(Wikipedia)](https://en.wikipedia.org/wiki/Integer_factorization)
describes how a number can be factorized into its prime factors. Use
the solution for the [Lazy Prime
Numbers](/11-lazy-sequences/#lazy-prime-numbers) exercise to get
candidates for prime factors. Use the `pmap` function as a concurrent
version of `map`.

Test: `(factor (factors (take 10 (iterate inc 10)))` shall return `([2 5] [11]
[2 2 3] [13] [2 7] [3 5] [2 2 2 2] [17] [2 3 3] [19])`.

{{% expand title="Solution" %}}
```clojure
;; lazy-primes from chapter 11

(defn factor [x]
  (loop [x x
         acc []
         candidates (lazy-primes)]
    (let [c (first candidates)]
      (cond
        (= x 1) acc
        (= (mod x c) 0) (recur (/ x c) (conj acc c) candidates)
        (< c x) (recur x acc (rest candidates))
        :else (conj acc x)))))

(defn factors [xs]
  (pmap factor xs))
```
{{% /expand %}}

### Prime Factors Promise

Write a function `factor-prom` that expects a parameter `x`, which is
an integer to be factorized into its prime factors, and returns a
promise that delivers a sequence of that number's prime factors.  The
actual computation shall take place in its own thread.

Hint: Re-use the `factor` function from the last exercise.

Test: `@(factor-prom 324)` shall return `[2 2 3 3 3 3]`.

{{% expand title="Solution" %}}
```clojure
(defn factor-prom [x]
  (let [prom (promise)
        thread (Thread. #(deliver prom (factor x)))]
    (.start thread)
    prom))
```
{{% /expand %}}

### Prime Factors Future

Write a function `factor-fut` that performs the same computation as
`factor-prom` from the exercise before, but returns a future instead
of a promise.

Hint: Start from `factor-prom` of the last exercise and simplify its
code.

Test: `@(factor-fut 324)` shall return `[2 2 3 3 3 3]`.

{{% expand title="Solution" %}}
```clojure
(defn factor-fut [x]
  (future (factor x)))
```
{{% /expand %}}