package com.example.test;

import java.io.Serializable;
import java.util.ArrayList;

public class Reply{
	

	public String replyName;// 评论的名字
	public String replyContent;// 评论的内容
	
	public String getReplyName() {
		return replyName;
	}
	public void setReplyName(String replyName) {
		this.replyName = replyName;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public Reply(){
		
	}
	public Reply(String replyName,String replyContent){	
		this.replyName = replyName;
		this.replyContent = replyContent;
	}

}
