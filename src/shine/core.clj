(ns shine.core
  (:import (java.util Scanner)))

(defn call
  "This func send message to server."
  [ch]
  (let [input (Scanner. (System/in))]
    (loop [value (.next input)]
      (if (not= value "exit")
        (.writeAndFlush ch value)
        (recur (.next input)))))
  )