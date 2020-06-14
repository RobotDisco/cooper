(ns net.robot-disco.cooper.petal-test
  (:require [net.robot-disco.cooper.petal :as sut]
            [clojure.test :as t :refer [deftest is]]))

(deftest petal-advance
  (let [old (sut/make-petal false 10 1 '() '())
        new (sut/advance old)]
    (is (< (sut/countdown new) (sut/countdown old)))
    (is (= (sut/hidden? new) (sut/hidden? old)))))

(deftest petal-flip-visible-to-hidden
  (let [old (sut/make-petal false 0 1 (iterate inc 2) (iterate inc 3))
        new (sut/advance old)]
    (is (sut/hidden? new))
    (is (= sut/max-countdown (sut/countdown new)))
    (is (= (sut/current-rate new)
           (-> old sut/hidden-rates first)))
    (is (= (-> new sut/hidden-rates first)
           (-> old sut/hidden-rates (nth 1))))))

(deftest petal-flip-hidden-to-visible
  (let [old (sut/make-petal true 0 1 (iterate inc 2) (iterate inc 3))
        new (sut/advance old)]
    (is (sut/visible? new))
    (is (= (sut/countdown new) sut/max-countdown))
    (is (= (sut/current-rate new)
           (-> old sut/visible-rates first)))
    (is (= (-> new sut/visible-rates first)
           (-> old sut/visible-rates (nth 1))))))
