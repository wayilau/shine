(ns net.client
  (:import (io.netty.channel.nio NioEventLoop NioEventLoopGroup)
           (io.netty.bootstrap Bootstrap)
           (io.netty.channel.socket.nio NioSocketChannel)
           (io.netty.channel ChannelOption ChannelInitializer SimpleChannelInboundHandler ChannelHandler ChannelOutboundHandlerAdapter ChannelOutboundHandler ChannelFuture)
           (io.netty.handler.codec.string StringDecoder StringEncoder)
           (java.util Scanner)))


(defn ^ChannelHandler in-handler
  "Implement a handler extends ChannelHandler
  process the message custom."
  []
  (proxy [SimpleChannelInboundHandler] []
    (channelRead0 [ctx ^String message]
      (println "get message from server" (.toString message)))
    (channelRegistered [ctx]
      (println "connected to server.")
      ;(let [scan (Scanner. System/in)
      ;      line (.nextLine scan)]
      ;  (while (not (nil? line))
      ;    (do
      ;      ;(println (str line))
      ;      (.writeAndFlush ctx line))
      ;    (def line (.nextLine scan))
      ;    ))
      )

    ))


(defn client
  "Given a client connect to server. send msg."
  [host port]
  (let [^NioEventLoopGroup group (NioEventLoopGroup.)
        ^Bootstrap b (Bootstrap.)
        handler (proxy [ChannelInitializer] []
                  (initChannel [ch]
                    (let [pipeline (.pipeline ch)]
                      (.addLast pipeline "stringencoder" (StringEncoder.))
                      (.addLast pipeline "stringdecoder" (StringDecoder.))
                      (.addLast pipeline "inhandler" (in-handler))
                      ;(.addLast pipeline "outhandler" (out-handler))
                      )))]
    (doto b
      (.group group)
      (.channel (class (NioSocketChannel.)))
      (.option ChannelOption/SO_KEEPALIVE true)
      (.handler handler))

    (let [^ChannelFuture f (.connect b host port)]
      (.sync f)
      (let [scan (Scanner. System/in)
            channel (.channel f)]
        (def line (.nextLine scan))
        (while (not (nil? line))
          (do
            (.writeAndFlush channel line)
            (def line (.nextLine scan))
            )))
      (.sync (.closeFuture (.channel f)))))
  )

(defn -main
  "client main"
  [& args]
  (client "localhost" 3000))

