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
(def ^:const +PETAL_MARGIN+ 0.8)
(def ^:const +PETAL_PADDING+ (- 1.0 +PETAL_MARGIN+))

(defn max-petal-width [game-width]
  (* (/ game-width +NUM_COLS+)))

(defn max-petal-height [game-height]
  ;; remember, we've allocated one extra row's worth of space for a scoring area
  (* (/ game-height (+ +NUM_ROWS+ 1))))

(defn petal-x-from-index [i game-width]
  (let [mpw (max-petal-width game-width)]
    (+ (* (rem i +NUM_COLS+) mpw)
       (/ (* mpw +PETAL_PADDING+) 2))))

(defn petal-y-from-index [i game-height]
  (let [mph (max-petal-height game-height)]
    (+ (- (- game-height mph)
          (* (quot i +NUM_COLS+) mph))
       (/ (* mph +PETAL_PADDING+) 2))))

(defn player-x [player])

(defonce *state (atom {:petals []
                       :player {:x 0
                                :y 0}
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
                       (t/project game-width game-height))
        petals (for [n (range (* +NUM_ROWS+ +NUM_COLS+))]
                 (-> petal-seed
                     (t/color [(/ 170 255) (/ 170 255) (/ 85 255) 1])
                     (t/translate (petal-x-from-index n game-width)
                                  (petal-y-from-index n game-height))
                     (t/scale (* (max-petal-width game-width) +PETAL_MARGIN+)
                              (* (max-petal-height game-height) +PETAL_MARGIN+))))
        petals (->> petals
                    vec
                    (reduce-kv i/assoc (i/->instanced-entity petal-seed))
                    (c/compile game))
        player (let [player-size (min (* (max-petal-width game-width) 0.25)
                                      (* (max-petal-height game-height) 0.25))]
                 (-> (ge/->entity game e/rect)
                     (t/project game-width game-height)
                     (t/color [0 0 0 1])
                     (t/translate (petal-x-from-index 0 game-width)
                                  (petal-y-from-index 0 game-height))
                     (t/scale player-size player-size)))
        chicken (let [player-size (min (* (max-petal-width game-width) 0.25)
                                      (* (max-petal-height game-height) 0.25))]
                 (-> (ge/->entity game e/rect)
                     (t/project game-width game-height)
                     (t/color [(/ 171 255) (/ 108 255) (/ 36 255) 1])
                     (t/translate (petal-x-from-index 31 game-width)
                                  (petal-y-from-index 31 game-height))
                     (t/scale player-size player-size)))]
    (when (and (pos? game-width) (pos? game-height))
      ;; render the blue background
      (c/render game (update screen-entity :viewport
                             assoc :width game-width :height game-height))
      (c/render game score-entity)
      (c/render game petals)
      (c/render game (c/compile game player))
      (c/render game (c/compile game chicken))))
  game)
