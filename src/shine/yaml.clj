(ns shine.yaml
  (:refer-clojure :exclude [load])
  (:require [yaml.core :as yaml]))


(defn loadFile
  "Read yaml file."
  []
  (println "test loadFile")
  (yaml/from-file "config.yml"))

(defn -main
  "test"
  [& args]
  (println "hello")
  (loadFile))