package com.cy.gaea.common.protocol;

import java.nio.ByteBuffer;

/**
 * Created by cy on 2016/8/17.
 */
public class Response extends Command{

	public Response(String requestId) {
		super(requestId);
	}

	@Override
	ByteBuffer encode() {
		return null;
	}
}
