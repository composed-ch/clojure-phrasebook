# Vectors and Lists

Create a vector literal:

```clojure
[1 2 3]
```

Create a vector literal with elements of different types:

```clojure
[42 13.5 "hello" true nil]
```

Create a nested vector:

```clojure
[1 [2 3 [4 5] 6] 7]
```

Create a vector using a function:

```clojure
(vector 1 2 3) ; [1 2 3]
```

Get the first element of a vector:

```clojure
(first [1 2 3]) ; 1
(first [1]) ; 1
(first []) ; nil
```

Get all elements _except_ the first of a vector:

```clojure
(rest [1 2 3]) ; (2 3)
(rest [1]) ; ()
(rest []) ; ()
```

A _sequence_ rather than a vector is returned.

Tet the third element of a vector:

```clojure
(first (rest (rest [1 2 3 4 5]))) ; 3
```

To get to the nth element requires `n-1` calls of `rest`, followed by a single
call of `first`. The `nth` function is more convenient (and faster):

```clojure
(nth [1 2 3 4 5] 2) ; 3
```

The index is zero-based. The same can be achieved by using the vector like a
function:

```clojure
([1 2 3 4 5] 2) ; 3
```

Add an element to the end of a vector:

```clojure
(def songs ["Pale Fire" "The Eleventh Hour" "One"])
(conj songs "Monument")
;; ["Pale Fire" "The Eleventh Hour" "One" "Monument"]
```

The original vector is _not_ modified, but a new, larger vector is created,
sharing the common data with the original vector.

Add an element to the front of a vector:

```clojure
(def amigos ["Dave" "Janick"])
(cons "Adrian" amigos) ; ("Adrian" "Dave" "Janick")
```

Again, a sequence is returned, and the original vector is not modified.

Create a list literal:

```clojure
'(1 2 3)
```

Create a list using a function:

```
(list 1 2 3) ; (1 2 3)
```

The leading `'` can be left away for empty lists and within list literals:

```clojure
()
'(1 2 ("hello" true (1.25 "what" 3) 2.5 5.0))
```

Apply additional operations to a list:

```clojure
(def years (list 1987 1992 2003 2007 2012))
(count years) ; 5
(first years) ; 1987
(rest years) ; (1992 2003 2007 2012)
(nth year 2) ; 2003
```

Extend a list:

```clojure
(def days (list "Tuesday" "Wednesday" "Thursday"))
(conj days "Monday") ; ("Monday" "Tuesday" "Wednesday" "Thursday")
(cons "Monday" days) ; ("Monday" "Tuesday" "Wednesday" "Thursday")
```

Applied to a list, both `conj` and `cons` add new elements to the front for
better efficiency.

## Exercises

### Accessing Elements

Given the following vector:

```clojure
(def numbers [1 2 [3 [4 5 6 [7 8] 9]]])
```

Write an expression that returns the element `8`.

Hint: Use the `first` and `rest` functions.

Test: The element `8` is returned.

### Extending a List

Given the following list:

```clojure
(def numbers '(3 4 5 6 7 8 9))
```

Write an expression that appends the elements `0`, `1`, and `2` to the front of
the list. 

Hint: Use the `cons` function.

Test: The sequence `(0 1 2 3 4 5 6 7 8 9)` is returned.

### Extending a List

Given the following vector:

```clojure
(def numbers [3 4 5 6 7 8])
```

Write an expression that appends the elements `0`, `1`, and `2` to the front,
and the element `9` to the end.

Hint: Use the `cons` and `conj` functions. Unlike `cons`, `conj` returns a
vector.

Test: The sequence `(0 1 2 3 4 5 6 7 8 9)` is returned.
