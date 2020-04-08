package com.lemon.mapper;

import com.lemon.pojo.ApiRequestParam;

import java.util.List;

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
public interface ApiRequestParamMapper extends BaseMapper<ApiRequestParam> {

	//根据apiId查询api的请求参数
	@Select("select * from api_request_param where api_id=#{apiId}")
	public List<ApiRequestParam> findAll(Integer apiId);
	
	//根据caseId查询 
	@Select("SELECT DISTINCT t2.*,t1.api_request_param_value 'value',t1.id valueId FROM case_param_value as t1 JOIN api_request_param AS t2 ON t1.api_request_param_id = t2.id JOIN api AS t3 ON t2.api_id = t3.id WHERE t1.case_id=#{caseId}")
	public List<ApiRequestParam> findByCase(Integer caseId);
}
