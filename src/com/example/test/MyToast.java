package com.example.test;



import com.example.test.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 自定义提示Toast
 * 
 * @author zihao
 */
public class MyToast extends Toast {

	public MyToast(Context context) {
		super(context);
	}

	/**
	 * 生成一个图文并存的Toast
	 * 
	 * @param context
	 *            // 上下文对象
	 * @param drawable
	 *            // 要显示的图片
	 * @param text
	 *            // 要显示的文字
	 * @param duration
	 *            // 显示时间
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static MyToast makeImgAndTextToast(Context context,
			Drawable drawable, CharSequence text, int duration) {
		MyToast result = new MyToast(context);

		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflate.inflate(R.layout.view_tips, null);

		ImageView img = (ImageView) v.findViewById(R.id.tips_icon);
		img.setBackgroundDrawable(drawable);
		TextView tv = (TextView) v.findViewById(R.id.tips_msg);
		tv.setText(text);

		result.setView(v);
		// setGravity方法用于设置位置，此处为垂直居中
		result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		result.setDuration(duration);

		return result;
	}

	

}