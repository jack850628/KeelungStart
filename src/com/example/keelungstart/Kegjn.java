package com.example.keelungstart;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class Kegjn extends Activity{
	private final static String gte_NAME="Sfgtsql.pref";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kegjn);
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		setTitle(getString(R.string.kegjn));
		TextView tv=(TextView)findViewById(R.id.textView2);
		if(spf.getInt("text", 0)==0){
			 tv.setTextSize(15);
		}else if(spf.getInt("text", 0)==1){
			tv.setTextSize(20);
		}else if(spf.getInt("text", 0)==2){
			tv.setTextSize(25);
		}else if(spf.getInt("text", 0)==3){
			tv.setTextSize(30);
		}
	}
}
