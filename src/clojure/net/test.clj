(ns net.test
  (:require [adapter.netty :as netty]
            [net.server :as server])
  (:import (io.netty.handler.codec.string StringEncoder StringDecoder)))


(defn -main
  [& args]
  (netty/addHandler (StringEncoder.))
  (netty/addHandler (StringDecoder.))
  (netty/addHandler (server/inbound-handler))
  (netty/run-netty {:port 3000}))