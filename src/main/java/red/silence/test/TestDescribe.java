package red.silence.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName TestDescribe
 * @author WangDongling
 * @date 2019年7月24日
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TestDescribe {
	
	/**方法描述*/
	public abstract String value();
}

