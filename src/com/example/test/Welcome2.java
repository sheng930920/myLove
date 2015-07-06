package com.example.test;


import com.example.test.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class Welcome2 extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome2);
		
	    new Handler().postDelayed(new Runnable() {  
	        @Override  
	        public void run() {	 
	        	
	            Intent intent = new Intent(Welcome2.this,Login.class);  
	            startActivity(intent);  
	            Welcome2.this.finish(); 
	          
	        }  
	    }, 3000); 
	}

}
