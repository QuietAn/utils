package red.silence.utils.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReadUtils {
	
	public static String readFile(File file) {
		FileReadWriteUtils frwU = new FileReadWriteUtils(file);
		BufferedReader bf = null;
		StringBuilder sb = new StringBuilder();
		try {
			bf = frwU.getBufferedReader();
			
			String line = null;
			while(null != (line = bf.readLine())) {
				sb.append(line).append("\r\n");
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("FileNotFoundException", e);
		} catch (IOException e) {
			throw new RuntimeException("IOException", e);
		}
		
		frwU.destroy();
		return sb.toString();
	}
}
