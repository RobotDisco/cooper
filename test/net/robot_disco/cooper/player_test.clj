(ns net.robot-disco.cooper.player-test
  (:require  [clojure.test :as t :refer [deftest is]]
             [net.robot-disco.cooper.player :as sut]))

(deftest player-move-left
  (let [old {:row 1 :col 1}
        new (sut/player-left old)]
    (is (= (-> old sut/player-col dec) (sut/player-col new)))
    (is (= (sut/player-row old) (sut/player-row new)))))

(deftest player-on-edge-move-left
  (let [old {:row 1 :col sut/MIN-ROW}
        new (sut/player-left old)]
    (is (= (sut/player-col old) (sut/player-col new)))
    (is (= (sut/player-row old) (sut/player-row new)))))

(deftest player-move-right
  (let [old {:row 1 :col 1}
        new (sut/player-right old)]
    (is (= (-> old sut/player-col inc) (sut/player-col new)))
    (is (= (sut/player-row old) (sut/player-row new)))))

(deftest player-on-edge-move-right
  (let [old {:row 1 :col sut/MAX-COL}
        new (sut/player-right old)]
    (is (= (sut/player-col old) (sut/player-col new)))
    (is (= (sut/player-row old) (sut/player-row new)))))

(deftest player-move-up
  (let [old {:row 1 :col 1}
        new (sut/player-up old)]
    (is (= (-> old sut/player-row inc) (sut/player-row new)))
    (is (= (sut/player-col old) (sut/player-col new)))))

(deftest player-on-edge-move-up
  (let [old {:row sut/MAX-ROW :col 1}
        new (sut/player-up old)]
    (is (= (sut/player-row old) (sut/player-row new)))
    (is (= (sut/player-col old) (sut/player-col new)))))

(deftest player-move-down
  (let [old {:row 1 :col 1}
        new (sut/player-down old)]
    (is (= (-> old sut/player-row dec) (sut/player-row new)))
    (is (= (sut/player-col old) (sut/player-col new)))))

(deftest player-on-edge-move-down
  (let [old {:row sut/MIN-ROW :col 1}
        new (sut/player-down old)]
    (is (= (sut/player-row old) (sut/player-row new)))
    (is (= (sut/player-col old) (sut/player-col new)))))
