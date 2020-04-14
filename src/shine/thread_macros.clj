(ns shine.thread-macros)


(defn transform [person]
  (update (assoc person :hair-color :gray) :age inc))

;;we can  rewrite it using  -> thread macros 增强可读性
(defn transforms [person]
  (-> person
      (assoc :hair-color :gray)
      (update age inc)))
; we can means that it takes first arg as second expresson

;any position 将前一个值绑定到v， 后边都可以使用前一个表达式的值
(as-> [:foo :bar] v
      (map name v)
      (first v)
      (.substring v 1))

;defrecord test 用来构建一个对象
(defrecord Person [fname lname address])

(defrecord Address [street city])

(def stu (Person. "Stu" "Helloway"
                  (Address. "tingyuan" "suzhou")))

(:lname stu)

;^:private 代表定义私有，与 ^{:private true}一致
