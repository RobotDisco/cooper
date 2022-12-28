(ns robot-disco.cooper.core
  (:require [clojure.spec.alpha :as s]))


(comment
;; What's the dumbest UI thing we could show off?
;; A 4x7 grid of petals

  (def canvas (.getElementById js/document "drawing1"))
  (def ctx (.getContext canvas "2d"))

  (set! (.-fillStyle ctx) "rgb(255,0,0)")
  (dotimes [r 4]
    (dotimes [c 7]
      (.beginPath ctx)
      (.arc ctx (+ (* c 100) 50) (+ (* r 100) 50) 45 0 (* 2 Math/PI))
      (.stroke ctx))))

(comment
  ;; Let's start with some repl-driven development.
  ;; Can I make a petal structure and render it?
  ;; Start position-less.

  (def MAX_PETAL_SIZE
    "What is the largest size our petals can be?.

Right now this value is coupled with the number of pixels used in the UI."
    45)

  (defn size [_]
    MAX_PETAL_SIZE)

  (defn draw-petal
    "butt"
    [ctx p]
    (.beginPath ctx)
    (.arc ctx 50 50 (size p) 0 (* 2 Math/PI))
    (.stroke ctx))

  (def canvas (.getElementById js/document "drawing1"))
  (def ctx (.getContext canvas "2d"))

  (def my-petal nil)
  (draw-petal ctx my-petal)
  ) ;; Comment ends here
