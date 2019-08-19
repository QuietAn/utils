package red.silence.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import red.silence.utils.common.FileReadUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName Test
 * @author WangDongling
 * @date 2018年10月22日
 */
public class Test {

	private static String table_1 = "T001_1";
	private static String table_2 = "T001_2";
	private static String table_3 = "T001_3";

	public static String datamap = "datamap";
	public static String name = "name";
	public static final String FORMULA_TEMPSTORE = "formula_tempStore";

	static String key1;
	static String key2;
	static String key3;
	static String key4;
	static String key5;
	static String key6;
	static String key7;
	static String key8;

	static String uuid;
	static List<String> columnNames;
	
	public static void test(String fileName) {
		File file = new File(fileName);
		String jsonString = FileReadUtils.readFile(file);

		JSONArray jsonArray = JSON.parseArray(jsonString);

		int idx1 = getIndexByName(jsonArray, table_1);

		int idx2 = getIndexByName(jsonArray, table_2);

		int idx3 = getIndexByName(jsonArray, table_3);

		if (idx1 != -1) {
			insertT1(jsonArray.getJSONObject(idx1));
		}
		if (idx2 != -1) {
			insertT2(jsonArray.getJSONObject(idx2));
		}
		if (idx3 != -1) {
			insertT3(jsonArray.getJSONObject(idx3));
		}
	}
	
	
	private static void insertT1(JSONObject jsonObj) {
		JSONObject data = jsonObj.getJSONObject(datamap);
		
		String baseKey = "001_1_";
		StringBuilder sql = new StringBuilder();
		for(int i=1; i<=22; i++) {
			columnNames = new ArrayList<>();
			
			key1 = baseKey + i + "_1";
			key2 = baseKey + i + "_2";
			key3 = baseKey + i + "_3";
			key4 = baseKey + i + "_4";
			
			columnNames.add(key1);
			columnNames.add(key2);
			columnNames.add(key3);
			columnNames.add(key4);
			
			sql.append(generateSQL(table_1, uuid, columnNames,data));
			sql.append("\r\n");
			
		}
		
		
		key1 = baseKey + "1_0";
		key2 = baseKey + "2_0";
		key3 = baseKey + "3_0";
		key4 = baseKey + "4_0";
		
		columnNames = new ArrayList<>();
		columnNames.add(key1);
		columnNames.add(key2);
		columnNames.add(key3);
		columnNames.add(key4);
		
		sql.append(generateSQL(table_1, uuid, columnNames,data));
		sql.append("\r\n");
		
		key1 = baseKey + "5_0";
		key2 = baseKey + "6_0";
		key3 = baseKey + "7_0";
		key4 = baseKey + "8_0";
		
		columnNames = new ArrayList<>();
		columnNames.add(key1);
		columnNames.add(key2);
		columnNames.add(key3);
		columnNames.add(key4);
		
		sql.append(generateSQL(table_1, uuid, columnNames,data));
		sql.append("\r\n");
		
		System.out.println(sql);
	}

	private static void insertT2(JSONObject jsonObj) {
		String baseKey = "001_2_1_";
		StringBuilder sql = new StringBuilder();
		
		JSONObject data = jsonObj.getJSONObject(datamap);
		columnNames = new ArrayList<>();
		for(int i=1; i<=16; i++) {
			key1 = baseKey + i;
			
			columnNames.add(key1);
			
			if(i%4 == 0) {
				sql.append(generateSQL(table_2, uuid, columnNames, data));
				columnNames = new ArrayList<>();
			}
		}
		
		
		System.out.println(sql);
	}

	private static void insertT3(JSONObject jsonObj) {
		String baseKey = "001_3_";
		StringBuilder sql = new StringBuilder();
		JSONObject data = jsonObj.getJSONObject(datamap);
		
		for(int i=1; i<=19; i++) {
			columnNames = new ArrayList<>();
			key1 = baseKey + i + "1";
			key2 = baseKey + i + "2";
			key3 = baseKey + i + "3";
			key4 = baseKey + i + "4";
			key5 = baseKey + i + "5";
			key6 = baseKey + i + "6";
			key7 = baseKey + i + "7";
			key8 = baseKey + i + "0";
			
			columnNames.add(key1);
			columnNames.add(key2);
			columnNames.add(key3);
			columnNames.add(key4);
			columnNames.add(key5);
			columnNames.add(key6);
			columnNames.add(key7);
			columnNames.add(key8);
			
			sql.append(generateSQL(table_3, uuid, columnNames,data));
			sql.append("\r\n");
		}
		
		System.out.println(sql);
	}

	public static void main(String[] args) {
		for(int i=1; i<=100; i++) {
			uuid = generateUUID();
			String filePath = Test.class
					.getResource("/Copy (" + i + ") of data")
					.getFile().replaceAll("%20"," ");
			
			System.out.println("-- " + i + "===========================================");
			test(filePath);
			System.out.println("-- " + "**********************************************");
		}
	}

	public static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static int getIndexByName(JSONArray source, String nameP) {
		if (source != null) {
			for (int i = 0; i < source.size(); i++) {
				JSONObject obj = (JSONObject) source.get(i);
				if (nameP.equals(obj.getString(name))) {
					return i;
				}
			}
		}
		return -1;
	}
	
	
	public static String generateSQL(String tableName, String uuid, List<String> columnNames, JSONObject data) {
		
		List<String> columns = new ArrayList<>(columnNames.size());
		for(String key : columnNames) {
			columns.add(data.getString(key) == null ? "" : data.getString(key));
		}
		
		return generateSQL(tableName, uuid, columns);
	}
	
	public static String generateSQL(String tableName, String uuid, List<String> columns) {
		StringBuilder sb = new StringBuilder("INSERT INTO \"DWLSB\".");
		sb.append(tableName);
		sb.append(" VALUES ");
		sb.append("(\r\n");
		
		sb.append("'");
		sb.append(uuid);
		sb.append("',");
		for(String str : columns) {
			sb.append("'");
			sb.append(str);
			sb.append("'");
			sb.append(",");
		}
		
		sb.deleteCharAt(sb.length() -1);
		sb.append("); \r\n");
		return sb.toString();
	}
}
