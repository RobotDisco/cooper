(ns net.robot-disco.cooper.core
  (:require [net.robot-disco.cooper.constants :as c]
            [net.robot-disco.cooper.petal :as pt]
            [clojure.string :as str]))

(defn make-game [player petals]
  {:player player
   :petals petals})

(defn player-petal [player petals]
  (let [{:keys [row col]} player]
    (nth petals (+ (* c/rows row) col))))

(defn game-player
  [{:keys [player]}]
  player)

(defn game-petals
  [{:keys [petals]}]
  petals)

(defn player-dead? [game]
  (let [petal (player-petal (game-player game) (game-petals game))]
    (pt/hidden? petal)))

;;; Demo game loop
(defn my-loop []
  (loop [game-board (repeatedly 32 (fn [] (pt/make-petal true 100 10 (repeatedly #(inc (rand-int 20))) (repeatedly #(+ 10 (rand-int 20))))))]
    (loop [row (take 8 game-board)
           rest (drop 8 game-board)]
      (let [string (map #(format "%03d%s" (get % 1) (if (pt/hidden? %) "h" " ")) row)
            string (str/join " " string)]
        (println string)
        (if (seq rest)
          (recur (take 8 rest)
                 (drop 8 rest))
          (newline))))
    (when-not (= (read-line) "q")
      (recur (map pt/advance game-board)))))

(comment
  (require 'net.robot-disco.cooper.core :reload-all))
