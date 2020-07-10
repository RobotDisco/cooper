(ns net.cooper.petal-test
  (:require [net.cooper.petal :as sut]
            [clojure.pprint :as pp]
            [clojure.test :as t :refer [deftest testing is]]
            [clojure.spec.test.alpha :as st]))

(def visible-petal-full
  {:petal/type :petal/visible :petal/size 100 :petal/rate 10})
(def visible-petal-empty
  #:petal{:type :petal/visible :size 0 :rate 10})

(def invisible-petal-pending
  #:petal{:type :petal/hidden :countdown 100})
(def invisible-petal-triggers
  #:petal{:type :petal/hidden :countdown 0})

(defn instrument-fixture [f]
  (st/instrument `[sut/advance-petal-hidden sut/advance-petal-visible])
  (f)
  (st/unstrument `[sut/advance-petal-hidden sut/advance-petal-visible]))

(t/use-fixtures :once instrument-fixture)

(defn spec-check [fn-to-check options]
  (let [results (st/check [fn-to-check] options)]
    (if (some :failure results)
      (do
        (println "\nFailed specs:")
        (doseq [result results
                :when (:failure result)]
          (println (:sym result))
          (pp/pprint (or (ex-data (:failure result))
                         (:failure result)))))
      true)))


(deftest visible-petal-test
  (testing "Full petal shrinks"
    (let [shrunk-petal (sut/advance-petal visible-petal-full)]
      (is (= (+ (:petal/size shrunk-petal) (:petal/rate shrunk-petal))
             (:petal/size visible-petal-full)))))
  (testing "Empty petal flips"
    (let [flipped-petal (sut/advance-petal visible-petal-empty)]
      (is (= (set (keys flipped-petal))
             (set (keys invisible-petal-pending)))))))

(deftest invisible-petal-test
  (testing "Pending respawn counts down"
    (let [pending-respawn (sut/advance-petal invisible-petal-pending)]
      (is (= (inc (:petal/countdown pending-respawn))
             (:petal/countdown invisible-petal-pending)))))
  (testing "Hidden petal respawns when countdown finished"
    (let [flipped-petal (sut/advance-petal invisible-petal-triggers)]
      (is (= (set (keys flipped-petal))
             (set (keys visible-petal-full)))))))

(deftest spec-checks
  (is (spec-check `advance-petal-hidden {}))
  (is (spec-check `advance-petal-visible {})))
