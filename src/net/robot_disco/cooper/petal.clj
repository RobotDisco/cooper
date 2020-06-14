(ns net.robot-disco.cooper.petal
  (:require [clojure.spec.alpha :as s]
            [clojure.set :as set]))

;;; Magic numbers
(def max-countdown
  "Maximum countdown value of petal"
  100)
(def min-countdown
  "Minimum countdown value of petal"
  0)

;;; Spec for internal components
(s/def ::hidden boolean?)
(s/def ::countdown (s/and (complement neg?)))
(s/def ::rate pos?)
(s/def ::rates (s/every ::rate))

;;; Spec for public components
(s/def ::petal (s/tuple ::hidden ::countdown :rate ::rates ::rates))

(s/fdef make-petal
  :args (s/cat :hidden ::hidden
               :size ::countdown
               :current-rate ::rate
               :visible-rates ::rates
               :hidden-rates ::rates)
  :ret ::petal
  :fn #(set/subset? (-> % :args set) (-> % :ret set)))
(defn make-petal
  "Create a petal structure from the following:

  Inputs:
  `hidden`: Is the petal currently hidden?
  `countdown`: The starting countdown of the petal. Indicates petal size when visible and respawn timer when hidden.
  `current-rate`: The initial rate the petal shrinks by on each tick.
  `visible-rates`: An infinite sequence of shrinking rates for visible petals.
  `hidden-rates`: An infinite sequence of shrinking rates for hidden petals.

  Returns:
  A petal instance."
  [hidden countdown current-rate visible-rates hidden-rates]
  [hidden countdown current-rate visible-rates hidden-rates])

(s/fdef hidden?
  :args ::petal
  :ret boolean?)
(defn hidden?
  [[hidden]]
  hidden)

(s/fdef visible?
  :args ::petal
  :ret boolean?)
(def visible?
  (complement #'hidden?))

(s/fdef countdown
  :args ::petal
  :ret ::countdown)
(defn countdown [[_ countdown]]
  countdown)

(s/fdef current-rate
  :args ::petal
  :ret ::rate)
(defn current-rate [[_ _ current-rate]]
  current-rate)

(s/fdef visible-rates
  :args ::petal
  :ret ::rates)
(defn visible-rates [[_ _ _ visible-rates]]
  visible-rates)

(s/fdef hidden-rates
  :args ::petal
  :ret ::rates)
(defn hidden-rates [[_ _ _ _ hidden-rates]]
  hidden-rates)

(s/fdef advance
  :args ::petal
  :ret ::petal)
(defn advance
  "Advance a petal with the passage of time.

  A petal with a non-zero size will shrink by its current rate.

  A petal with zero size will flip from visible to hidden or vice-versa.
  The new rate will be pulled by its accompanying infinite sequence for visible
  or hidden rates, respectively."
  [petal]
  (if (zero? (countdown petal))
    (if (hidden? petal)
      (let [[next-rate & rest-of-rates] (visible-rates petal)]
        (make-petal (not (hidden? petal))
                    max-countdown
                    next-rate
                    rest-of-rates
                    (hidden-rates petal)))
      (let [[next-rate & rest-of-rates] (hidden-rates petal)]
        (make-petal (not (hidden? petal))
                    max-countdown
                    next-rate
                    (visible-rates petal)
                    rest-of-rates)))
    (make-petal (hidden? petal)
                (max 0 (- (countdown petal) (current-rate petal)))
                (current-rate petal)
                (visible-rates petal)
                (hidden-rates petal))))
