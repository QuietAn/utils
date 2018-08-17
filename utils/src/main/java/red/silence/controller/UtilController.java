package red.silence.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import red.silence.utils.common.SystemUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller()
@RequestMapping("/sysUtil")
public class UtilController {

	@RequestMapping(value = "/changVal", method = RequestMethod.POST)
	@ResponseBody
	public Object sysUtil(@RequestBody JSONObject data) {
		
		String className = data.getString("className");
		String type = data.getString("type");
		String name = data.getString("name");
		
		JSONArray args = data.getJSONArray("args");
		JSONObject fieldVal = data.getJSONObject("fieldVal");
		
		Map<String, Object> result = new HashMap<>();
		Object[] objects = null;
		if(null != args) {
			objects = new Object[args.size()];
			
			objects = args.toArray();
		}
		
		try {
			SystemUtils.call(className, type, name, objects, fieldVal);
			result.put("status", "OK");
		} catch (Exception e) {
			
			StringWriter sw = new StringWriter();
			PrintWriter pW = new PrintWriter(sw);
			e.printStackTrace(pW);
			result.put("stack", sw.toString());
			
			result.put("status", "err");
			result.put("msg", e.getMessage());
		}
		
		return result;
	}
}
