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
  (is (music/parse-duration "3m15s") 195))
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
    (is (music/parse-duration "3m15s") 195)
    (is (music/parse-duration "6m12s") 312)
    (is (music/parse-duration "2m51s") 171))
  (testing "only minutes or seconds"
    (is (music/parse-duration "3m") 180)
    (is (music/parse-duration "17s") 17)))
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

TODO: second part of the chapter on property-based tests

## Exercises

TODO: provide proper exercises

- Make all the tests pass by improving the implementation of the `parse-duration` function.
  - Hint: Use the quantors `*` and/or `?`.
  - Test: Run the tests.
- Define additional (yet failing) test cases in a group containing hour indications.
  - Hint: Use `testing` to group the tests, and `h` as an hour indication in the input string.
  - Test: Run the tests.