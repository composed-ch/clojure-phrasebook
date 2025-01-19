+++
title = "Namespaces"
weight = 9
+++

Create a new namespace:

```clojure
(ns music)
```

The new namespace is activated as the _current_ namespace, which will
hold the vars in a lookup table indexed by their symbols.

Define vars with the same name in two different namespaces:

```clojure
(ns iron-maiden)
(def first-album "Iron Maiden")

(ns fates-warning)
(def first-album "Night on Bröcken")

(ns iron-maiden)
(println first-album) ; "Iron Maiden"

(ns fates-warning)
(println first-album) ; "Night on Bröcken"
```

Using `ns` on an existing namespace does not create a new namespace,
but activates the existing one.

Use _fully qualified symbols_ to access other namespaces:

```clojure
(ns music-shop)
(println iron-maiden/first-album) ; "Iron Maiden"
(println fates-warning/first-album) ; "Night on Bröcken"
```

Use a namespace from the standard library, and use it to compare data
objects:

```clojure
(require 'clojure.data)

(clojure.data/diff {:song "The Apparition" :band "Iron Maiden" :duration "5m50s"}
                   {:song "The Apparition" :band "Fates Warning" :duration "3m54s"})
;; ({:duration "5m50s", :band "Iron Maiden"}
;;  {:duration "3m54s", :band "Fates Warning"}
;;  {:song "The Apparition"})
```

The `diff` function returns a three-element list with

1. data that only exists in the first argument,
2. data that only exists in the second argument, and
3. data that is common to both arguments.

Create a new [Leiningen](https://leiningen.org/) project in the shell:

```sh
lein new app music_store
```

The file `music_store/src/music_store.clj` defines the namespace
`music-store.core` and contains the following code:

```clojure
(ns music-store.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
```

Dashes are used for symbol names, and underscores for file names.

Create a new namespace `music-store.bands` in `music_store/src/music_store/bands.clj`:

```clojure
(ns music-store.bands)

(def iron-maiden {:name "Iron Maiden" :founded 1975})
(def fates-warning {:name "Fates Warning" :founded 1983})
```

Use a namespace in the `ns` declaration in `music_store/src/mustic_store/core.clj`:

```clojure
(ns music-store.core
  (:require music-store.bands))
```

Unlike the standalone `require` function used in the REPL, `:require`
declarations within `ns` do not need quoting:

```clojure
(:require music-store.bands) ; within ns: no quoting
(require 'music-store.bands) ; from the REPL: quoting
```

Create a (shorter) alias for a namespace:

```clojure
(require '[music-store.bands :as bands])
```

Same within `ns` without quoting:

```clojure
(ns music-store.core
  (:require [music-store.bands :as bands]))
```

Pull in specific symbols from another namespace:

```clojure
(require '[music-store.bands :refer [fates-warning]])
(println fates-warning) ; {:name Fates Warning, :founded 1983}
```

Pull in _all_ the symbols from another namespace:

```clojure
(require '[music-store.bands :refer :all])
(println iron-maiden) ; {:name Iron Maiden, :founded 1975}
```

Use `:refer :all` with caution, for it pollutes the current namespace.

Get hold of the current namespace:

```clojure
(println *ns*) ; #namespace[music-store.core]
```

Lookup a namespace by its name:

```clojure
(find-ns 'music-store.bands) ; #namespace[music-store.bands]
```

Discover the bindings of a namespace:

```clojure
(ns-map (find-ns 'music-store.bands))
```

If a symbol instead of a namespace is given, `ns-map` will look up the namespace:

```clojure
(ns-map 'music-store.bands)
```

Get the namespace of a symbol:

```clojure
(namespace 'music-store.bands/fates-warning) ; "music-store.bands"
```

Qualify a keyword with a namespace:

```clojure
:music-store.bands/iron-maiden
```

Qualify a keyword with the current namespace (e.g. from `music-store.core`):

```clojure
::whatever ; :music-store.core/whatever
```

Reload the symbols of a namespace (force-reloads already loaded symbols):

```clojure
(require :reload '[music-store.bands])
```

Get rid of loaded symbols:

```clojure
(ns-unmap 'music-store.bands 'iron-maiden)
```

Define a var that resits `:reload` (e.g. when initialized using a heavy function call):

```clojure
(defonce fib-42 (fib 42))
```

## Exercises

### Leiningen Project

Create a new Leiningen project called `fibonacci` and run the generated code.

Hint: Use `lein run` to run the project from the `fibonacci/` folder.

Test: `lein run` shall output `"Hello, World!"`.

{{% expand title="Solution" %}}
```sh
lein new app fibonacci
cd fibonacci
lein run
```
{{% /expand %}}


### Additional Namespace

Create a new namespace `recursive` within the `fibonacci`
project. Implement a function called `fib` that calculates the nth
Fibonacci number given the parameter `n`. Use that function from the
`core` namespace (`src/fibonacci/core.clj`) in the `-main` function
and call it with the argument `35` and output the result.

Hint: Put the file into the `src/fibonacci` folder and name it
according to the namespace defined therin.

Test: The application shall output `fib(35)=14930352`.

{{% expand title="Solution" %}}

`src/fibonacci/recursive.clj`:

```clojure
(ns recursive)

(defn fib [n]
  (if (<= n 1)
    1
    (+ (fib (- n 2)) (fib (- n 1)))))
```

`src/fibonacci/core.clj`:

```clojure
(ns fibonacci.core
  (:require fibonacci.recursive)
  (:gen-class))

(defn -main
  [& args]
  (println (str "fib(35)=" (recursive/fib 35))))
```
{{% /expand %}}

### Yet Another Namespace

- create a new namespace tail-recursion with a tail-recursive fib implementation
  - link to original exercise
  - /05-more-capable-functions/index.html#fibonacci-numbers
- use the function in core/-main with :as reference
  - run both functions with timed?
  - maybe use defonce?
