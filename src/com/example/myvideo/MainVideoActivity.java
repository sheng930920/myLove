package com.example.myvideo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.myvideo.MovieRecorderView.OnRecordFinishListener;
import com.example.test.R;



public class MainVideoActivity extends Activity {

	private MovieRecorderView mRecorderView;
	private Button mShootBtn;
	private boolean isFinish = true;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		mRecorderView = (MovieRecorderView) findViewById(R.id.movieRecorderView);
		mShootBtn = (Button) findViewById(R.id.shoot_button);
		
		mShootBtn.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//按住事件发生后执行代码的区域
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mRecorderView.record(new OnRecordFinishListener() {
						@Override
						public void onRecordFinish() {
							handler.sendEmptyMessage(1);
						}
					});//松开事件发生后执行代码的区域
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (mRecorderView.getTimeCount() > 1)
						handler.sendEmptyMessage(1);
					else {
						Toast.makeText(MainVideoActivity.this, "视频录制时间太短", Toast.LENGTH_SHORT).show();
						if (mRecorderView.getVecordFile() != null)
							mRecorderView.getVecordFile().delete();
						mRecorderView.stopRecord();
					}
				}
				return true;
			}
		});
	}


	@Override
	public void onResume() {
		super.onResume();
		isFinish = true;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		isFinish = false;
		mRecorderView.stop();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			finishActivity();
		}
	};

	private void finishActivity() {
		if (isFinish) {
			mRecorderView.stop();
			VideoPlayerActivity.startActivity(this, mRecorderView.getVecordFile().toString());
		}
	}

	/**
	 * 录制完成回调
	 *
	 * @author liuyinjun
	 * 
	 * @date 2015-2-9
	 */
	public interface OnShootCompletionListener {
		public void OnShootSuccess(String path, int second);

		public void OnShootFailure();
	}
}

