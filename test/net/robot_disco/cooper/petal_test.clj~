(ns net.robot-disco.cooper.core-test
  (:require [net.robot-disco.cooper.core :as sut]
            [clojure.test :as t :refer [deftest is]]))

(deftest visible-hidden
  (let [p (sut/make-visible-petal 0 10)]
    (is (sut/hidden? (sut/tick p)))))

(deftest hidden-visible
  (let [p (sut/make-hidden-petal 0)]
    (is (not (sut/hidden? (sut/tick p))))))
