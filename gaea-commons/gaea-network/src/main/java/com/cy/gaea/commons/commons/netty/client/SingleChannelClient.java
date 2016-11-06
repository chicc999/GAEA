package com.cy.gaea.commons.commons.netty.client;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

import com.cy.gaea.commons.commons.netty.NettyTransport;
import com.cy.gaea.commons.commons.exception.ConnectTimeoutException;
import com.cy.gaea.commons.commons.exception.RequestTimeoutException;
import com.cy.gaea.commons.commons.netty.ResponseFuture;
import com.cy.gaea.commons.commons.util.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by cy on 2016/7/12.
 *
 * 单连接客户端
 */
public class SingleChannelClient extends NettyTransport {

	Logger logger = LoggerFactory.getLogger(SingleChannelClient.class);

	// Netty客户端启动器
	protected Bootstrap bootstrap;

	//超期请求清理线程
	ScheduledExecutorService cleanup ;

	// 存放同步和异步命令应答
	protected Map<String, ResponseFuture> futures = new ConcurrentHashMap<String, ResponseFuture>(100);

	public SingleChannelClient(NettyClientConfig config) {
		super(config);
	}

	public SingleChannelClient(NettyClientConfig config, ExecutorService serviceExecutor,
							   EventLoopGroup ioLoopGroup) {
		super(config,ioLoopGroup,serviceExecutor,null);
	}


	/**
	 * 创建连接，阻塞直到成功或失败
	 *
	 * @param address 地址
	 * @throws IOException
	 */
	public Channel createChannel(String address) throws IOException {
		return createChannel(address, 0);
	}

	/**
	 * 创建连接，阻塞直到成功或失败
	 *
	 * @param address           地址
	 * @param connectionTimeout 连接超时
	 * @throws IOException
	 */
	public Channel createChannel(String address, long connectionTimeout) throws IOException {
		if (address == null || address.isEmpty()) {
			throw new IllegalArgumentException("address must not be empty!");
		}
		String[] parts = address.split("[._:]");
		if (parts.length < 5) {
			throw new ConnectTimeoutException(address);
		}
		int port;
		try {
			port = Integer.parseInt(parts[parts.length - 1]);
		} catch (NumberFormatException e) {
			throw new ConnectTimeoutException(address);
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < parts.length - 1; i++) {
			if (i > 0) {
				builder.append('.');
			}
			builder.append(parts[i]);
		}
		String ip = builder.toString();
		try {
			return createChannel(new InetSocketAddress(InetAddress.getByName(ip), port), connectionTimeout);
		} catch (ConnectTimeoutException e) {
			throw e;
		} catch (UnknownHostException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			throw new SocketException("Connect "+address+" causes unKnow exception");
		}
	}

	/**
	 * 创建连接，阻塞直到成功或失败
	 *
	 * @param address 地址
	 * @throws IOException
	 */
	public Channel createChannel(SocketAddress address) throws IOException {
		return createChannel(address, 0);
	}

	/**
	 * 创建连接
	 *
	 * @param address           地址
	 * @param connectionTimeout 连接超时
	 * @throws ConnectTimeoutException
	 */
	public Channel createChannel(SocketAddress address, long connectionTimeout) throws IOException {
		if (address == null) {
			throw new IllegalArgumentException("address must not be null!");
		}
		InetSocketAddress isa = (InetSocketAddress) address;
		String ip = isa.getAddress().getHostAddress() + ":" + isa.getPort();
		ChannelFuture channelFuture;
		Channel channel = null;
		try {
			if (connectionTimeout <= 0) {
				connectionTimeout = ((NettyClientConfig) config).getConnectionTimeout();
			}

			channelFuture = this.bootstrap.connect(address);
			if (!channelFuture.await(connectionTimeout)) {
				throw new ConnectTimeoutException( ip);
			}
			channel = channelFuture.channel();
			if (channel == null || !channel.isActive()) {
				throw new SocketException("Connect "+address+" causes unKnow exception");
			}
		} catch (InterruptedException ignored) {
		}
		return channel;
	}


	/**
	 * 启动前
	 *
	 * @throws Exception
	 */
	public void beforeStart() throws Exception {

	}

	/**
	 * 启动
	 *
	 * @throws Exception
	 */
	public void doStart() throws Exception {
		super.doStart();
		// Netty客户端启动器
		bootstrap = new Bootstrap();
		bootstrap.group(ioLoopGroup).channel(config.isEpoll() ? EpollSocketChannel.class : NioSocketChannel.class)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
						((NettyClientConfig) config).getConnectionTimeout()).option(ChannelOption.TCP_NODELAY,
				config.isTcpNoDelay())
				.option(ChannelOption.SO_REUSEADDR, config.isReuseAddress())
				.option(ChannelOption.SO_KEEPALIVE, config.isKeepAlive())
				.option(ChannelOption.SO_LINGER, config.getSoLinger())
				.option(ChannelOption.SO_RCVBUF, config.getSocketBufferSize())
				.option(ChannelOption.SO_SNDBUF, config.getSocketBufferSize()).handler(new SingleChannelClientInitializer());
		cleanup =  Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("RequestCleanUp"));
		cleanup.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (!isStarted()) {
					return;
				}
				clearTimeoutFuture();
			}
		},1000*3,1000, TimeUnit.MILLISECONDS);
	}

	private void clearTimeoutFuture() {
		Iterator<Map.Entry<String, ResponseFuture>> it = futures.entrySet().iterator();
		Map.Entry<String, ResponseFuture> entry;
		ResponseFuture responseFuture;
		long timeout;
		while (it.hasNext()) {
			entry = it.next();
			responseFuture = entry.getValue();
			timeout = responseFuture.getBeginTime() + responseFuture.getTimeout() + 1000;

			if (timeout <= System.currentTimeMillis()) {
				it.remove();
				if (responseFuture.release()) {
					try {
						responseFuture.cancel(new RequestTimeoutException("请求RequestId = "+ responseFuture.getRequestId()+"超时，远程地址为"+responseFuture.getChannel().remoteAddress()));
					} catch (Throwable e) {
						logger.error("clear timeout response exception", e);
					}
					logger.info(String.format("remove timeout request id=%s begin=%d timeout=%d",
							responseFuture.getRequestId(), responseFuture.getBeginTime(), timeout));
				}
			}
		}
	}

	/**
	 * 启动后
	 *
	 * @throws Exception
	 */
	public void afterStart() throws Exception {

	}

	/**
	 * 停止前
	 */
	public void beforeStop()  {

	}

	/**
	 * 停止
	 */
	public void doStop()  {
			super.doStop();
		if(cleanup != null){
			cleanup.shutdown();
		}
		// 关闭所有Future
		ResponseFuture responseFuture;
		for (Map.Entry<String, ResponseFuture> entry : futures.entrySet()) {
			responseFuture = entry.getValue();
			if (responseFuture.release()) {
				try {
					responseFuture.cancel(new InterruptedException("服务被终止"));
				} catch (Throwable ignored) {
				}
			}
		}
		futures.clear();

	}

	/**
	 * 停止后
	 */
	@Override
	public void afterStop()  {

	}
}
