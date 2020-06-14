(ns net.robot-disco.cooper.player-test
  (:require  [clojure.test :as t :refer [deftest is]]
             [net.robot-disco.cooper.constants :as c]
             [net.robot-disco.cooper.player :as sut]))

(deftest player-move-left
  (let [old {:row 1 :col 1}
        new (sut/move-left old)]
    (is (= (sut/col new) (-> old sut/col dec)))
    (is (= (sut/row new) (sut/row old)))))

(deftest player-on-edge-move-left
  (let [old {:row 1 :col c/min-row}
        new (sut/move-left old)]
    (is (= (sut/col new) (sut/col old)))
    (is (= (sut/row new) (sut/row old)))))

(deftest player-move-right
  (let [old {:row 1 :col 1}
        new (sut/move-right old)]
    (is (= (sut/col new) (-> old sut/col inc)))
    (is (= (sut/row new) (sut/row old)))))

(deftest player-on-edge-move-right
  (let [old {:row 1 :col c/max-col}
        new (sut/move-right old)]
    (is (= (sut/col new) (sut/col old)))
    (is (= (sut/row new) (sut/row old)))))

(deftest player-move-up
  (let [old {:row 1 :col 1}
        new (sut/move-up old)]
    (is (= (sut/row new) (-> old sut/row inc)))
    (is (= (sut/col new) (sut/col old)))))

(deftest player-on-edge-move-up
  (let [old {:row c/max-row :col 1}
        new (sut/move-up old)]
    (is (= (sut/row new) (sut/row old)))
    (is (= (sut/col new) (sut/col old)))))

(deftest player-move-down
  (let [old {:row 1 :col 1}
        new (sut/move-down old)]
    (is (= (sut/row new) (-> old sut/row dec)))
    (is (= (sut/col new) (sut/col old)))))

(deftest player-on-edge-move-down
  (let [old {:row c/min-row :col 1}
        new (sut/move-down old)]
    (is (= (sut/row new) (sut/row old)))
    (is (= (sut/col new) (sut/col old)))))
