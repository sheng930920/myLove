package com.example.test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;


import com.example.myvideo.MainVideoActivity;
import com.example.test.R;
import com.example.testpic.PublishedActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;


import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class MainActivity extends Activity {
	
	public ListView lv1;
	NewsData newsdata;
	MyAdapter adapter;
	
	Uri photoUri;
	private static final int TAKE_PICTURE = 0x000001;
	private MyApplication app;
	String imgpath ="";
	ImageView avatar;

	private PopupWindow editWindow;// �ظ�window
	EditText replyEdit;
	Button sendBtn;
	LinearLayout topLayout;
	InputMethodManager manager;
	int newsId = 0;
	int Position = -1;
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		
		app = (MyApplication) getApplication();
		lv1 = (ListView) this.findViewById(R.id.lv1);
		lv1.addHeaderView(getheadView());
		initPopWindow();
		new Mydownload().execute("http://1.love930110.sinaapp.com/news2.php");
	}
	/**
	 * ���ͷ��
	*/
	private View getheadView() {
		View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.header, null);
		
		TextView nickname = (TextView)view.findViewById(R.id.nickname);
		nickname.setText(app.getNickname());
		ImageView takephoto = (ImageView)view.findViewById(R.id.imageView1);
		takephoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showPhotoDialog();
			}
		});
		
		avatar = (ImageView)view.findViewById(R.id.siv_img);
		ImageLoader.getInstance().displayImage(app.getAvtar(), avatar);
		avatar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showPhotoDialog2();
			}
		});

		return view;
	}
	
	/**
	 * ��ʼ������popWindow
	 */
	private void initPopWindow() {
		topLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
		View editView = getLayoutInflater().inflate(R.layout.reply_input, null);
		editWindow = new PopupWindow(editView, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, true);
		editWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
		editWindow.setOutsideTouchable(true);
		replyEdit = (EditText) editView.findViewById(R.id.reply);
		
		sendBtn = (Button) editView.findViewById(R.id.send_msg);
		sendBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String content = replyEdit.getText().toString();
				reply(content);
			}
		});
	}
	
	/**
	 * ��������
	 */
	public void reply(String content) {
		replyEdit.setText("");
		RequestParams params = new RequestParams();
		 params.addHeader("name", "value");
		 params.addQueryStringParameter("name", "value");
		 
		 String id = String.valueOf(newsId);
		 params.addBodyParameter("comment", content);
		 params.addBodyParameter("name", app.getNickname());
		 params.addBodyParameter("newsId", id);
		 
		 HttpUtils http = new HttpUtils();
		 http.send(HttpRequest.HttpMethod.POST,
					"http://1.love930110.sinaapp.com/reply.php", params,
					new RequestCallBack<String>() {

						@Override
						public void onStart() {
							// tv.setText("conn...");
						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
						
						}

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							Toast.makeText(MainActivity.this, "���۳ɹ�", 3000).show();
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							Toast.makeText(MainActivity.this, "����ʧ��", 3000).show();
						}
					});

	}

	
	/**
	 * ��ʾ��ʾ��
	 */
	protected void showPhotoDialog() {
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		 dlg.show();
		 Window window = dlg.getWindow();
		// *** ��Ҫ����������ʵ������Ч����.
	   // ���ô��ڵ�����ҳ��,alertdialog.xml�ļ��ж���view����
		 window.setContentView(R.layout.alertdialog);
		// Ϊȷ�ϰ�ť����¼�
	    TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
	    tv_paizhao.setText("��Ƭ");
	    tv_paizhao.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SdCardPath")
            public void onClick(View v) {
            	
            	Intent intent = new Intent(MainActivity.this,PublishedActivity.class);
            	startActivity(intent);
                dlg.cancel();
            }
        });
	    
	    TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
        tv_xiangce.setText("��Ƶ");
        tv_xiangce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
       
            	Intent intent = new Intent(MainActivity.this,MainVideoActivity.class);
            	startActivity(intent);
                dlg.cancel();
            }
        });
		
	}
	
	/**
	 * ����ͷ�����ʾ��ʾ��
	 */
	protected void showPhotoDialog2() {
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		 dlg.show();
		 Window window = dlg.getWindow();
		// *** ��Ҫ����������ʵ������Ч����.
	   // ���ô��ڵ�����ҳ��,alertdialog.xml�ļ��ж���view����
		 window.setContentView(R.layout.alertdialog);
		// Ϊȷ�ϰ�ť����¼�
	    TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
	    tv_paizhao.setText("����");
	    tv_paizhao.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SdCardPath")
            public void onClick(View v) {
            	
            	ContentValues values = new ContentValues();
				photoUri = getContentResolver().insert(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
				// ׼��intent���� ָ�� �� ��Ƭ ���ļ�����photoUri��
				System.out.println("-------------------------"
						+ photoUri.toString());
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				// �������յĴ��塣��ע�� �ص�����
				startActivityForResult(intent, TAKE_PICTURE);
				dlg.cancel();
            }
        });
	    
	    TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
        tv_xiangce.setText("���");
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
		 if (resultCode == RESULT_OK) {
			 switch (requestCode) {
			 case TAKE_PICTURE:
				 ContentResolver cr = this.getContentResolver();
					Cursor cursor = cr.query(photoUri, null, null, null, null);
					Bitmap bm = null;
					if (cursor != null) {
						if (cursor.moveToNext()) {
							imgpath = cursor.getString(1);
							System.out.println("·����---->>>>" + imgpath);
							bm = BitmapFactory.decodeFile(imgpath);
						}
					}
					 
					cursor.close();
				 avatar.setImageBitmap(bm);
				 int id = app.getId();
				 UpdateAvatr updateAvatar = new UpdateAvatr(id);
				 updateAvatar.execute( imgpath);
				// Toast.makeText(this, imgpath, Toast.LENGTH_LONG).show(); 
			 break;
			 case 2:
				 Uri uri = data.getData(); 
				 Cursor cursor1 = this.getContentResolver().query(uri, null,null, null, null); 
				 cursor1.moveToFirst(); 
				 imgpath = cursor1.getString(1); // ͼƬ�ļ�·�� 
				 Bitmap bm1 = BitmapFactory.decodeFile(imgpath);
				 avatar.setImageBitmap(bm1);
				 int id2 = app.getId();
				 UpdateAvatr update = new UpdateAvatr(id2);
				 update.execute( imgpath);
				 break; 
			
			 
			 }
		 }
	 }


	/**
	 * �첽������
	*/
	public class Mydownload extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			//createLoadingDialog(MainActivity.this, "���ݼ�����...").show();
		}

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				return Mydownjson(params[0]);
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);		
				DisplayJson(result);								
				//createLoadingDialog(MainActivity.this, "").dismiss();
				adapter.notifyDataSetChanged();//��̬����ListView
			}

		}
		/**
		 * ���ʷ�������ȡJson����
		 * 
		 */
		public String Mydownjson(String url) {
			String result = "";
			String temp1 = "";
			
			HttpGet httpget = new HttpGet(url);
			HttpClient client = new DefaultHttpClient();
			try {
				
				HttpResponse response = client.execute(httpget);
				if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
					
				result = EntityUtils.toString(response.getEntity(), "utf-8");
				
				int start=result.indexOf("[{");
				int stop=result.lastIndexOf("}]");
				temp1=result.substring(start, stop+2);
				System.out.println("Json��-->>>"+temp1);
				
				//System.out.println("��Ӧͨ����");
				}else{
					System.out.println("�����쳣�������������ã�");
					Toast.makeText(MainActivity.this, "�����쳣�������������ã�", 2000).show();
					
				}
				
				return temp1;
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("adcc");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("aaaaaaaa");
			}
			
			return temp1;
		}
		
		
		/**
		 * JSON����
		 */

		public void DisplayJson(String jsonstring) {

			try {
				
				JSONArray array = new JSONArray(jsonstring);
				newsdata = new NewsData(array);
				adapter = new MyAdapter(newsdata);
				lv1.setAdapter(adapter);
				//System.out.println("--->>ListView���������<<----");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		/**
		 * ListView������
		 *
		 */
		public class MyAdapter extends BaseAdapter {
			
			NewsData newsdata;
			
			public MyAdapter(NewsData newsdata) {
				this.newsdata = newsdata;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return newsdata.list.size();
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				
				LayoutInflater inflater=getLayoutInflater();
				View temp = null;
				
				if(newsdata.list.get(position).style == 1){
					
					//System.out.println("--->>>������");
					temp = inflater.inflate(R.layout.style1, null);
					//topLayout = (RelativeLayout)temp.findViewById(R.id.Reply);
					// ���ݵİ�
					TextView name =(TextView) temp.findViewById(R.id.name);
					name.setText(newsdata.list.get(position).name);
					
					//ImageView photo = (ImageView)temp.findViewById(R.id.photo);
					//ImageLoader.getInstance().displayImage(newsdata.list.get(position).avatar, photo);
					
					TextView place = (TextView) temp.findViewById(R.id.place);
					
	                if(newsdata.list.get(position).location!=""&&newsdata.list.get(position).location.length()>0){
	                	place.setVisibility(View.VISIBLE);
	                	place.setText(newsdata.list.get(position).location);
					}else{
						place.setVisibility(View.GONE);
					}
					
					TextView content =(TextView) temp.findViewById(R.id.message);
					content.setText(newsdata.list.get(position).content);
					System.out.println("��ȡ��������--->>>"+newsdata.list.get(position).content);					
					
					GridView image = (NoScrollGridView) temp.findViewById(R.id.image_content);
					ListView myList = (MyListView)temp.findViewById(R.id.reply_list);
					
					TextView time =(TextView) temp.findViewById(R.id.time);
					time.setText(handTime(newsdata.list.get(position).time));
					
					ImageButton reply = (ImageButton)temp.findViewById(R.id.reply);
					final View view = temp;
					reply.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							lv1.setSelectionFromTop(newsdata.list.get(position).num + 1,view.getHeight());
							newsId = newsdata.list.get(position).id;
							showDiscuss();
						}
					});
					
					TextView share = (TextView) temp.findViewById(R.id.share);
					if(newsdata.list.get(position).url!=null&&newsdata.list.get(position).url.length>0){
						
						final String[] url = newsdata.list.get(position).url;  //���ݹ�ȥ��ͼƬurl����
						
						image.setVisibility(View.VISIBLE);
						share.setVisibility(View.VISIBLE);
						image.setAdapter(new MyGirdAdapter(newsdata.list.get(position).url,MainActivity.this) );
						
						image.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								System.out.println("���ݹ�ȥ���±���--->>"+position);
								System.out.println("���ݹ�ȥ��urls��--->>"+url);
								imageBrower(position,url);
							}
						});
						
						
					}else{
						image.setVisibility(View.GONE);
						share.setVisibility(View.GONE);
					}
					
					if(newsdata.list.get(position).replyList!=null&&newsdata.list.get(position).replyList.size()>0){
						myList.setVisibility(View.VISIBLE);
						myList.setAdapter(new MyListAdapter(newsdata.list.get(position).replyList,MainActivity.this));
					}else{
						myList.setVisibility(View.GONE);
					}
					
					
					
				}else{
					temp = inflater.inflate(R.layout.style2, null);
					// ���ݵİ�
					TextView name =(TextView) temp.findViewById(R.id.name);
					name.setText(newsdata.list.get(position).name);
					
                    TextView place = (TextView) temp.findViewById(R.id.place);
					
	                if(newsdata.list.get(position).location!=""&&newsdata.list.get(position).location.length()>0){
	                	place.setVisibility(View.VISIBLE);
	                	place.setText(newsdata.list.get(position).location);
					}else{
						place.setVisibility(View.GONE);
					}
	                
	                TextView time =(TextView) temp.findViewById(R.id.time);
					time.setText(handTime(newsdata.list.get(position).time));
					
					TextView content =(TextView) temp.findViewById(R.id.message);
					content.setText(newsdata.list.get(position).content);
					
					ImageView video = (ImageView)temp.findViewById(R.id.video);
					final String url1 = newsdata.list.get(position).video;
					new myvideo(video).execute(url1);
					video.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(MainActivity.this, VideoPlay.class);
							System.out.println("��Ƶurl--->>>"+url1);
							intent.putExtra("PATH", url1);					
							startActivity(intent);
						}
					});
					
											
				}
				
				return temp;
			}
			
			

			/**
             * ���ͼƬ��ͼ          
             */
			private void imageBrower(int position, String[] urls) {
				Intent intent = new Intent(MainActivity.this, ImagePagerActivity.class);
				// ͼƬurl,Ϊ����ʾ����ʹ�ó�����һ������ݿ��л������л�ȡ
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
				startActivity(intent);
			}
			
		}
		
		/**
		 * ��ʾ���ۿ�	 
		 */
        public void showDiscuss() {
        	replyEdit.setFocusable(true);
    		replyEdit.requestFocus();
    		// ���ý��㣬��Ȼ�޷��������뷨
    		editWindow.setFocusable(true);

    		// �������䲻�ܵߵ�
    		editWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
    		editWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    		editWindow.showAtLocation(topLayout, Gravity.BOTTOM, 0, 0);

    		// ��ʾ����
    		manager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
    		manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    		editWindow.setOnDismissListener(new OnDismissListener() {
    			@Override
    			public void onDismiss() {
    				manager.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
    			}
    		});
			
		}
        /**
    	 * ����ʱ��
    	 * 
    	 * @param string
    	 * @return
    	 */
    	public String handTime(String time) {
//    		if (time == null || "".equals(time.trim())) {
//    			return "";
//    		}
    		String Time ="";
    		try {
    			Date date = format.parse(time);
    			long tm = System.currentTimeMillis();// ��ǰʱ���
    			long tm2 = date.getTime();// ����̬��ʱ���
    			long d = (tm - tm2) / 1000;// ʱ���� ��λ��
    			if ((d / (60 * 60 * 24)) > 0) {
    				Time = d / (60 * 60 * 24) + "��ǰ";
    			} else if ((d / (60 * 60)) > 0) {
    				Time = d / (60 * 60) + "Сʱǰ";
    			} else if ((d / 60) > 0) {
    				Time =d / 60 + "����ǰ";
    			} else {
    				// return d + "��ǰ";
    				Time = "�ո�";
    			}
    		} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		return Time ;
    	}
		
		
		
		

}
