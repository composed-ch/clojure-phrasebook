# Hello, Clojure

Print "Hello, Clojure!":

```clojure
(println "Hello, Clojure!")
```

Add comments:

```clojure
;; prints "Hello, Clojure!"
(println "Hello, Clojure!") ; "Hello, Clojure!"
```

Concatenate strings:

```clojure
(str "Hello" ", " "Clojure" "!") ; "Hello, Clojure!"
```

Count the number of characters in a string:

```clojure
(count "Hello, World!") ; 13
```

Use arithmetic operations:

```clojure
(/ (* (+ 4 3) (- 2 3))
   (* (+ 2 3 4)))
;; -7/9
```

Use truncating division and compute the rest of that division:

```clojure
(quot 10 3) ; 3
(mod 10 3) ; 1
```

Compute the power and square root:

```clojure
(Math/pow 2 10) ; 1024.0
(Math/sqrt 16) ; 4.0
```

Define and call a function:

```clojure
(defn say-hi [to-whom]
  (str "Hello, " to-whom "!"))
(say-hi "Jim") ; "Hello, Jim!"
```

Define and call a function that calculates the hypotenuse of a right-angled triangle:

```clojure
;; a²+b²=c² <=> c=sqrt(a²+b²)
(defn hypotenuse [a b]
  (Math/sqrt (+ (Math/pow a 2) (Math/pow b 2))))
(hypotenuse 3 4) ; 5.0
```

## Exercises

### Circumference of a Rectangle

Define a function `circumference` that computes the circumference of a rectangle
given its sides `a` and `b`.

Hint: Use the formula `2ab`.

Test: `(circumference 3 4)` shall return `14`.

### Area of a Circle

Define a function that computes the area of a circle given its radius `r`.

Hint: Use the constant `Math/PI` and the formula `πr²`.

Test: `(area 2)` shall return `12.566370614359172`.

### Minutes and Seconds

Write a function `min-sec` that formats a given number of seconds to a string
of the format `xmys`, with `x` being the number of minutes and `y` the number
of (remaining) seconds.

Hint: Use the `quot` and `mod` functions for the calculations.

Test: `(min-sec 3578)` shall return `59m38s`.
