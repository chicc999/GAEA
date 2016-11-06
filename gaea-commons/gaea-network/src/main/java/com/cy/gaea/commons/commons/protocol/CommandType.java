package com.cy.gaea.commons.commons.protocol;

/**
 * Created by cy on 2016/8/17.
 */
public enum CommandType {

/*	REQUEST, RESPONSE;

	public static CommandType valueOf(int ordinal) {
		switch (ordinal) {
			case 0:
				return REQUEST;
			default:
				return RESPONSE;
		}
	}*/

	CREATE(1,"CREATE"),
	HEARTBEAT(51,"HEARTBEAT");

	private int key;

	private String val;

	private CommandType(int key,String val){
		this.key=key;
		this.val=val;
	}

	public int getKey() {
		return key;
	}

	public String getVal() {
		return val;
	}
}
