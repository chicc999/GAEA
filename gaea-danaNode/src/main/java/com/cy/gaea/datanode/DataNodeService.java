package com.cy.gaea.datanode;

import com.cy.gaea.commons.netty.NettyConfig;
import com.cy.gaea.commons.Service;
import com.cy.gaea.commons.netty.server.NettyServer;
import com.cy.gaea.commons.netty.server.NettyServerConfig;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by cy on 16/11/5.
 */
public class DataNodeService extends Service {

	@Autowired
	private DataNodeConfig dataNodeConfig;

	// Netty服务
	@Autowired
	protected NettyServer nettyServer;

	public DataNodeService() {
	}

	public void setDataNodeConfig(DataNodeConfig dataNodeConfig) {
		this.dataNodeConfig = dataNodeConfig;
	}

	@Override
	public void beforeStart() throws Exception {

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
