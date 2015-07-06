package com.example.test;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.test.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Login extends Activity  {
	
	
	EditText phone;
	EditText Password;
	Button login;
	Button register;
	
	private int id;
	private String nickname;
	private String avatar;
	
	private MyApplication app;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
		app = (MyApplication) getApplication();
		init();
	}
	
	public void init(){
		
		phone = (EditText) this.findViewById(R.id.et_username);
		Password = (EditText) this.findViewById(R.id.et_password);
		login = (Button) this.findViewById(R.id.btn_login);
		register = (Button) this.findViewById(R.id.btn_register);

		
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new LoginTask().execute();
			}
		});
		
		register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Login.this,Register.class);
				startActivity(intent); // 跳转到成功页面
			}
		});
		
	}

	
	/**
	 * 判断用户登录返回信息
	 */
	public class LoginTask extends AsyncTask<String,String,String>{

		@Override
		protected String doInBackground(String... params) {
			return getJSONFromServer();
		}
		@Override
		protected void onPostExecute(String result) {
			if(result.equals("success")){
				MyToast.makeImgAndTextToast(Login.this,getResources().getDrawable(R.drawable.tips_smile),
						"登录成功！", 100).show();
				app.setId(id);
				app.setNickname(nickname);
				app.setAvtar(avatar);
				new Handler().postDelayed(new Runnable() {  

			        @Override  
			        public void run() {  
			        	Intent intent = new Intent(Login.this,MainActivity.class);
						startActivity(intent); // 跳转到成功页面
						Login.this.finish(); 
			        }  
			    }, 1500); 
				
				
			}else{
				Toast.makeText(Login.this, "登陆失败！", 4000).show();
			}
			
		}
		
	}
	
	/**
	 * 访问服务器的方法
	 */
	public String getJSONFromServer(){
		
		String flag="";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost mPost = new HttpPost("http://1.love930110.sinaapp.com/login.php");
		String phoneNum = phone.getText().toString();
		String password = Password.getText().toString();
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("phoneNum", phoneNum));
		pairs.add(new BasicNameValuePair("password", password));		
		try {
			mPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(mPost);
			int res = response.getStatusLine().getStatusCode();
			if (res == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String Result = EntityUtils.toString(entity,"utf-8");
					int start=Result.indexOf("{");
					int stop=Result.lastIndexOf("}");
					String temp=Result.substring(start, stop+1);
					System.out.println("Result---------->>是"+temp+"长度是"+temp.length());
					JSONObject json = new JSONObject(temp);
					flag = json.getString("flag");
					System.out.println("flag是--->>>"+flag);
					id = json.getInt("id");
					System.out.println("id是--->>>"+id);
					nickname = json.getString("nickname");
					System.out.println("nickname是--->>>"+nickname);
					avatar = json.getString("avatar");
					System.out.println("avatar是--->>>"+avatar);
					return flag;
				}
				
			}else{
				Toast.makeText(this, "响应不通过！", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return flag;		
	}
	
	

}
