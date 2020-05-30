(ns net.robot-disco.cooper.petal
  (:require [clojure.spec.alpha :as s]))

(def ^:const max-size
  "Maximum size of petal"
  100)

(s/def ::rate-seq (s/every (s/and int? pos?)))
(s/def ::countdown-seq (s/every (s/and int? pos?)))

(s/def ::visible-size (s/and (complement neg?)))
(s/def ::visible-rate pos?)
(s/def ::hidden-countdown (s/and (complement neg?)))

(s/def ::visible-petal (s/tuple false? ::visible-size ::visible-rate ::rate-seq ::countdown-seq))
(s/def ::hidden-petal (s/tuple true? ::hidden-countdown ::visible-rate ::countdown-seq))
(s/def ::petal (s/alt :visible ::visible-petal :hidden ::hidden-petal))

(defn make-visible
  "Construct a petal with a `starting-size`. The `rate` determines how quickly
  a visible petal will shrink. `rseq` is an infinite sequence that will
  determine the rate of shrinkage on every respawn. `cseq` is an infinite
  sequence that will determine the amount of time a empty petal will wait
  before respawning."
  [starting-size rate rseq cseq]
  {:pre [(s/assert ::visible-size starting-size)
         (s/assert ::visible-rate rate)
         (s/assert ::rate-seq rseq)
         (s/assert ::countdown-seq cseq)]
   :post [#(s/assert ::visible-petal %)]}
  [false starting-size rate rseq cseq])

(defn make-hidden
  "Construct a hidden petal that will become visible in `respawn-count` ticks."
  [respawn-count rseq cseq]
  {:pre [(s/assert ::hidden-countdown respawn-count)
         (s/assert ::rate-seq rseq)
         (s/assert ::countdown-seq cseq)]
   :post [#(s/assert ::hidden-petal %)]}
  [true respawn-count rseq cseq])

(defn hidden?
  "Return true if the `petal` is hidden, false if it is visible."
  [[hidden :as _petal]]
  {:pre [(s/assert ::petal _petal)]
   :post [#(s/assert boolean? %)]}
  hidden)

(defmulti tick
  "Advance a `petal`, causing it to either shrink according to its specified rate
  or to count down until the tick at which it respawns as a new visible petal."
  (fn [petal] (hidden? petal)))

(defmethod tick true
  [[_ countdown [next-rate & rrest] cseq :as petal]]
  (if (zero? countdown)
    (make-visible max-size next-rate rrest cseq)
    (update petal 1 dec)))

(defmethod tick false
  [[_ size rate rseq [next-countdown & crest] :as petal]]
  (if (pos? size)
    (update petal 1 #(- % rate))
    (make-hidden next-countdown rseq crest)))

;; Snippets to keep around for REPL-driven development 
(comment
  (take 30 (iterate tick (make-visible 100 10 (repeat 10) (repeat 10))))
  (take 30 (iterate tick (make-hidden 10 (repeatedly #(+ 1 (rand-int 10))) (repeatedly #(+ 1 (rand-int 10)))))))
