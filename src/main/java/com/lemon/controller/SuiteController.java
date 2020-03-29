package com.lemon.controller;


import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lemon.common.Result;
import com.lemon.pojo.ApiClassification;
import com.lemon.pojo.Cases;
import com.lemon.pojo.Suite;
import com.lemon.pojo.User;
import com.lemon.service.SuiteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kk
 * @since 2020-02-15
 */
@RestController
@RequestMapping("/suite")
@Api("集合模块")
public class SuiteController {
	
	@Autowired
	SuiteService suiteService;
	
	//根据projectId获得套件
		@PostMapping("/listAll")
		@ApiOperation(value="查找分类信息",httpMethod="GET")
		public Result listAll(Integer projectId) {
			QueryWrapper queryWrapper=new QueryWrapper();
			queryWrapper.eq("project_id",projectId );
			List<Suite> list=suiteService.list(queryWrapper);
			return new Result("1",list);
		}
		
		@PostMapping("/add")
		@ApiOperation(value="增加集合方法",httpMethod="POST")
		public Result add(Suite suite,Integer projectId) {
			Result result=null;
			//设置创建人
			User user=(User) SecurityUtils.getSubject().getPrincipal();
			suite.setCreateUser(user.getId());
			//设置project_id
			suite.setProjectId(projectId);
			//保存套件信息
			suiteService.save(suite);
			result=new Result("1","集合添加成功");
			return result;
		}
		
		@DeleteMapping("/{suiteId}")
		@ApiOperation(value="删除测试用例方法",httpMethod="DELETE")
		public Result deleteById(@PathVariable("suiteId") Integer suiteId,Suite suite){
			Result result=null;
			//调用业务层方法：插入到数据库中，统一处理异常
			suite.setId(suiteId);
			suiteService.removeById(suiteId);
			result=new Result("1",suite,"删除套件");
			return result;
		}
		
}