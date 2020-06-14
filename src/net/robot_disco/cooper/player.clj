(ns net.robot-disco.cooper.player)

(def MIN-ROW 0)
(def MAX-ROW 3)

(def MIN-COL 0)
(def MAX-COL 7)


(defn move-left [player]
  (update player :col #(max MIN-COL (dec %))))

(defn move-right [player]
  (update player :col #(min MAX-COL (inc %))))

(defn make-player [] {:row 0 :col 0})

(defn move-up [player]
  (update player :row #(min MAX-ROW (inc %))))

(defn move-down [player]
  (update player :row #(max MIN-ROW (dec %))))

(defn row [{:keys [row]}]
  row)

(defn col [{:keys [col]}]
  col)

