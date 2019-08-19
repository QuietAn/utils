package red.silence.test;

/**
 * @ClassName BaseJunit4Test
 * @author WangDongling
 * @date 2019年4月19日
 */
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

public class BaseJunit4Test {	
	@Rule
    public TestNameW name= new TestNameW();
	private static final ThreadLocal<Long> local = new ThreadLocal<>();
	
	@After
	public void after() {
		Long end = System.currentTimeMillis();
		System.out.println(desc() + "-----执行结束：共耗时[" + (end-local.get()) + "]毫秒");
	}

	@Before
	public void before() {
		local.set(System.currentTimeMillis());
		System.out.println(desc() + "-----测试开始");
	}
	
	private String desc() {
		String desc = name.getMethodName();
		if(null != name.getMethodDesc()) {
			desc += ":" + name.getMethodDesc();
		}
		
		return desc;
	}
}
