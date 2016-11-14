package com.cy.gaea.datanode.startup;

import com.cy.gaea.datanode.DataNodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * Created by cy on 16/11/4.
 */
public class Bootstrap {
	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

	private final static String DATA_NODE_SERVICE = "dataNodeService";

	public static void main(String[] args){

		String ip;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
			logger.info(String.format("本机ip为 %s",ip));
		} catch (UnknownHostException e) {
			logger.error("获取本机ip失败",e);
		}


		ApplicationContext ctx;
		try {

			logger.info("load spring xml...");

			ctx = new ClassPathXmlApplicationContext("spring-*.xml");
		} catch (Exception e) {
			logger.error("load config error.", e);
			return;
		}
 			final DataNodeService dataNodeService = (DataNodeService)ctx.getBean(DATA_NODE_SERVICE);
		try {
			dataNodeService.start();
		} catch (Exception e) {
			logger.error("启动失败",e);
		}


		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				logger.info("system is being shutdown.");
				if (dataNodeService.isStarted()) {
					dataNodeService.stop();
				}
			}
		});

	}
}
