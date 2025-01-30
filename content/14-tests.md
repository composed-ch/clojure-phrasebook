+++
title = "Tests"
weight = 14
+++

Create a new Leiningen project:

```sh
lein new app music
```

In the module `music.core` (`music/src/music/core.clj`), provide a
function to parse duration strings to seconds:

```clojure
(defn parse-duration [dur]
  (let [matcher (re-matcher #"^([0-9]+)m([0-9]+)s$" dur)
        results (re-find matcher)
        minutes (Integer/parseInt (nth results 1))
        seconds (Integer/parseInt (nth results 2))]
    (+ (* minutes 60) seconds)))
```

Write a test in the module `music.core-test`
(`music/test/music/core_test.clj`) using the built-in `clojure.test`
library:

```clojure
(ns music.core-test
  (:require [clojure.test :refer :all])
  (:require [music.core :as music]))

(deftest test-parse-duration
  (is (= (music/parse-duration "3m15s") 195)))
```

Run the test from the REPL:

```clojure
(require '[music.core-test :as tests])
(tests/test-parse-duration) ; nil
```

The result `nil` stands for success (absence of errors).

Write a test using multiple `is` assertions:

```clojure
(deftest test-parse-duration
  (is (music/parse-duration "3m15s") 195)
  (is (music/parse-duration "6m12s") 312)
  (is (music/parse-duration "2m51s") 171))
```

Group tests together using `testing` with a description:

```clojure
(deftest test-parse-duration
  (testing "minutes and seconds"
    (is (= (music/parse-duration "3m15s") 195))
    (is (= (music/parse-duration "6m12s") 312))
    (is (= (music/parse-duration "2m51s") 171)))
  (testing "only minutes or seconds"
    (is (= (music/parse-duration "3m") 180))
    (is (= (music/parse-duration "17s") 17))))
```

The test fails because `parse-duration` cannot handle the input from
the second group properly.

Run all the tests of a particular namespace from the REPL:

```clojure
(ns music.core)
(require '[clojure.test :as test])
(test/run-tests 'music.core-test)
;; {:test 1, :pass 3, :fail 0, :error 2, :type :summary}
```

Run all the tests of the current namespace from the REPL:

```clojure
(ns music.core-test)
(require '[clojure.test :as test])
(test/run-tests)
;; {:test 1, :pass 3, :fail 0, :error 2, :type :summary}
```

Run all the tests using Leiningen from the shell:

```sh
lein test
```

Write a parametrized test using `are`:

```clojure
(deftest test-parse-durations
  (are [input expected] (= (music/parse-duration input) expected)
    "1s" 1
    "1m" 60
    "1m1s" 61
    "2m3s" 123
    "15m32s" 932))
```

Extend `:dependencies` in `project.clj` for test data generators:

```clojure
:dependencies [[org.clojure/clojure "1.11.1"] ; existing
               [org.clojure/test.check "1.1.1"]] ; new
```

Install the additional dependency:

```sh
lein deps
```

Create a random test data generator for positive integers:

```clojure
(require '[clojure.test.check.generators :as gen])
(def numbers-gen gen/pos-int)
```

Constrain the positive integers to non-zero values:

```clojure
(def non-zero-numbers-gen (gen/such-that (complement zero?) gen/pos-int))
```

Generate maps of test data:

```clojure
(def duration-gen (gen/hash-map :m non-zero-numbers-gen :s non-zero-numbers-gen))
```

Get a function that picks a random element from a generator:

```clojure
(gen/elements duration-gen)
```

Write a _property-based_ test that exercises a property on 100
generated inputs:

```clojure
(ns music.core-test
  (:require [clojure.test :refer :all])
  (:require [clojure.test.check :as tc])
  (:require [clojure.test.check.clojure-test :as ctest])
  (:require [clojure.test.check.generators :as gen])
  (:require [clojure.test.check.properties :as prop])
  (:require [music.core :as music]))

(def non-zero-numbers-gen (gen/such-that (complement zero?) gen/pos-int))
(def duration-gen (gen/hash-map :m non-zero-numbers-gen :s non-zero-numbers-gen))

(ctest/defspec parse-duration-not-zero 100
  (prop/for-all [duration duration-gen]
                (let [dur (str (:m duration) "m" (:s duration) "s")]
                  (> (music/parse-duration dur) 0))))
```

