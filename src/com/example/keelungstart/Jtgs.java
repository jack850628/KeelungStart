package com.example.keelungstart;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class Jtgs extends Activity {
	private final static String gte_NAME="Sfgtsql.pref";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jtgs);
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		setTitle("交通資訊");
        Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new ButtonClickListener());
		
		ListView listview = (ListView) findViewById(R.id.listview);
		listview.setOnItemClickListener(new ListViewClickListener());
				
	}
	class ListViewClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> parent,
				View view,
				int position,
				long id) {
	 
			ListView listview = (ListView) parent;
			String item = (String) listview.getItemAtPosition(position);
			Intent intent = new Intent();
			intent.putExtra("SELECTED_PICT", item);
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			switch(position){
			case 0:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Bus.class);
		           Jtgs.this.startActivity(intent);
			             break;
			}
		}
	}
	
	
	class ButtonClickListener implements  OnClickListener {
		    @Override
		    public void onClick(View V) {
		    	SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		    	if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
		       finish();
		    }
	}
}
