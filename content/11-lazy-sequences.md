+++
title = "Lazy Sequences"
weight = 11
+++

Create a lazy sequence by repeating a value:

```clojure
(def chorus (repeat "oh"))
```

Such a sequence is lazy by providing its values on demand, and it is
unbounded by providing values forever.