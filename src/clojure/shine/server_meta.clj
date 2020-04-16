(ns shine.server-meta
  (:import (io.netty.bootstrap ServerBootstrap)
           (io.netty.channel.nio NioEventLoopGroup)
           (io.netty.channel.socket.nio NioServerSocketChannel)
           (io.netty.channel ChannelInitializer ChannelOption)))

(defn start
  "server start"
  [port]
  (let [b ^ServerBootstrap (ServerBootstrap .)
        boosGroup ^NioEventLoopGroup (NioEventLoopGroup .)
        workerGroup ^NioEventLoopGroup (NioEventLoopGroup .)]
    (.group b boosGroup workerGroup)
    (.channel b (class (NioServerSocketChannel.)))
    (.childHandler b (proxy [ChannelInitializer] []
                       (initChannel [ch]
                         (.addLast (.pipeline ch) handler)
                         )))
    (.option b ChannelOption/SO_BACKLOG (int 128))
    (.childOption b ChannelOption/SO_KEEPALIVE true)
    (.bind b port)
    ))
