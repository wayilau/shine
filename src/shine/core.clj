(ns shine.core
  (:gen-class)
  (:import (io.netty.bootstrap ServerBootstrap)
           (io.netty.channel.nio NioEventLoopGroup)
           (io.netty.channel.socket.nio NioServerSocketChannel)
           (io.netty.channel ChannelInitializer ChannelOption SimpleChannelInboundHandler)))


(def bossGroup (NioEventLoopGroup.))
(def workGroup (NioEventLoopGroup. 1))
(def b (ServerBootstrap.))

(defn init
  "Init a tcp server with port"
  [port]
  (.group b bossGroup workGroup)
  (.channel b (class (NioServerSocketChannel.)))
  (.childHandler b (proxy [ChannelInitializer] []
                     (initChannel [ch]
                       (.addLast (.pipeline ch) (proxy [SimpleChannelInboundHandler] []
                                                  (channelRead [ctx msg]
                                                    (println "get msg" (str msg))))))))
  (.option b ChannelOption/SO_BACKLOG (int 128))
  (.childOption b ChannelOption/SO_KEEPALIVE true)
  (.sync (.closeFuture (.channel (.sync (.bind b port)))))
  )
