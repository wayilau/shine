(ns shine.protocol)

(defprotocol AProtocol
  "A doc string for AProtocol abstraction"
  (bar [a b] "bar docs")
  (baz [a] [a b] [a b c] "baz docs"))


(defprotocol P
  (foo [x])
  (bar-me [x] [x y]))

(foo
  (let [x 42]
    (reify P
      (foo [this] 17)
      (bar-me [this] x)
      (bar-me [this y] x))))


(defn -main
  [& args]
  )
