package red.silence.utils.common;

public class CommonUtils {

	/**
	 * 获取堆栈调用className + MethodName
	 * <p>
	 * 格式:idx=1 "com.wlsb.util.CommonTools.getStackCMName"
	 * @Title getStackCMName
	 * @param idx 1:本方法
	 * @return
	 */
	public static String getStackCMName(int idx) {
		StackTraceElement ele = Thread.currentThread().getStackTrace()[idx];
		return ele.getClassName() + "." + ele.getMethodName();
	}
}
