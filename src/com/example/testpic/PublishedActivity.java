package com.example.testpic;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import com.example.test.Login;
import com.example.test.MainActivity;
import com.example.test.MyApplication;
import com.example.test.R;
import com.example.test.Register;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PublishedActivity extends Activity {

	private GridView noScrollgridview;
	private GridAdapter adapter;
	private TextView activity_selectimg_send;
	private EditText message;
	String Message = "";
	
	RelativeLayout showLocaltion;
	TextView local_position;
	private String localResult;
	public String Location;
	
	private MyApplication app;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_selectimg);
		Init();
		app = (MyApplication) getApplication();
	}

	public void Init() {
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		
		//获取所在位置
		showLocaltion = (RelativeLayout) findViewById(R.id.showLocaltion);
		local_position = (TextView) findViewById(R.id.local_position);
		showLocaltion.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(PublishedActivity.this,LocationActivity.class);
				startActivityForResult(intent, 5);
			}
		});
		
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					new PopupWindows(PublishedActivity.this, noScrollgridview);
				} else {
					Intent intent = new Intent(PublishedActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
		message= (EditText) this.findViewById(R.id.message);
		activity_selectimg_send = (TextView) findViewById(R.id.activity_selectimg_send);
		activity_selectimg_send.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Message = message.getText().toString();
				Location = local_position.getText().toString();
				//for (int i = 0; i < Bimp.drr.size(); i++) {
				//	System.out.println("图片"+i+"是--->>>"+Bimp.drr.get(i));
				//}
				
				new ImageUpload().execute(Bimp.drr);
				
				// 高清的压缩图片全部就在  list 路径里面了
				// 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
				// 完成上传服务器后 .........
				FileUtils.deleteDir();
			}
		});
	}
	public class ImageUpload extends AsyncTask<List<String>, String, String> {
		
		

		@Override
		protected String doInBackground(List<String>... params) {
			
			String flag ="";
			
			String[] filePath = new String[params[0].size()]; 
			int size = params[0].size(); 
			for (int i = 0; i < size; i++) {  
                filePath[i] = params[0].get(i);  
            }  
			
			// 链接超时，请求超时设置  
			BasicHttpParams httpParams = new BasicHttpParams();  
			HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);  
			HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);  
			
			 // 请求参数设置  
			HttpClient client = new DefaultHttpClient(httpParams);  
			HttpPost post = new HttpPost("http://1.love930110.sinaapp.com/uploadImage2.php");
			
			
			MultipartEntityBuilder entitys = MultipartEntityBuilder.create();
			entitys.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			entitys.setCharset(Charset.forName(HTTP.UTF_8));
			ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
			
			StringBody stringBody = new StringBody(Message,contentType);
			StringBody location = new StringBody(Location,contentType);
			StringBody name = new StringBody(app.getNickname(),contentType);
			
	
			try {
				String Num = String.valueOf(size);
				String userId = String.valueOf(app.getId());
				entitys.addPart("message", stringBody);
				entitys.addPart("mylocation", location);
				entitys.addPart("name", name);
				entitys.addTextBody("userId", userId);
				System.out.println("message是----->>>"+Message);
				entitys.addTextBody("num",  Num);
				
				for(int i = 1; i < size+1; i++){
					System.out.println("第"+i+"张是----->>>"+filePath[i-1]);
					FileBody filebody = new FileBody(new File(filePath[i-1]));
					entitys.addPart("img"+i,filebody );  		            					
				}
				HttpEntity httpEntity = entitys.build();
				post.setEntity(httpEntity);  
		        HttpResponse httpResponse = client.execute(post); 
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
		        	Toast.makeText(PublishedActivity.this, "响应不通过！", Toast.LENGTH_SHORT).show();
		        }	        
				
			} catch (Exception e) {
				e.printStackTrace();
			}			
			return flag;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result.equals("success")){
				createLoadingDialog(PublishedActivity.this, "").dismiss();
				Intent intent = new Intent(PublishedActivity.this,MainActivity.class);
				startActivityForResult(intent,3);
				//startActivity(intent); // 跳转到成功页面
				//PublishedActivity.this.finish();
				
			}else{
				Toast.makeText(PublishedActivity.this, "照片上传失败！", 4000).show();
			}
		}
		
		@Override
		protected void onPreExecute() {
			createLoadingDialog(PublishedActivity.this, "照片上传中...").show();
		}
		
		
		

	}
	
	
	
/***************************************************/
	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return (Bimp.bmp.size() + 1);
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp.drr.get(Bimp.max);
								System.out.println(path);
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}



	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(PublishedActivity.this,
							TestPicActivity.class);
					startActivity(intent);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	private static final int TAKE_PICTURE = 0x000001;
	private String path = "";
	Uri photoUri;
	String path1;

	public void photo() {
		//确认sdcard的存在
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		   {
			  System.out.println("----------进来了---------------");
			    ContentValues values = new ContentValues();
				photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
				//准备intent，并 指定 新 照片 的文件名（photoUri）
				System.out.println("-------------------------"+photoUri.toString());
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				//启动拍照的窗体。并注册 回调处理。
				startActivityForResult(intent, TAKE_PICTURE);
		   }else{
			   Toast.makeText(this, "没有内存卡不能拍照", Toast.LENGTH_LONG).show();
		   }
		
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			ContentResolver cr = this.getContentResolver();
			Cursor cursor = cr.query(photoUri, null, null, null, null);
			Bitmap bm = null;
			if (cursor != null) {
				if (cursor.moveToNext()) {
					 path1 = cursor.getString(1);
					System.out.println("路径是---->>>>"+path1);
					bm = BitmapFactory.decodeFile(path);
			}
			}
			cursor.close();
			if (Bimp.drr.size() < 9 && resultCode == RESULT_OK) {					
				Bimp.drr.add(path1);
				System.out.println("里面有"+Bimp.drr.size()+"张图片");
			}
			bm = null;
			break;
		case 5:
			boolean local_clear = false;
			try {
				localResult = data.getStringExtra("local_result");
			} catch (Exception e) {
				Log.e("StartActivityActivity","onActivityResult------>定位当前信息返回出错");
			}
			try {
				local_clear = data.getBooleanExtra("local_clear",false);
			} catch (Exception e) {
				Log.e("StartActivityActivity","onActivityResult------>当前位置信息清除标志返回出错");
			}

			if (localResult != null) {
				local_position.setText(localResult);
			}
			break;
		case 3: finish();
		break;
		}
	}
	/********************************************/
	public Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.progressdialog_no_deal, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.anim);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(true);// true可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;

	}
	
	/********************************************/

}
