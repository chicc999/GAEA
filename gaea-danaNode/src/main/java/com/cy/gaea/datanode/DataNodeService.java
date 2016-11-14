package com.cy.gaea.datanode;

import com.cy.gaea.commons.netty.NettyConfig;
import com.cy.gaea.commons.Service;
import com.cy.gaea.commons.netty.handler.CommandHandler;
import com.cy.gaea.commons.netty.handler.CommandHandlerFactory;
import com.cy.gaea.commons.netty.handler.DefaultHandlerFactory;
import com.cy.gaea.commons.netty.server.NettyServer;
import com.cy.gaea.commons.netty.server.NettyServerConfig;
import com.cy.gaea.commons.utils.ArgumentUtil;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by cy on 16/11/5.
 */
public class DataNodeService extends Service {

	@Autowired
	private DataNodeConfig dataNodeConfig;

	// Netty服务
	protected NettyServer nettyServer;

	public DataNodeService() {
	}

	public void setDataNodeConfig(DataNodeConfig dataNodeConfig) {
		this.dataNodeConfig = dataNodeConfig;
	}

	@Override
	public void beforeStart() throws Exception {
		ArgumentUtil.checkNotNull(dataNodeConfig,"dataNodeConfig");
		ArgumentUtil.checkNotNull(dataNodeConfig.getNettyServerConfig(),"nettyServerConfig");

		CommandHandlerFactory defaultHandlerFactory = new DefaultHandlerFactory();

		nettyServer = new NettyServer(dataNodeConfig.getNettyServerConfig(),null,null,null, defaultHandlerFactory);
	}

	@Override
	public void doStart() throws Exception {

	}


	@Override
	public void afterStart() throws Exception {

	}

	@Override
	public void beforeStop() {

	}

	@Override
	public void doStop() {

	}

	@Override
	public void afterStop() {

	}

}
