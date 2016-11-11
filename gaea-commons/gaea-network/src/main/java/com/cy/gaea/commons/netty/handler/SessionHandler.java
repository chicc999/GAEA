package com.cy.gaea.commons.netty.handler;

import com.cy.gaea.commons.protocol.Command;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;

/**
 * Created by cy on 16/11/11.
 */
public class SessionHandler implements CommandHandler{
	@Override
	public Command process(ChannelHandlerContext ctx, Command command) throws Exception {
		return null;
	}

	@Override
	public ExecutorService getExecutorService(Command command) {
		return null;
	}
}
