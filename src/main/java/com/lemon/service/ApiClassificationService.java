package com.lemon.service;

import com.lemon.common.ApiClassificationVO;
import com.lemon.pojo.ApiClassification;

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
public interface ApiClassificationService extends IService<ApiClassification> {

	public List<ApiClassificationVO> getWithApi(Integer projectId);
}
