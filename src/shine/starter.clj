(ns shine.starter
  (:require [shine.server :as server])
  )

(defn -main
  "This is entrance of the shine. from here we run
  a tcp or udp server to listen on 3000 default. Server accepts
  connection from private network, and response directly."
  [& args]
  (server/server 3000)
  ()
  )
