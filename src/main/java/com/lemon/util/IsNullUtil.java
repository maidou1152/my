package com.lemon.util;

import java.lang.reflect.Field;

import com.lemon.pojo.User;

/**
 * 判断该对象是否为空对象
 * @author asus
 *
 */
public class IsNullUtil {

	
	 /**
     * 校验对象属性是否都为null
     * //判断该对象是否: 返回ture表示所有属性为null  返回false表示不是所有属性都是null
     * @param obj 对象
     * @return
     * @throws Exception
     */
//    public static  boolean isAllFieldNull(Object obj) throws Exception{
//        Class stuCla = (Class) obj.getClass();// 得到类对象
//        Field[] fs = stuCla.getDeclaredFields();//得到属性集合
//        boolean flag = true;
//        for (Field f : fs) {//遍历属性
//            f.setAccessible(true); // 设置属性是可以访问的(私有的也可以)
//            Object val = f.get(obj);// 得到此属性的值
//            if(val!=null) {//只要有1个属性不为空,那么就不是所有的属性值都为空
//                flag = false;
//                break;
//            }
//        }
//        return flag;
	 public static boolean isAllFieldNull(Object obj) throws IllegalAccessException {
	        Class<?> aClass = obj.getClass();
	        Field[] fs = aClass.getDeclaredFields();
	        boolean flag=true;
	        for (Field f : fs) {
	            f.setAccessible(true);
	            Object o = f.get(obj);
	            if (o!=null){
	                flag=false;
	            }
	        }
	        return flag;
	    }

    public static void main(String[] args) throws Exception {
    	User user = new User();
        user.setId(1);
        try {
            boolean allFieldNull = IsNullUtil.isAllFieldNull(user);
            System.out.println(allFieldNull);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}