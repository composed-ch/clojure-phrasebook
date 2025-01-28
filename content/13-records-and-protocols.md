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

## Exercises

### Shape Protocol

Define a protocol `Shape` with two functions `area` and `circumference`.

Hint: Use `defprotocol`; the functions only need a `this` parameter.

Test: `(area (reify Shape (area [this] 0)))` shall return `0`.

{{% expand title="Solution" %}}
```clojure
(defprotocol Shape
  (area [this])
  (circumference [this]))
```
{{% /expand %}}

### Square and Circle Records

Implement the `Shape` protocol for two new record types: `Rectangle`
and `Circle`.

Hint: Use `defprotocol`; the `Rectangle` has a `width` and a `height`;
the `Circle` has a `radius`. Use `Math/PI` and `Math/pow` for
computing the circle's area and circumference.

Test: `(area (->Rectangle 3 4))` shall return `12`, and
`(circumference (->Circle 5))` shall return `31.41592653589793`.

{{% expand title="Solution" %}}
```clojure
(defrecord Rectangle [width height]
  Shape
  (area [this]
    (* (:width this) (:height this)))
  (circumference [this]
    (+ (* 2 (:width this)) (* 2 (:height this)))))

(defrecord Circle [radius]
  Shape
  (area [this]
    (* Math/PI (Math/pow (:radius this) 2.0)))
  (circumference [this]
    (* 2 Math/PI (:radius this))))
```
{{% /expand %}}

### Volume Extension

Define a protocol `Body` with a function `volume` that expects a
`height` parameter. Extend the `Rectangle` and `Circle` records to
implement `Body`. The function shall compute the volume of a cuboid
(`Rectangle`) and a cylinder (`Circle`).

Hint: Use `extend-protocol`. A cuboid's volume is the area of its
rectangular base multiplied by a height; a cylinder's volume is the
area of its circular base multiplied by a height.

Test: `(volume (->Rectangle 3 4) 5)` shall return `60`, and `(volume
(->Circle 5) 3)` shall return `235.61944901923448`.

{{% expand title="Solution" %}}
```clojure
(defprotocol Body
  (volume [this height]))

(extend-protocol Body
  Rectangle
  (volume [this height]
    (* (area this) height))
  Circle
  (volume [this height]
    (* (area this) height)))
```
{{% /expand %}}