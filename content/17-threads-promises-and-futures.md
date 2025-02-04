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

### Time Announcer

Write a function `announce-time` that expects a parameter `freq` and
prints the current date and time every `freq` seconds in a separate
thread.

Hint: Use `java.util.Date` and its `toString` method to get the
current date/time string. Use `Thread/sleep` to wait for a given
amount of milliseconds.

### Prime Factors Promise

Write a function `factor-prom` that expects a parameter `x`, which is
an integer to be factorized into its prime factors, and returns a
promise that delivers a sequence of that number's prime factors.

Hint: Use the solution for the [Lazy Prime
Numbers](/11-lazy-sequences/#lazy-prime-numbers) exercise to get
candidates for prime factors.

### Prime Factors Future

Write a function `factor-fut` that performs the same computation as
`factor-prom` from the exercise before, but returns a future instead
of a promise.

Hint: Start from `factor-prom` of the last exercise and simplify its
code.

### Map in Parallel

Write a function `factor` that performs prime factorization without
any promises or futures and calculates the prime factors
synchronuously.

Then write a function `factors` that accepts a sequence of numbers,
which shall be factorized using the `factor` function just written,
but doing so in parallel.

Hint: Use the `pmap` function as a concurrent version of `map`.