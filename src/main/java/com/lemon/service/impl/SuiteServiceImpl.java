package com.lemon.service.impl;

import com.lemon.pojo.Suite;
import com.lemon.mapper.SuiteMapper;
import com.lemon.service.SuiteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
public class SuiteServiceImpl extends ServiceImpl<SuiteMapper, Suite> implements SuiteService {

	@Autowired
	SuiteMapper suiteMapper;
	
	@Override
	public List<Suite> findSuiteAndReleadtedCasesBy(Integer projectId) {

		return suiteMapper.findSuiteAndReleadtedCasesBy(projectId);
	}
}