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

public class Bus101 extends Activity {
	private final static String gte_NAME="Sfgtsql.pref";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus101);
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		setTitle("101 和平島-基隆車站(經中正路)");
        Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new ButtonClickListener());
		Button button2 = (Button) findViewById(R.id.button2);
		
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
			Bundle bundle = new Bundle();
			intent.putExtra("SELECTED_PICT", item);
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			switch(position){
			case 0:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "0");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
			             break;
			case 1:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "1");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
			         break;
			case 2:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "2");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 3:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "3");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 4:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "4");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 5:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "5");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 6:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "6");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 7:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "7");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 8:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "8");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 9:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "9");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 10:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "10");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 11:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "11");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 12:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "12");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 13:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "13");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 14:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "14");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;     
			case 15:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "15");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 16:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "16");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 17:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "17");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 18:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "18");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 19:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "19");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 20:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "20");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 21:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "21");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 22:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "22");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 23:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "23");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 24:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "24");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
		         break;
			case 25:
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				intent.setClass(getApplicationContext(), Busview.class);
				bundle.putString("ciass", "11");
				bundle.putString("ph", "25");
				intent.putExtras(bundle);
		           Bus101.this.startActivity(intent);
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