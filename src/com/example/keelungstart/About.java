package com.example.keelungstart;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class About extends Activity {
	private final static String gte_NAME="Sfgtsql.pref";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==0){
			DisplayMetrics dm = getResources().getDisplayMetrics();
			// 取得螢幕顯示的資料
			int ScreenWidth = dm.widthPixels;
			int ScreenHeight = dm.heightPixels;
			// 螢幕寬和高的Pixels
			if (ScreenHeight > ScreenWidth){
				setContentView(R.layout.about);
			}
			else{
				setContentView(R.layout.about2);
			}
		}else if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			 setContentView(R.layout.about); 
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.about2); 
		}
		if(spf.getInt("nmfc2", 0)==1){
      	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		Resources res = getResources(); 
		String[] tit=res.getStringArray(R.array.arrangement);
		setTitle(tit[bundle.getInt("PF")]);
	}
}
