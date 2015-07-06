package com.example.test;

import java.util.ArrayList;
import java.util.List;

public class News {

	public String avatar = ""; // ͷ��
	public int id;
	public String name = ""; // ����
	public String content = ""; // ��������
	public String time = ""; // ����ʱ��
	public int style = 1; // ��ʽ
	public String[] url;//ͼƬurl
	public String video;  //��Ƶurl
	public ArrayList<Reply> replyList;// �ظ�������listView�Ķ���
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
