package com.lemon.service.impl;

import com.lemon.pojo.ApiRequestParam;
import com.lemon.pojo.CaseParamValue;
import com.lemon.pojo.Cases;
import com.lemon.common.ApiVO;
import com.lemon.common.CaseEditVO;
import com.lemon.common.CaseListVO;
import com.lemon.mapper.CasesMapper;
import com.lemon.service.CaseParamValueService;
import com.lemon.service.CasesService;
import com.lemon.service.TestReportService;
import com.lemon.service.TestRuleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kk
 * @since 2020-02-15
 */
@Service
public class CasesServiceImpl extends ServiceImpl<CasesMapper, Cases> implements CasesService {

	
	@Autowired
	CaseParamValueService caseParamValueService;
	@Autowired
	CasesMapper casesMapper;
	
	@Autowired
	TestRuleService testRuleService;
	
	public void add(Cases casesVo,ApiVO apiRunVO) {
		//添加到cases
		this.save(casesVo);
		//批量添加到case_param_value
		List<ApiRequestParam> requestParams=apiRunVO.getRequestParams();
		
		List<CaseParamValue> caseParamValues=new ArrayList<CaseParamValue>();
		for (ApiRequestParam apiRequestParam:requestParams) {
			CaseParamValue caseParamValue=new CaseParamValue();
			caseParamValue.setCaseId(casesVo.getId());
			caseParamValue.setApiRequestParamId(apiRequestParam.getId());
			caseParamValue.setApiRequestParamValue(apiRequestParam.getValue());
			caseParamValues.add(caseParamValue);
		}
		caseParamValueService.saveBatch(caseParamValues);
	}

	@Override
	public List<CaseListVO> showCasesUnderProject(Integer projectId) {
		return casesMapper.showCasesUnderProject(projectId);
	}

	@Override
	public List<CaseListVO> showCasesUnderSuite(Integer suiteId) {
		return casesMapper.showCasesUnderSuite(suiteId);
	}

	
	public CaseEditVO findCaseEditVo(Integer caseId) {
		return casesMapper.findCaseEditVo(caseId);
	}

	@Override
	public void updateCase(CaseEditVO caseEditVo) {
		//此时只要更新case表和case_param_value表，还需要更新test_rule表
		updateById(caseEditVo);
		List<ApiRequestParam> requestParams=caseEditVo.getRequestParams();
		
		List<CaseParamValue> list=new ArrayList<CaseParamValue>();
		for (ApiRequestParam apiRequestParam : requestParams) {
			CaseParamValue caseParamValue=new CaseParamValue();
			caseParamValue.setId(apiRequestParam.getValueId());
			caseParamValue.setApiRequestParamId(apiRequestParam.getId());
			caseParamValue.setApiRequestParamValue(apiRequestParam.getValue());
			caseParamValue.setCaseId(caseEditVo.getId());
			list.add(caseParamValue);
		}
		caseParamValueService.updateBatchById(list);
	
		//先删除test_Rule（这张表没有被其他表引用--根据外键删除），再插入
		QueryWrapper queryWrapper=new QueryWrapper();
		queryWrapper.eq("case_id", caseEditVo.getId());
		testRuleService.remove(queryWrapper);
		//insert
		testRuleService.saveBatch(caseEditVo.getTestRules());	
	}
}
