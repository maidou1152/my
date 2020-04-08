package com.lemon.controller;


import java.io.UnsupportedEncodingException;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alibaba.fastjson.JSONPath;


//支持junit+mockmvc

@SpringBootTest//启动对单元测试的支持
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc//实例化放到spring容器中
public class TestCK {

	@Autowired
	private MockMvc mockmvc;
	
	String sessionId;
	//用户登录 post
	@Before
	@Test
	public void test() throws UnsupportedEncodingException, Exception {
		String resultJson=mockmvc.perform(MockMvcRequestBuilders.post("/user/login")
		.param("username","20@qq.com")
		.param("password", "e10adc3949ba59abbe56e057f20f883e")
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString();
		System.out.println(resultJson);
		sessionId=(String) JSONPath.read(resultJson,"$.message");
	}
	
	//用户验重  gets
	@Test
	public void find() throws Exception {
		mockmvc.perform(MockMvcRequestBuilders.get("/user/find")
			   .header("Authorization", sessionId)
			   .param("username","20@qq.com"))
			   .andDo(print())
			   .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("用户账号已存在"))
			   .andReturn();
	}
}
