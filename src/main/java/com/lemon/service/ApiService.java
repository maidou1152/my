package com.lemon.service;

import com.lemon.common.ApiListVO;
import com.lemon.common.ApiRunResult;
import com.lemon.common.ApiVO;
import com.lemon.pojo.Api;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kk
 * @since 2020-02-15
 */
public interface ApiService extends IService<Api> {

	public List<ApiListVO> showApiListByProject(Integer projectId);
	public List<ApiListVO> showApiListByClassification(Integer apiclassificationId);
	public ApiVO findApiViewVO(Integer apiId);
	public ApiRunResult run(ApiVO apiRunVO) throws JsonProcessingException;
}
