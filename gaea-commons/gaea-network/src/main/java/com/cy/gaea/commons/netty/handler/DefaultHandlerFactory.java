package com.cy.gaea.commons.netty.handler;

/**
 * Created by cy on 16/11/11.
 */
public class DefaultHandlerFactory implements CommandHandlerFactory{

	protected SessionHandler sessionHandler;

	@Override
	public CommandHandler getHandler(int type) {
		return null;
	}
}
