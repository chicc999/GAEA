package com.cy.gaea.commons;

/**
 * Created by cy on 2016/7/19.
 *
 * 服务状态
 */
public enum ServiceState {
	/**
	 * 准备启动
	 */
	WILL_START,
	/**
	 * 启动中
	 */
	STARTING,
	/**
	 * 启动失败
	 */
	START_FAILED,
	/**
	 * 启动完成
	 */
	STARTED,
	/**
	 * 准备关闭
	 */
	WILL_STOP,
	/**
	 * 关闭中
	 */
	STOPPING,
	/**
	 * 关闭完成
	 */
	STOPPED
}
