package com.example.myvideo;

import java.io.File;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.test.R;
import com.example.testpic.LocationActivity;
import com.example.testpic.PublishedActivity;

public class VideoPlayerActivity extends Activity {

	private static final String EXTRA_VIDEO_PATH = "extra_video_path";// 视频路径

	private MoviePlayerView mPlayerView;

	private TextView upload, localtion;

	private EditText message;
	RelativeLayout showLocaltion;
	String Message;
	String Location;
	private String localResult;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.upload);
		mPlayerView = (MoviePlayerView) findViewById(R.id.moviePlayerView);
		localtion = (TextView) findViewById(R.id.localtion);
		message = (EditText) findViewById(R.id.message);
		showLocaltion = (RelativeLayout) findViewById(R.id.showLocaltion);
		showLocaltion.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(VideoPlayerActivity.this,LocationActivity.class);
				startActivityForResult(intent, 5);
			}
		});

		upload = (TextView) this.findViewById(R.id.upload);
		upload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPlayerView.stop();
				Bundle bundle = getIntent().getExtras();
				final String videoPath = bundle.getString(EXTRA_VIDEO_PATH);
				Message = message.getText().toString();
				Location = localtion.getText().toString();
				File file = new File(videoPath);
				new FileUploadAsyncTask(VideoPlayerActivity.this,Message,Location).execute(file);

			}
		});

	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 5:
			boolean local_clear = false;
			try {
				localResult = data.getStringExtra("local_result");
			} catch (Exception e) {
				Log.e("StartActivityActivity","onActivityResult------>定位当前信息返回出错");
			}
			try {
				local_clear = data.getBooleanExtra("local_clear",false);
			} catch (Exception e) {
				Log.e("StartActivityActivity","onActivityResult------>当前位置信息清除标志返回出错");
			}

			if (localResult != null) {
				localtion.setText(localResult);
			}
			break;
		
		}
		
    }

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Bundle bundle = getIntent().getExtras();
		if (bundle == null)
			return;
		final String videoPath = bundle.getString(EXTRA_VIDEO_PATH);

		new Handler().postDelayed(new Runnable() {
			public void run() {
				mPlayerView.play(videoPath);
			}
		}, 1000);

	}

	@Override
	public void onStop() {
		mPlayerView.release();
		super.onStop();
	}

	@Override
	public void onDestroy() {
		mPlayerView.release();
		super.onDestroy();
	}

	public static void startActivity(Context context, String path) {
		Intent intent = new Intent(context, VideoPlayerActivity.class).putExtra(EXTRA_VIDEO_PATH, path);
		context.startActivity(intent);
	}
}
