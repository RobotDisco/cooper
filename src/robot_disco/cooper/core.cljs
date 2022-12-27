(ns robot-disco.cooper.core)

;; What's the dumbest UI thing we could show off?
;; A 4x7 grid of petals

(def canvas (.getElementById js/document "drawing1"))
(def ctx (.getContext canvas "2d"))

(set! (.-fillStyle ctx) "rgb(255,0,0)")
(dotimes [r 4]
  (dotimes [c 7]
    (.beginPath ctx)
    (.arc ctx (+ (* c 100) 50) (+ (* r 100) 50) 45 0 (* 2 Math/PI))
    (.stroke ctx)))
