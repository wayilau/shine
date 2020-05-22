package handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author lwy
 */
public class StringHandler extends SimpleChannelInboundHandler<String> {

    @Override protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("get message from client: " +  msg);
        ctx.write(msg);
    }

    @Override public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
