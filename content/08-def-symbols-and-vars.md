+++
title = "Def, Symbols, and Vars"
weight = 8
+++

Access a symbol using `'`:

```clojure
(def album "Parallels")
(str 'album) ; "album"
```

Just like keywords, symbols stand for some values. Unlike keywords, which only
stand for themselves, symbols stand for some other value.

Compare symbols and their values with one another:

```clojure
(def artist "Iron Maiden")
(def song "Iron Maiden")

(= 'artist 'song) ; false
(= artist song) ; true
```

Note the difference between the comparison of the symbols and their values.

Get hold of a _var_ using `#'`:

```clojure
(def song "Pale Fire")
#'song ; #'user/song
```

A var is a bindig of a value (`"Pale Fire"`) to a symbol (`'song`).

Get the value and the symbol behind a var:

```clojure
(def song "Monument")
(.get #'song) ; "Monument"
(.-sym #'song) ; song
```

Create a _dynamic_ var:

```clojure
(def ^:dynamic *verbose* false)
```

By convention, dynamic vars are surrounded by _earmuffs_ (`*…*`).

Bind the values of dynamic vars:

```clojure
(binding [*verbose* true] (println "verbose?" *verbose*)) ; verbose? true
```

The var `*verbose*` hast the value `true` withing the `binding` scope and in all
the functions that are called within that scope.

Change a dynamic var from inside the binding:

```clojure
(binding [*verbose* true]
  (set! *verbose* false)
  (println "verbose?" *verbose*)) ;; verbose? false
```

Unlike `def`, `let` does _not_ create any vars:

```clojure
(let [foo "bar"] (println #'foo)) ; Unable to resolve var: foo in this context
```

The REPL provides additional dynamic vars, such as `*1`, `*2` for the last,
second last, etc. result, and `*e` for the last exception.

## Exercises

### Verbose Fibonacci

Write a function `fib` that expects a parameter `n`. The function shall compute
the n-th Fibonacci number (see [chapter
5](/05-more-capable-functions/index.html#fibonacci-numbers)), but write a
recursive function _without_ tail-calls.

Define a dynamic var `*verbose*`, which is set to `false` by default. Enhance
the function `fib` so that it logs it calls in the form `fib(n)=x`, with `n`
being the argument for `n`, and `x` the calculated result.

Define another function `do-fib` that, besides the `n` parameter, also accepts a
parameter `debug`. The original `fib` function then shall be invoked while
setting the `*verbose*` flag to the value of `debug`.

Hint: Use `binding` so set the `*verbose*` var temporarely.

Test: `(do-fib 4 false)` shall return `5` and output nothing. `(do-fib 3 true)`
shall return 3 and output the following:

```plain
fib(1)=1
fib(0)=1
fib(1)=1
fib(2)=2
fib(3)=3
```

{{% expand title="Solution" %}}
```clojure
(def ^:dynamic *verbose* false)

(defn do-fib [n debug]
  (binding [*verbose* debug]
    (fib n)))

(defn fib [n]
    (let [result (if (<= n 1) 1 (+ (fib (- n 2)) (fib (- n 1))))]
      (when *verbose*
        (println (str "fib(" n ")=" result)))
      result))
```
{{% /expand %}}

### Memoized Fibonacci

Create a dynamic binding `*cache*` that is initialized to an empty map.

Write a _memoized_ implementation of a function that computes the nth Fibonacci
number, expecting the parameter `n`. _Memoization_ is an optimization that works
as follows:

1. For the arguments `0` and `1`, alsways return `1`.
2. If the parameter `n` is a key in the map, return the value associated with it.
3. Otherwise, calculate the result `n`, and store it in the map.

Hint: Handle the three cases using `cond`. For the third case, first compute
`(fib (- n 2)` and cache its result using `binding`. Then calculate `(fib (- n
1)`, which now can make use of the cache.

Test: `(fib 42)` shall return 433494437—and finish within a second.

{{% expand title="Solution" %}}
```clojure
(def ^:dynamic *cache* {})

(defn fib [n]
  (cond
    (<= n 1) 1
    (contains? *cache* n) (get *cache* n)
    :else
    (let [n-2 (- n 2)
          fib-n-2 (fib n-2)]
      (binding [*cache* (assoc *cache* n-2 fib-n-2)]
        (let [n-1 (- n 1)
              fib-n-1 (fib n-1)]
          (+ fib-n-1 fib-n-2))))))
```

Note: The cached values are only available within the `binding` block, i.e.
cached values are only available for the calculation of `(fib (- n 1))`.
{{% /expand %}}

### Verbose Memoized Fibonacci

Combine the solutions of the two previous exercises to enable debug output of
the memoized Fibonacci function. Write a function `(do-fib n debug)` that turns
verbose output on and off based on the value of `debug`. The output shall
indicate the argument `n`, the result of the computation `(fib n)`, and whether
or not the cache was hit or missed.

Hint: Use two dynamic vars: `*cache*` and `*verbose*`. 

Test: `(do-fib 6 true)` shall return `13` and output:

```plain
fib(2)=2 cache hit
fib(2)=2 cache missed
fib(3)=3 cache hit
fib(4)=5 cache hit
fib(2)=2 cache hit
fib(3)=3 cache hit
fib(4)=5 cache missed
fib(5)=8 cache hit
fib(6)=13 cache hit
13
```

{{% expand title="Solution" %}}
```clojure
(def ^:dynamic *verbose* false)
(def ^:dynamic *cache* {})

(defn fib [n]
  (cond
    (<= n 1) 1
    (contains? *cache* n)
    (let [result (get *cache* n)]
      (println (str "fib(" n ")=" result) "cache missed")
      result)
    :else
    (let [n-2 (- n 2)
          fib-n-2 (fib n-2)]
      (binding [*cache* (assoc *cache* n-2 fib-n-2)]
        (let [n-1 (- n 1)
              fib-n-1 (fib n-1)
              result (+ fib-n-1 fib-n-2)]
          (when *verbose*
            (println (str "fib(" n ")=" result) "cache hit"))
          result)))))

(defn do-fib [n debug]
  (binding [*verbose* debug]
    (fib n)))
```
{{% /expand %}}
