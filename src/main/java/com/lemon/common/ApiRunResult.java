package com.lemon.common;

import lombok.Data;

/**
 * api运行结果
 * 响应码、响应头、响应头
 * @author asus
 *
 */
@Data
public class ApiRunResult {
	/**
	 * 状态码
	 */
	private String statusCode;
	/**
	 * 消息头
	 */
	private String headers;//HttpHeaders 是MultiValueMap  需要转string
	/**
	 * 请求体
	 */
	private String body;
}
