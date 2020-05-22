package handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lwy
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {

    ExecutorService service = Executors.newFixedThreadPool(1);

    @Override public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("get message from server: " +  msg);
    }

    @Override public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("connected to server success.");
        service.submit(()->{
            Scanner scanner = new Scanner(System.in);
            String  line = scanner.nextLine();
            while (line != null) {
                ctx.writeAndFlush(line);
                ctx.fireChannelActive();
                line = scanner.nextLine();
            }
        });
    }

    @Override public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
