package com.cy.gaea.commons.commons.netty.event;

import java.net.InetSocketAddress;

import io.netty.channel.Channel;

/**
 * Created by cy on 2016/7/10.
 */
public class NettyEvent {
	private final NettyEventType type;
	private final InetSocketAddress remoteAddr;
	private final Channel channel;


	public NettyEvent(NettyEventType type, InetSocketAddress remoteAddr, Channel channel) {
		this.type = type;
		this.remoteAddr = remoteAddr;
		this.channel = channel;
	}


	public NettyEventType getType() {
		return type;
	}


	public InetSocketAddress getRemoteAddr() {
		return remoteAddr;
	}


	public Channel getChannel() {
		return channel;
	}


	@Override
	public String toString() {
		return "NettyEvent [type=" + type + ", remoteAddr=" + remoteAddr.getAddress() + ", channel=" + channel + "]";
	}
}