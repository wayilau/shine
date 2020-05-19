(ns net.test
  (:require [adapter.netty :as netty]
            [net.server :as server])
  (:import (io.netty.handler.codec.string StringEncoder StringDecoder)))


(defn -main
  [& args]
  (netty/run-netty {:port     3000
                    :handlers [(StringEncoder.) (StringDecoder.) (server/inbound-handler)]}))