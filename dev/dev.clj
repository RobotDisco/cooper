(ns dev
  (:require [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]
            [clojure.reflect :refer [reflect]]
            [clojure.repl :refer [apropos dir doc find-doc pst source]]
            [clojure.spec.alpha :as s]
            [clojure.test :refer [run-all-tests]]
            [orchestra.spec.test :as st]))

(defmacro print-and-run
  "Print forms before we execute them."
  [& forms]
  (cons 'do (map (fn [form]
                   `(do (pprint '~form)
                        ~form)) forms)))

(defn test-all []
  (run-all-tests #"cooper.*test$"))

(println "Welcome to the cooper project development REPL!")
(println)

(print-and-run
 (set! *print-length* 20)
 (st/instrument))



