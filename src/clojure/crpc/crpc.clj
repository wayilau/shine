(ns crpc.crpc
  (:require [adapter.netty :as netty]
            [yaml.core :as yaml])
  (:import (io.netty.handler.codec.string StringEncoder StringDecoder)))

;this is a client for crpc, we run cprc in the lan
;connect to crp server.

(def conf (yaml/from-file "config.yml"))

(defn -main
  "run crpc client to connect to crp server. which will be used for
  transfer requests to self services."
  [& args]
  (netty/netty-client {:addr     "localhost"
                       :port     3000
                       :handlers [(StringEncoder.) (StringDecoder.) (netty/new-handler (fn [ctx message]
                                                                                         ))]}))


