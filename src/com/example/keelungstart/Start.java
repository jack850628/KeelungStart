package com.example.keelungstart;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class Start extends Activity {
	private final static String gte_NAME="Sfgtsql.pref";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
	      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	      WindowManager.LayoutParams.FLAG_FULLSCREEN);
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("nmfc", 0)==0){
				DisplayMetrics  dm = getResources().getDisplayMetrics();
				// 取得螢幕顯示的資料
				int ScreenWidth = dm.widthPixels;
				int ScreenHeight = dm.heightPixels;
				// 螢幕寬和高的Pixels
				if (ScreenHeight > ScreenWidth){
					setContentView(R.layout.start);
				}
				else{
					setContentView(R.layout.start2);
				}
			}else if(spf.getInt("nmfc", 0)==1){
				 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				 setContentView(R.layout.start);
			}else if(spf.getInt("nmfc", 0)==2){
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				setContentView(R.layout.start2);
			}
			if(spf.getInt("nmfc2", 0)==1){
	      	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			}
		if(spf.getInt("music", 0)==0){
			MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.kgestart);
			mp.start();
		}
		new Thread (new Runnable() {
			public void run(){
				try{
					Thread.sleep(4000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				Intent intent = new Intent ();
				    intent.setClass(Start.this,Home.class);
				    Start.this.startActivity(intent);
    			finish();
			}
		}).start();

	}
}