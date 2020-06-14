(ns net.robot-disco.cooper.core
  (:require [net.robot-disco.cooper.petal :as pt]
            [clojure.string :as str]))

(def ROWS 4)
(def COLS 8)

(defn make-game [player petals]
  {:player player
   :petals petals})

(defn player-petal [player petals]
  (let [{:keys [row col]} player]
    (nth petals (+ (* ROWS row) col))))

(defn game-player
  [game]
  (:player game))

(defn game-petals
  [game]
  (:petals game))

(defn player-dead? [game]
  (let [petal (player-petal (game-player game) (game-petals game))]
    (pt/hidden? petal)))

(defn my-loop []
  (loop [game-board (take 32 (repeatedly (fn [] (pt/make-petal true 100 10 (repeatedly #(+ 1 (rand-int 20))) (repeatedly #(+ 10 (rand-int 20)))))))]
    (loop [row (take 8 game-board)
           rest (drop 8 game-board)]
      (let [string (map #(format "%03d%s" (get % 1) (if (pt/hidden? %) "h" " ")) row)
            string (str/join " " string)]
        (println string)
        (if (seq rest)
          (recur (take 8 rest)
                 (drop 8 rest))
          (newline))))
    (if (= (read-line) "q")
      nil
      (recur (map pt/advance game-board)))))

(comment
  (require 'net.robot-disco.cooper.core :reload-all))
