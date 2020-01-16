(ns shine.starter
  (:require [shine.core :as shine])
  )

(defn -main
  "This is entrance of the shine. from here we run
  a tcp or udp server to listen on 3000 default. Server accepts
  connection from private network, and response directly."
  [& args]
  (shine/init 3000)
  )
