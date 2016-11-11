package com.cy.gaea.datanode.startup;

import com.cy.gaea.datanode.DataNodeService;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;


/**
 * Created by cy on 16/11/4.
 */
public class Bootstrap {
	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

	private final static String DATA_NODE_SERVICE = "dataNodeService";

	public static void main(String[] args){

		// 重新加载log4j配置文件
		URL url = Bootstrap.class.getClassLoader().getResource("logback.xml");
		PropertyConfigurator.configure(url);

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
			dataNodeService.doStart();
		} catch (Exception e) {
			logger.error("启动失败",e);
		}

	}
}
