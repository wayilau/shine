(ns shine.thread-macros)


(defn transform [person]
  (update (assoc person :hair-color :gray) :age inc))

;;we can  rewrite it using  -> thread macros
(defn  transforms [person]
  (-> person
      (assoc :hair-color :gray)
      (update age inc)))
; we can means that it takes first arg as second expresson

;any position 将前一个值绑定到v， 后边都可以使用前一个表达式的值
(as-> [:foo :bar] v
      (map name v)
      (first v)
      (.substring v 1))