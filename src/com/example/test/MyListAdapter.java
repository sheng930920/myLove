package com.example.test;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.Reply;
public class MyListAdapter extends BaseAdapter {
	
	private ArrayList<Reply> replyList;
	private LayoutInflater mLayoutInflater;
	
	public MyListAdapter(ArrayList<Reply> replyList, Context context){
		this.replyList = replyList;
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return replyList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return replyList.get(arg0);
	}
	
	

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Reply reply = replyList.get(position);
		
		MyListHolder viewHolder;
		if (convertView == null) {
			viewHolder = new MyListHolder();
			convertView = mLayoutInflater.inflate(R.layout.reply,parent, false);
			viewHolder.Name = (TextView) convertView.findViewById(R.id.send_name);
			viewHolder.Name.setText(reply.replyName);
			viewHolder.Content = (TextView) convertView.findViewById(R.id.content);
			viewHolder.Content.setText(reply.replyContent);
		}else {
			viewHolder = (MyListHolder) convertView.getTag();
		}
		
		
		
		return convertView;
	}
	
	public static class MyListHolder {
		
		TextView Name;
		TextView Content;
		
	}

}