## Exercises

### Flexible Duration Parsing

Make all the tests pass by improving the implementation of the given
`parse-duration` function.

Hint: Define additional matching groups and use the regular expression
quantor `?` for optional groups.

Test: The provided tests shall pass, e.g. `(parse-duration "3m")`
shall return `180`, and `(parse-duration "17s")` shall return `17`.

{{% expand title="Solution" %}}
```clojure
(defn parse-duration [dur]
  (let [matcher (re-matcher #"^(([0-9]+)(m))?(([0-9]+)(s))?$" dur)
        results (re-find matcher)
        minutes (if (nil? (nth results 3)) 0 (Integer/parseInt (nth results 2)))
        seconds (if (nil? (nth results 6)) 0 (Integer/parseInt (nth results 5)))]
    (+ (* minutes 60) seconds)))
```
{{% /expand %}}

### Hours Duration Parsing

Define additional (yet failing) test cases for duration containing
hour indications, e.g. `"1h15m50s"`. Then extend the implementation of
`parse-duration` in order to make the tests pass.

Hint: Use `testing` to group the tests, and `h` as an hour indication
in the input string. Repeated multiplication with the factor `60` can
be expressed using `reduce`.

Test: `(parse-duration "2h5m3s")` shall return `7503`,
`(parse-duration "1h10m")` shall return `4200`, and `(parse-duration
"1h")` shall return 3600. All defined tests shall pass.

{{% expand title="Solution" %}}
Test:

```clojure
(ns music.core-test
  (:require [clojure.test :refer :all])
  (:require [music.core :as music]))

(deftest test-parse-duration
  (testing "hours, minutes, and seconds"
    (is (= (music/parse-duration "2h5m3s") 7503)))
  (testing "hours and minutes"
    (is (= (music/parse-duration "1h10m") 4200)))
  (testing "only hours"
    (is (= (music/parse-duration "1h") 3600))))
```

Implementation:

```clojure
(defn parse-duration [dur]
  (let [matcher (re-matcher #"^(([0-9]+)(h))?(([0-9]+)(m))?(([0-9]+)(s))?$" dur)
        results (re-find matcher)
        hours (if (nil? (nth results 3)) 0 (Integer/parseInt (nth results 2)))
        minutes (if (nil? (nth results 6)) 0 (Integer/parseInt (nth results 5)))
        seconds (if (nil? (nth results 9)) 0 (Integer/parseInt (nth results 8)))
        values [hours minutes seconds]]
    (reduce (fn [acc v] (+ (* acc 60) v)) values)))
```
{{% /expand %}}

### Property-Based Hypotenuse Test

Given a function that calculates the hypotenuse given the triangle's
short sides:

```clojure
(defn hypot [a b]
  (Math/sqrt (+ (Math/pow a 2) (Math/pow b 2))))

(hypot 3 4) ; 5.0
```

Write a property-based `hypot-longest-side` test to ensure that the
hypotenuse is longer than both `a` and `b`, but shorter than their
sum. Run it for a million samples.

Hint: Use the `pos-int` generator from above to generate the test data.

Test: All tests shall pass using `lein run`.

{{% expand title="Solution" %}}
```clojure
(ns music.core-test
  (:require [clojure.test :refer :all])
  (:require [clojure.test.check :as tc])
  (:require [clojure.test.check.clojure-test :as ctest])
  (:require [clojure.test.check.generators :as gen])
  (:require [clojure.test.check.properties :as prop]))

(def pos-int (gen/such-that (complement zero?) gen/pos-int))

(ctest/defspec hypot-longest-side 1e6
  (prop/for-all [a pos-int
                 b pos-int]
                (let [c (hypot a b)]
                  (and (> c a) (> c b) (< c (+ a b))))))
```
{{% /expand %}}