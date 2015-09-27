(ns plywood-client.test-runner
  (:require
   [cljs.test :refer-macros [run-tests]]
   [plywood-client.core-test]))

(enable-console-print!)

(defn runner []
  (if (cljs.test/successful?
       (run-tests
        'plywood-client.core-test))
    0
    1))
