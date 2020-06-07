(ns net.robot-disco.cooper.player)

(def ^:const MIN-ROW 0)
(def ^:const MAX-ROW 3)

(def ^:const MIN-COL 0)
(def ^:const MAX-COL 7)


(defn player-left [player]
  (update player :col #(max MIN-COL (dec %))))

(defn player-right [player]
  (update player :col #(min MAX-COL (inc %))))

(defn player-up [player]
  (update player :row #(min MAX-ROW (inc %))))

(defn player-down [player]
  (update player :row #(max MIN-ROW (dec %))))


(defn player-row [{:keys [row]}]
  row)

(defn player-col [{:keys [col]}]
  col)

