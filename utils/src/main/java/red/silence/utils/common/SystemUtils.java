package red.silence.utils.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 修改系统静态属性，调用静态方法
 * @ClassName: SystemUtils
 * @author quiet
 * @date 2018年8月17日
 */
public class SystemUtils {
	
	public static void call(String className, String type, String name, Object[] args, Object fieldVal) 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, 
			IllegalArgumentException, InvocationTargetException, 
			NoSuchFieldException, SecurityException {
		Class<?> clazz = Class.forName(className);
		
		switch (type) {
		case "method":
			Method[] methods = clazz.getMethods();
			boolean flag = false;
			for(Method method : methods) {
				if(!name.equals(method.getName())) {
					continue;
				}
				
				if(!Modifier.isStatic(method.getModifiers())) {
					throw new RuntimeException("非静态方法不能调用改方法");
				}
				
				flag = true;
				method.setAccessible(true);
				method.invoke(null, args);
				break;
			}
			
			if(!flag) {
				throw new RuntimeException("className: " + className + 
						" method:" + name + "未找到");
			}
			
			break;

		case "field":
			Field field = clazz.getField(name);
			
			if(!Modifier.isStatic(field.getModifiers())) {
				throw new RuntimeException("非静态属性不能设置值");
			}
			
			if(Modifier.isFinal(field.getModifiers())) {
				throw new RuntimeException("final 属性不可修改");
			}
			
			field.setAccessible(true);
			field.set(null, fieldVal);
			break;
			
		default : 
			throw new RuntimeException("err type:" + type);
		}
		
	}
}
