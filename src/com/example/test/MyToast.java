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
 * �Զ�����ʾToast
 * 
 * @author zihao
 */
public class MyToast extends Toast {

	public MyToast(Context context) {
		super(context);
	}

	/**
	 * ����һ��ͼ�Ĳ����Toast
	 * 
	 * @param context
	 *            // �����Ķ���
	 * @param drawable
	 *            // Ҫ��ʾ��ͼƬ
	 * @param text
	 *            // Ҫ��ʾ������
	 * @param duration
	 *            // ��ʾʱ��
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
		// setGravity������������λ�ã��˴�Ϊ��ֱ����
		result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		result.setDuration(duration);

		return result;
	}

	

}