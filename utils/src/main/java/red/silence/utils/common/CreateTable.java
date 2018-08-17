package red.silence.utils.common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class CreateTable {

	public static void main(String[] args) {
		String pkg = "com/landtax/model";
		createTable(pkg);
	}

	
	public static void createTable(String pkg){
		String classpath = CreateTable.class.getResource("/").getFile();
		File filePath = new File(classpath + "/" + pkg);
		
		ClassLoader loader = CreateTable.class.getClassLoader();
		File[] files = filePath.listFiles();
		Class<?>[] clazzs = new Class[files.length];
		
		for(int i=0; i<files.length; i++) {
			File tempFile =  files[i];
			try {
				clazzs[i] = loader.loadClass(pkg.replace("/", ".") + "." + tempFile.getName().split("\\.")[0]);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		for(Class<?> clazz : clazzs) {
			createTable(clazz);
		}
	}
	
	public static StringBuilder getFieldName(Field field) {
		Map<String, String> types = new HashMap<>();
		types.put("string", "VARCHAR2(50)");
		types.put("double", "NUMBER(20)");
		StringBuilder builder = new StringBuilder();
		
		builder.append("\t").append(changeFileName(field.getName()).toUpperCase()) .append(" VARCHAR2(50) , \r\n");
		return builder;
	}
	
	public static String getFieldName(String str) {
		return "\t"+str+" VARCHAR2(50) , \r\n";
	}
	
	public static String getTableComment(String tableName) {
		return "comment on table " + tableName + " is 'TABLEMSG'; \r\n";
	}
	
	public static String getColumnComment(String tableName, String column) {
		return "comment on column " + tableName + "." + column + " is 'columnMSG'; \r\n";
	}
	
	public static String getColumnComment(String tableName, String column, String msg) {
		return "comment on column " + tableName + "." + column + " is '" + msg + "'; \r\n";
	}
	
	public static void createTable(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		StringBuilder builder = new StringBuilder();
		
		String baseTName = "QC_";
		String sqlPath = "tdsys";
		
		String tableName = baseTName + changeFileName(clazz.getSimpleName()).toUpperCase();
		
		//-----建表sql start-------
		builder.append("CREATE TABLE ").append(tableName).append(" (").append("\r\n");
		//类属性字段生成
		for(Field field : fields) {
			if("serialVersionUID".equals(field.getName())) {
				continue;
			}
			builder.append(getFieldName(field));
		}
		
		//通用字段
		String[] baseField = new String[]{"ID", "SBXXXH", "ISVALIDATE", "CREATETIME","UPDATETIME","SYNCTIME"};
		//通用字段注释
		String[] baseColumnMsg = new String[]{"主键", "申报信息序号", "是否有效", "创建时间", "更新时间", "同步时间"};
		
		//通用字段sql生成
		for(String str : baseField) {
			builder.append(getFieldName(str));
		}
		
		String temp = builder.substring(0, builder.lastIndexOf(","));
		
		builder = new StringBuilder(temp);
		builder.append("\r\n);\r\n");
		//-----建表sql end-------
		
		//类注释
		builder.append(getTableComment(tableName));
		
		//字段注释
		for(Field field : fields) {
			if("serialVersionUID".equals(field.getName())) {
				continue;
			}
			builder.append(getColumnComment(tableName, changeFileName(field.getName()).toUpperCase()));
		}
		
		//字段注释
		for(int i=0; i<baseField.length; i++) {
			builder.append(getColumnComment(tableName, baseField[i].toUpperCase(), baseColumnMsg[i]));
		}
		
		//主键索引
		builder.append(createPkIdx(tableName));
		
		File file = new File(CreateTable.class.getResource("/").getFile());
		
		File sqlFile = new File(file.getParentFile().getPath() +  "/sources/"+ sqlPath + "/" +  tableName + ".sql");
		
		if(!sqlFile.getParentFile().exists()) {
			sqlFile.getParentFile().mkdirs();
		}
		
		
		//写文件
		FileReadWriteUtils fRW = new FileReadWriteUtils(sqlFile);
		try {
			fRW.createFile();
			fRW.getBufferWriter().write(builder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		//关闭流
		fRW.destroy();
	}
	
	public static Field[] getFields(Class<?> clazz) {
		return clazz.getDeclaredFields();
	}
	
	public static String changeFileName(String filed) {
		StringBuilder builder = new StringBuilder();
		char[] chars = filed.toCharArray();
		boolean falg = false;
		for(char ch : chars) {
			if(Character.isUpperCase(ch)) {
				if(falg) {
					builder.append(ch);
				} else {
					falg = true;
					if(builder.length()>0) {
						builder.append("_");
					}
					builder.append(ch);
				}
				
			} else {
				falg = false;
				builder.append(ch);
			}
			
		}
		return builder.toString();
	}
	
	public static String createPkIdx(String tableName) {
		
		String pk_idx = "ALTER TABLE " + tableName + " ADD CONSTRAINT PK_" 
				+ tableName + "_ID PRIMARY KEY(ID);\r\n";
		
		pk_idx += "Create Index IDX_" + tableName + "_SBXXXH ON "
				+ tableName + "(SBXXXH);\r\n";
		
		return pk_idx;
	}
}
