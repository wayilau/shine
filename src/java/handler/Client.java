package handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.util.Scanner;

/**
 * @author lwy
 */
public class Client {

    public static void main(String[] args) throws Exception {
        Bootstrap b = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup(2);
        b.group(group).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true).handler(new ChannelInitializer<SocketChannel>() {
            @Override protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast("decoder", new StringDecoder());
                p.addLast("encoder", new StringEncoder());
                p.addLast("clienthandler", new ClientHandler());
            }
        });

        ChannelFuture f = b.connect("localhost", 3000);
        f.sync();

//        Channel channel = f.channel();
//        Scanner scanner = new Scanner(System.in);
//        String  line = scanner.nextLine();
//        while (line != null) {
//            channel.writeAndFlush(line);
//            line = scanner.nextLine();
//        }
        f.channel().closeFuture().sync();
    }
}
