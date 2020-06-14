(ns net.robot-disco.cooper.player-test
  (:require  [clojure.test :as t :refer [deftest is]]
             [net.robot-disco.cooper.constants :as c]
             [net.robot-disco.cooper.player :as sut]))

(deftest player-move-left
  (let [old {:row 1 :col 1}
        new (sut/move-left old)]
    (is (= (-> old sut/col dec) (sut/col new)))
    (is (= (sut/row old) (sut/row new)))))

(deftest player-on-edge-move-left
  (let [old {:row 1 :col c/min-row}
        new (sut/move-left old)]
    (is (= (sut/col old) (sut/col new)))
    (is (= (sut/row old) (sut/row new)))))

(deftest player-move-right
  (let [old {:row 1 :col 1}
        new (sut/move-right old)]
    (is (= (-> old sut/col inc) (sut/col new)))
    (is (= (sut/row old) (sut/row new)))))

(deftest player-on-edge-move-right
  (let [old {:row 1 :col c/max-col}
        new (sut/move-right old)]
    (is (= (sut/col old) (sut/col new)))
    (is (= (sut/row old) (sut/row new)))))

(deftest player-move-up
  (let [old {:row 1 :col 1}
        new (sut/move-up old)]
    (is (= (-> old sut/row inc) (sut/row new)))
    (is (= (sut/col old) (sut/col new)))))

(deftest player-on-edge-move-up
  (let [old {:row c/max-row :col 1}
        new (sut/move-up old)]
    (is (= (sut/row old) (sut/row new)))
    (is (= (sut/col old) (sut/col new)))))

(deftest player-move-down
  (let [old {:row 1 :col 1}
        new (sut/move-down old)]
    (is (= (-> old sut/row dec) (sut/row new)))
    (is (= (sut/col old) (sut/col new)))))

(deftest player-on-edge-move-down
  (let [old {:row c/min-row :col 1}
        new (sut/move-down old)]
    (is (= (sut/row old) (sut/row new)))
    (is (= (sut/col old) (sut/col new)))))
