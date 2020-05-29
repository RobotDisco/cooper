(ns net.robot-disco.cooper.core)

(def
  ^:const max-size
  "Maximum size of petal"
  100)
(def ^:const max-countdown
  "Maximum number of ticks petal can remain hidden for."
  10)

(defn make-petal
  "Construct a petal. A `hidden` petal is one that is waiting to respawn; a player will die if they try to move here.  non-hidden petal is one the player can step on. The `rate` determines how quickly a visible petal will string or how long it will taken a hidden petal to respawn into a visible one."
  [rate hidden]
  (let [val (if hidden
              max-countdown
              max-size)]
    [val rate hidden]))

(defn hidden?
  "Return true if the petal is hidden, false if the petal is visible."
  [[_ _ hidden]]
  hidden)

(def visible?
  "Return true if the petal is visible, false if the petal is hidden."
  (complement #'hidden?))

(defmulti tick
  "Advance a `petal`, causing it to either shrink according to its specified rate
  or to count down until the tick at which it respawns as a new visible petal."
  (fn [petal] (hidden? petal)))

(defmethod tick true
  [[countdown rate :as petal]]
  (if (zero? countdown)
    (make-petal 10 false)
    (update petal 0 #(- % rate))))

(defmethod tick false
  [[size rate :as petal]]
  (if (zero? size)
    (make-petal 1 true)
    (update petal 0 #(- % rate))))

;; Snippets to keep around for REPL-driven development 
(comment

  (println "Hello World")
  (take 30 (iterate tick (make-petal 10 false))))

