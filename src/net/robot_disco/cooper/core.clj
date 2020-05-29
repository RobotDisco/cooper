(ns net.robot-disco.cooper.core)

(defn make-petal
  "rate - What percentage of the petal disappears with every tick?"
  [rate]
  [1.0 rate])

(defn petal-size [[size _]]
  size)

(defn tick [[_ rate :as petal]]
  (update petal 0 #(- % rate)))

;; Snippets to keep around for REPL-driven development 
(comment

  (println "Hello World")
  (take 11 (iterate tick (make-petal 0.1))))

