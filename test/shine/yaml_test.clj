(ns shine.yaml_test
  (:require [clojure.test :refer :all]
            [shine.yaml :as y]))

(deftest a-test
  (testing "Read yaml"
    (println (y/loadFile))))