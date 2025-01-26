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

(defn peek [[[first-song _] _ [_ third-album]]]
  (str "some song: " first-song ", some album: " third-album))

(peek songs-albums) ; "some song: Wish, some album: Inside Out"
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