package com.lemon.mapper;

import com.lemon.pojo.TestRule;

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
public interface TestRuleMapper extends BaseMapper<TestRule> {
	@Select("SELECT * from test_rule where case_id=#{caseId}")
	public List<TestRule> findByCase(Integer caseId);
}
