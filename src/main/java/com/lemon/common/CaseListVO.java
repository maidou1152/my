package com.lemon.common;

import com.lemon.pojo.TestReport;

import lombok.Data;

@Data
public class CaseListVO {

	/**
	 * case id
	 */
	private Integer id;
	/**
	 * case name
	 */
	private String name;
	/**
	 * api编号
	 */
	private Integer apiId;
	/**
	 * api url地址
	 */
	private String apiUrl;

	private TestReport testReport;

}