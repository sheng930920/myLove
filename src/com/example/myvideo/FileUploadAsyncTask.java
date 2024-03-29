package com.example.myvideo;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;

import com.example.test.MainActivity;
import com.example.test.MyApplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.sax.StartElementListener;
import android.widget.Toast;

public class FileUploadAsyncTask extends AsyncTask<File, Integer, String> {

	private String url = "http://1.love930110.sinaapp.com/upload1.php";
	private Context context;
	private ProgressDialog pd;
	private long totalSize;
	
	String Message;
	String Location;
	
	private MyApplication app;

	public FileUploadAsyncTask(Context context,String Message,String Location) {
		this.context = context;
		this.Message = Message;
		this.Location = Location;
		app = (MyApplication) context.getApplicationContext();
	}

	@Override
	protected void onPreExecute() {
		pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("视频上传中....");
		pd.setCancelable(false);
		pd.show();
	}

	@Override
	protected String doInBackground(File... params) {
		// 保存需上传文件信息
		MultipartEntityBuilder entitys = MultipartEntityBuilder.create();
		entitys.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		entitys.setCharset(Charset.forName(HTTP.UTF_8));
		
		ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8); 
		StringBody message = new StringBody(Message,contentType);
		StringBody location = new StringBody(Location,contentType);//
		StringBody Nickname = new StringBody(app.getNickname(),contentType);

		File file = params[0];
		entitys.addPart("uploadedfile", new FileBody(file));
		entitys.addPart("message", message);
		entitys.addPart("location", location);
		entitys.addPart("Nickname", Nickname);
		
		HttpEntity httpEntity = entitys.build();
		totalSize = httpEntity.getContentLength();
		ProgressOutHttpEntity progressHttpEntity = new ProgressOutHttpEntity(
				httpEntity, new ProgressListener() {
					@Override
					public void transferred(long transferedBytes) {
						publishProgress((int) (100 * transferedBytes / totalSize));
					}
				});
		return uploadFile(url, progressHttpEntity);
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		pd.setProgress((int) (progress[0]));
	}

	@Override
	protected void onPostExecute(String result) {
		pd.dismiss();
		//Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(context,MainActivity.class);
		context.startActivity(intent);
		Activity aa = (Activity)context;
		aa.finish();
	}

	/**
	 * 上传文件到服务器
	 * 
	 * @param url
	 *            服务器地址
	 * @param entity
	 *            文件
	 * @return
	 */
	public static String uploadFile(String url, ProgressOutHttpEntity entity) {
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		// 设置连接超时时间
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(entity);
		try {
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return "文件上传成功";
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpClient != null && httpClient.getConnectionManager() != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}
		return "文件上传失败";
	}

}
