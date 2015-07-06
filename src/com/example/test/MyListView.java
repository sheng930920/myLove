package com.example.test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
public class MyListView extends ListView {
	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyListView(Context context) {
		super(context);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// ���Զ���ؼ�ֻ����д��ListView��onMeasure������ʹ�䲻����ֹ�������ScrollViewǶ��ListViewҲ��ͬ���ĵ�������׸����
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}