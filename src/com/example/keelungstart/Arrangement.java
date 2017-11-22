package com.example.keelungstart;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class Arrangement extends Activity {
    private final static String gte_NAME="Sfgtsql.pref";
	private ArrayList<HashMap> tiem2;
	int size;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.arrangement); 
		setTitle(getString(R.string.set));
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		SharedPreferences.Editor ed=spf.edit();
		ed.putInt("home", 1);
 	    ed.commit();
 	   if(spf.getInt("text", 0)==0){
			 size=15;
		}else if(spf.getInt("text", 0)==1){
			size=20;
		}else if(spf.getInt("text", 0)==2){
			size=25;
		}else if(spf.getInt("text", 0)==3){
			size=30;
		}
		ListView listview = (ListView) findViewById(R.id.arrangementlistview);
		listview.setOnItemClickListener(new ListViewClickListener());
		Resources res=getResources();
		String[] st=res.getStringArray(R.array.arrangement);
		HashMap hh;
		tiem2 = new ArrayList<HashMap>();
		for(int i=0;i<st.length;i++)
		{
			hh = new HashMap();
    		hh.put("id", i);
    		hh.put("name",st[i]);
    		hh.put("tv", size);
    		tiem2.add(hh); 
		}
		listview.setAdapter(new Listview2(Arrangement.this, tiem2));
		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
        	Intent intent=new Intent(Arrangement.this, Home.class);
	        Arrangement.this.startActivity(intent);
	    	finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	
	class ListViewClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> parent,
				View view,
				int position,
				long id) {
			Intent intent = new Intent();
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
			intent.putExtra("PF",position);
			switch(position){
			case 0:
			        intent.setClass(getApplicationContext(), GPSArrangement.class);
			        Arrangement.this.startActivity(intent);
			        break;
			case 1:
				intent.setClass(getApplicationContext(), Gte.class);
		        Arrangement.this.startActivity(intent);
				    break;
			case 2:
				intent.setClass(getApplicationContext(), Backup.class);
		        Arrangement.this.startActivity(intent);
				    break;
			case 3:
				intent.setClass(getApplicationContext(), Textsize.class);
		        Arrangement.this.startActivity(intent);
		        finish();
				    break;
			case 4:
				intent.setClass(getApplicationContext(), Music.class);
		        Arrangement.this.startActivity(intent);
				    break;

			case 5:
				intent.setClass(getApplicationContext(), Nmfc.class);
		        Arrangement.this.startActivity(intent);
		        finish();
				    break;
			case 6:
			        intent.setClass(getApplicationContext(), About.class);
			        Arrangement.this.startActivity(intent);
			        break;
			}
		}
	}
}
