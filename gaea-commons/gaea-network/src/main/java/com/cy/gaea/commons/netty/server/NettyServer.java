package com.cy.gaea.commons.netty.server;

import com.cy.gaea.commons.netty.NettyTransport;
import com.cy.gaea.commons.netty.handler.CommandHandlerFactory;
import com.cy.gaea.commons.util.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * 服务
 */
public class NettyServer extends NettyTransport {

	private static Logger logger = LoggerFactory.getLogger(NettyServer.class);

	// 接受连接请求的线程池
	protected EventLoopGroup bossLoopGroup;
	// 服务端启动器
	protected ServerBootstrap bootStrap;
	// 是否是内部创建
	protected boolean createBossLoopGroup;


	/**
	 * 构造函数
	 *
	 * @param config 配置
	 */
	public NettyServer(NettyServerConfig config) {
		this(config, null, null, null, null);
	}

	/**
	 * 构造函数
	 *
	 * @param config        配置
	 * @param bossLoopGroup 接受连接请求的线程池
	 * @param ioLoopGroup   IO处理的线程池
	 * @param workerGroup   命令处理线程池
	 */
	public NettyServer(NettyServerConfig config, EventLoopGroup bossLoopGroup, EventLoopGroup ioLoopGroup,
					   EventExecutorGroup workerGroup) {
		this(config, bossLoopGroup, ioLoopGroup, workerGroup, null);
	}

	/**
	 * 构造函数
	 *
	 * @param config        配置
	 * @param bossLoopGroup 接受连接请求的线程池
	 * @param ioLoopGroup   IO处理的线程池
	 * @param workerGroup   命令处理线程池
	 * @param factory       命令工厂类
	 */
	public NettyServer(NettyServerConfig config, EventLoopGroup bossLoopGroup, EventLoopGroup ioLoopGroup,
					   EventExecutorGroup workerGroup, CommandHandlerFactory factory) {
		super(config, ioLoopGroup, workerGroup, factory);
		this.bossLoopGroup = bossLoopGroup;
	}

	public NettyServer config(NettyServerConfig config) {
		this.config = config;
		return this;
	}

	public EventLoopGroup getBossLoopGroup() {
		return bossLoopGroup;
	}

	public NettyServerConfig getConfig() {
		return (NettyServerConfig) config;
	}

	@Override
	public void doStart() throws Exception {
		super.doStart();
		NettyServerConfig serverConfig = (NettyServerConfig) config;
		InetSocketAddress address;
		if (serverConfig.getIp() == null || serverConfig.getIp().isEmpty()) {
			address = new InetSocketAddress(InetAddress.getByName(serverConfig.getIp()), serverConfig.getPort());
		} else {
			address = new InetSocketAddress(serverConfig.getIp(), serverConfig.getPort());
		}
		bootStrap = new ServerBootstrap();
		configure(serverConfig, address);
		bootStrap.bind().sync();
		logger.info(String.format("开始监听%s端口提供服务",serverConfig.getPort()));
	}

	/**
	 * 配置启动器
	 *
	 * @param serverConfig 服务端配置
	 * @param address      地址
	 */
	protected void configure(NettyServerConfig serverConfig, InetSocketAddress address) {
		if (bossLoopGroup == null) {
			bossLoopGroup = createEventLoopGroup(config.getSelectorThreads(), new NamedThreadFactory("BossLoopGroup"));
			createBossLoopGroup = true;
		}
		bootStrap.group(bossLoopGroup, ioLoopGroup)
				.channel(config.isEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class).option(
				ChannelOption.TCP_NODELAY, serverConfig.isTcpNoDelay())
				.option(ChannelOption.SO_REUSEADDR, serverConfig.isReuseAddress())
				.option(ChannelOption.SO_KEEPALIVE, serverConfig.isKeepAlive())
				.option(ChannelOption.SO_LINGER, serverConfig.getSoLinger())
				.option(ChannelOption.SO_RCVBUF, serverConfig.getSocketBufferSize())
				.option(ChannelOption.SO_SNDBUF, serverConfig.getSocketBufferSize())
				.option(ChannelOption.SO_BACKLOG, serverConfig.getBacklog()).localAddress(address)
				.childHandler(new ServerInitializer());
	}

	@Override
	public void doStop() {
		if (createBossLoopGroup) {
			bossLoopGroup.shutdownGracefully();
			bossLoopGroup = null;
			createBossLoopGroup = false;
		}
		super.doStop();
	}

}