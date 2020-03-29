package com.lemon.service;

import com.lemon.common.ApiVO;
import com.lemon.common.CaseEditVO;
import com.lemon.common.CaseListVO;
import com.lemon.pojo.Cases;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @since 2020-02-15
 */
public interface CasesService extends IService<Cases> {
	public void add(Cases casesVo,ApiVO apiRunVO);
	List<CaseListVO> showCasesUnderProject(Integer projectId);
	List<CaseListVO> showCasesUnderSuite(Integer suiteId);
	CaseEditVO findCaseEditVo(Integer caseId);
	public void updateCase(CaseEditVO caseEditVo);
}
