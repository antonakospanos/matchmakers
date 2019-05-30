package com.workable.matchmakers.support;


public class LoggingHelper {

	public static String logInboundRequest(Object msg) {
		return "<-- REQ: " + msg;
	}

	public static String logInboundResponse(Object msg) {
		return "--> RES: " + msg;
	}

	public static String logOutboundRequest(Object msg) {
		return "--> REQ: " + msg;
	}

	public static String logOutboundResponse(Object msg) {
		return "<-- RES: " + msg;
	}
}
