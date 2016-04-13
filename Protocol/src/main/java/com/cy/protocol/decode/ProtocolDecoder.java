package com.cy.protocol.decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;


public class ProtocolDecoder extends ByteToMessageDecoder {

	private enum State {
		Prefix, Header, Body
	}

	private State state = State.Prefix;

	private int headerLength;

	private int bodyLength;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
			throws Exception {

	}

}
