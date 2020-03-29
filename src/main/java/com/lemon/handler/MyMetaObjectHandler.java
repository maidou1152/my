package com.lemon.handler;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

	@Override
	/**
	 * 更新操作，自动填充字段
	 */
	public void insertFill(MetaObject metaObject) {
		//插入填充的写这里  列名  ,值
		this.setFieldValByName("regtime", new Date(), metaObject);
		//项目创建时间
		this.setFieldValByName("createTime", new Date(), metaObject);
		
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		// TODO Auto-generated method stub
		
	}

}
