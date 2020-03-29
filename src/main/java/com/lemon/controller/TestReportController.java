package com.lemon.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.lemon.common.ReportVO;
import com.lemon.common.Result;
import com.lemon.pojo.TestReport;
import com.lemon.service.TestReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kk
 * @since 2020-02-15
 */
@RestController
@RequestMapping("/testReport")
@Api("测试报告模块")
public class TestReportController {

	@Autowired
	TestReportService  testReportService;
	
	@RequestMapping("/run")
	@ApiOperation(value="执行测试",httpMethod="POST")
	public Result run(Integer suiteId) {
		List<TestReport> reportList =testReportService.run(suiteId);
		return new Result("1",reportList,"执行成功");
	}
	
	@RequestMapping("/findCaseRunResult")
	@ApiOperation(value="查询测试报告",httpMethod="POST")
	public Result findCaseRunResult(Integer caseId) {
		TestReport report =testReportService.finbByCaseId(caseId);
		return new Result("1",report,"执行成功");
	}
	
	@RequestMapping("/get")
	@ApiOperation(value="查看套件报告",httpMethod="POST")
	public Result get(Integer suiteId) {
		ReportVO report =testReportService.getReport(suiteId);
		return new Result("1",report,"执行成功");
	}
}
