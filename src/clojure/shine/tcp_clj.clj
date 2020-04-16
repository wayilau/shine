(ns shine.tcp-clj
  (:import (java.util.concurrent.atomic AtomicBoolean AtomicInteger)
           (io.netty.channel ChannelOption ChannelFuture SimpleChannelInboundHandler ChannelHandlerContext ChannelInitializer ChannelPipeline)
           (io.netty.channel.nio NioEventLoopGroup)
           (io.netty.util.concurrent DefaultThreadFactory EventExecutorGroup)
           (io.netty.buffer ByteBuf)))

(defn- get-default-thread
  "for thread we choose reasonable default"
  []
  (let [n (int (/ (-> Runtime/getRuntime) .availableProcessors 2))]
    (if (> n 0) n 1)))

(defrecord Client [group read-group channel-f write-ch read-ch internal-error-ch error-ch ^AtomicInteger reconnect-count ^AtomicBoolean closed id])
(defrecord Reconnected [^Client client cause])

(defrecord Pause [time])
(defrecord Stop [])
(defrecord Poison [])
(defrecord FailedWrite [])

(defonce ALLOCATOR ChannelOption/ALLOCATOR)
(defonce ALLOW-HALF-CLOSUER ChannelOption/ALLOW_HALF_CLOSURE)
(defonce AUTO-READ ChannelOption/AUTO_READ)
(defonce CONNECT-TIMEOUT-MILLS ChannelOption/CONNECT_TIMEOUT_MILLIS)
(defonce WRITE-BUFFER-HIGH-WATER-MARK ChannelOption/WRITE_BUFFER_HIGH_WATER_MARK)
(defonce WRITE_BUFFER_LOW_WATER_MARK ChannelOption/WRITE_BUFFER_LOW_WATER_MARK)
(defonce MAX-MESSAGES-PER-READ ChannelOption/MAX_MESSAGES_PER_READ)
(defonce MESSAGE-SIZE-ESTIMATOR ChannelOption/MESSAGE_SIZE_ESTIMATOR)
(defonce RCVBUF-ALLOCATOR ChannelOption/RCVBUF_ALLOCATOR)
(defonce SO-BACKLOG ChannelOption/SO_BACKLOG)
(defonce SO-KEEPALIVE ChannelOption/SO_KEEPALIVE)
(defonce SO-RCVBUF ChannelOption/SO_RCVBUF)
(defonce SO-REUSEADDR ChannelOption/SO_REUSEADDR)
(defonce SO-SNDBUF ChannelOption/SO_SNDBUF)
(defonce SO-TIMEOUT ChannelOption/SO_TIMEOUT)
(defonce TCP-NODELAY ChannelOption/TCP_NODELAY)

;global  event  loop share
(defonce ^NioEventLoopGroup EVENT-LOOP_GROUP (NioEventLoopGroup. (get-default-thread) (DefaultThreadFactory. "global-netty-nio-threads" true)))
(defn close-client
  [{:keys [^NioEventLoopGroup group ^NioEventLoopGroup read-group ^ChannelFuture channel-f internal-error-ch write-ch id
           ^AtomicBoolean closed]}]
  (let [^Channel channel (.channel channel-f)]
    (when-not (identical? group EVENT-LOOP_GROUP)
      (-> group .shutdownGracefully (.await 2000)))

    (when-not (identical? read-group EVENT-LOOP-GROUP)
      (-> read-group .shutdownGracefully (.await 2000)))
    (.await ^ChannelFuture (.disconnect channel))
    (.await ^ChannelFuture (.close channel))
    (close! write-ch)
    (.set closed true)
    (close! internal-error-ch)
    (mon/leave-ctx :tcp-conn id)))

)

(defn close-all [{:keys [group closed] :as conf}]
  (future
    (try
      (close-client conf)
      (catch Exception e (error e (str "Error while close conf" conf))))
    (.set ^AtomicBoolean closed true)
    true))

(defn close-and-wait [conf]
  (deref (close-all conf)))

(defn handler [{:keys [group read-ch internal-error-ch write-ch]} decoder]
  (proxy [SimpleChannelInboundHandler]
         []
    (channelActive [^ChannelHandlerContext ctx])
    (channelRead0 [^ChannelHandlerContext ctx in]
      ;prn "channel read 0"
      (decoder in))
    (exceptionCaught [^ChannelHandlerContext ctx cause]
      (error "Handler exception" cause)
      (error cause (>!! internal-error-ch [cause ctx]))
      (.close ctx))))


(defn- default-decoder [read-ch in]
  (let [d (if (instance? ByteBuf in) (buffer->bytes in))]
    (>!! read-ch d)))

(defn ^ChannelInitializer client-channel-intializer [{:keys [^EventExecutorGroup group ^EventExecutorGroup read-group read-ch internal-error-ch handlers decoder] :as conf}]
  (proxy [ChannelInitializer]
         []
    (initChannel [^Channel ch]
      (try
        (let [^ChannelPipeline pipeline (.pipeline ch)]
          (if handlers
            (PipelineUtil/addLast pipeline group (map #(%) handlers)))))
      (catch Exception e (do
                           (error str ("channel  intilizer error" e) e)
                           (>!! internal-error-ch [e nil])
                           )))))


(defn open? [client]
  (-> client ^ChannelFuture (:channel-f) ^Channel (.channel) (.isOpen)))