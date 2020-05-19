(ns shine.flow)

(defn testif
  "this a func for if demo."
  []
  (println (str "2 is " (if (even? 2) "even" "odd")))
  )

;Truth

(if true :truthy :falsey)
(if nil :truthy :falsey)

;if and do
(if (even? 5)
  (do (println "even")
      true)
  (do (println "odd")
      false))


; when
(when (neg? -1)
  (throw (RuntimeException. (str "x must be positive: " -1))))

;cond
(let [x 5]
  (cond
    (< x 2) "x is less than 2"
    (< x 10) "x is less than 10"))

;cond and else
(let [x 11]
  (cond
    (< x 2) "x is less than 2"
    (< x 10) "x is less than 10"
    :else "x is greater than or equal 10."))

;case
(defn foo [x]
  (case x
    5 "x is 5"
    10 "x is 10"))

(foo 11)

;case and else
(defn foo [x]
  (case x
    5 "x is 5"
    10 "x is 10"
    "x isn't 5 or 10"))

;dotimes
(dotimes [i 3]
  (println i))

;doseq
(doseq [n (range 3)]
  (println n))

;doseq with multiple bindings
(doseq [letter [:a :b]
        number (range 3)]                                   ; list of 0 1 2
  (prn [letter number]))

;for
(for [letter [:a :b]
      number (range 3)]
  [letter number])

;recur
(loop [i 0]
  (if (< i 10)
    (recur (inc i))
    i))

;defn and recur
(defn increace [i]
  (if (< i 10)
    (recur (inc i))
    i))

(increace 0)

;exceptions
(try
  (/ 0 1)
  (catch ArithmeticException e
    "divide by zero")
  (finally
    (println "cleanup")))
(defn -main
  [& args]
  (testif)                                                  ; if

  )