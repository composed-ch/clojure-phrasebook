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
