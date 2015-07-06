package com.example.testpic;

import java.io.Serializable;

/**
 * 一个图片对象
 * 
 * @author Administrator
 * 
 */
public class ImageItem implements Serializable {
	public String imageId;                   //照片在手机中的存储地址ID
	public String thumbnailPath;             //相机中的位置
	public String imagePath;                 //图片的路径
	public boolean isSelected = false;
}
