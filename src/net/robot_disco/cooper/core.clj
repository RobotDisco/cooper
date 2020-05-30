(ns net.robot-disco.cooper.core
  (:require [net.robot-disco.cooper.petal :as p]
            [clojure.string :as str]))

(defn my-loop []
  (loop [game-board (take 32 (repeatedly (fn [] (p/make-hidden 10 (repeatedly #(+ 1 (rand-int 20))) (repeatedly #(+ 1 (rand-int 10)))))))]
    (loop [row (take 8 game-board)
           rest (drop 8 game-board)]
      (let [string (map #(format "%03d%s" (get % 1) (if (p/hidden? %) "h" " ")) row)
            string (str/join " " string)]
        (println string)
        (if (seq rest)
          (recur (take 8 rest)
                 (drop 8 rest))
          (newline))))
    (if (= (read-line) "q")
      nil
      (recur (map p/tick game-board)))))

(comment
  (require 'net.robot-disco.cooper.core :reload-all)

  (def ^:const max-rate
    "Maximum shrinking rate of petal"
    20)
  (def ^:const max-countdown
    "Maximum number of ticks petal can remain hidden for."
    10)

  ;; Create the game board and iterate some ticks
  (take 2 (iterate (partial map p/tick) (take (* rows cols) (repeatedly #(p/make-visible 100 10)))))
  (take 2 (repeatedly (fn [] (p/make-hidden 10 (repeatedly #(+ 1 (rand-int 20))) (repeatedly #(+ 1 (rand-int 10))))))))
