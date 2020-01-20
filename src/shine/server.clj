(ns shine.server
  (:import (io.netty.channel ChannelOption ChannelInitializer SimpleChannelInboundHandler)
           (io.netty.channel.socket.nio NioServerSocketChannel)
           (io.netty.bootstrap ServerBootstrap)
           (io.netty.channel.nio NioEventLoopGroup)
           (io.netty.util ReferenceCountUtil)))

(def bossGroup (NioEventLoopGroup.))
(def workGroup (NioEventLoopGroup. 1))
(def b (ServerBootstrap.))

(def handler
  "Implement a handler extends ChannelHandler
  process the message custom."
  (proxy [SimpleChannelInboundHandler] []
    (channelRead [ctx message]
      (try
        (.writeAndFlush ctx message)
        (finally (ReferenceCountUtil/release message)))
      )))

(defn server
  "This is a shine server, which used to accept conn
  from client which behind nat. collect the info from clients
  and send info to services out the nat."
  [port]
  (.group b bossGroup workGroup)
  (.channel b (class (NioServerSocketChannel.)))
  (.childHandler b (proxy [ChannelInitializer] []
                     (initChannel [ch]
                       (.addLast (.pipeline ch) handler)
                       )))
  (.option b ChannelOption/SO_BACKLOG (int 128))
  (.childOption b ChannelOption/SO_KEEPALIVE true)
  ;(.sync (.closeFuture (.channel (.sync (.bind b port)))))
  (let [f (.bind b port)]
    (.sync (.closeFuture (.channel f)))))
