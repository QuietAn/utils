package red.silence.utils.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUtils {
	public static String getFileValue(File file, String charset) {
		FileReadWriteUtils fileReadWriteUtils = new FileReadWriteUtils(file);
		BufferedReader bufferedReader = null;
		StringBuilder values = new StringBuilder();
		try {
			bufferedReader = fileReadWriteUtils.getBufferedReader(charset);
			String line = bufferedReader.readLine();
			
			while(null !=line){
				values.append(line).append("\r\n");
				line = bufferedReader.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		fileReadWriteUtils.destroy();
		return values.toString();
	}
	
	/**
	 * 返回指定路径下的所有文件
	 * @param Path
	 * @return
	 */
	public static File[] getFiles(String Path) {
		File file = new File(Path);
		return file.listFiles();
	}
}
