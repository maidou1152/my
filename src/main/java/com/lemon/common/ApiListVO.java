package com.lemon.common;

import lombok.Data;

/**
 * api信息
 * @author asus
 *
 */
@Data
public class ApiListVO {
	/**
	 * api的id
	 */
	private String  id;
	/**
	 * api的名字
	 */
	private String  name;
	/**
	 * api请求方式
	 */
	private String  method;
	/**
	 * api地址
	 */
	private String  url;
	/**
	 * api分类名字
	 */
	private String  classificationName;
}