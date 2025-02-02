+++
title = "Interoperating with Java"
weight = 16
+++

Instantiate a Java class, thereby invoking its constructor:

```clojure
(def songs (java.io.File. "songs.txt"))
```

Call a method on an object using `.` notation:

```clojure
(.exists songs) ; false
```

Access a public field of an object:

```clojure
(def rect (java.awt.Rectangle. 0 0 3 4))
(.-width rect) ; 3
(.-height rect) ; 4
```

Import a Java package from the REPL:

```clojure
(import java.io.File)
```

Import multiple classes from the same Java from the REPL:

```clojure
(import '(java.io File InputStream))
```

Import a Java package from a source file:

```clojure
(ns interop.core
  (:import java.io.File))
```

Import multiple classes from the same Java from a source file:

```clojure
(ns interop.core
  (:import (java.io File InputStream)))
```

Use the imported class without package qualification:

```clojure
(def albums (File. "albums.txt"))
```

The `java.lang` package, providing `String`, `Boolean`, etc., is
imported automatically.

Access static fields and methods using `/` notation:

```clojure
(interpose File/separator ["home" "patrick" "clojure"])
(String/format "%s%s%s.%s" (to-array ["docs" File/separator "diary" "txt"]))
;; "docs/diary.txt"
```

The `to-array` function turns a seqable into a Java array.

Add a Java library (e.g. Gson) to the Leiningen project's dependencies
in `project.clj`:

```clojure
:dependencies [[org.clojure/clojure "1.11.1"]
               [com.google.code.gson/gson "2.11.0"]]
```

Install added dependencies on the shell:

```sh
lein deps
```

Use Gson to output values as JSON:

```clojure
(import com.google.gson.Gson)
(def gson (Gson.))
(println (.toJson gson {:name "Java" :creator "James Gosling" :properties ["verbose" "big"]}))
;; {":name":"Java",":creator":"James Gosling",":properties":["verbose","big"]}
```

Turn a method into a function using `memfn`:

```clojure
(def file-names ["albums.txt" "songs.txt" "bands.txt"])
(def files (map #(File. %) file-names))
(map (memfn exists) files) ; (false false false)
```

Java objects are mutable:

```clojure
(def songs (java.util.ArrayList.))
(.add songs "Pale Fire")
(.add songs "Monument")
(map str songs) ; ("Pale Fire" "Monument")
```

## Exercises

### Rectangle Enclosure

Write a function `rect` that expects a map with the keys `:x`, `:y`,
`:w`, and `:h` that describe the a rectangle's upper-left corner x/y
coordinates as well as its width and height, and returns a
`java.awt.Rectangle` instance.

Write a function `encloses?` that expects two `java.awt.Rectangle`
instances as arguments and returns `true` if the first rectangle
completely encloses the second rectangle, and `false` otherwise.

Hint: Use map key destructuring in the `rect` function. The first two
constructor arguments describe the `x` and `y` position of the
rectangle's upper-left corner. Access the relevant properties using
`.-x`, `.-y`, `.-width`, and `.-height`.

Test: `(encloses? (rect {:x 3 :y 4 :w 5 :h 6}) (rect {:x 4 :y 5 :w 1
:h 2}))` shall return `true`, and `(encloses? (rect {:x 3 :y 4 :w 5 :h
6}) (rect {:x 4 :y 5 :w 10 :h 2}))` shall return `false`.

{{% expand title="Solution" %}}
```clojure
(defn rect [{:keys [:x :y :w :h]}]
  (java.awt.Rectangle. x y w h))

(defn encloses? [outer inner]
  (let [x1 (.-x outer)
        y1 (.-y outer)
        x2 (.-x inner)
        y2 (.-y inner)
        h1 (.-height outer)
        w1 (.-width outer)
        h2 (.-height inner)
        w2 (.-width inner)]
    (and (<= x1 x2)
         (<= y1 y2)
         (>= (+ x1 w1) (+ x2 w2))
         (>= (+ y1 h1) (+ y2 w2)))))
```
{{% /expand %}}

### String Concatenation

Write a function `concatenate` that returns a string of the
concatenated items of the seqable given as an argument. The
concatenation shall be performed _in-place_ using the
`java.util.StringBuilder` class.

Hint: Use the `.toString` method to turn the individual items into
strings.

Test: `(concatenate [1 "2" 3 "456" 7 \8 "9"])` shall return `"123456789"`.

{{% expand title="Solution" %}}
```clojure
(defn concatenate [items]
  (loop [buf (StringBuilder.)
         items items]
    (if (empty? items)
      (.toString buf)
      (let [head (first items)
            string (.toString head)]
        (.append buf string)
        (recur buf (rest items))))))
```
{{% /expand %}}

### CamelCase

Write a function `camel-case` that converts Clojure identifiers to CamelCase.

Hint: Use the class
[`CaseUtils`](https://commons.apache.org/proper/commons-text/apidocs/org/apache/commons/text/CaseUtils.html)
from the Apache Commons Text package
`org.apache.commons.commons-text`.

Test: `(camel-case "private-field-accessor-util")` shall return `"PrivateFieldAccessorUtil"`.

{{% expand title="Solution" %}}

> [!NOTE]
> The implementation looks correct, but throws an error.

`project.clj`:

```clojure
:dependencies [[org.clojure/clojure "1.11.1"]
               [org.apache.commons/commons-text "1.13.0"]]
```

`core.clj`:

```clojure
(ns interop.core
  (:import org.apache.commons.text.CaseUtils))

(defn camel-case [kebap]
  (CaseUtils/toCamelCase kebap true (to-array [\-])))
```
{{% /expand %}}