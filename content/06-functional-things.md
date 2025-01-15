+++
title = "Functional Things"
weight = 6
+++

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
((both? old? metal?) vengeance) ; true
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

## Exercises

### Combining and Complementing Predicate Functions

Given the following country data:

```clojure
(def fr {:name "France" :population 68373433
         :hdi 0.91 :gdp 4.359e12})
(def it {:name "Italy" :population 58968501
         :hdi 0.906 :gdp 2.376e12})
(def ng {:name "Nigeria" :population 230842743
         :hdi 0.548 :gdp 0.252738e12})
(def bd {:name "Bangladesh" :population 174655977
         :hdi 0.670 :gdp 1.801e12})
```

Write the following predicate functions:

- `wealthy?` returns `true` if the _GDP per Capita_ is `25000` or higher, and
  `false` otherwise.
- `populous?` returns `true` if the population is `100000000` or larger, and
  `false` otherwise.
- `developed?` return `true` if the _Human Development Index_ (HDI) is `0.75` or
  higher, and `false` otherwise.

Combine these predicates to a single predicate `first-world?` that returns true
if the predicates `wealthy?` and `developed?` _do_ and `populous?` _does not_
apply.

Hint: Use `every-pred` to combine predicates and `complement` to negate a
predicate.

Test: `(first-world? fr)` and `(first-world? it)` shall return `true`;
`(first-world? ng)` and `(first-world? bd)` shall return `false`.

{{% expand title="Solution" %}}
```clojure
(def fr {:name "France" :population 68373433 :hdi 0.91 :gdp 4.359e12})
(def it {:name "Italy" :population 58968501 :hdi 0.906 :gdp 2.376e12})
(def ng {:name "Nigeria" :population 230842743 :hdi 0.548 :gdp 0.252738e12})
(def bd {:name "Bangladesh" :population 174655977 :hdi 0.670 :gdp 1.801e12})

(defn wealthy? [country]
  (>= (/ (:gdp country) (:population country)) 25000))

(defn populous? [country]
  (>= (:population country) 100000000))

(defn developed? [country]
  (>= (:hdi country) 0.75))

(def first-world? (every-pred wealthy? (complement populous?) developed?))
```
{{% /expand %}}

### Partially Applied Predicate Functions

The predicates in the last exercises used arbitrary thresholds. Re-write the
predicates so that the threshold used for comparison can be passed as their
first argument. Re-create the single-parameter predicates from the last exercise
by applying the two-parameter predicates partially with the threshold values
from before.

Hint: Use `partial` for partial function application. Re-define the predicates
`wealthy?`, `populous?`, `developed?`, and `first-world?` using `def` bindings.

Test: Same as before.

{{% expand title="Solution" %}}
```clojure
(def fr {:name "France" :population 68373433 :hdi 0.91 :gdp 4.359e12})
(def it {:name "Italy" :population 58968501 :hdi 0.906 :gdp 2.376e12})
(def ng {:name "Nigeria" :population 230842743 :hdi 0.548 :gdp 0.252738e12})
(def bd {:name "Bangladesh" :population 174655977 :hdi 0.670 :gdp 1.801e12})

(defn wealthy? [threshold country]
  (>= (/ (:gdp country) (:population country)) threshold))

(defn populous? [threshold country]
  (>= (:population country) threshold))

(defn developed? [threshold country]
  (>= (:hdi country) threshold))

(def first-world? (every-pred (partial wealthy? 25000)
                              (complement (partial populous? 100000000))
                              (partial developed? 0.75)))
```
{{% /expand %}}

### Parametrized Predicate Functions

Re-write the predicate functions once more, but this time the parametrized
predicate functions return a predicate function:

- `wealthy-pred` accepts a parameter `gdp-capita` and returns a predicate
  corresponding to `wealthy?`.
- `populous-pred` accepts a parameter `population` and returns a predicate
  corresponding to `populous?`.
- `developed-pred` accepts a parameter `hdi` and returns a predicate
  correspondng to `developed?`.

Hint: The outer function accepts a threshold parameter, and the inner, returned
function accepts a country parameter. The `first-world?` predicate provides the
actual arguments to create the predicates.

Test: Same as before.

{{% expand title="Solution" %}}
```clojure
(def fr {:name "France" :population 68373433 :hdi 0.91 :gdp 4.359e12})
(def it {:name "Italy" :population 58968501 :hdi 0.906 :gdp 2.376e12})
(def ng {:name "Nigeria" :population 230842743 :hdi 0.548 :gdp 0.252738e12})
(def bd {:name "Bangladesh" :population 174655977 :hdi 0.670 :gdp 1.801e12})

(defn wealthy-pred [gdp-capita]
  (fn [country] (>= (/ (:gdp country) (:population country)) gdp-capita)))

(defn populous-pred [population]
  (fn [country] (>= (:population country) population)))

(defn developed-pred [hdi]
  (fn [country] (>= (:hdi country) hdi)))

(def first-world? (every-pred (wealthy-pred 25000)
                              (complement (populous-pred 100000000))
                              (developed-pred 0.75)))
```
{{% /expand %}}

### Number Transformations

Write a function `transform` that accepts a vector of single argument functions
that transform a given initial number. Use `apply` to run the functions. Write a
tail-recursive function that applies the functions in the vector one by one.

Hint: Pass function literals with the vector.

Test: `(transform 2 [#(* % 10) #(+ % 2) #(/ % 2) #(- % 1)])` shall return `10`,
and `(transform 7 [])` shall return `7`.

{{% expand title="Solution" %}}
```clojure
(defn transform [x trans]
  (if (empty? trans)
    x
    (transform ((first trans) x) (rest trans))))
```
{{% /expand %}}

### Map Update Function

Given the following inventory data:

```clojure
(def pliers {:price 7.55 :stock 23 :value 173.65 :revenue 0.0})
(def hammers {:price 3.95 :stock 10 :value 39.50 :revenue 0.0})
(def nails {:price 0.05 :stock 1974 :value 98.70 :revenue 0.0})
```

Write a function `(sell quantity item)` that decreases `:stock` by the given
`quantity`, discounts `:value` by the product of `:price` and `quantity`, and
increases the `:revenue` by the same product.

Hint: Use nested `update` function calls to update the relevant fields.

Test: `(sell 1 pliers)` shall return `{:price 7.55 :stock 22 :value 166.1
:revenue 7.55}`, and `(sell 7 hammers)` shall return `{:price 3.95 :stock 3 
:value 11.85 :revenue 27.65}`.

{{% expand title="Solution" %}}
```clojure
(def pliers {:price 7.55 :stock 23 :value 173.65 :revenue 0.0})
(def hammers {:price 3.95 :stock 10 :value 39.50 :revenue 0.0})
(def nails {:price 0.05 :stock 1974 :value 98.70 :revenue 0.0})

(defn sell [quantity item]
  (update (update (update item
                          :stock
                          #(- % quantity))
                  :value
                  #(- % (* quantity (:price item))))
          :revenue
          #(+ % (* quantity (:price item)))))
```
{{% /expand %}}
