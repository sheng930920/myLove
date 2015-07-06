package com.example.myvideo;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.example.test.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.media.MediaRecorder.AudioSource;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OutputFormat;
import android.media.MediaRecorder.VideoEncoder;
import android.media.MediaRecorder.VideoSource;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * 视频播放控件
 */
public class MovieRecorderView extends LinearLayout implements OnErrorListener {

	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private ProgressBar mProgressBar;

	private MediaRecorder mMediaRecorder;
	private Camera mCamera;
	private Timer mTimer;//  计时器
	private OnRecordFinishListener mOnRecordFinishListener;// 录制完成回调接口

	private int mWidth;// 视频分辨率宽度
	private int mHeight;// 视频分辨率高度
	private boolean isOpenCamera;// 是否一开始就打开摄像头
	private int mRecordMaxTime;// 一次拍摄最长时间
	private int mTimeCount;// 时间计数
	private File vecordFile = null;// 文件

	public MovieRecorderView(Context context) {
		this(context, null);
	}

	public MovieRecorderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MovieRecorderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MovieRecorderView, defStyle, 0);
		mWidth = a.getInteger(R.styleable.MovieRecorderView_width, 1280);// 默认640  
		mHeight = a.getInteger(R.styleable.MovieRecorderView_height1, 960);// 默认480    

		isOpenCamera = a.getBoolean(R.styleable.MovieRecorderView_is_open_camera, true);// 默认打开
		mRecordMaxTime = a.getInteger(R.styleable.MovieRecorderView_record_max_time, 60);// 默认60

		LayoutInflater.from(context).inflate(R.layout.activity_movie_recorder_view, this);
		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		mProgressBar.setMax(mRecordMaxTime);// 设置进度条最大量

		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(new CustomCallBack());
		//设置SurfaceView不需要自己维护缓冲区 
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		a.recycle();
	}

	/**
	 * 
	 * @author liqisheng
	 * 
	 * @date 2015-4-5
	 */
	private class CustomCallBack implements Callback {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			if (!isOpenCamera)
				return;
			try {
				initCamera();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			System.out.println("SurfaceView被改变了");
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if (!isOpenCamera)
				return;
			freeCameraResource();
		}

	}

	/**
	 * 初始化摄像头
	 * 
	 * @author liuyinjun
	 * @date 2015-2-5
	 * @throws IOException
	 */
	private void initCamera() throws IOException {
		if (mCamera != null) {
			freeCameraResource();
		}
		try {
			mCamera = Camera.open();
		} catch (Exception e) {
			e.printStackTrace();
			freeCameraResource();
		}
		if (mCamera == null)
			return;

		setCameraParams();
		mCamera.setDisplayOrientation(90);
		mCamera.setPreviewDisplay(mSurfaceHolder);
		mCamera.startPreview();//开始预览取景
		mCamera.unlock();
	}

	/**
	 * 设置摄像头为竖屏并设置最佳的分辨率
	 * 
	 */
	private void setCameraParams() {
		if (mCamera != null) {
			Parameters params = mCamera.getParameters();
			Camera.Size size = getBestPreviewSize(mWidth, mHeight, params);
			mWidth = size.width;
			mHeight = size.height;
			params.set("orientation", "portrait");
			mCamera.setParameters(params);
		}
	}

	/**
	 * 释放摄像头资源
	 * 
	 */
	private void freeCameraResource() {
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();//结束取景预览
			mCamera.lock();
			mCamera.release();//释放资源。
			mCamera = null;
		}
	}
     //创建文件夹
	private void createRecordDir() {
		File sampleDir = new File(Environment.getExternalStorageDirectory() + File.separator + "video");
		if (!sampleDir.exists()) {
			sampleDir.mkdirs();
		}
		File vecordDir = sampleDir;
		// 创建文件
		try {
			vecordFile = File.createTempFile("video", ".mp4", vecordDir);
		} catch (IOException e) {
		}
	}

	/**
	 * 初始化
	 * 
	 * @throws IOException
	 */
	private void initRecord() throws IOException {
		System.out.println("111111111111111111111111");
		mMediaRecorder = new MediaRecorder();  // 创建mediarecorder对象  
		mMediaRecorder.reset(); // 释放资源  
		if (mCamera != null)
			mMediaRecorder.setCamera(mCamera);
		mMediaRecorder.setOnErrorListener(this);
		mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
		mMediaRecorder.setVideoSource(VideoSource.CAMERA);// 视频源
		mMediaRecorder.setAudioSource(AudioSource.MIC);// 音频源
		mMediaRecorder.setOutputFormat(OutputFormat.MPEG_4);// 视频输出格式
		mMediaRecorder.setAudioEncoder(AudioEncoder.AMR_NB);// 音频格式
		mMediaRecorder.setVideoSize(mWidth, mHeight);// 设置分辨率：
			
		mMediaRecorder.setVideoFrameRate(29);
		mMediaRecorder.setVideoEncodingBitRate(1 * 1280 * 960);// 设置帧频率，然后就清晰了
		mMediaRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
		mMediaRecorder.setVideoEncoder(VideoEncoder.H264);// 视频录制格式
		mMediaRecorder.setOutputFile(vecordFile.getAbsolutePath());  // 设置视频文件输出的路径
		mMediaRecorder.prepare();  // 准备录制  
		try {
			mMediaRecorder.start();  // 开始录制  
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取�?��的分辨率
	 * @param width
	 * @param height
	 * @param parameters
	 * @return
	 */
	private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}
		return result;
	}

	/**
	 * �?��录制视频
	 * 
	 * @      fileName
	 *            视频储存位置
	 * @      onRecordFinishListener
	 *            达到指定时间之后回调接口
	 */
	public void record(final OnRecordFinishListener onRecordFinishListener) {
		this.mOnRecordFinishListener = onRecordFinishListener;
		createRecordDir();
		try {
			if (!isOpenCamera)// 如果未打�?��像头，则打开
				initCamera();
			initRecord();
			mTimeCount = 0;// 时间计数器重新赋�?
			mTimer = new Timer();
			mTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mTimeCount++;
					mProgressBar.setProgress(mTimeCount);// 设置进度�?
					if (mTimeCount == mRecordMaxTime) {// 达到指定时间，停止拍�?
						stop();
						
					}
				}
			}, 0, 1000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 停止拍摄
	 * 
	 * 
	 */
	public void stop() {
		stopRecord();
		releaseRecord();
		freeCameraResource();
	}

	/**
	 * 停止录制
	 * 
	 * 
	 */
	public void stopRecord() {
		mProgressBar.setProgress(0);
		if (mTimer != null)
			mTimer.cancel();
		if (mMediaRecorder != null) {
			// 设置后不会崩
			mMediaRecorder.setOnErrorListener(null);
			mMediaRecorder.setPreviewDisplay(null);
			try {
				mMediaRecorder.stop();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (RuntimeException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 释放资源
	 * 
	 * 
	 */
	private void releaseRecord() {
		if (mMediaRecorder != null) {
			mMediaRecorder.setOnErrorListener(null);
			try {
				mMediaRecorder.release();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mMediaRecorder = null;
	}

	public int getTimeCount() {
		return mTimeCount;
	}

	/**
	 * @return the vecordFile
	 */
	public File getVecordFile() {
		return vecordFile;
	}

	/**
	 * 录制完成回调接口
	 */
	public interface OnRecordFinishListener {
		public void onRecordFinish();
	}

	@Override
	public void onError(MediaRecorder mr, int what, int extra) {
		try {
			if (mr != null)
				mr.reset();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
