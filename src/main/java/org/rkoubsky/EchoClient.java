package org.rkoubsky;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(final String host, final int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(final String[] args) throws Exception {
        /*if (args.length != 2) {
            System.err.printf("Usage: %s <host> <port>\n", EchoClient.class.getSimpleName());
        }
        final String host = args[0];
        final int port = Integer.parseInt(args[1]);*/
        new EchoClient("localhost", 8080).start();
    }

    public void start() throws Exception {
        final EventLoopGroup group = new NioEventLoopGroup();
        try {
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(this.host,
                    this.port)).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(final SocketChannel ch) {
                    ch.pipeline().addLast(new EchoClientHandler());
                }
            });
            final ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
