(ns adapter.netty
  (:import
    (io.netty.bootstrap ServerBootstrap)
    (io.netty.channel.socket.nio NioServerSocketChannel)
    (io.netty.channel ChannelFuture ChannelOption SimpleChannelInboundHandler ChannelPipeline ChannelInitializer ChannelHandler)
    (io.netty.channel.nio NioEventLoopGroup)))

(def boot (ServerBootstrap.))
(def handlers [])

(defn addHandler
  [handler]
  (conj handlers handler))

(defn- ^ChannelHandler init-channel
  "give the pipeline to user,can modify the channelhandler at runtime."
  []
  (proxy [ChannelInitializer] []
    (initChannel [ch]
      (let [pipeline (.pipeline ch)]
        (.addLast pipeline handlers))))
  )

;run a netty server
(defn run-netty
  "new a netty server
  :port use to listen
  :bossgroup
  :workergroup
  :backlog
  :keepalive
  :channelInitializer channel init"
  [options]
  (doto boot
    (.group (:bossgroup options (NioEventLoopGroup.)) (:workgroup options (NioEventLoopGroup.)))
    (.channel (class (NioServerSocketChannel.)))
    (.option ChannelOption/SO_BACKLOG (:backlog options (int 128)))
    (.childOption ChannelOption/SO_KEEPALIVE (:keepalive options true))
    (.childHandler (init-channel)))

  (when-let [^ChannelFuture f (.bind boot (:port options))]
    (println (str "server listenning on :" (:port options)))
    (.sync (.closeFuture (.channel f)))))
