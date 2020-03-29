package com.lemon.common;

import lombok.Data;

@Data
public class Result {
	/**
	 * 状态码
	 */
	private String status;//1 0
	/**
	 * 数据
	 */
	private Object data;
	/**
	 * 信息
	 */
	private String message;
	public Result(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	public Result(String status, Object data) {
		super();
		this.status = status;
		this.data = data;
	}
	public Result(String status, Object data, String message) {
		super();
		this.status = status;
		this.data = data;
		this.message = message;
	}
	
}
