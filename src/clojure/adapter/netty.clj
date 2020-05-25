(ns adapter.netty
  (:import
    (io.netty.bootstrap ServerBootstrap Bootstrap)
    (io.netty.channel.socket.nio NioServerSocketChannel NioSocketChannel)
    (io.netty.channel ChannelFuture ChannelOption ChannelPipeline ChannelInitializer ChannelHandler ChannelHandlerContext ChannelInboundHandlerAdapter)
    (io.netty.channel.nio NioEventLoopGroup)))

(def boot (ServerBootstrap.))
(def b (Bootstrap.))

(defn ^ChannelHandler new-handler
  "build handler from this func."
  [f]
  (proxy [ChannelInboundHandlerAdapter] []
    (channelRead [^ChannelHandlerContext ctx ^Object message]
      (f ctx message))))

(defn- ^ChannelInitializer init-channel
  "give the pipeline to user,can modify the channelhandler at runtime."
  [handlers]
  (proxy [ChannelInitializer] []
    (initChannel [ch]
      (let [^ChannelPipeline pipeline (.pipeline ch)]
        (doseq [index (range (count handlers))]
          (println index)
          (.addLast pipeline (str "handler" index) (get handlers index))))))
  )

;run a netty server
(defn netty-server
  "build a netty server easily from function, help us not need use netty every time
  just use this function is enough.

  :port which port listening on
  :bossgroup  if not provided, default will be used
  :workergroup if not provided, default will be used
  :backlog
  :keepalive
  :handlers handlers for channnel execute, contains self created handler.
  :channelInitializer channel init"
  [options]
  (doto boot
    (.group (:bossgroup options (NioEventLoopGroup.)) (:workgroup options (NioEventLoopGroup.)))
    (.channel (class (NioServerSocketChannel.)))
    (.option ChannelOption/SO_BACKLOG (:backlog options (int 128)))
    (.childOption ChannelOption/SO_KEEPALIVE (:keepalive options true))
    (.childHandler (init-channel (:handlers options))))

  (when-let [^ChannelFuture f (.bind boot (:port options))]
    (println (str "server listenning on :" (:port options)))
    (.sync (.closeFuture (.channel f)))))

(defn netty-client
  "this fn used to build a netty client easily from func, help us not need use netty lib every time
  just use this func is enough.
  :addr remote address to connect to, default: localhost
  :port remote server port to connect to"
  [options]
  (doto b
    (.group (:group options (NioEventLoopGroup.)))
    (.channel (:channel options (class (NioSocketChannel.))))
    (.option (:option options (ChannelOption/SO_BACKLOG (int 128))))
    (.childOption (:child_option options (ChannelOption/SO_KEEPALIVE true)))
    (.handler (init-channel (:handlers options))))

  (when-let [addr (:addr options)
             port (:port options)
             ^ChannelFuture f (.connect addr port)]
    (println (str "connect to " addr port "success"))
    (.sync (.closeFuture (.channel f)))))