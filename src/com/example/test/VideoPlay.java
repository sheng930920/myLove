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
	String videoURL = ""; // ���մ��ݹ�������Ƶ��ַ
	String VideoName = ""; // ��Ƶ����
	int A;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��ͷ��title
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// ������Ļ����
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
		HttpHandler handler = http.download(videoURL, "/sdcard/���ز���/"
				+ VideoName, true, // ���Ŀ���ļ����ڣ�����δ��ɵĲ��ּ������ء���������֧��RANGEʱ���������ء�
				true, // ��������󷵻���Ϣ�л�ȡ���ļ�����������ɺ��Զ���������
				new RequestCallBack<File>() {

					@Override
					public void onStart() {
						System.out.println("��ʼ���أ�");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						float a = Float.valueOf(total);
						float b = Float.valueOf(current);
						float c = b / a;
						float num2 = c * 100;
						 A = (int) num2;
						System.out.println("�Ѿ����أ�" + A + "%");
						createLoadingDialog(VideoPlay.this,"�Ѿ����أ�" + A + "%").show();
						
					}

					@Override
					public void onSuccess(ResponseInfo<File> responseInfo) {
						createLoadingDialog(VideoPlay.this,"�Ѿ����أ�" + A + "%" ).cancel();
						
						mPlayerView.play(responseInfo.result.getPath(), new OnPlayCompletionListener() {

							public void onPlayCompletion() {
								img.setVisibility(View.VISIBLE);													
							}
						});
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						mPlayerView.play("/sdcard/���ز���/" + VideoName, new OnPlayCompletionListener() {
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
		View v = inflater.inflate(R.layout.progressdialog_no_deal, null);// �õ�����view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// ���ز���
		// main.xml�е�ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// ��ʾ����
		// ���ض���
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.anim);
		// ʹ��ImageView��ʾ����
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// ���ü�����Ϣ

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// �����Զ�����ʽdialog

		loadingDialog.setCancelable(true);// true�����á����ؼ���ȡ��
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// ���ò���
		return loadingDialog;

	}
	
	
	
	
	
	
	/**************************************************************/

}
