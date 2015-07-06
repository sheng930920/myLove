package com.example.test;

import java.util.HashMap;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.widget.ImageView;
/**
 * �첽������Ƶ��ĳһ֡��Ϊbitmap�������ImageView�ؼ�
 * @author sheng
 *
 */
public class myvideo extends AsyncTask<String, String, Bitmap> {
	private ImageView video;

	public myvideo(ImageView video){
		this.video = video;
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	protected Bitmap doInBackground(String... params) {
		
		System.out.println("������");
		Bitmap bitmap = null;
		String url = params[0];
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		int kind = MediaStore.Video.Thumbnails.MINI_KIND;
		try {
			if(Build.VERSION.SDK_INT>=14){
				retriever.setDataSource(url, new HashMap<String, String>());
			}else{
				retriever.setDataSource(url);
			}								
			bitmap = retriever.getFrameAtTime();
			System.out.println("bitmap1��--->>>"+bitmap);
			System.out.println("�����˵�3��");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		
		return bitmap;
	}
	
	protected void onPostExecute(Bitmap btm) {
		video.setImageBitmap(btm);
	}
	
	

}
