(ns shine.yaml
  (:refer-clojure :exclude [load])
  (:require [yaml.core :as yaml]))


(defn loadFile
  "Read yaml file."
  []
  (yaml/from-file "config.yml"))

(defn -main
  [& args]
  (println (loadFile)))