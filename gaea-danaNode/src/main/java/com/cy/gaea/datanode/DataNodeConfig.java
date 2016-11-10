package com.cy.gaea.datanode;

import com.cy.gaea.commons.netty.server.NettyServerConfig;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cy on 16/11/7.
 */
public class DataNodeConfig {

	@Autowired
	private NettyServerConfig nettyServerConfig;

	public NettyServerConfig getNettyServerConfig() {
		return nettyServerConfig;
	}

	public void setNettyServerConfig(NettyServerConfig nettyServerConfig) {
		this.nettyServerConfig = nettyServerConfig;
	}
}
