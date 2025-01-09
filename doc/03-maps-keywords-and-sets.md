# Maps, Keywords, and Sets

Create a map literal:

```clojure
{"title" "Parallels" "artist" "Fates Warning" "year" 1991}
```

Create a map using a function:

```clojure
(hash-map "title" "Parallels" "artist" "Fates Warning" "year" 1991)
```

Retrieve a value from a map by its key:

```clojure
(def album {"title" "Parallels" "artist" "Fates Warning" "year" 1991})
(get album "title") ; "Parallels"
```

Same, but using the map like a function:

```clojure
(album "title") ; "Parallels"
```

If the key is not found, `nil` is returned:

```clojure
(get album "duration") ; nil
(album "duration") ; nil
```

Use _keywords_ instead of strings as map keys:

```clojure
(def song {:title "Pale Fire" :artist "Fates Warning" :duration "4m17s"})
(get song :title) ; "Pale Fire"
(song :title) ; "Pale Fire"
```

Use the keyword like a function for map lookup:

```clojure
(:title song) ; "Pale Fire"
```

Add a new entry (key-value pair) to a map:

```clojure
(assoc song :album "Inside Out")
```

Add multiple new entries to a map:

```clojure
(assoc song :album "Inside Out" :year 1994)
```

Remove entries from a map:

```clojure
(dissoc song :artist :duration)
```

Non-existant keys are ignored silently:

```clojure
(dissoc song :artist :duration :genre :top-chart-position)
```

Retrieve all the keys and values, respectively, from a map:

```clojure
(keys song) ; (:title :artist :duration)
(vals song) ; ("Pale Fire" "Fates Warning" "4m17s")
```

The order of keys is not guaranteed, but matches the order of the values. Use
`sorted-map` to create a map with keys kept in sorted order.

Check whether or not a map contains a key:

```clojure
(contains? song :title) ; true
(contains? song :genre) ; false
```

Create a set literal:

```clojure
#{"Monument" "One" "Pale Fire"}
```

Create a set using a function:

```clojure
(hash-set "Monument" "One" "Pale Fire")
```

Check whether or not a set contains a value:

```clojure
(def songs #{"Monument" "One" "Pale Fire"})
(contains? songs "One") ; true
(contains? songs "Two") ; false
```

Lookup of keywords like a function:

```clojure
(def genres #{:metal :pop :electro})
(:metal genres) ; :metal
(:classic genres) ; nil
```

Extend a set:

```clojure
(conj genres :classic)
```

Remove a value from a set:

```clojure
(disj genres :pop)
```
