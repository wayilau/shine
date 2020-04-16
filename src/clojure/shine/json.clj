(ns shine.json
  (:require [cheshire.core :refer :all]))

;;encode
(defn encode
  "Transfer a object to json."
  [o]
  (generate-string o)
  )

;;decode
(defn decode
  "Decode a json string to an object"
  [strings]
  (parse-string strings))