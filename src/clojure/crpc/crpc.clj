(ns crpc.crpc
  (:require [adapter.netty :as netty]))

;this is a client for crpc, we run cprc in the lan
;connect to crp server.

(defn -main
  "run crpc client to connect to crp server."
  [& args]
  (netty/netty-client {:addr "localhost"
                       :port 3000}))


