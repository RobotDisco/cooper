(ns cooper.engine.utils
  #?(:clj (:require [clojure.java.io :as io]))
  #?(:clj (:import [java.nio ByteBuffer]
                   [org.lwjgl.glfw GLFW]
                   [org.lwjgl.system MemoryUtil]
                   [org.lwjgl.stb STBImage])))

(defn get-width
  "Get width of game screen"
  [engine-state]
  #?(:clj (let [*width (MemoryUtil/memAllocInt 1)
                _ (GLFW/glfwGetFramebufferSize ^long (:context engine-state) *width nil)
                n (.get *width)]
            (MemoryUtil/memFree *width)
            n)
     :cljs (-> engine-state :context .-canvas .-clientWidth)))

(defn get-height
  "Get height of game screen"
  [engine-state]
  #?(:clj (let [*height (MemoryUtil/memAllocInt 1)
                _ (GLFW/glfwGetFramebufferSize ^long (:context engine-state) nil *height)
                n (.get *height)]
            (MemoryUtil/memFree *height)
            n)
     :cljs (-> engine-state :context .-canvas .-clientHeight)))
