(ns net.robot-disco.cooper.core)

(def
  ^:const max-size
  "Maximum size of petal"
  100)
(def ^:const max-countdown
  "Maximum number of ticks petal can remain hidden for."
  10)

(defn make-visible-petal
  "Construct a petal with a `starting-size`. The `rate` determines how
  quickly a visible petal will shrink."
  [starting-size rate]
  {:pre [(not (neg? starting-size))
         (<= starting-size max-size)
         (pos? rate)]}
  [false starting-size rate])

(defn make-hidden-petal
  "Construct a hidden petal that will become visible in `respawn-count` ticks."
  [respawn-count]
  {:pre [(not (neg? respawn-count))]}
  [true respawn-count])

(defn hidden?
  "Return true if the `petal` is hidden, false if it is visible."
  [[hidden :as _petal]]
  hidden)

(defmulti tick
  "Advance a `petal`, causing it to either shrink according to its specified rate
  or to count down until the tick at which it respawns as a new visible petal."
  (fn [petal] (hidden? petal)))

(defmethod tick true
  [[_ countdown :as petal]]
  (if (zero? countdown)
    (make-visible-petal 100 10)
    (update petal 1 dec)))

(defmethod tick false
  [[_ size rate :as petal]]
  (if (zero? size)
    (make-hidden-petal 10)
    (update petal 1 #(- % rate))))

;; Snippets to keep around for REPL-driven development 
(comment

  (println "Hello World")
  (take 30 (iterate tick (make-visible-petal 100 10))))

