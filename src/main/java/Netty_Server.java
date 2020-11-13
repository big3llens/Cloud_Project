import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.beans.Encoder;
import java.util.Base64;


public class Netty_Server {
    private static final int PORT = 9000;

    public Netty_Server() {
        EventLoopGroup auth = new NioEventLoopGroup(1);
        final EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(auth, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(
//                                    new ChunkedWriteHandler(),
                                    new OutHandler(),
//                                    new LineBasedFrameDecoder(8192),
                                    new InHandler()
                            );
                        }
                    });
            ChannelFuture future = b.bind(PORT).sync();
            System.out.println("Сервер запущен!!");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            auth.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new Netty_Server();
    }
}
