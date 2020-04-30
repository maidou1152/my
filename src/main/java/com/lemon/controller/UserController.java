package com.lemon.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lemon.common.Result;
import com.lemon.pojo.User;
import com.lemon.service.UserService;
import com.lemon.util.LogerUtil;

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
@RequestMapping("/user")
@Api("用户模块")
//@CrossOrigin
public class UserController {

	@Autowired
	private UserService userService;
	
	//注册方法   调用接口
	//@RequestMapping("/register")
	@PostMapping("/register")
	@ApiOperation(value="注册方法",httpMethod="POST")
	public Result register(User user){
		//user.setRegtime(new Date());
		//调用业务层方法：插入到数据库中，统一处理异常
		userService.save(user);
		Result result=new Result("1", "注册成功");
		return result;
	}
	
	//账号验重方法
	@GetMapping("/find")
	@ApiOperation(value="账号验重方法",httpMethod="GET")
	public Result find(String username){
		Result result=null;
		//调用业务层方法，查询DB非主键列，统一处理异常
		QueryWrapper<User> queryWrapper=new QueryWrapper<User>();
		queryWrapper.eq("username", username);
		User user=userService.getOne(queryWrapper);
		if(user==null){
			result=new Result("1", "用户账号不存在");
		}else{
			result=new Result("0", "用户账号已存在");
		}		
		return result;
		}
	
	
	//注册方法   调用接口
		@PostMapping("/login")
		@ApiOperation(value="登录方法",httpMethod="POST")
		public Result login(User user){
			Result result=null;
			try {
				UsernamePasswordToken token=new UsernamePasswordToken(user.getUsername(),user.getPassword());
				//shiro
				Subject subject= SecurityUtils.getSubject();
				subject.login(token);
				//sessionId 返回回去
				String sessionId=(String) SecurityUtils.getSubject().getSession().getId();
				User loginUser=(User)subject.getPrincipal();
				result=new Result("1",loginUser.getId(),sessionId);
			} catch (AuthenticationException e) {
				if(e instanceof UnknownAccountException){
					result=new Result("0","用户名不正确");
				}else{
					result=new Result("0","密码不正确");
				}
				//e.printStackTrace();
				LogerUtil.log(UserController.class,"用户控制层异常",e);
			}		
			return result;
}
		
		/**
		 * 退出
		 * @return
		 */
		@GetMapping("/logout")
		@ApiOperation(value="退出方法",httpMethod="GET")
		public Result logout(){
			Result result=null;
			//从shiro退出会话
			SecurityUtils.getSubject().logout();
			result=new Result("1","账号未登录");
			return result;
			}
		
		/**
		 * 未授权方法
		 * @return
		 */
		@GetMapping("/unauth")
		@ApiOperation(value="未授权方法",httpMethod="GET")
		public Result unauth(){
			Result result=null;
			result=new Result("1","账号未登录");
			return result;
			}
}