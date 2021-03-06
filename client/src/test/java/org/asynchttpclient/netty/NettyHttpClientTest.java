package org.asynchttpclient.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import java.net.URI;

/**
 * @author 李海江
 * @date 2021/3/3
 * @version 1.0
 */
public class NettyHttpClientTest {

	private static class HttpClientHandler extends ChannelInboundHandlerAdapter {
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			URI uri = new URI("/");
			FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, uri.toASCIIString());
			request.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			request.headers().add(HttpHeaderNames.CONTENT_LENGTH,request.content().readableBytes());
			ctx.writeAndFlush(request);
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg)
				throws Exception {
			System.out.println("msg -> "+msg);
			if(msg instanceof FullHttpResponse){
				FullHttpResponse response = (FullHttpResponse)msg;
				ByteBuf buf = response.content();
				String result = buf.toString(CharsetUtil.UTF_8);
				System.out.println("response -> "+result);
			}
		}

	}

	public static void start(String host,int port){
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		try {
			bootstrap.group(group)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.SO_KEEPALIVE, true)
					.handler(new ChannelInitializer<Channel>() {

						@Override
						protected void initChannel(Channel channel)
								throws Exception {
							//channel.pipeline().addLast(new HttpRequestEncoder());
							//channel.pipeline().addLast(new HttpResponseDecoder());
							channel.pipeline().addLast(new HttpClientCodec());
							channel.pipeline().addLast(new HttpObjectAggregator(65536));
							channel.pipeline().addLast(new HttpContentDecompressor());
							channel.pipeline().addLast(new HttpClientHandler());
						}
					});
			ChannelFuture future = bootstrap.connect(host, port).sync();
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		//start("127.0.0.1", 8000);
		start("www.example.com", 80);
	}

}
