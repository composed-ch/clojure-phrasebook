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