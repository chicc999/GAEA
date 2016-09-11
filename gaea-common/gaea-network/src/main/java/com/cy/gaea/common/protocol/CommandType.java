package com.cy.gaea.common.protocol;

/**
 * Created by cy on 2016/8/17.
 */
public enum CommandType {

	REQUEST, RESPONSE;

	public static CommandType valueOf(int ordinal) {
		switch (ordinal) {
			case 0:
				return REQUEST;
			default:
				return RESPONSE;
		}
	}
}
