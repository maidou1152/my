package com.lemon.util;

import org.apache.log4j.Logger;

public class LogerUtil {
	
	
	private static Logger logger=Logger.getLogger(LogerUtil.class);
		
	/**
	 * 记录日志
	 * @param clazz  所在类名
	 * @param str	哪里异常
	 * @param e		什么异常
	 */
	public static void log(Class<?> clazz,String str,Exception e){
		logger.info(clazz.getSimpleName()+": "+str+", "+e);
	}
}