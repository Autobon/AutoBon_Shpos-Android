package cn.com.incardata.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 浮点数处理
 * @author wanghao
 */
public class DecimalUtil {
	
	/**
	 * 保留小数点后一位
	 * @param pDouble
	 * @return
	 */
	public static double DoubleDecimal1(double pDouble){
		BigDecimal bd = new BigDecimal(pDouble);
		BigDecimal bd1 = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		return bd1.doubleValue();
	}
	
	/**
	 * 保留小数点后两位
	 * @param pDouble
	 * @return
	 */
	public static double DoubleDecimal2(double pDouble){
		BigDecimal bd = new BigDecimal(pDouble);
		BigDecimal bd1 = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		return bd1.doubleValue();
	}
	
	/**
	 * 保留小数点后一位
	 * @param pFloat
	 * @return
	 */
	public static float FloatDecimal1(float pFloat){
		BigDecimal bd = new BigDecimal(pFloat);
		BigDecimal bd1 = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		return bd1.floatValue();
	}
	
	/**
	 * 保留小数点后两位
	 * @param pFloat
	 * @return
	 */
	public static float FloatDecimal2(float pFloat){
		BigDecimal bd = new BigDecimal(pFloat);
		BigDecimal bd1 = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		return bd1.floatValue();
	}

	/**
	 * float类型四舍五入成int类型
	 * @param f
	 * @return
     */
	public static int floatToInt(float f) {
		int i = 0;
		if (f > 0) {
			i = (int) ((f * 10 + 5) / 10);
		} else if (f < 0) {
			i = (int) ((f * 10 - 5) / 10);
		} else {
			i = 0;
		}
		return i;
	}

	
	public static String Number1(double nFloat) {
		return new DecimalFormat("##0.0").format(nFloat);    
	}
	
	public static String Number2(float nFloat) {
		return new DecimalFormat("##0.00").format(nFloat);    
	}
}