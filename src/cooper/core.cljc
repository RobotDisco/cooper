(ns cooper.core
  (:require [cooper.utils :as utils]
            [play-cljc.gl.core :as c]
            [play-cljc.primitives-2d :as e]
            [play-cljc.gl.entities-2d :as ge]
            [play-cljc.transforms :as t]
            #?(:clj [play-cljc.macros-java :refer [gl math]]
               :cljs [play-cljc.macros-js :refer-macros [gl math]])))

(defonce *state (atom {:petals []
                       :player {}
                       :level 0
                       :score 0
                       :points 0}))

(defn init [game]
  ;; allow transparency in images
  (gl game enable (gl game BLEND))
  (gl game blendFunc (gl game SRC_ALPHA) (gl game ONE_MINUS_SRC_ALPHA))

  ;; don't cull faces or process depth since this is a 2D game
  (gl game disable (gl game CULL_FACE))
  (gl game disable (gl game DEPTH_TEST)))

(def screen-entity
  {:viewport {:x 0 :y 0 :width 0 :height 0}
   :clear {:color [(/ 85 255) (/ 85 255) (/ 170 255) 1] :depth 1}})

(defn tick [game]
  (let [game-width (utils/get-width game)
        game-height (utils/get-height game)]
    (when (and (pos? game-width) (pos? game-height))
      ;; render the blue background
      (c/render game (update screen-entity :viewport
                             assoc :width game-width :height game-height))
      (c/render game (->> (-> (ge/->entity game e/rect)
                              (t/project game-width game-height)
                              (t/color [(/ 85 255) (/ 170 255) 1 1])
                              (t/scale game-width (* game-height (/ 1 5))))
                          (c/compile game)))))
  game)
    
        
