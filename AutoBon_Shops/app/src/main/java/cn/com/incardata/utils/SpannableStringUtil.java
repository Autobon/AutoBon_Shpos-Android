package cn.com.incardata.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;

/**
 * SpannableString工具类
 * Created by wanghao on 16/4/22.
 */
public class SpannableStringUtil {
    /**
     * 设置字体大小，用px
     *
     * @param context
     *
     * @param str
     *            目标字符串
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @param pxSize
     *            像素大小
     * @return
     */
    public static SpannableString getSizeSpanUsePx(Context context, String str, int start, int end, int pxSize) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new AbsoluteSizeSpan(pxSize), 4, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置字体大小，用dip
     *
     * @param context
     *
     * @param str
     *            目标字符串
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @param dipSize
     *            像素大小
     * @return
     */
    public static SpannableString getSizeSpanUseDip(Context context, String str, int start, int end, int dipSize) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new AbsoluteSizeSpan(dipSize, true), 4, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置字体大小，用sp
     *
     * @param context
     *
     * @param str
     *            目标字符串
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @param spSize
     *            sp大小
     * @return
     */
    public static SpannableString getSizeSpanSpToPx(Context context, String str, int start, int end, int spSize) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new AbsoluteSizeSpan(context.getResources().getDimensionPixelOffset(spSize)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置字体相对大小
     *
     * @param context
     * @param str
     *            目标字符串
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @param relativeSize
     *            相对大小 如：0.5f，2.0f
     * @return
     */
    public static SpannableString getRelativeSizeSpan(Context context, String str, int start, int end, float relativeSize) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new RelativeSizeSpan(relativeSize), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置字体
     *
     * @param context
     * @param str
     *            目标字符串
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @param typeface
     *            字体类型 如：default，efault-bold,monospace,serif,sans-serif
     * @return
     */
    public static SpannableString getTypeFaceSpan(Context context, String str, int start, int end, String typeface) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new TypefaceSpan(typeface), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置字体形体
     *
     * @param context
     * @param str
     *            目标字符串
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @param style
     *            字体类型 如： Typeface.NORMAL正常 Typeface.BOLD粗体 Typeface.ITALIC斜体
     *            Typeface.BOLD_ITALIC粗斜体
     * @return
     */
    public static SpannableString getStyleSpan(Context context, String str, int start, int end, int style) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置字体下划线
     *
     * @param context
     * @param str
     *            目标字符串
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @return
     */
    public static SpannableString getUnderLineSpan(Context context, String str, int start, int end) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置字体删除线
     *
     * @param context
     * @param str
     *            目标字符串
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @return
     */
    public static SpannableString getDeleteLineSpan(Context context, String str, int start, int end) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置上标
     *
     * @param context
     * @param str
     *            目标字符串
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @return
     */
    public static SpannableString getSuperscriptSpan(Context context, String str, int start, int end) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new SuperscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置放大系数
     *
     * @param context
     * @param str
     *            目标字符串
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @param scale
     *            放大多少倍，x轴方向，y轴不变 如：0.5f， 2.0f
     * @return
     */
    public static SpannableString getScaleSpan(Context context, String str, int start, int end, float scale) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ScaleXSpan(scale), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置下标
     *
     * @param context
     * @param str
     *            目标字符串
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @return
     */
    public static SpannableString getSubscriptSpan(Context context, String str, int start, int end) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new SubscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置背景色
     *
     * @param context
     * @param str
     *            目标字符串
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @param color
     *            颜色值 如Color.BLACK
     * @return
     */
    public static SpannableString getBackGroundColorSpan(Context context, String str, int start, int end, int color) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new BackgroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置背景色
     *
     * @param context
     *
     * @param str
     *            目标字符串
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @param color
     *            颜色值 如：#CCCCCC
     * @return
     */
    public static SpannableString getBackGroundColorSpan(Context context, String str, int start, int end, String color) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new BackgroundColorSpan(Color.parseColor(color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置前景色
     *
     * @param context
     *
     * @param str
     *            目标字符串
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @param color
     *            颜色值 如Color.BLACK
     * @return
     */
    public static SpannableString getForegroundColorSpan(Context context, String str, int start, int end, int color) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置前景色
     *
     * @param context
     *
     * @param str
     *            目标字符串
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @param color
     *            颜色值 如：#CCCCCC
     * @return
     */
    public static SpannableString getForegroundColorSpan(Context context, String str, int start, int end, String color) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor(color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
