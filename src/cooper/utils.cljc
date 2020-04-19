(ns cooper.utils
  #?(:clj (:require [clojure.java.io :as io]))
  #?(:clj (:import [java.nio ByteBuffer]
                   [org.lwjgl.glfw GLFW]
                   [org.lwjgl.system MemoryUtil]
                   [org.lwjgl.stb STBImage])))

(defn get-width [game]
  #?(:clj (let [*width (MemoryUtil/memAllocInt 1)
                _ (GLFW/glfwGetFramebufferSize ^long (:context game) *width nil)
                n (.get *width)]
            (MemoryUtil/memFree *width)
            n)
     :cljs (-> game :context .-canvas .-clientWidth)))

(defn get-height [game]
  #?(:clj (let [*height (MemoryUtil/memAllocInt 1)
                _ (GLFW/glfwGetFramebufferSize ^long (:context game) nil *height)
                n (.get *height)]
            (MemoryUtil/memFree *height)
            n)
     :cljs (-> game :context .-canvas .-clientHeight)))
