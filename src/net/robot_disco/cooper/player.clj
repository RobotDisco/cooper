(ns net.robot-disco.cooper.player
  (:require [net.robot-disco.cooper.constants :as c]))

(defn move-left [player]
  (update player :col #(max c/min-col (dec %))))

(defn move-right [player]
  (update player :col #(min c/max-col (inc %))))

(defn make-player [] {:row c/min-row :col c/min-col})

(defn move-up [player]
  (update player :row #(min c/max-row (inc %))))

(defn move-down [player]
  (update player :row #(max c/min-row (dec %))))

(defn row [{:keys [row]}]
  row)

(defn col [{:keys [col]}]
  col)

