+++
title = "Spec"
weight = 15
+++

Use `clojure.spec` from the REPL:

```clojure
(require '[clojure.spec.alpha :as s])
```

Write a _spec_ using a predicate function:

```clojure
(s/valid? number? 37) ; true
(s/valid? number? :foo) ; false
```

Combine predicates using _and_ logic:

```clojure
(def positive-number (s/and number? #(> % 0)))
(s/valid? positive-number 9) ; true
(s/valid? positive-number 0) ; false
```

Combine predicates using _or_ logic:

```clojure
(def number-or-string (s/or :numeric number? :stringy string?))
(s/valid? number-or-string 13) ; true
(s/valid? number-or-string "99") ; true
(s/valid? number-or-string :gotcha) ; false
```

The additional keyword preceding the predicate function is required for feedback.

Create a spec for collections of certain things:

```clojure
(def str-coll (s/coll-of string?))
(s/valid? str-coll ["this" "and" "that"]) ; true
(s/valid? str-coll ["this" :and "that"]) ; false
```

Create a spec with rules for succession:

```clojure
(def chess-move (s/cat :s1 string? :n1 number? :s2 string? :n2 number?))
(s/valid? chess-move ["A" 3 "C" 5]) ; true
(s/valid? chess-move [:A 3 "C" "5"]) ; false
```

Specify required and optional keys for a map:

```clojure
(def employee
  (s/keys :req-un [:acme.core/name
                   :acme.core/salary]
          :opt-un [:acme.core/revenue]))
```

The `req` and `opt` in `:req-un` and `:opt-un` stand for _required_
and _optional_, respectively. The `un` stands for _unqualified_: The
keys in the spec are namespace-qualified, but the ones in the
validated maps need not be:

```clojure
(s/valid? employee {:name "Dilbert" :salary 120000}) ; true
(s/valid? employee {:name "Topper" :salary 130000 :revenue 500000}) ; true
(s/valid? employee {:name "Dogbert" :salary 150000 :position "consultant"}) ; true
(s/valid? employee {:name "Ashok"}) ; false
```

Use keyword shortcuts when defining specs in their respective namespace:

```clojure
(def employee
  (s/keys :req-un [::name ::salary]
          :opt-un [::revenue]))
```

Register and use a spec globally:

```clojure
(s/def :acme.core/employee employee)
(s/valid? :acme.core/employee {:name "Alice" :salary 115000}) ; true
```

Same, but using shortcuts for the keywords within the same namespace:

```clojure
(s/def ::employee employee)
(s/valid? ::employee {:name "Alice" :salary 115000}) ; true
```

Further constrain the values for the specified keys in the map:

```clojure
(def employee
  (s/keys :req-un [::name ::salary]
          :opt-un [::revenue]))

(s/def ::name string?)
(s/def ::salary number?)
(s/def ::revenue number?)

(s/valid? ::employee {:name "Alice" :salary 115000}) ; true
(s/valid? ::employee {:name :alice :salary 115000}) ; false
(s/valid? ::employee {:name "Alice" :salary "too little"}) ; false
```

Explain the cause of a spec mismatch:

```clojure
(s/explain ::employee {:name :alice :salary 115000})
;; :alice - failed: string? in: [:name] at: [:name] spec: :acme.core/name
(s/explain ::employee {:name "Alice" :salary "too little"})
;; "too little" - failed: number? in: [:salary] at: [:salary] spec: :acme.core/salary
```

Return value for matching spec, and `invalid` for mismatch:

```clojure
(s/conform ::employee {:name "Alice" :salary 115000}) ; {:name "Alice", :salary 115000}
(s/conform ::employee {:name :alice :salary 115000}) ; :clojure.spec.alpha/invalid
```

Define a function for which to write a spec for:

```clojure
(defn calc-bonus [employee percentage]
  (let [factor (/ percentage 100)]
    (+ (if (:revenue employee)
         (* factor (:revenue employee))
         0)
       (* (:salary employee) factor))))

(calc-bonus {:name "Alice" :salary 115000} 1.25) ; 1437.5
(calc-bonus {:name "Topper" :salary 130000 :revenue 500000} 1.25) ; 7875.0
```

Create a spec for the function's arguments and return values:

