(ns cooper.core
  (:require [cooper.utils :as utils]
            [play-cljc.gl.core :as c]
            [play-cljc.primitives-2d :as e]
            [play-cljc.gl.entities-2d :as ge]
            [play-cljc.transforms :as t]
            [play-cljc.instances :as i]
            #?(:clj [play-cljc.macros-java :refer [gl math]]
               :cljs [play-cljc.macros-js :refer-macros [gl math]])))

(def ^:const +NUM_ROWS+ 4)
(def ^:const +NUM_COLS+ 8)
(def ^:const +PETAL_MARGIN+ 0.9)

(defn max-petal-width [game-width]
  (* (/ game-width +NUM_COLS+)
     ;; Add some padding so that we see some of the ocean
     1))

(defn max-petal-height [game-height]
  ;; remember, we've allocated one extra row's worth of space for a scoring area
  (* (/ game-height (+ +NUM_ROWS+ 1))
     1))

(defn petal-x-from-index [i game-width]
  (let [mpw (max-petal-width game-width)]
    (* mpw (mod +NUM_COLS+ i))))

(defn petal-y-from-index [i game-height]
  (let [mph (max-petal-height game-height)]
    (+ mph                              ; The first row is for the score
       (* (mod +NUM_ROWS+ i)))))

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
        game-height (utils/get-height game)
        score-entity (->> (-> (ge/->entity game e/rect)
                               (t/project game-width game-height)
                               (t/color [(/ 85 255) (/ 170 255) 1 1])
                               (t/scale game-width (* game-height (/ 1 5))))
                          (c/compile game))
        petal-seed (-> (ge/->entity game e/rect)
                       (assoc :viewport {:x 0 :y 0 :width game-width :height game-height})
                       (t/project game-width game-height))
        petals (for [n (range (* +NUM_ROWS+ +NUM_COLS+))]
                 (-> petal-seed
                     (t/color [(rand) (rand) (rand) 1])
                     (t/translate (petal-x-from-index n game-width)
                                  (petal-y-from-index n game-height))
                     (t/scale (max-petal-width game-width)
                              (max-petal-height game-height))))
        petals (->> petals
                    vec
                    (reduce-kv i/assoc (i/->instanced-entity petal-seed))
                    (c/compile game))]
    (when (and (pos? game-width) (pos? game-height))
      ;; render the blue background
      (c/render game (update screen-entity :viewport
                             assoc :width game-width :height game-height))
      (c/render game score-entity)
      (c/render game petals)))
  game)
    
        
