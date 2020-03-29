package com.lemon.service;

import com.lemon.common.ReportVO;
import com.lemon.pojo.TestReport;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kk
 * @since 2020-02-15
 */
public interface TestReportService extends IService<TestReport> {

	public List<TestReport> run(Integer suiteId);

	public TestReport finbByCaseId(Integer caseId);

	public ReportVO getReport(Integer suiteId);
}
