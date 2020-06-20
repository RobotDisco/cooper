(ns net.robot-disco.cooper.player
  (:require [net.robot-disco.cooper.constants :as c]
            [clojure.spec.alpha :as s]))

(s/def ::row int?)
(s/def ::col int?)

(s/def ::player {:row ::row, :col ::col})

(s/fdef make-player :ret ::player)
(defn make-player [] {:row c/min-row :col c/min-col})

(s/fdef move-left :args (s/cat :player ::player) :ret ::player)
(defn move-left [player]
  (update player :col #(max c/min-col (dec %))))

(s/fdef move-right :args (s/cat :player ::player) :ret ::player)
(defn move-right [player]
  (update player :col #(min c/max-col (inc %))))

(s/fdef move-up :args (s/cat :player ::player) :ret ::player)
(defn move-up [player]
  (update player :row #(min c/max-row (inc %))))

(s/fdef move-down :args (s/cat :player ::player) :ret ::player)
(defn move-down [player]
  (update player :row #(max c/min-row (dec %))))

(s/fdef row :args (s/cat :player ::player) :ret ::row)
(defn row [{:keys [row]}]
  row)

(s/fdef col :args (s/cat :player ::player) :ret ::col)
(defn col [{:keys [col]}]
  col)

