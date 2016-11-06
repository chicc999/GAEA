package com.cy.gaea.commons.commons.netty;


import com.cy.gaea.commons.commons.exception.RemotingIOException;
import com.cy.gaea.commons.commons.protocol.Command;
import io.netty.channel.Channel;

/**
 * Created by cy on 2016/7/10.
 */
public interface Transport {
	/**
	 * 同步发送，需要应答
	 *
	 * @param channel 通道
	 * @param command 命令
	 * @return 应答命令
	 * @throws RemotingIOException
	 */
	Command sync(Channel channel, Command command) throws RemotingIOException;

	/**
	 * 同步发送，需要应答
	 *
	 * @param channel 通道
	 * @param command 命令
	 * @param timeout 超时
	 * @return 应答命令
	 * @throws RemotingIOException
	 */
	Command sync(Channel channel, Command command, int timeout) throws RemotingIOException;

	/**
	 * 异步发送，需要应答
	 *
	 * @param channel  通道
	 * @param command  命令
	 * @param callback 回调
	 * @return 异步执行的结果
	 * @throws RemotingIOException
	 */
	ResponseFuture async(Channel channel, Command command, CommandCallback callback) throws RemotingIOException;
}
