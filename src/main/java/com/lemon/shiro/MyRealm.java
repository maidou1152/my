package com.lemon.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lemon.pojo.User;
import com.lemon.service.UserService;

public class MyRealm extends AuthorizingRealm {
	/**
	 * Dao层访问 
	 * 查询方法封装在业务类里
	 */
	@Autowired
	UserService userService;
	
	//授权（权限管理）
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//身份认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//认证逻辑
		String username=token.getPrincipal().toString();
		//QueryWrapper：查询的包装类   泛型声明 :User
		QueryWrapper<User> queryWrapper=new QueryWrapper<>();
		//封装查询条件    列名
		queryWrapper.eq("username", username);
		//查询非主键列  返回值是一个对象（从数据库里查出来的）
		User dbUser=userService.getOne(queryWrapper);
		//手工比对
		if (dbUser!=null) {
			//密码
			/*if (dbUser.getPassword().equals(token.getCredentials())) {
				
			}*/
			//getName():获取当前的名字   抛出密码不对的异常
			return new SimpleAuthenticationInfo(dbUser,dbUser.getPassword(),getName());
		}
		//自动比对
		
		return null;
		
	}

}
