(ns user
  (:require [net.robot-disco.cooper.core]))

(defn dev []
  (println "Starting dev REPL mode")
  (require 'dev)
  (in-ns 'dev))

(println "Welcome to the cooper project REPL")
(println)
(println "Run: (dev) to start development mode.")




