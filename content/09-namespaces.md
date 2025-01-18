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
lein new app music-store
```

The file `music_store/src/music_store.clj` defines the namespace `music-store.core` and contains the following code:

```clojure
```
