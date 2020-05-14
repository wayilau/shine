(ns net.server
  (:import (io.netty.bootstrap ServerBootstrap)
           (io.netty.channel.nio NioEventLoopGroup)
           (io.netty.channel.socket.nio NioServerSocketChannel)
           (io.netty.channel ChannelInitializer ChannelOption ChannelPipeline ChannelFuture SimpleChannelInboundHandler ChannelHandler ChannelInboundHandlerAdapter)
           (io.netty.handler.codec.string StringDecoder StringEncoder)))

(defn ^ChannelHandler inbound-handler
  "Implement a handler extends ChannelHandler
  process the message custom."
  []
  (proxy [SimpleChannelInboundHandler] []
    (channelRead0 [ctx ^String message]
      (println "get message from client" (.toString message))
      (.writeAndFlush ctx (.toString message))
      )))


(defn start
  "start a netty server."
  [port]
  (let [bossGroup (NioEventLoopGroup.)
        workerGroup (NioEventLoopGroup.)
        ^ServerBootstrap b (ServerBootstrap.)
        handler1 (proxy [ChannelInitializer] []
                   (initChannel [ch]
                     (let [pipeline (.pipeline ch)]
                       (.addLast pipeline "stringencoder" (StringEncoder.))
                       (.addLast pipeline "stringdecoder" (StringDecoder.))
                       (.addLast pipeline "selfhandler" (inbound-handler)))))]
    (doto b
      (.group bossGroup workerGroup)
      (.channel (class (NioServerSocketChannel.)))
      (.option ChannelOption/SO_BACKLOG (int 128))
      (.childOption ChannelOption/SO_KEEPALIVE true)
      (.childHandler handler1)
      )
    (println "starting server on port:" 3000)
    (let [f (.bind b port)]
      (.sync (.closeFuture (.channel f))))
    ))


