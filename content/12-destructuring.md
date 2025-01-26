+++
title = "Destructuring"
weight = 12
+++

Destructure a vector function argument:

```clojure
(def songs ["Wish" "Monument" "Firefly" "One" "Guardian"])

(defn reverse-five [[e d c b a]]
  (str "1) " a ", 2) " b ", 3) " c ", 4) " d ", 5) " e))

(reverse-five songs) ; "1) Guardian, 2) One, 3) Firefly, 4) Monument, 5) Wish"
```

Ignore entries at the beginning and in the middle explicitly (using
`_`), and entries at the end implicitly (omitting them):

```clojure
(def songs ["Wish" "Monument" "Firefly" "One" "Guardian"])

(defn reverse-some [[_ d _ b]]
  (str "1) " d ", 2) " b))

(reverse-some songs) ; "1) Monument, 2) One"
```

Destructure nested vectors:

```clojure
(def songs-albums [["Wish" "FWX"] ["One" "Disconnected"] ["Monument" "Inside Out"]])

(defn peek-into [[[first-song _] _ [_ third-album]]]
  (str "some song: " first-song ", some album: " third-album))

(peek-into songs-albums) ; "some song: Wish, some album: Inside Out"
```

Destructure other seqables:

```clojure
(defn starts-with-descending-triplet? [[a b c]]
  (>= a b c))

(starts-with-descending-triplet? '(9 5 3 1 8)) ; true
(starts-with-descending-triplet? [9 1 8 5 3 2]) ; false
(starts-with-descending-triplet? (iterate dec 10)) ; true
```

Destructure strings into individual characters:

```clojure
(defn row-col [[r c]]
  (str "[" r ";" c "]"))

(row-col "a5") ; "[a;5]"
(row-col "x0") ; "[x;0]"
```

Destructure a map by its keys:

```clojure
(def band [{:firstname "Jim" :lastname "Matheos" :instrument "guitar"}
           {:firstname "Bobby" :lastname "Jarzombek" :instrument "drums"}
           {:firstname "Joey" :lastname "Vera" :instrument "bass"}])

(defn describe-member [{name :firstname plays :instrument}]
  (str name " plays " plays))

(map describe-member band) ; ("Jim plays guitar" "Bobby plays drums" "Joey plays bass")
```

When destructuring maps, values come first, keys come second.

Destructure a nested map:

```clojure
(def choices {:one {:song "Monument" :band "Fates Warning"}
              :two {:song "Trial by Fire" :band "Judas Priest"}
              :three {:song "Wrathchild" :band "Iron Maiden"}})

(defn announce [{{song :song} :one {band :band} :three}]
  (str "We start with the song " song " and finish with something from " band "."))

(announce choices)
;; "We start with the song Monument and finish with something from Iron Maiden."
```

Extract a given set of values from a map by providing their keys
(`:keys`), filling in fallback values for missing keys (`:or`), and
storing the argument in its entirety (`:as`):

```clojure
(def songs [{:title "Pale Fire"
             :duration "4m17s"
             :album "Inside Out"
             :artist "Fates Warning"
             :year 1994
             :genre "Progressive Metal"}
            {:title "Ghost in the Machine"
             :duration "4m21s"
             :album "Silicon Messiah"
             :artist "Blaze Bayley"
             :year 2000}])

(defn describe-song [{:keys [:title :year :genre]
                      :or {genre "Metal"}
                      :as song}]
  (str title " (" year ", " genre ") defines " (count song) " properties."))

(map describe-song songs)
;; ("Pale Fire (1994, Progressive Metal) defines 6 properties."
;;  "Ghost in the Machine (2000, Metal) defines 5 properties."
```

## Exercises

### Parse CSV Lines

Given the following CSV lines:

```clojure
(def lines ["Jim,Matheos,Guitar",
            "Ray,Alder,Vocals",
            "Bobby,Jarzombek,Drums",
            "Joey,Vera,Bass",
            "Michael,Abdow,Guitar"])
```

Write a function `to-records` that expects and destructures a
three-element vector, and returns a map consisting of the values
extracted with appropriate keys.

Hint: Use `clojure.string/split` to turn the CSV lines into vectors.

Test: `(->> lines (map #(clojure.string/split % #",")) (map
to-records))` shall return the following sequence of maps:

```clojure
({:firstname "Jim", :lastname "Matheos", :plays "Guitar"}
 {:firstname "Ray", :lastname "Alder", :plays "Vocals"}
 {:firstname "Bobby", :lastname "Jarzombek", :plays "Drums"}
 {:firstname "Joey", :lastname "Vera", :plays "Bass"}
 {:firstname "Michael", :lastname "Abdow", :plays "Guitar"})
```

{{% expand title="Solution" %}}
```clojure
(defn to-records [[firstname, lastname, plays]]
  {:firstname firstname :lastname lastname :plays plays})
```
{{% /expand %}}

### Diagonal Distance

Write a function `diag-dist` that accepts an two coordinates encoded
in a single string of the form `[x1][y1][x2][y2]`, such as `"a1d5"`,
indicating a move on a game board (e.g. from point `a1` to point
`d5`). Calculate the diagonal distance of the points using Pythagoras'
theorem.

Hint: Destructure the string argument into four characters. Characters
can be converted to their code point using `int`. Use the notation
`\x` to refer to the character `x`.

Test: `(diag-dist "a1d5")` shall return `5.0`.

{{% expand title="Solution" %}}
```clojure
(defn diag-dist [[x1 y1 x2 y2]]
  (let [x-dist (fn [x] (inc (- (int x) (int \a))))
        x1 (x-dist x1)
        x2 (x-dist x2)
        y1 (int y1)
        y2 (int y2)
        dx (abs (- x1 x2))
        dy (abs (- y1 y2))]
    (Math/sqrt (+ (Math/pow dx 2) (Math/pow dy 2)))))
```
{{% /expand %}}

### Bonus Calculation

Given a vector of banker maps:

```clojure
(def bankers [{:name "Patrick Bateman" :revenue 7345234.95 :rate 0.01125}
              {:name "Marcus Halberstram" :revenue 945376.25 :rate 0.0125}
              {:name "Paul Allen" :revenue 15913498.90 :rate 0.0175}
              {:name "Timothy Bryce" :revenue 9754234.20 :rate 0.015}
              {:name "Luis Carruthers" :revenue 2454397.55}])
```

Write a function `bonus` that destructures a given map, calculates a
bonus as the product of the revenue and the rate, and returns a map
consisting of the banker's name and bonus.

Hint: Use a fallback value of `0.01` for the rate when destructuring
the map.

Test: `(map bonus bankers)` shall return the following sequence of maps:

```clojure
({:name "Patrick Bateman", :bonus 82633.8931875}
 {:name "Marcus Halberstram", :bonus 11817.203125}
 {:name "Paul Allen", :bonus 278486.23075000005}
 {:name "Timothy Bryce", :bonus 146313.51299999998}
 {:name "Luis Carruthers", :bonus 24543.9755})
```

{{% expand title="Solution" %}}
```clojure
(defn bonus [{:keys [:name :revenue :rate]
              :or {rate 0.01}}]
  {:name name :bonus (* rate revenue)})
```
{{% /expand %}}