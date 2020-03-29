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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.lemon.common.ApiListVO;
import com.lemon.common.ApiRunResult;
import com.lemon.common.ApiVO;
import com.lemon.common.Result;
import com.lemon.pojo.Api;
import com.lemon.pojo.ApiClassification;
import com.lemon.pojo.ApiRequestParam;
import com.lemon.pojo.User;
import com.lemon.service.ApiRequestParamService;
import com.lemon.service.ApiService;
import com.lemon.util.IsNullUtil;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wumy
 * @since 2020-02-15
 */
@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	ApiService apiService;
	
	@Autowired
	ApiRequestParamService apiRequestParamService;
	
	@GetMapping("/showApiUnderProject")
	@ApiOperation(value="显示项目下的api信息",httpMethod="GET")
	public Result showApiUnderProject(Integer projectId) {
		 List<ApiListVO> list=apiService.showApiListByProject(projectId);
		 Result result=new Result("1", list);
		 return result;
	}
	
	
	@GetMapping("/showApiUnderApiClassification")
	@ApiOperation(value="显示分类下的api信息",httpMethod="GET")
	public Result showApiUnderApiClassification(Integer apiClassificationId) {
		 List<ApiListVO> list=apiService.showApiListByClassification(apiClassificationId);
		 Result result=new Result("1", list);
		 return result;
	}
	
	@GetMapping("/toApiView")
	@ApiOperation(value="显示分类下的api信息",httpMethod="GET")
	public Result toApiView(Integer apiId) {
		 ApiVO api=apiService.findApiViewVO(apiId);
		 Result result=new Result("1", api);
		 return result;
	}
	
	//添加接口
	@PostMapping("/add")
	@ApiOperation(value="新增api信息",httpMethod="POST")
	public Result add(Api api){
		//设置创建人
		User user=(User) SecurityUtils.getSubject().getPrincipal();
		api.setCreateUser(user.getId());
		//调用业务层方法：插入到数据库中，统一处理异常
		apiService.save(api);
		Result result=new Result("1","接口添加成功");
		return result;
	}
	
	//删除分类
	@DeleteMapping("/{apiId}")
	@ApiOperation(value="删除接口分类方法",httpMethod="DELETE")
	public Result deleteById(@PathVariable("apiId") Integer apiId,Api api){
		Result result=null;
		//调用业务层方法：插入到数据库中，统一处理异常
		api.setId(apiId);
		apiService.removeById(apiId);
		result=new Result("1",api,"删除接口");
		return result;
	}
	
	//更新接口
	@PutMapping("/edit")
	@ApiOperation(value="更新api信息",httpMethod="PUT")
	public Result edit(ApiVO apiEdit){
		//1、根据apiId更新api的基本信息
		apiService.updateById(apiEdit);
		//2、delete删除原来的apiRequestParam
		QueryWrapper queryWrapper=new QueryWrapper();
		queryWrapper.eq("api_id", apiEdit.getId());
		apiRequestParamService.remove(queryWrapper);
		//3、insert apiRequestParam
		apiEdit.getRequestParams().addAll(apiEdit.getBodyParams());
		apiEdit.getRequestParams().addAll(apiEdit.getQueryParams());
		apiEdit.getRequestParams().addAll(apiEdit.getHeaderParams());
		apiEdit.getRequestParams().addAll(apiEdit.getBodyRawParams());
		//遍历requestparams 判断其每个对象属性值是否为null  true ,remove
		List<ApiRequestParam> list=apiEdit.getRequestParams();
//		if () {
//			
//		}
		apiRequestParamService.saveBatch(apiEdit.getRequestParams());
		Result result=new Result("1","接口更新成功");
		return result;
	}
	
	
	
	
	@RequestMapping("/run")
	//@ApiOperation(value="执行api",httpMethod="PUT")
	public Result run(ApiVO apiRunVO) throws JsonProcessingException{
		ApiRunResult apiRunResult=apiService.run(apiRunVO);
		Result result=new Result("1",apiRunResult);
		return result;
	}
	
}