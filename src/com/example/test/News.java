package com.example.test;

import java.util.ArrayList;
import java.util.List;

public class News {

	public String avatar = ""; // 头像
	public int id;
	public String name = ""; // 名字
	public String content = ""; // 文字内容
	public String time = ""; // 发表时间
	public int style = 1; // 样式
	public String[] url;//图片url
	public String video;  //视频url
	public ArrayList<Reply> replyList;// 回复的评论listView的对象
	public String location = "";
	int num;

	public News(String time,String name, String content, int id,int style,String[] url,String video,ArrayList<Reply> replyList,String location,int num,String avatar) {

		super();
        this.id = id;
		this.name = name;
		this.content = content;
		this.time = time;
		this.style = style;
		this.url = url;
		this.video = video;
        this.replyList = replyList;
        this.location = location;
        this.num = num;
        this.avatar = avatar;
	}

}
