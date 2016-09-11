package com.cy.gaea.common.exception;

import java.net.SocketTimeoutException;

/**
 * Created by cy on 2016/8/18.
 */
public class RequestTimeoutException extends SocketTimeoutException {
	public RequestTimeoutException(String msg) {
		super(msg);
	}

	public RequestTimeoutException() {
	}
}
