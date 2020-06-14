(ns net.robot-disco.cooper.core-test
  (:require [net.robot-disco.cooper.core :as sut]
            [net.robot-disco.cooper.player :as pl]
            [net.robot-disco.cooper.petal :as pt]
            [clojure.test :as t :refer [deftest is]]))

(deftest player-fell-in-water
  (let [game (sut/make-game (pl/make-player) [(pt/make-petal true 100 1 '() '())])]
    (is (sut/player-dead? game))))

(deftest player-did-not-fall-in-water
  (let [game (sut/make-game (pl/make-player) [(pt/make-petal false 100 1 '() '())])]
    (is (not (sut/player-dead? game)))))
