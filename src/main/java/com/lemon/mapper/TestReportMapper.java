package com.lemon.mapper;

import com.lemon.common.CaseEditVO;
import com.lemon.common.ReportVO;
import com.lemon.pojo.TestReport;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kk
 * @since 2020-02-15
 */
public interface TestReportMapper extends BaseMapper<TestReport> {

	@Select("SELECT DISTINCT t1.*, t6.id apiId,t6.method,t6.url,t3.`host` FROM cases AS t1 JOIN suite AS t2 ON t1.suite_id = t2.id JOIN project AS t3 ON t2.project_id = t3.id JOIN case_param_value AS t4 ON t1.id = t4.case_id JOIN api_request_param AS t5 ON t4.api_request_param_id = t5.id JOIN api AS t6 ON t5.api_id = t6.id WHERE t2.id=#{suiteId}"
//"SELECT DISTINCT t1.*,t6.id apiId,t6.url,t6.method,t6.host FROM cases AS t1 JOIN suite AS t2 ON t1.suite_id = t2.id JOIN case_param_value AS t4 ON t1.id = t4.case_id JOIN api_request_param AS t5 ON t4.api_request_param_id = t5.id JOIN api AS t6 ON t5.api_id = t6.id WHERE t1.suite_id =#{suiteId}"
)
	@Results({
		@Result(column="id",property="id"),
		@Result(column="id",property="requestParams",many=@Many(select="com.lemon.mapper.ApiRequestParamMapper.findByCase")),
		@Result(column="id",property="testRules",many=@Many(select="com.lemon.mapper.TestRuleMapper.findByCase"))
	})
	List<CaseEditVO> findCaseEditVosUnderSuite(Integer suiteId);
	
	//删除某个套件下的测试报告
	@Delete("DELETE from test_report WHERE case_id in (SELECT case_id from suite where id=#{suitId})")
	void deleteReport(Integer suiteId);
	
	//查询某个测试用例的测试报告
	@Select("select * from test_report where case_id=#{caseId}")
	TestReport findByCaseId(Integer caseId);
	
	//查询套件信息
	@Select("select * from suite where id=#{suiteId}")
	//套件下面的所有用例
	@Results({@Result(column="id",property="id"),
		@Result(column="id",property="caseList",
		many=@Many(select="com.lemon.mapper.CasesMapper.showCasesUnderSuite"))
	})
	public ReportVO getReport(Integer suiteId);
}