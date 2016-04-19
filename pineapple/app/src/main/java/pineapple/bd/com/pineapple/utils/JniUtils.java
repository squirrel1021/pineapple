package pineapple.bd.com.pineapple.utils;

public class JniUtils {
	
	static{
	 System.loadLibrary("jniutils");
	}
	
	public static native String getKey();

}
