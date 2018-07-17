package phone.ktv.tootls;

import android.util.Log;

/**
 * @描述	     1.各种不同类型的log输出方法的工具类
 */
public class Logger {
	//控制Log是否输出
	public static int LOWEST_LOG_LEVEL = 0;
	private static int SYSTEM = 1;
	private static int VERBOS = 2;
	private static int DEBUG = 3;
	private static int INFO = 4;
	private static int WARN = 5;
	private static int ERROR = 6;

	/**
	 * @param tag TAG
	 * @param message 输出的内容
	 */
	public static void i(String tag, String message) {
		if(message==null){
			return;
		}
		if (LOWEST_LOG_LEVEL <= INFO) {
			Log.i(tag, message);
		}
	}

	public static void e(String tag, String message) {
		if(message==null){
			return;
		}
		if (LOWEST_LOG_LEVEL <= ERROR) {
			Log.e(tag, message);
		}
	}
	public static void e(Exception ex) {
		if(ex==null){
			return;
		}
		if (LOWEST_LOG_LEVEL <= ERROR) {
			ex.printStackTrace();
		}
	}
	public static void d(String tag, String message) {
		if(message==null){
			return;
		}
		if (LOWEST_LOG_LEVEL <= DEBUG) {
			Log.d(tag, message);
		}
	}

	public static void w(String tag, String message) {
		if(message==null){
			return;
		}
		if (LOWEST_LOG_LEVEL <= WARN) {
			Log.w(tag, message);
		}
	}

	public static void v(String tag, String message) {
		if(message==null){
			return;
		}
		if (LOWEST_LOG_LEVEL <= VERBOS) {
			Log.v(tag, message);
		}
	}

	/**
	 * @des 内容输出在控制台
	 * @param message 输出的内容
	 */
	public static void s(String message) {
		if(message==null){
			return;
		}
		if (LOWEST_LOG_LEVEL <= SYSTEM) {
			System.out.println(message);
		}
	}

}
