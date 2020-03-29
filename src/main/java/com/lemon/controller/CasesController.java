package com.lemon.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lemon.common.ApiVO;
import com.lemon.common.CaseEditVO;
import com.lemon.common.CaseListVO;
import com.lemon.common.Result;
import com.lemon.pojo.Cases;
import com.lemon.service.CasesService;

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
@RequestMapping("/cases")
@Api("集合模块")
public class CasesController {
	
	@Autowired
	CasesService casesService;

		//根据projectId单表查询分类信息s
		@PostMapping("/add")
		@ApiOperation(value="添加测试集",httpMethod="POST")
		public Result add(Cases casesVo,ApiVO apiRunVO) {
			//添加到cases
			casesService.add(casesVo, apiRunVO);
			return new Result("1", "测试集合添加成功");
		}
		
		@GetMapping("/showCasesUnderProject")
		@ApiOperation(value="查询工程下的测试集",httpMethod="GET")
		public Result showCasesUnderProject(Integer projectId) {
			List<CaseListVO> list=casesService.showCasesUnderProject(projectId);
			return new Result("1",list);
		}
		
		@GetMapping("/showCasesUnderSuite")
		@ApiOperation(value="查询套件下的测试集",httpMethod="GET")
		public Result showCasesUnderSuite(Integer suiteId) {
			List<CaseListVO> list=casesService.showCasesUnderSuite(suiteId);
			return new Result("1", list);
		}
		
		@GetMapping("/toCaseEdit")
		@ApiOperation(value="查询套件下的测试集",httpMethod="GET")
		public Result toCaseEdit(Integer caseId) {
			CaseEditVO caseEditVo=casesService.findCaseEditVo(caseId);
			return new Result("1", caseEditVo,"查询成功");
		}
		
		@PutMapping("/update")
		@ApiOperation(value="更新用例",httpMethod="PUT")
		public Result updateCase(CaseEditVO caseEditVo) {
			casesService.updateCase(caseEditVo);
			return new Result("1","更新用例成功");
		}
		
		@DeleteMapping("/{caseId}")
		@ApiOperation(value="删除测试用例方法",httpMethod="DELETE")
		public Result deleteById(@PathVariable("caseId") Integer caseId,Cases cases){
			Result result=null;
			//调用业务层方法：插入到数据库中，统一处理异常
			cases.setId(caseId);
			casesService.removeById(caseId);
			result=new Result("1",cases,"删除用例");
			return result;
		}
		
}