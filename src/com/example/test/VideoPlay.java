package com.example.test;



import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.MoviePlayerView.OnPlayCompletionListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class VideoPlay extends Activity {

	private MoviePlayerView mPlayerView;
	ImageView img;
	String videoURL = ""; // 接收传递过来的视频地址
	String VideoName = ""; // 视频名字
	int A;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉头部title
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 设置屏幕常亮
		setContentView(R.layout.videoplayer);

		
		mPlayerView = (MoviePlayerView) findViewById(R.id.moviePlayerView);
		img = (ImageView) findViewById(R.id.imageView1);

		Intent intent = getIntent();
		videoURL = intent.getStringExtra("PATH");
		VideoName = getName(videoURL);
		init();
		img.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				img.setVisibility(View.GONE);
				mPlayerView.replay();
			}
		});
	}

	public void init() {
		HttpUtils http = new HttpUtils();
		HttpHandler handler = http.download(videoURL, "/sdcard/下载测试/"
				+ VideoName, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
				true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
				new RequestCallBack<File>() {

					@Override
					public void onStart() {
						System.out.println("开始下载：");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						float a = Float.valueOf(total);
						float b = Float.valueOf(current);
						float c = b / a;
						float num2 = c * 100;
						 A = (int) num2;
						System.out.println("已经下载：" + A + "%");
						createLoadingDialog(VideoPlay.this,"已经下载：" + A + "%").show();
						
					}

					@Override
					public void onSuccess(ResponseInfo<File> responseInfo) {
						createLoadingDialog(VideoPlay.this,"已经下载：" + A + "%" ).cancel();
						
						mPlayerView.play(responseInfo.result.getPath(), new OnPlayCompletionListener() {

							public void onPlayCompletion() {
								img.setVisibility(View.VISIBLE);													
							}
						});
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						mPlayerView.play("/sdcard/下载测试/" + VideoName, new OnPlayCompletionListener() {
							public void onPlayCompletion() {
								img.setVisibility(View.VISIBLE);
														
							}
						});
					}

				});
	}

	private String getName(String strFileName) {
		File myFile = new File(strFileName);
		String strFileExtension = myFile.getName();
		strFileExtension = strFileExtension.substring(
				(strFileExtension.lastIndexOf("/") + 1)).toLowerCase();
		if (strFileExtension == "") {
			strFileExtension = "dat";
		}
		return strFileExtension;

	}
	
	public Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.progressdialog_no_deal, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.anim);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(true);// true可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;

	}
	
	
	
	
	
	
	/**************************************************************/

}
