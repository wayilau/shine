(ns net.server
  (:require [adapter.netty :as netty])
  (:import
    (io.netty.handler.codec.string StringDecoder StringEncoder)
    (io.netty.channel ChannelHandlerContext)))


(defn -main
  [& args]
  (netty/netty-server {:port  3000
                    :handlers [(StringEncoder.) (StringDecoder.) (netty/new-handler (fn [^ChannelHandlerContext ctx ^String message]
                                                                                      (println (str "get message from client:" message))
                                                                                      (.writeAndFlush ctx message)))]}))