```clojure
(s/fdef calc-bonus
  :args (s/cat :employee ::employee
               :percentage number?)
  :ret number?)
```

Activate the function spec:

```clojure
(require '[clojure.spec.test.alpha :as stest])
(stest/instrument 'acme.core/calc-bonus)

(calc-bonus {:name "Alice" :salary 115000} 1.25) ; 1437.5

(calc-bonus {:name "Alice"} 1.25)
;; Execution error - invalid arguments to acme.core/calc-bonus at (REPL:94).
;; {:name "Alice"} - failed: (contains? % :salary) at: [:employee] spec: :acme.core/employee
```

## Exercises

For the following exercises, a Leiningen project called `music` and a
`music.core` module in `music/src/music/core.clj` with the following
content is assumed:

```clojure
(ns music.core
  (:require [clojure.spec.alpha :as s]))
```

### Songs and Musicians

Write and register two specs for maps:

1. for a song with a name and a duration string, and
2. for a musician with a name and an instrument.

Hint: Use the `clojure.spec.alpha/keys` function and the `string?`
predicate.

Test:

```clojure
(s/valid? ::song {:name "Pale Fire" :duration "4m17s"}) ; true
(s/valid? ::song {:name "Pale Fire" :duration 257}) ; false
(s/valid? ::musician {:name "Jim Matheos" :instrument "Guitar"}) ; true
(s/valid? ::musician {:name "Jim Matheos" :instrument :keytar}) ; false
```

{{% expand title="Solution" %}}
```clojure
(s/def ::name string?)
(s/def ::duration string?)
(s/def ::instrument string?)
(s/def ::song (s/keys :req-un [::name ::duration]))
(s/def ::musician (s/keys :req-un [::name ::instrument]))
```
{{% /expand %}}

### Bands and Albums

Write and register two specs for maps:

1. for a band with a name and a vector of musicians, and
2. for an album with a name and a vector of songs.

Hint: Use the `keys` and `coll-of` function from the
`clojure.spec.alpha` namespace.

Test:

```clojure
(def jim {:name "Jim Matheos" :instrument "Guitar"})
(def ray {:name "Ray Alder" :instrument "Vocals"})
(def fates-warning {:name "Fates Warning" :members [jim ray]})
(def coldplay {:name "Coldplay" :members [:chris :jonny]})

(def pale-fire {:name "Pale Fire" :duration "4m17s"})
(def apparition {:name "The Apparition" :duration "5m50s"})
(def best-of {:name "Best of Fates Warning" :songs [pale-fire apparition]})
(def worst-of {:name "Best of Coldplay" :songs [:viva-la-vida :clocks]})

(s/valid? ::band fates-warning) ; true
(s/valid? ::band coldplay) ; false
(s/valid? ::album best-of) ; true
(s/valid? ::album worst-of) ; false
```

{{% expand title="Solution" %}}
```clojure
(s/def ::name string?)
(s/def ::duration string?)
(s/def ::instrument string?)
(s/def ::song (s/keys :req-un [::name ::duration]))
(s/def ::musician (s/keys :req-un [::name ::instrument]))

(s/def ::members (s/coll-of ::musician))
(s/def ::songs (s/coll-of ::song))
(s/def ::band (s/keys :req-un [::name ::members]))
(s/def ::album (s/keys :req-un [::name ::songs]))
```
{{% /expand %}}

### Song and Album Duration

Write a spec for the [`parse-duration`
function](/14-tests/#flexible-duration-parsing). It shall accept a
string and return an integer.

Hint: Copy the function's code into the current project. Use the
`fdef` function from the `clojure.spec.alpha` namespace and the
`instrument` function from the `clojure.spec.test.alpha` namespace.

Test:

```clojure
(require '[clojure.spec.test.alpha :as stest])
(stest/instrument 'music.core/parse-duration)
(parse-duration "3m15s") ; 195
(parse-duration 300)
;; Execution error - invalid arguments to music.core/parse-duration at (REPL:106).
;; 300 - failed: string? at: [:dur]
```

{{% expand title="Solution" %}}
```clojure
(s/fdef parse-duration
  :args (s/cat :dur string?)
  :ret number?)
```
{{% /expand %}}