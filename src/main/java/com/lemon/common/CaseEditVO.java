package com.lemon.common;

import java.util.ArrayList;
import java.util.List;

import com.lemon.pojo.ApiRequestParam;
import com.lemon.pojo.Cases;
import com.lemon.pojo.TestRule;

import lombok.Data;

@Data
public class CaseEditVO extends Cases {

	/**
	 * case id
	 *//*
	private String id;
	*//**
	 * case name
	 *//*
	private String name;*/
	/**
	 * api编号
	 */
	private String apiId;
	/**
	 * api url地址
	 */
	private String url;
	/**
	 * 接口请求ip+端口
	 */
	private String host;
	/**
	 * 请求方式
	 */
	private String  method;
	/**
	 * 请求参数
	 */
	private List<ApiRequestParam> requestParams=new ArrayList<ApiRequestParam>();
	/**
	 * testRule 测试规则集合
	 */
	private List<TestRule> testRules=new ArrayList<TestRule>();
}