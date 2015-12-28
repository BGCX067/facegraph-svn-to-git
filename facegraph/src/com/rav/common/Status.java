package com.rav.common;

public final class Status {
	private int status;
	private String msg;

	public static final Status SUCCESS = new Status(0);
	public static final Status ERROR = new Status(1);
	public static final Status WARRNING = new Status(2);

	public Status(int status) {
		this(status, "");
	}

	public Status(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	public int getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}
}
