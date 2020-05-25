(ns shine.yaml
  (:refer-clojure :exclude [load])
  (:require [yaml.core :as yaml]))


;(yaml/from-file "/Users/francis/workspc/shine/resources/config.yml")


(defn loadFile
  "Read yaml file."
  []
  (yaml/from-file "/Users/francis/workspc/shine/resources/config.yml"))

(defn -main
  "test"
  [& args]
  (when let)
  (println (:addr (loadFile))))
