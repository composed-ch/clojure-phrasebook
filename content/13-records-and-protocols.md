+++
title = "Records and Protocols"
weight = 13
+++

Define a _record_:

```clojure
(defrecord Musician [name instrument band])
```

Use the automatically generated _factory functions_ to create a new
record instance:

```clojure
(def jimmy (->Musician "Jim Matheos" "Guitar" "Fates Warning"))
(def bobby (map->Musician {:name "Bobby Jarzombek"
                           :instrument "Drums"
                           :band "Fates Warning"}))
```

Records can be used like maps:

```clojure
(:instrument jimmy) ; "Guitar"
(count bobby) ; 3
```

Extend a record with additional fields:

```clojure
(assoc jimmy :first-album "Night on Bröcken")
;; {:name "Jim Matheos",
;;  :instrument "Guitar",
;;  :band "Fates Warning",
;;  :first-album "Night on Bröcken"}

(:instrument jimmy) ; "Guitar"
```

Access to pre-defined record fields is faster than access to
additional fields added later.

Define a _protocol_:

```clojure
(defprotocol Personalized
  (name [this])
  (describe [this]))
```

Implement a protocol for different record types:

```clojure
(defrecord Musician [name instrument band]
  Personalized
  (name [this]
    (:name this))
  (describe [this]
    (str (:name this) " plays " (:instrument this) " for " (:band this))))

(defrecord Employee [name position company]
  Personalized
  (name [this]
    (:name this))
  (describe [this]
    (str (:name this) " works as a " (:position this) " for " (:company this))))
```

Use the polymorphic functions on both record types:

```clojure
(def people [(->Musician "Ian Hill" "Bass" "Judas Priest")
             (->Employee "Ashok" "Intern" "ACME Corp.")])
(map describe people)
;; ("Ian Hill plays Bass for Judas Priest"
;;  "Ashok works as a Intern for ACME Corp.")
```

Implement a protocol for an existing type:

```clojure
(defprotocol Greetable
  (greet [this greeting]))

(extend-protocol Greetable
  Musician
  (greet [this greeting]
    (str greeting ", " (name this) "!"))
  Employee
  (greet [this greeting]
    (str greeting ", underling " (name this) ".")))

(map #(greet % "Hi") people) ; ("Hi, Ian Hill!" "Hi, underling Ashok.")
```

Protocols can be implemented for _any_ existing types, including
`String`, `Boolean`, and the like.

Create a one-off implementation for a protocol:

```clojure
(def hello-kitty
  (reify Personalized
    (name [this] "Kitty")
    (describe [this] "Kitty is a cat!")))

(describe hello-kitty) ; "Kitty is a cat!"
```

Protocols can be implemented partially using `reify`, but calling
missing functions causes runtime exceptions.