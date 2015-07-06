package com.example.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class UpdateAvatr extends AsyncTask<String, String, String> {
	
	private int id;
	private Context context;
	
	public UpdateAvatr(int id){		
		this.id = id;
	
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String flag = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://1.love930110.sinaapp.com/UpdataAvatr.php");
		
		MultipartEntityBuilder entitys = MultipartEntityBuilder.create();
		entitys.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		entitys.setCharset(Charset.forName(HTTP.UTF_8));
		
		FileBody filebody = new FileBody(new File(params[0]));
		entitys.addPart("avatar", filebody);
		entitys.addTextBody("id",String.valueOf(id));
		HttpEntity httpEntity = entitys.build();		
		post.setEntity(httpEntity);
		
		try {
			HttpResponse httpResponse = httpClient.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					String Result = EntityUtils.toString(entity,"utf-8");
					int start=Result.indexOf("{");
					int stop=Result.lastIndexOf("}");
					String temp=Result.substring(start, stop+1);
					System.out.println("Result---------->>是"+temp+"长度是"+temp.length());
					JSONObject json = new JSONObject(temp);
					flag = json.getString("flag");
					return flag;
				}
			}else{
				Toast.makeText(context, "响应不通过！", Toast.LENGTH_SHORT).show();
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if(result.equals("success")){
			System.out.println("头像更新成功！");
			
		}else{
			System.out.println("头像更新失败！");
			
		}
	}
	
	
	

}
