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
import com.lemon.common.Result;
import com.lemon.pojo.Project;
import com.lemon.pojo.User;
import com.lemon.service.ProjectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wmy
 * @since 2020-02-15
 */
@RestController
@RequestMapping("/project")
@Api("项目模块")
public class ProjectController {

	@Autowired
	private ProjectService projectService;
	
		@GetMapping("/toList")
		@ApiOperation(value="项目列表方法",httpMethod="GET")
		public Result toList(Integer userId){
			Result result=null;
			//调用业务层方法：插入到数据库中，统一处理异常
			QueryWrapper queryWrapper=new QueryWrapper();
			queryWrapper.eq("create_user",userId);
			List list=projectService.list(queryWrapper);
			result=new Result("1",list,"项目列表");
			return result;
		}
		
		/**
		 * 添加项目
		 * @param project
		 * @return
		 */
		@PostMapping("/add")
		@ApiOperation(value="新增项目",httpMethod="POST")
		public Result add(Project project) {
			Result result=null;
			User user=(User) SecurityUtils.getSubject().getPrincipal();
			project.setCreateUser(user.getId());
			projectService.save(project);
			result=new Result("1","项目添加成功");
			return result;
		}
		
		/**
		 * 查询项目
		 * @param projectId
		 * @return
		 */
		@GetMapping("/{projectId}")
		@ApiOperation(value="查询项目方法",httpMethod="GET")
		public Result getById(@PathVariable("projectId") Integer projectId){
			Result result=null;
			//调用业务层方法：插入到数据库中，统一处理异常
			Project project=projectService.getById(projectId);
			result=new Result("1",project,"查询项目");
			return result;
		}
		
		/**
		 * 更新项目
		 * @param projectId
		 * @param project
		 * @return
		 */
		@PutMapping("/{projectId}")
		@ApiOperation(value="查询项目方法",httpMethod="PUT")
		public Result updateById(@PathVariable("projectId") Integer projectId,Project project){
			Result result=null;
			//调用业务层方法：插入到数据库中，统一处理异常
			project.setId(projectId);
			projectService.updateById(project);
			result=new Result("1",project,"更新项目");
			return result;
		}
		
		/**
		 * 删除项目
		 * @param projectId
		 * @param project
		 * @return
		 */
		@DeleteMapping("/{projectId}")
		@ApiOperation(value="删除项目方法",httpMethod="DELETE")
		public Result deleteById(@PathVariable("projectId") Integer projectId,Project project){
			Result result=null;
			//调用业务层方法：插入到数据库中，统一处理异常
			project.setId(projectId);
			projectService.removeById(projectId);
			result=new Result("1",project,"删除项目");
			return result;
		}
		
}