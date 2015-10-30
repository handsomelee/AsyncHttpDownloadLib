package indi.liji.common.util;

import android.os.Build;

public class APILevelUtil {

	private APILevelUtil() {
	}

	/**
	 * 2.3.3及以上
	 * @return
	 */
	public static final boolean hasGINGERBREADMR1(){
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD_MR1){
			return false;
		}
		return true;
	}
	
	/**
	 * 3.0及以上
	 * @return
	 */
	public static final boolean hasHONEYCOMB(){
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
			return false;
		} 
		return true;
	}
	
	/**
	 * 4.1及以上
	 * @return
	 */
	public static final boolean hasJellyBean(){
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
			return false;
		}
		return true;
	}
	
	/**
	 * 4.4及以上
	 * @return
	 */
	public static final boolean hasKitKat(){
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
			return false;
		}
		return true;
	}
	
	/**
	 * 5.0及以上
	 * @return
	 */
	public static final boolean hasLollipop(){
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
			return false;
		}
		return true;
	}
	
}
