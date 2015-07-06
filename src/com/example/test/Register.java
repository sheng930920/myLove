package com.example.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.example.test.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {

	EditText nickname;
	EditText phone;
	EditText Password;
	ImageView avatar;
	ImageView back;
	ImageView iv_hide;
	ImageView iv_show;
	Button register;
	ProgressDialog dialog;
	private String imgpath = "";
	Uri photoUri;
	private static final int TAKE_PICTURE = 0x000001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		init();
	}

	public void init() {
		nickname = (EditText) this.findViewById(R.id.et_usernick);
		phone = (EditText) this.findViewById(R.id.et_usertel);
		Password = (EditText) this.findViewById(R.id.et_password);
		
		avatar = (ImageView) this.findViewById(R.id.iv_photo);
		back = (ImageView) this.findViewById(R.id.iv_back);
		iv_hide = (ImageView)this.findViewById(R.id.iv_hide);
		iv_show = (ImageView)this.findViewById(R.id.iv_show);

		register = (Button) this.findViewById(R.id.btn_register);

		// 监听多个输入框
		nickname.addTextChangedListener(new TextChange());
		phone.addTextChangedListener(new TextChange());
		Password.addTextChangedListener(new TextChange());
		
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		avatar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showCamera();
			}
		});
		
		iv_hide.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				System.out.println("进来了---->>>AAAAAAAAAA");
				iv_hide.setVisibility(View.GONE);
				iv_show.setVisibility(View.VISIBLE);
				Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				// 切换后将EditText光标置于末尾
                CharSequence charSequence = Password.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
				
			}
		});
		
				
		iv_show.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				iv_show.setVisibility(View.GONE);
				iv_hide.setVisibility(View.VISIBLE);
				Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
				// 切换后将EditText光标置于末尾
                CharSequence charSequence = Password.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
			}
			
		});
		
		register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				new register().execute();
				
			}
		});
		
		

	}

	public class register extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String flag="";
			
			String name = nickname.getText().toString();
			String phoneNum = phone.getText().toString();
			String password = Password.getText().toString();
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://1.love930110.sinaapp.com/register.php");
			
			MultipartEntityBuilder entitys = MultipartEntityBuilder.create();
			entitys.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			entitys.setCharset(Charset.forName(HTTP.UTF_8));
			ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8); 
			StringBody stringBody = new StringBody(name,contentType);
			
			FileBody filebody = new FileBody(new File(imgpath));
			entitys.addPart("avatar", filebody);
			entitys.addPart("nickname",  stringBody);
			entitys.addTextBody("phoneNum", phoneNum);
			entitys.addTextBody("password", password);
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
					}else{
						Toast.makeText(Register.this, "响应不通过！", Toast.LENGTH_SHORT).show();
					}
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
				Intent intent = new Intent(Register.this,Login.class);
				startActivity(intent); // 跳转到成功页面
				finish();
				
			}else{
				Toast.makeText(Register.this, "注册失败！", 4000).show();
			}
		}
		
	}

	/**
	 * 
	 * EditText监听器
	 * 
	 */
	class TextChange implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			boolean Sign1 = nickname.getText().length() > 0;
			boolean Sign2 = phone.getText().length() > 0;
			boolean Sign3 = Password.getText().length() > 0;
			if (Sign1 & Sign2 & Sign3) {
				register.setTextColor(0xFFFFFFFF);
				register.setEnabled(true);
			} else { // 在layout文件中，对Button的text属性应预先设置默认值，否则刚打开程序的时候Button是无显示的
				register.setTextColor(0xFFD0EFC6);
				register.setEnabled(false);
			}
		}

	}

	private void showCamera() {
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.show();
		Window window = dlg.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.alertdialog);
		// 为确认按钮添加事件,执行退出应用操作
		TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
		tv_paizhao.setText("拍照");
		tv_paizhao.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("SdCardPath")
			public void onClick(View v) {

				ContentValues values = new ContentValues();
				photoUri = getContentResolver().insert(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
				// 准备intent，并 指定 新 照片 的文件名（photoUri）
				System.out.println("-------------------------"
						+ photoUri.toString());
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				// 启动拍照的窗体。并注册 回调处理。
				startActivityForResult(intent, TAKE_PICTURE);
				dlg.cancel();
			}
		});
		TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
		tv_xiangce.setText("相册");
		tv_xiangce.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, 2);
				dlg.cancel();
			}
		});

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case TAKE_PICTURE:
			ContentResolver cr = this.getContentResolver();
			Cursor cursor = cr.query(photoUri, null, null, null, null);
			Bitmap bm = null;
			if (cursor != null) {
				if (cursor.moveToNext()) {
					imgpath = cursor.getString(1);
					System.out.println("路径是---->>>>" + imgpath);
					bm = BitmapFactory.decodeFile(imgpath);
				}
			}
			cursor.close();
			avatar.setImageBitmap(bm);
			System.out.println("imgpath时--->>>" + imgpath);
			Toast.makeText(this, imgpath, 50000).show();
			break;
		case 2:
			 Uri uri = data.getData(); 
			 Cursor cursor1 = this.getContentResolver().query(uri, null,null, null, null); 
			 cursor1.moveToFirst(); 
			 imgpath = cursor1.getString(1); // 图片文件路径 
			 Bitmap bm1 = BitmapFactory.decodeFile(imgpath);
			 avatar.setImageBitmap(bm1);
			// new UpdateAvatr().execute(imgPath);
			 Toast.makeText(this, imgpath, Toast.LENGTH_LONG).show(); 
			 break; 

		}
	}

}
