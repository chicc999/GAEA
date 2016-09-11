package com.cy.gaea.common.netty.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;

/**
 * Created by cy on 2016/8/14.
 */
@ChannelHandler.Sharable
public class SingleChannelClientInitializer extends ChannelInitializer {
	@Override
	protected void initChannel(Channel channel) throws Exception {

	}
}
