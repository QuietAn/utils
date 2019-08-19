package red.silence.data;

import java.io.File;
import java.nio.file.Paths;

import red.silence.utils.common.FileReadUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * @ClassName LogDataUtils
 * @author WangDongling
 * @date 2019年7月24日
 */
public class LogDataUtils {
	
	String basePath = "E:/Code/eclipse-luna/utils/src/test/resources";
	
	public JSONArray getJsonArray(String fileName) {
		return JSON.parseArray(getData(fileName));
	}
	
	public String getData(String fileName) {
		return FileReadUtils.readFile(getFile(fileName));
	}
	
	public File getFile(String path) {
		return Paths.get(basePath, path).toFile();
	}
	
	public <T> void printf(Iterable<T> iter) {
		if(null == iter) {
			System.out.print("\r\n");
			return;
		}
		
		for(T t: iter) {
			System.out.print(t);
			System.out.print(",");
		}
		
		System.out.print("\r\n");
	}
}

