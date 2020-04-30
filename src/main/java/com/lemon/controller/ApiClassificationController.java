package com.lemon.controller;


import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lemon.common.ApiClassificationVO;
import com.lemon.service.ApiClassificationService;
import com.lemon.service.SuiteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.lemon.common.Result;
import com.lemon.mapper.SuiteMapper;
import com.lemon.pojo.ApiClassification;
import com.lemon.pojo.Project;
import com.lemon.pojo.Suite;
import com.lemon.pojo.User;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kk
 * @since 2020-02-15
 */
@RestController
@RequestMapping("/apiClassification")
@Api("接口分类模块")
public class ApiClassificationController {

	@Autowired 
	ApiClassificationService apiClassificationService;
	@Autowired
	SuiteService suiteService;
	
	@GetMapping("/toIndex")
	@ApiOperation(value="接口分类",httpMethod="GET")
	public Result getWithApi(Integer projectId,Integer tab){
		Result result=null;
		if (tab==1) {
			//接口列表
			List<ApiClassificationVO> list=	apiClassificationService.getWithApi(projectId);
			result=new Result("1",list,"查询分类同时也延迟加载分类");
		}else{
			//tab=2 用例列表
			List<Suite> list2=suiteService.findSuiteAndReleadtedCasesBy(projectId);
			result=new Result("1",list2,"查询集合同时也延迟加载测试集合");
		}
		return result;
	}
	
	@PostMapping("/add")
	@ApiOperation(value="增加接口分类方法",httpMethod="POST")
	public Result add(ApiClassification apiClassification,Integer projectId) {
		Result result=null;
		//设置创建人
		User user=(User) SecurityUtils.getSubject().getPrincipal();
		apiClassification.setCreateUser(user.getId());
		apiClassification.setProjectId(projectId);
		apiClassificationService.save(apiClassification);
		result=new Result("1","接口分类添加成功");
		return result;
	}
	
	//根据projectId单表查询分类信息
	@GetMapping("/findAll")
	@ApiOperation(value="查找分类信息",httpMethod="GET")
	public Result findAll(Integer projectId) {
		QueryWrapper queryWrapper=new QueryWrapper();
		queryWrapper.eq("project_id",projectId );
		List<ApiClassification> list=apiClassificationService.list(queryWrapper);
		return new Result("1",list);
	}
	
	//更新分类名称
	
	@PutMapping("/{apiclassificationId}")
	@ApiOperation(value="更新分类名称",httpMethod="PUT")
	public Result updateById(@PathVariable("apiclassificationId") Integer apiclassificationId,ApiClassification apiClassification){
		Result result=null;
		//调用业务层方法：插入到数据库中，统一处理异常
		apiClassification.setId(apiclassificationId);
		apiClassificationService.updateById(apiClassification);
		result=new Result("1",apiClassification,"更新项目");
		return result;
	}
	
	//删除分类
	@DeleteMapping("/{apiclassificationId}")
	@ApiOperation(value="删除接口分类方法",httpMethod="DELETE")
	public Result deleteById(@PathVariable("apiclassificationId") Integer apiclassificationId,ApiClassification	apiClassification){
		Result result=null;
		//调用业务层方法：插入到数据库中，统一处理异常
		apiClassification.setId(apiclassificationId);
		apiClassificationService.removeById(apiclassificationId);
		result=new Result("1",apiClassification,"删除分类");
		return result;
	}
}