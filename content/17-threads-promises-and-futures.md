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

TODO: promises and futures