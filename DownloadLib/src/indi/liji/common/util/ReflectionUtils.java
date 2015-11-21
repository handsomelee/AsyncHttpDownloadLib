package indi.liji.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {
	
	@SuppressWarnings("unused")
	private static final String TAG = ReflectionUtils.class.getSimpleName();

	private ReflectionUtils() {
		
	}
	
	public static Object getFieldValue(String fieldName,Object obj) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException{
		if (obj == null || fieldName == null){
			throw new NullPointerException("Parameter can not be null!");
		}
		Class<?> clz = obj.getClass();
        Field field = clz.getField(fieldName);
        return field.get(obj);
	}
	
	public static Object getFieldValueNoThrows(String fieldName,Object obj){
		Object fieldValue = null;
		try {
			fieldValue = getFieldValue(fieldName, obj);
		} catch (Exception e) {
			fieldValue = null;
		}
		return fieldValue;
	}
	
	public static Object getFieldValue(String clzName,String fieldName,Object obj) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IllegalArgumentException{
		Class<?> clz = Class.forName(clzName);
		Field field = clz.getField(fieldName);
		return field.get(obj);
	}
	
	public static Object getFieldValueNoThrows(String clzName,String fieldName,Object obj){
		Object result = null;
		try {
			result = getFieldValue(clzName, fieldName, obj);
		} catch (Exception e) {
			result = null;
			e.printStackTrace();
		}
		return result;
	}
	
	public static Object invokeMethod(Class<?> clz,Object receiver,String metholdName,Class<?>[] argsType,Object[] args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method m = clz.getMethod(metholdName, argsType);
		return m.invoke(receiver, args);
	}
	
	public static Object invokeMethodNoThrows(Class<?> clz,Object receiver,String metholdName,Class<?>[] argsType,Object[] args){
		Object result = null;
		try {
			result = invokeMethod(clz, receiver, metholdName, argsType, args);
		} catch (Exception e) {
			result = null;
			e.printStackTrace();
		}
		return result;
	}
	
}
