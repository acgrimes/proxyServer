package com.dd.vbc.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.Security;

/**
 * Listing 2.2 EchoServer class
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class ProxyServer {
    private final int port;
    public static final String SERVER_NAME = "server";
    public static final char[] SERVER_PASSWORD = "serverPassword".toCharArray();
    public static final String TRUST_STORE_NAME = "trustStore";
    public static final char[] TRUST_STORE_PASSWORD = "trustPassword".toCharArray();

    public ProxyServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {

        Security.addProvider(new BouncyCastleJsseProvider());
        int port = 61005;
        new ProxyServer(port).start();
    }

    public void start() throws Exception {
        final ProxyServerHandler serverHandler = new ProxyServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress("192.168.0.8", port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        KeyManagerFactory mgrFact = KeyManagerFactory.getInstance("SunX509");
                        KeyStore serverStore = KeyStore.getInstance("JKS");

                        serverStore.load(new FileInputStream("src/main/resources/server.jks"), SERVER_PASSWORD);

                        mgrFact.init(serverStore, SERVER_PASSWORD);

                        // set up a trust manager so we can recognize the server
                        TrustManagerFactory trustFact = TrustManagerFactory.getInstance("SunX509");
                        KeyStore            trustStore = KeyStore.getInstance("JKS");

                        trustStore.load(new FileInputStream("src/main/resources/trustStore.jks"), TRUST_STORE_PASSWORD);

                        trustFact.init(trustStore);

                        SslContext sslContext = SslContextBuilder.forServer(mgrFact).
                                                    trustManager(trustFact).
                                                    sslProvider(SslProvider.JDK).
                                                    protocols("TLSv1.3").
                                                    clientAuth(ClientAuth.REQUIRE).
                                                    build();
                        ch.pipeline().addLast(serverHandler);
                        ch.pipeline().addLast(sslContext.newHandler(ch.alloc()));
                    }
                });

            ChannelFuture f = b.bind().sync();
            System.out.println(ProxyServer.class.getName() +
                " started and listening for connections on " + f.channel().localAddress());
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
