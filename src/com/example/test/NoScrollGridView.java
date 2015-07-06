package com.example.test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 
 * @author sheng 
 *       �Զ���ġ��Ź��񡱡���������ʾ���������ͼƬ���� 
 *       ��������⣺GridView��ʾ��ȫ��ֻ��ʾ��һ�е�ͼƬ���Ƚ����
 *       �ٶȲ���ԭ��ʱ������Ϊ��Android�У�������һ�����ƣ���ScrollView�͵Ŀؼ������໥Ƕ�ס�
 *       ��ListView��GridView�Ͷ���ScrollView�͵Ŀؼ�����ΪǶ�׺�����ScrollView�Ϳؼ��Ļ���Ч����ɥʧ�ˣ�
 *       ͬʱ��Ƕ�׿ؼ��ĸ߶�Ҳ���޶�Ϊһ�еĸ߶�
 *       ������дGridView�����
 *       ListViewǶ��GridViewʹ�� ��ListView��ÿ��Item�ж�����һ��GridView 
 *       ע������:����ListView��GridView���ǿɻ����Ŀؼ�. ������Ҫ�Զ���GridView,��д��onMeasure()����.
 *       �ڸ÷�����ʹGridView�ĸ�Ϊwrap_content�Ĵ�С,����GridView�� ������ֻ����ʾ��Сһ����
 * 
 *         �Զ���GridView������̳���GridView������onMeasure����
 * 
 */

public class NoScrollGridView extends GridView {

	public NoScrollGridView(Context context) {
		super(context);
	}

	public NoScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * ���ò�����
	 * ����onMeasure���������������ʾ�ĸ߶����ȣ�
	 * makeMeasureSpec�����е�һ�������������ֿռ�Ĵ�С���ڶ��������ǲ���ģʽ
	 * MeasureSpec.AT_MOST����˼�����ӿؼ���Ҫ���Ŀؼ�����չ�����Ŀռ�
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
