package red.silence.data;

import java.io.File;
import java.nio.file.Paths;

import org.junit.Test;

import red.silence.test.BaseJunit4Test;
import red.silence.test.TestDescribe;
import red.silence.utils.common.FileReadUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName JsonChanag
 * @author WangDongling
 * @date 2019年7月24日
 */
public class JsonChanag extends BaseJunit4Test{
	
	String basePath = "E:/Code/eclipse-luna/utils/src/test/resources";
	
	@Test
	@TestDescribe("wlsb 日访问量数据转换")
	public void changerDate() {
		String data = FileReadUtils.readFile(getFile("日访问量.json"));
		
		JSONArray array = JSON.parseArray(data);
		
		for(Object obj : array) {
			JSONObject jsonObj = (JSONObject) obj;
			
			printf(jsonObj.getString("key_as_string").split("T")[0]);
			printData(jsonObj);
		}
	}
	
	@Test
	@TestDescribe("wlsb 小时访问量数据转换")
	public void changerDate_H() {
		String data = FileReadUtils.readFile(getFile("小时访问量.json"));
		
		JSONArray array = JSON.parseArray(data);
		
		for(Object obj : array) {
			JSONObject jsonObj = (JSONObject) obj;
			
			printf(jsonObj.getString("key_as_string").split(":")[0]);
			printData(jsonObj);
		}
	}
	
	@Test
	@TestDescribe("wlsb 3分钟访问量数据转换")
	public void changerDate_M() {
		String data = FileReadUtils.readFile(getFile("10M访问量.json"));
		
		JSONArray array = JSON.parseArray(data);
		
		for(Object obj : array) {
			JSONObject jsonObj = (JSONObject) obj;
			
			String[] dates = jsonObj.getString("key_as_string").split(":");
			printf(dates[0] + ":" + dates[1]);
			
			printData(jsonObj);
		}
	}
	
	private File getFile(String path) {
		return Paths.get(basePath, path).toFile();
	}
	
	private void printf(Object obj){
		System.out.print(obj);
		System.out.print(",");
	} 
	
	private void printData(JSONObject jsonObj){
		printf(jsonObj.getIntValue("doc_count"));
		
		int _static = 0;
		int _dynamic = 0;
		try {
			if(null == jsonObj.get("_type")) {
				return;
			}
			JSONArray typeArray = jsonObj.getJSONObject("_type").getJSONArray("buckets");
			
			for(Object tObj : typeArray) {
				JSONObject tJsonObj = (JSONObject) tObj;
				String key = tJsonObj.getString("key");
				int val = tJsonObj.getInteger("doc_count");
				
				if("static".equals(key)) {
					_static = val;
				} else if("dynamic".equals(key)) {
					_dynamic = val;
				}
			}
			
		} finally{
			printf(_static);
			printf(_dynamic);
			System.out.print("\r\n");
		}
	}
}

