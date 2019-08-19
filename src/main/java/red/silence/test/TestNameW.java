package red.silence.test;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * @ClassName TestNameW
 * @author WangDongling
 * @date 2019年7月24日
 */
public class TestNameW extends TestWatcher{
	private String desc;
	private String name;

    @Override
    protected void starting(Description d) {
    	name = d.getMethodName();
    	desc = getDesc(d);
    }
    
    public String getMethodName() {
    	return name; 
    }
    
    public String getMethodDesc() {
    	return desc;
    }
    
    private String getDesc(Description d) {
    	TestDescribe test = d.getAnnotation(TestDescribe.class);
    	
		return null!=test ? test.value() : null;
    }
}

