package red.silence.utils.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * bean反射相关操作
 * @author quiet
 * @date 2017年12月25日
 */
public class BeanUtils {
	//基本数据类型与包装类型映射关系
	private static Map<Class<?>, Class<?>> baseTypesMapping;
	
	static {
		baseTypesMapping = new HashMap<>(8);
		
		baseTypesMapping.put(Byte.class, Byte.TYPE);
		baseTypesMapping.put(Short.class, Short.TYPE);
		baseTypesMapping.put(Integer.class, Integer.TYPE);
		baseTypesMapping.put(Long.class, Long.TYPE);
		
		baseTypesMapping.put(Float.class, Float.TYPE);
		baseTypesMapping.put(Double.class, Double.TYPE);
		
		baseTypesMapping.put(Boolean.class, Boolean.TYPE);
		
		baseTypesMapping.put(Character.class, Character.TYPE);
	}
	/**
	 * 获取bean的静态属性值
	 * 包括私有属性
	 * @param clazz
	 * @return
	 */
	public static Map objectToMap(Object obj) {
		Class clazz = obj.getClass();
		Map<String, Object> result = new HashMap<>();
		
		//对于Class类型数据不解析
		if(clazz == Class.class) {
			return result;
		}
		
		Field[] fields = clazz.getDeclaredFields();
		
		for(Field field : fields) {
			try {
				Class<?> fType = field.getType();
				field.setAccessible(true);
				Object value = getFieldValue(field, obj, true);
				
				if(value == null) {
					result.put(field.getName(), value);
					continue;
				}
				//基本数据类型， 基本数据类型包装类型， String类型直接放入map
				if(isEasyType(fType)) {
					//是否为Collection类型
				} else if(isAssignableFrom(Collection.class, fType)){
					value = arrayToMap((Collection<?>) value);
					//是否为map类型
				} else if(isAssignableFrom(Map.class, fType)) {
					value = mapToMap((Map) value);
				} else {
					value = objectToMap(value);
				}
				
				result.put(field.getName(), value);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("IllegalArgumentException", e);
			}
		}
		return result;
	}
	
	/**
	 * 是否为基础类型的包装类型
	 * @param clazz
	 * @return
	 */
	public static boolean isBaseTypePacked(Class<?> clazz) {
		if(null != getBaseType(clazz)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断parent是否为sub类的父类
	 * @param parent 父类
	 * @param sub 子类
	 * @return
	 */
	public static boolean isAssignableFrom(Class<?> parent, Class<?> sub) {
		return parent.isAssignableFrom(sub);
	}
	
	/**
	 * 返回基本数据类型
	 */
	public static Class<?> getBaseType(Class<?> clazz) {
		return baseTypesMapping.get(clazz);
	}
	
	/**
	 * 转换数组数据类型
	 * @param collection
	 * @return
	 */
	public static Collection<?> arrayToMap(Collection<?> collection) {
		List<Object> list = new ArrayList<>(collection.size());
		
		for(Object obj : collection) {
			Class clazz = obj.getClass();
			Object value = obj;
			//基本数据类型， 基本数据类型包装类型， String类型直接放入map
			if(isEasyType(clazz)) {
				//是否为Collection类型
			} else if(isAssignableFrom(Collection.class, clazz)){
				value = arrayToMap((Collection<?>) value);
				//是否为map类型
			} else if(isAssignableFrom(Map.class, clazz)) {
				value = mapToMap((Map) value);
			} else {
				value = objectToMap(value);
			}
			
			list.add(value);
		}
		return list;
	}
	
	/**
	 * 转换map数据类型
	 * @param map
	 * @return
	 */
	public static Map mapToMap(Map<Object, Object> map) {
		Map<Object,Object> result = new HashMap<>();
		for(Map.Entry<Object, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			if(null == value) {
				result.put(entry.getKey(), value);
				continue;
			}
			Class clazz = value.getClass();
			//基本数据类型， 基本数据类型包装类型， String类型直接放入map
			if(isEasyType(clazz)) {
				//是否为Collection类型
			} else if(isAssignableFrom(Collection.class, clazz)){
				value = arrayToMap((Collection<?>) value);
				//是否为map类型
			} else if(isAssignableFrom(Map.class, clazz)) {
				value = mapToMap((Map) value);
			} else {
				value = objectToMap(value);
			}
			
			result.put(entry.getKey(), value);
		}
		return result;
	}
	
	/**
	 * 获取属性值
	 * @param field 属性对象
	 * @param obj 当前对象
	 * @param accessible 是否不检查访问权限，true不检查，false检查
	 * @return 属性值
	 */
	public static Object getFieldValue(Field field, Object obj, boolean accessible) {
		Object result = null;
		try {
			field.setAccessible(accessible);
			result = field.get(obj);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("IllegalArgumentException", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("IllegalAccessException", e);
		}
		
		return result;
	}
	
	//是否为简单类型：基础数据类型，基础数据类型包装类，String类型，Class类型， number类型
	public static boolean isEasyType(Class<?> clazz) {
		if(clazz.isPrimitive() || isBaseTypePacked(clazz) || clazz == String.class || clazz == Class.class) {
			return true;
		} else if(isAssignableFrom(Number.class, clazz)) {
			return true;
		}
		return false;
	}
}
