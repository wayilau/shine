(ns macro.macro
  (:require [clojure.walk :as walk]
            [clojure.string :as str]))


(defmacro reverse-it
  [form]
  (walk/postwalk #(if (symbol? %)
                    (symbol (str/reverse (name %)))
                    %)
                 form))

(reverse-it (nrp "lz"))