# Functional Things

Pass a function as an argument to another function:

```clojure
(def painkiller {:artist "Judas Priest" :name "Painkiller"
                 :genre "Metal" :year 1990})
(def vengeance {:artist "Judas Priest" :name "Screaming for Vengeance"
                :genre "Metal" :year 1980})

(defn old? [album]
  (< (:year album) 1990))

(defn metal? [album]
  (= (:genre album) "Metal"))

(defn both? [album a? b?]
  (and (a? album) (b? album)))

(both? painkiller old? metal?) ; false
(both? vengeance old? metal?) ; true
```

Return a function from a function:

```clojure
(defn both? [a? b?]
  (fn [album] (and (a? album) (b? album))))

((both? old? metal?) painkiller) ; false
(both? old? metal?) vengeance) ; true
```

In fact, `defn` is a shortcut of `def` and `fn`.

Apply a function to a number of values:

```clojure
(apply + [0 1 2 3 4])
```

Apply a function _partially_:

```clojure
(defn chatty-increment-by [x y]
  (println "increment" y "by" x)
  (+ x y))

(def increment (partial chatty-increment-by 1))

(increment 13)
;; increment 13 by 1
;; 14
```

Create a complement function, which negates a predicate:

```clojure
(defn old? (< (:year album) 1990))
(def new? (complement old?))

(old? thriller) ; true
(new? thriller) ; false
```

Combine multiple predicate functions:

```clojure
(defn old? [album]
  (< (:year album) 1990))

(def new? (complement old?))

(new? painkiller) ; true
(new? vengeance) ; false
```

Combine multiple predicate functions (using _and_ logic):

```clojure
(def new-priest-metal?
  (every-pred (complement old?)
              (fn [x] (= (:artist x) "Judas Priest"))
              metal?))

(new-priest-metal? painkiller) ; true
(new-priest-metal? vengeance) ; false
```

Use a _function literal_ or _lambda_:

```clojure
(#(Math/sqrt (+ (Math/pow %1 2) (Math/pow %2 2))) 3 4) ; 5.0
```

Instead of named parameters, function literals use numbered parameters `%1`,
`%2`, etc. If only a single parameter is used, it can be abbreviated as `%`:

```clojure
(#(* % 2) 13) ; 26
```
