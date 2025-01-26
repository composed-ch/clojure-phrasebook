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