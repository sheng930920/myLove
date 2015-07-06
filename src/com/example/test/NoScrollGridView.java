package com.example.test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 
 * @author sheng 
 *       自定义的“九宫格”——用在显示帖子详情的图片集合 
 *       解决的问题：GridView显示不全，只显示了一行的图片，比较奇怪
 *       百度查找原因时，是因为在Android中，有这样一个限制，两ScrollView型的控件不能相互嵌套。
 *       像ListView和GridView就都是ScrollView型的控件。因为嵌套后，两个ScrollView型控件的滑动效果就丧失了，
 *       同时被嵌套控件的高度也被限定为一行的高度
 *       所有重写GridView来解决
 *       ListView嵌套GridView使用 即ListView的每个Item中都包含一个GridView 
 *       注意事项:由于ListView和GridView都是可滑动的控件. 所以需要自定义GridView,重写其onMeasure()方法.
 *       在该方法中使GridView的高为wrap_content的大小,否则GridView中 的内容只能显示很小一部分
 * 
 *         自定义GridView组件，继承自GridView。重载onMeasure方法
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
	 * 设置不滚动
	 * 其中onMeasure函数决定了组件显示的高度与宽度；
	 * makeMeasureSpec函数中第一个函数决定布局空间的大小，第二个参数是布局模式
	 * MeasureSpec.AT_MOST的意思就是子控件需要多大的控件就扩展到多大的空间
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
