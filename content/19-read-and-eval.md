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

