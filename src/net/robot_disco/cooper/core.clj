(ns net.robot-disco.cooper.core)

(def ^:const MAX_SIZE 100)

(defn make-petal
  "rate - What percentage of the petal disappears with every tick?"
  [rate]
  [MAX_SIZE rate])

(defn petal-size [[size]]
  size)

(defn tick [[_ rate :as petal]]
  (-> petal
      (update 0 #(max (- % rate) 0))))

;; Snippets to keep around for REPL-driven development 
(comment

  (println "Hello World")
  (take 20 (iterate tick (make-petal 10))))

