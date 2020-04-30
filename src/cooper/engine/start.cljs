(ns cooper.engine.start
  (:require [cooper.core :as c]
            [play-cljc.gl.core :as pc]
            [goog.events :as events]))

(defn engine-loop
  "Prepare and run the game event loop,

  Args:
  engine-state - current `play-cljc` game engine runtime state

  Returns: Next iteration of `play-cljc` game runtime state."
  [engine-state]
  (let [game (c/tick engine-state)]
    (js/requestAnimationFrame
     (fn [ts]
       (let [ts (* ts 0.001)]
         (engine-loop (assoc engine-state
                           :delta-time (- ts (:total-time game))
                           :total-time ts)))))))

(defn keycode->keyword
  "Convert platform-specific `keycode` into platform-agnostic game logic keywords."
  [keycode]
  (condp = keycode
    37 :left
    39 :right
    38 :up
    40 :down
    nil))

(defn listen-for-keys!
  "Attach event listeners to game for processing keyboard input."
  []
  (events/listen js/window "keydown"
                 (fn [event]
                   (when-let [k (keycode->keyword (.-keyCode event))]
                     (swap! c/*state update :pressed-keys conj k))))
  (events/listen js/window "keyup"
                 (fn [event]
                   (when-let [k (keycode->keyword (.-keyCode event))]
                     (swap! c/*state update :pressed-keys disj k)))))

(defn resize!
  "Event handler for scaling game graphics if browser window is resized.

  Args:
  context - HTML canvas context."
  [context]
  (let [display-width context.canvas.clientWidth
        display-height context.canvas.clientHeight]
    (set! context.canvas.width display-width)
    (set! context.canvas.height display-height)))

(defn listen-for-resize!
  "Set up game window resize handler"
  [context]
  (events/listen js/window "resize"
                 (fn [_]
                   (resize! context))))


;; Set up HTML canvas and start the game loop.
;; We need to defonce this because we don't want this to reinitialize
;; and restart the game every time we re-evaluate this file during
;; development.
(defonce canvas-context
  (let [canvas (js/document.querySelector "canvas")
        context (.getContext canvas "webgl2")
        initial-engine-state (assoc (pc/->game context)
                                    :delete-time 0
                                    :total-time 0)]
    (listen-for-keys!)
    (resize! context)
    (listen-for-resize! context)
    (c/init initial-engine-state)
    (engine-loop initial-engine-state)
    context))

