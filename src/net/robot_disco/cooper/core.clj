(ns net.robot-disco.cooper.core)

(def ^:const MAX_SIZE 100)
(def ^:const MAX_COUNTDOWN 10)

(defn make-petal
  "rate - What percentage of the petal disappears with every tick?"
  [rate hidden]
  (let [val (if hidden
              MAX_COUNTDOWN
              MAX_SIZE)]
    [val rate hidden]))

(defn hidden? [[_ _ hidden]]
  hidden)

(defmulti tick
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

