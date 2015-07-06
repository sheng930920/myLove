package com.example.test;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsData {
	
	public static ArrayList<News> list = new ArrayList<News>();
	
	public  NewsData(JSONArray array) {
		list.clear();
		for (int i = 0; i < array.length(); i++) {
			
			 int num = i+1;
			
			JSONObject object;
			try {
				
				object = array.getJSONObject(i);
								
				String Tempurl =object.getString("url");
				 String[] urlList;
				System.out.println("Tempurl是--->>"+Tempurl);
				if(Tempurl==null||Tempurl.length()<=0){
					 urlList = null;
					 
				}else{
					urlList = Tempurl.split(",");
				}
				
				
				//System.out.println("urlList的长度是"+urlList.length);
				
				ArrayList<Reply> replyList = new ArrayList<Reply>();
				String replyName ="";
				String replyContent ="";
				JSONArray rep = object.getJSONArray("com");
				for(int j = 0; j < rep.length(); j++){
					
					JSONObject obj = rep.getJSONObject(j);  	 				
					replyName = obj.getString("name");
					//System.out.println("replyName的值---->>"+replyName);
					replyContent = obj.getString("comment");
					//System.out.println("replyContent的值---->>"+replyContent);
					Reply reply = new Reply(replyName, replyContent);
					replyList.add(reply);
				}
				
				
				News news = new News( object.getString("time"),
						             object.getString("name"),
						             object.getString("content"),
						             object.getInt("id"),
						             object.getInt("style"),
						             urlList,
						             object.getString("video"),
						             replyList,
						             object.getString("location"),
						             num,
						             object.getString("avatar")
						             );
				list.add(news);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
