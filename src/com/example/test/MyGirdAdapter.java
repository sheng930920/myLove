package com.example.test;

import java.util.ArrayList;

import com.example.test.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MyGirdAdapter extends BaseAdapter {
	
	private String[] files;
	private LayoutInflater mLayoutInflater;
	
	public MyGirdAdapter(String[] files, Context context) {
		this.files = files;
		mLayoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return  files.length;
	}

	@Override
	public String getItem(int arg0) {
		return files[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup parent) {
		MyGridViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new MyGridViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.gridview_item,parent, false);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.album_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (MyGridViewHolder) convertView.getTag();
		}
		
		String url = getItem(arg0);
		ImageLoader.getInstance().displayImage(url, viewHolder.imageView);
	
		return convertView;
	}
	
	private static class MyGridViewHolder {
		ImageView imageView;
	}

}
