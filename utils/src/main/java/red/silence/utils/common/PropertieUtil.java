package red.silence.utils.common;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 属性copy工具类
 * @ClassName: PropertieUtil
 * @date 2018年8月1日
 */
public class PropertieUtil {

	/**
	 * 转换数据对象
	 * @Title: copyProperties
	 * @param obj 待转换对象
	 * @param clazz 转换后对象类型
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public static <T> T copyProperties(Object obj, Class<T> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {
		T result = clazz.newInstance();
		BeanUtils.copyProperties(result, obj);
		return result;
	}
	
	/**
	 * 转换数据对象
	 * @Title: changeData
	 * @param list 待转换数据集合
	 * @param clazz 转换后对象类型
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public static <T> List<T> copyListProperties(List<?> list, Class<T> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {
		List<T> result = new ArrayList<>(list.size());
		
		for(Object obj : list) {
			result.add(copyProperties(obj, clazz));
		}
		
		return result;
	}
}
