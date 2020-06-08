(ns net.robot-disco.cooper.player-test
  (:require  [clojure.test :as t :refer [deftest is]]
             [net.robot-disco.cooper.player :as sut]))

(deftest player-move-left
  (let [old {:row 1 :col 1}
        new (sut/move-left old)]
    (is (= (-> old sut/col dec) (sut/col new)))
    (is (= (sut/row old) (sut/row new)))))

(deftest player-on-edge-move-left
  (let [old {:row 1 :col sut/MIN-ROW}
        new (sut/move-left old)]
    (is (= (sut/col old) (sut/col new)))
    (is (= (sut/row old) (sut/row new)))))

(deftest player-move-right
  (let [old {:row 1 :col 1}
        new (sut/move-right old)]
    (is (= (-> old sut/col inc) (sut/col new)))
    (is (= (sut/row old) (sut/row new)))))

(deftest player-on-edge-move-right
  (let [old {:row 1 :col sut/MAX-COL}
        new (sut/move-right old)]
    (is (= (sut/col old) (sut/col new)))
    (is (= (sut/row old) (sut/row new)))))

(deftest player-move-up
  (let [old {:row 1 :col 1}
        new (sut/move-up old)]
    (is (= (-> old sut/row inc) (sut/row new)))
    (is (= (sut/col old) (sut/col new)))))

(deftest player-on-edge-move-up
  (let [old {:row sut/MAX-ROW :col 1}
        new (sut/move-up old)]
    (is (= (sut/row old) (sut/row new)))
    (is (= (sut/col old) (sut/col new)))))

(deftest player-move-down
  (let [old {:row 1 :col 1}
        new (sut/move-down old)]
    (is (= (-> old sut/row dec) (sut/row new)))
    (is (= (sut/col old) (sut/col new)))))

(deftest player-on-edge-move-down
  (let [old {:row sut/MIN-ROW :col 1}
        new (sut/move-down old)]
    (is (= (sut/row old) (sut/row new)))
    (is (= (sut/col old) (sut/col new)))))
