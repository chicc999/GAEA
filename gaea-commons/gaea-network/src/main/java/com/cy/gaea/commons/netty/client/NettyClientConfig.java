package com.cy.gaea.commons.netty.client;

import com.cy.gaea.commons.netty.NettyConfig;

/**
 * Created by cy on 2016/7/10.
 */
public class NettyClientConfig extends NettyConfig {
	// 连接超时(毫秒)
	private int connectionTimeout = 5000;

	public NettyClientConfig() {
		super();
		workerThreads = 4;
		callbackExecutorThreads = Runtime.getRuntime().availableProcessors();
		selectorThreads = 1;
		channelMaxIdleTime = 120 * 1000;
		maxOneway = 256;
		maxAsync = 128;
		setEpoll(true);
	}

	public NettyClientConfig(NettyClientConfig config) {
		super(config);
		setConnectionTimeout(config.getConnectionTimeout());
	}

	public NettyClientConfig workerThreads(final int workerThreads) {
		setWorkerThreads(workerThreads);
		return this;
	}

	public NettyClientConfig callbackExecutorThreads(final int callbackExecutorThreads) {
		setCallbackExecutorThreads(callbackExecutorThreads);
		return this;
	}

	public NettyClientConfig selectorThreads(final int selectorThreads) {
		setSelectorThreads(selectorThreads);
		return this;
	}

	public NettyClientConfig channelMaxIdleTime(final int channelMaxIdleTime) {
		setChannelMaxIdleTime(channelMaxIdleTime);
		return this;
	}

	public NettyClientConfig reuseAddress(final boolean reuseAddress) {
		setReuseAddress(reuseAddress);
		return this;
	}

	public NettyClientConfig soLinger(final int soLinger) {
		setSoLinger(soLinger);
		return this;
	}

	public NettyClientConfig tcpNoDelay(final boolean tcpNoDelay) {
		setTcpNoDelay(tcpNoDelay);
		return this;
	}

	public NettyClientConfig keepAlive(final boolean keepAlive) {
		setKeepAlive(keepAlive);
		return this;
	}

	public NettyClientConfig soTimeout(final int soTimeout) {
		setSoTimeout(soTimeout);
		return this;
	}

	public NettyClientConfig socketBufferSize(final int socketBufferSize) {
		setSocketBufferSize(socketBufferSize);
		return this;
	}

	public NettyClientConfig epoll(final boolean epoll) {
		setEpoll(epoll);
		return this;
	}

	public NettyClientConfig maxOneway(final int maxOneway) {
		setMaxOneway(maxOneway);
		return this;
	}

	public NettyClientConfig maxAsync(final int maxAsync) {
		setMaxAsync(maxAsync);
		return this;
	}

	public NettyClientConfig frameMaxSize(final int frameMaxSize) {
		setFrameMaxSize(frameMaxSize);
		return this;
	}

	public NettyClientConfig connectionTimeout(final int connectionTimeout) {
		setConnectionTimeout(connectionTimeout);
		return this;
	}

	public int getConnectionTimeout() {
		return this.connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		if (connectionTimeout > 0) {
			this.connectionTimeout = connectionTimeout;
		}
	}

}
