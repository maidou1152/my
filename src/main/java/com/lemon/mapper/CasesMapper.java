package com.lemon.mapper;

import com.lemon.common.CaseEditVO;
import com.lemon.common.CaseListVO;
import com.lemon.pojo.Cases;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
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
public interface CasesMapper extends BaseMapper<Cases> {

	//某个suite下的cases
	@Select("select * from cases where suite_Id=#{suiteId}")
	List<Cases> findAll(Integer SuiteId);
	
	//某个工程下的cases api 信息
	@Select("SELECT DISTINCT t1.*,t6.id apiId,t6.url apiUrl FROM cases AS t1 JOIN suite AS t2 ON t1.suite_id = t2.id JOIN project AS t3 ON t2.project_id = t3.id JOIN case_param_value AS t4 ON t1.id = t4.case_id JOIN api_request_param AS t5 ON t4.api_request_param_id = t5.id JOIN api AS t6 ON t5.api_id = t6.id WHERE t3.id =#{projectId}")
	@Results({
		@Result(column="id",property="id"),
		@Result(column="id",property="testReport",
		one=@One(select="com.lemon.mapper.TestReportMapper.findByCaseId"))
	})
	List<CaseListVO> showCasesUnderProject(Integer projectId);
	
	//某个套件下的cases api 信息
	@Select("SELECT DISTINCT t1.*,t6.id apiId,t6.url apiUrl FROM cases AS t1 JOIN suite AS t2 ON t1.suite_id = t2.id JOIN case_param_value AS t4 ON t1.id = t4.case_id JOIN api_request_param AS t5 ON t4.api_request_param_id = t5.id JOIN api AS t6 ON t5.api_id = t6.id WHERE t1.suite_id =#{suiteId}")
	//查询测试报告-->外键id
	@Results({
		@Result(column="id",property="id"),
		@Result(column="id",property="testReport",
		one=@One(select="com.lemon.mapper.TestReportMapper.findByCaseId"))})
	List<CaseListVO> showCasesUnderSuite(Integer suiteId);
	
	//根据caseId查询case、api信息
	@Select("SELECT DISTINCT t1.*, t4.id apiId,t4.url,t4.method FROM cases AS t1 JOIN case_param_value as t2 on t2.case_id=t1.id JOIN api_request_param AS t3 ON t3.id=t2.api_request_param_id JOIN api AS t4 ON t3.api_id = t4.id WHERE t1.id =#{caseId}")
	@Results({
		@Result(column="id",property="id"),
		@Result(column="id",property="requestParams",many=@Many(select="com.lemon.mapper.ApiRequestParamMapper.findByCase")),
		@Result(column="id",property="testRules",many=@Many(select="com.lemon.mapper.TestRuleMapper.findByCase"))
	})
	CaseEditVO findCaseEditVo(Integer caseId);
}