(ns cooper.start
  (:require [cooper.core :as c]
            [play-cljc.gl.core :as pc]
            [goog.events :as events]))

(defn game-loop [game]
  (let [game (c/tick game)]
    (js/requestAnimationFrame
     (fn [ts]
       (let [ts (* ts 0.001)]
         (game-loop (assoc game
                           :delta-time (- ts (:total-time game))
                           :total-time ts)))))))

(defn keycode->keyword [keycode]
  (condp = keycode
    37 :left
    39 :right
    38 :up
    40 :down
    nil))

(defn listen-for-keys []
  (events/listen js/window "keydown"
                 (fn [event]
                   (when-let [k (keycode->keyword (.-keyCode event))]
                     (swap! c/*state update :pressed-keys conj k))))
  (events/listen js/window "keyup"
                 (fn [event]
                   (when-let [k (keycode->keyword (.-keyCode event))]
                     (swap! c/*state update :pressed-keys disj k)))))

(defn resize [context]
  (let [display-width context.canvas.clientWidth
        display-height context.canvas.clientHeight]
    (set! context.canvas.width display-width)
    (set! context.canvas.height display-height)))

(defn listen-for-resize [context]
  (events/listen js/window "resize"
                 (fn [event]
                   (resize context))))

;; start the game

(defonce context
  (let [canvas (js/document.querySelector "canvas")
        context (.getContext canvas "webgl2")
        initial-game (assoc (pc/->game context)
                            :delete-time 0
                            :total-time 0)]
    (listen-for-keys)
    (resize context)
    (listen-for-resize context)
    (c/init initial-game)
    (game-loop initial-game)
    context))

