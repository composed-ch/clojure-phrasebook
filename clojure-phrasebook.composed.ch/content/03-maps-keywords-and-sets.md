+++
title = "Maps, Keywords, and Sets"
weight = 3
+++

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
(def song {:title "Pale Fire"
           :artist "Fates Warning"
           :duration "4m17s"})
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
;; {:title "Pale Fire", :artist "Fates Warning",
;;  :duration "4m17s", :album "Inside Out"}
```

Add multiple new entries to a map:

```clojure
(assoc song :album "Inside Out" :year 1994)
;; {:title "Pale Fire", :artist "Fates Warning",
;;  :duration "4m17s", :album "Inside Out", :year 1994}
```

Remove entries from a map:

```clojure
(dissoc song :artist :duration) ; {:title "Pale Fire"}
```

Non-existant keys are ignored silently:

```clojure
(dissoc song :artist :duration :genre :top-chart-position)
;; {:title "Pale Fire"}
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
(conj genres :classic) ; #{:metal :pop :electro :classic}
```

Remove a value from a set:

```clojure
(disj genres :pop) ; #{:metal :electro}
```

## Exercises

### Change a Map

Given the following map:

```clojure
(def server {:name "Persephone" :uptime "13d7h5m43s" :active false})
```

Write an expression to remove the `:uptime` key (with its value) from the map,
to update the `:active` value to `false` in the map, and to add the key
`:decommissioned` with the value `2025` to the map.

Hint: Use the `assoc` function to alter and add a key-value pair, and the `disj`
function to remove a key-value pair.

Test: The map `{:name "Persephone" :active false :decommissioned 2025}` is returned.

{{% expand title="Solution" %}}
```clojure
(def server {:name "Persephone" :uptime "13d7h5m43s" :active false})
(assoc (assoc (dissoc server :uptime) :active false) :decommissioned 2025)
```
{{% /expand %}}

### Change a Set

Given the following set:

```clojure
(def members #{"Steve" "Dave" "Adrian" "Bruce" "Nicko"})
```

Write an expression to remove the value `"Adrian"` from the set, and to add the
value `"Janick"` to the set.

Hint: Use the `disj` function to remove a value, and the `conj` function to add
a value.

Test: The set `{"Steve" "Dave" "Bruce" "Nicko" "Janick"}` is returned.

{{% expand title="Solution" %}}
```clojure
(def members #{"Steve" "Dave" "Adrian" "Bruce" "Nicko"})
(conj (disj members "Adrian") "Janick")
```
{{% /expand %}}

### Combine Maps and Sets

Given the following map and set:

```clojure
(def guitarists #{"Dave" "Adrian" "Janick"})
(def song {:title "Paschendale" :duration "8m28s"})
```

Write an expression which first adds the `guitarists` set with the key `:solos`
to the `song` map, and then retrieves the `:solos` set again from the map,
thereby removing the value `"Janick"` from the set.

Hint: Use the `assoc` and `disj` functions.

Test: The set `{"Dave" "Adrian"}` is returned.

{{% expand title="Solution" %}}
```clojure
(def guitarists #{"Dave" "Adrian" "Janick"})
(def song {:title "Paschendale" :duration "8m28s"})
(disj (:solos (assoc song :solos guitarists)) "Janick")
```
{{% /expand %}}
