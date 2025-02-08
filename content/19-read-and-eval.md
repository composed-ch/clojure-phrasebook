+++
title = "Read and Eval"
weight = 19
+++

Read code from standard input:

```clojure
(read)
```

Read, evaluate and execute code from a string:

```clojure
(def func-text "(defn plus [a b] (+ a b))")
(def func-data (read-string func-text))
(def func-eval (eval func-data))

(func-eval 4 5) ; 9
```

Write a REPL:

```clojure
(defn repl []
  (loop []
    (println (eval (read)))
    (recur)))
```

Attach metadata to data and get it back:

```clojure
(def songs-data ["Pale Fire" "Monument"])
(def songs-meta (with-meta songs-data {:note "I especially like 'Pale Fire'"}))
(meta songs-meta) ; {:note "I especially like 'Pale Fire'"}
```

Metadata is not dealt with separate from regular data:

```clojure
(def points-a [3 5 8])
(def points-b [3 5 8])
(= points-a points-b) ; true

(def meta-points-a (with-meta points-a {:measured "yesterday"}))
(def meta-points-b (with-meta points-b {:measured "today"}))
(= meta-points-a meta-points-b) ; true
(= (meta meta-points-a) (meta meta-points-b)) ; false
```

## Exercises

### Read Song Data

Save the following song data to disk, e.g. under `/tmp/songs.clj`:

```clojure
{:name "Aces High" :band "Iron Maiden" :album "Powerslave" :year 1984}
{:name "Damnation" :band "Fates Warning" :album "Night on Bröcken" :year 1984}
{:name "The Apparition" :band "Fates Warning" :album "Spectre Within" :year 1985}
{:name "Wasted Years" :band "Iron Maiden" :album "Somewhere in Time" :year 1986}
{:name "Giant's Lore" :band "Fates Warning" :album "Awaken the Guardian" :year 1986}
{:name "The Ivory Gate of Dreams" :band "Fates Warning" :album "No Exit" :year 1988}
{:name "Infinite Dreams" :band "Iron Maiden" :album "Seventh Son of a Seventh Son" :year 1988}
{:name "Through Different Eyes" :band "Fates Warning" :album "Perfect Symmetry" :year 1989}
{:name "Fates Warning" :band "Iron Maiden" :album "No Prayer for the Dying" :year 1990}
{:name "Life in Still Water" :band "Fates Warning" :album "Parallels" :year 1991}
{:name "Judas Be My Guide" :band "Iron Maiden" :album "Fear of the Dark" :year 1992}
{:name "Pale Fire" :band "Fates Warning" :album "Inside Out" :year 1994}
{:name "Monument" :band "Fates Warning" :album "Inside Out" :year 1994}
{:name "Lord of the Flies" :band "Iron Maiden" :album "The X Factor" :year 1995}
```

Write function `songs-from-file` that expects a path as parameter and
returns a vector of songs.

Hint: Use `slurp` to read the data. Use `clojure.string/split-lines`
to separate the string into lines. Use `read` to turn the lines into
data.

Test: `(count (songs-from-file "/tmp/songs.clj"))` shall return `14`.

TODO: Solution

### Read Transformation Functions

TODO: functions with meta data (filter or map: predicate or transformer)

### Filter Songs Using Predicates


