package com.example.test;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.test.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ImageDetailFragment extends Fragment {
	private String mImageUrl;
	private ImageView mImageView;
	private ProgressBar progressBar;
	

	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
		ImageLoader.getInstance().displayImage(mImageUrl, mImageView, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
					message = "���ش���";
					break;
				case DECODING_ERROR:
					message = "ͼƬ�޷���ʾ";
					break;
				case NETWORK_DENIED:
					message = "���������⣬�޷�����";
					break;
				case OUT_OF_MEMORY:
					message = "ͼƬ̫���޷���ʾ";
					break;
				case UNKNOWN:
					message = "δ֪�Ĵ���";
					break;
				}
				Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				progressBar.setVisibility(View.GONE);
				
			}
		});

		
		
		
	}

}
