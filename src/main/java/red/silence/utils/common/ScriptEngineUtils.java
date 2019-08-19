package red.silence.utils.common;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

/**
 * @author quiet
 */
public class ScriptEngineUtils{
	
	//缓存对象
	private static Map<String, CompiledScript> cache = new HashMap<>(); 
	
	public static Object evalExcelScript(String key, String script, Map<String, Object> params) {
		
		Object result = evalScript(key, script, params);
		return result;
	}
	
	/**
	 * 描述：执行script脚本 <br/>
	 * @param key 用于缓存的key
	 * @param script 待执行脚本
	 * @param params 上下文参数
	 * @return Object 执行结果
	 */
	public static Object  evalScript(String key, String script, Map<String, Object> params) {
		Object result = null;
		
		String msg = "公式ID:"+key+"，内容："+script;
		try {
			long start = new Date().getTime();
			CompiledScript compiledScript = getCompiledScript(key, script);
			if(params instanceof Bindings) {
				result = compiledScript.eval((Bindings)params);
			} else {
				result = compiledScript.eval(getBindings(params));
			}
			long end = new Date().getTime();
			
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 描述：编译script脚本 <br/>
	 * @param script 待编译脚本
	 * @return CompiledScript 编译后对象
	 * @throws ScriptException 编译失败抛出
	 */
	private static CompiledScript compiledScript(String script) {
		ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Compilable compilable = (Compilable) engine;
        
        CompiledScript compiledScript = null;
		try {
			compiledScript = compilable.compile(script);
		} catch (ScriptException e) {
			String msg = "公式编译报错";
			e.printStackTrace();
		}
        
        return compiledScript;
	}
	
	/**
	 * 描述：获取编译的脚本对象<br/>
	 * @param key 缓存key
	 * @param script 待编译脚本
	 * @return CompiledScript 编译后的脚本对象
	 * @throws ScriptException 编译script失败时
	 */
	private static CompiledScript getCompiledScript(String key, String script) {
		CompiledScript compiledScript = cache.get(key);
		
		if(null == compiledScript) {
			compiledScript = compiledScript(script);
	        cache.put(key, compiledScript);
		}
		return compiledScript;
	}
	
	/**
	 * 修改内容：由于校验时，数字解析失败，对数据添加$标识符
	 * 描述：转换为脚本参数 <br/>
	 * @param map 初始化参数
	 * @return Bindings
	 */
	public static Bindings getBindings(Map<String,Object> map) {
		Bindings bindings = new SimpleBindings();
        
		bindings.putAll(map);
		return bindings;
	}
	
	public static void main(String [] args) {
		String key = "11";
		String filePath = ScriptEngineUtils.class.getResource("/params.txt").getFile();
		File file = new File(filePath);
		
		Map<String,Object> params = new HashMap<>(); 
		//params = GetParams.getParams(file);
		
		
		String fun_t = "function test(a,b){return a+b;} print(test(1,2));";
		Object object = evalExcelScript(key+"1", fun_t, params);
		
		String fun = null;
		//$A.length; for(var idx=0; idx<$A.length; idx++){}
		
		//params.put("$004000_26_18_0", "020000|邮政服务");
		//params.put("$004000_26_19_8", "222");
		
		Map[] arr = new Map[2];
		List<Map> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("a", "a");
		map.put("b", "b");
		
		arr[0] = map;
		list.add(map);
		map = new HashMap<>();
		map.put("a", "D");
		map.put("b", "E");
		arr[1] = map;
		list.add(map);
		
		params.put("$A", list);
		
		object = evalExcelScript(key, fun, params);
	}
}
