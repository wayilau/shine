(ns shine.client
  (:require [shine.core :as core])
  (:import (io.netty.bootstrap Bootstrap)
           (io.netty.channel.nio NioEventLoopGroup)
           (io.netty.channel SimpleChannelInboundHandler ChannelOption ChannelInitializer)
           (java.util Scanner)
           (io.netty.channel.socket.nio NioSocketChannel)))

(def b (Bootstrap.))
(def group (NioEventLoopGroup.))

(def handler
  "Implement a handler extends ChannelHandler
  process the message custom."
  (proxy [SimpleChannelInboundHandler] []
    (channelRead [ctx message]
      (.writeAndFlush ctx message)
      )))


(defn client
  "This is a client which run behind the nat, connect to
  server on the cloud, and send info(nat) to server, talk to
  real services from serverinfo."
  [addr port]
  (.group b group)
  (.channel b (class (NioSocketChannel.)))
  (.option b ChannelOption/SO_KEEPALIVE true)
  (.handler b (proxy [ChannelInitializer] []
                (initChannel [ch]
                  (.addLast (.pipeline ch) handler)
                  )))
  (let [ch (.channel (.sync (.connect b addr port)))]
    (core/call ch)
    (.sync (.closeFuture ch)))

  )


(defn -main
  "client main func"
  [& args]
  (client "localhost" 3000))

