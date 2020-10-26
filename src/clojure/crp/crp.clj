(ns crp.crp
  (:require [adapter.netty :as netty])
  (:import (io.netty.handler.codec.string StringEncoder StringDecoder)))


(defn -main
  "A server used for later requests from user outside to services at home or internal
  network. It is easy to make a server. this just store the meta data from crpc's requests,
  such as services name, port. then we use port or different uri to requests our services."
  [& args]
  (netty/netty-server {:port     3000
                       :handlers [(StringEncoder.)
                                  (StringDecoder.)
                                  (netty/new-handler (fn [ctx message]
                                                       (println message)
                                                       (.write ctx message)))]})
  )
