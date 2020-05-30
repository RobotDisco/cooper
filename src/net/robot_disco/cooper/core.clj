(ns net.robot-disco.cooper.core
  (:require [net.robot-disco.cooper.petal :as p]))

(defn my-loop []
  (loop [x (take 2 (repeatedly (fn [] (p/make-hidden 10 (repeatedly #(+ 1 (rand-int 20))) (repeatedly #(+ 1 (rand-int 10)))))))]
    (println (-> x first drop-last drop-last) " " (-> x second drop-last drop-last))
    (if (= (read-line) "q")
      nil
      (recur (map p/tick x)))))

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
