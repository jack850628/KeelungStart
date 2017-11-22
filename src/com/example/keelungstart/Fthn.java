package com.example.keelungstart;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class Fthn extends Activity {

	double[][] gpsxy;
	String[] item1;
	ArrayList<Integer> xy;
	double gpsx;
	double gpsy;
	double fg;
	LocationManager lm;								//宣告LocationManager
	LocationListener ll;
	SoundPool sound;
	private GpsStatus.Listener statusListener;
    private final static String gte_NAME="Sfgtsql.pref";//自訂義名稱
	String teg,gl;
	private ArrayList<HashMap> tiem2;
	Listview2 lv;
	HashMap hh;
	int size,ButtonSound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fthn);
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		ButtonSound = sound.load(this, R.raw.button , 1);
		Resources res = getResources(); 
		item1=res.getStringArray(R.array.list_name);
		gpsxy=new Gtgps().attgps();
		final TextView tgtk=(TextView)findViewById(R.id.tvgtk);
		TextView tv=(TextView)findViewById(R.id.textView1);
		ListView listview = (ListView) findViewById(R.id.listViewgtk);
		lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		lm.addGpsStatusListener(statusListener);
		ll=new LocationListener()
		{
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				ListView listview = (ListView) findViewById(R.id.listViewgtk);
				TextView tv=(TextView)findViewById(R.id.textView1);	
				tgtk.setText(getString(R.string.gps4));
				gpsx=location.getLatitude();
				gpsy=location.getLongitude();
				tiem2.clear();
				xy.clear();
				for(int i=0;i<item1.length;i++)
				{
					if(Math.pow(gpsxy[i][0]-gpsx, 2)+Math.pow(gpsxy[i][1]-gpsy, 2)<=Math.pow(fg, 2)){
						xy.add(i);
						hh = new HashMap();
						hh.put("id", i);
	            		hh.put("name",item1[i]);
	            		hh.put("tv", size);
	            		tiem2.add(hh); 
					}
				}
				if(tiem2.size()==0){
					tv.setVisibility(View.VISIBLE);
	            	listview.setVisibility(View.INVISIBLE);
				}else{
					tv.setVisibility(View.INVISIBLE); 
	            	listview.setVisibility(View.VISIBLE);
				}
				lv.setListview(tiem2);
				lv.notifyDataSetChanged();
			}
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				tgtk.setText(getString(R.string.gps4));
			}
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				tgtk.setText(getString(R.string.gps));
			}
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
			}
		};
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
		if(spf.getInt("td2", 0)==0){
			fg=0.003;
			teg=getString(R.string.nfg)+"300"+getString(R.string.mno)+getString(R.string.att);
			gl="300"+getString(R.string.m);
		}else if(spf.getInt("td2", 0)==1){
			fg=0.005;
			teg=getString(R.string.nfg)+"500"+getString(R.string.mno)+getString(R.string.att);
			gl="500"+getString(R.string.m);
		}else if(spf.getInt("td2", 0)==2){
			fg=0.008;
			teg=getString(R.string.nfg)+"800"+getString(R.string.mno)+getString(R.string.att);
			gl="800"+getString(R.string.m);
		}else if(spf.getInt("td2", 0)==3){
			fg=0.01;
			teg=getString(R.string.nfg)+"1"+getString(R.string.kno)+getString(R.string.att);
			gl="1"+getString(R.string.k);
		}else if(spf.getInt("td2", 0)==4){
			if(spf.getInt("gz2", 0)==0){
				fg=Double.valueOf(spf.getString("gzg2","0"))*0.00001;
				gl=getString(R.string.m);
			}else if(spf.getInt("gz2", 0)==1){
				fg=Double.valueOf(spf.getString("gzg2","0"))*0.01;
				gl=getString(R.string.k);
			}
			teg=getString(R.string.nfg)+spf.getString("gzg2","0")+gl+getString(R.string.no)+getString(R.string.att);
		}
		if(spf.getInt("td2", 0)==4){
			setTitle(getString(R.string.fgatt)+"("+spf.getString("gzg2","0")+gl+")");
		}else{
			setTitle(getString(R.string.fgatt)+"("+gl+")");
		}
		tv.setVisibility(View.VISIBLE);
    	listview.setVisibility(View.INVISIBLE);
		tv.setText(teg);
		if(spf.getInt("text", 0)==0){
			size=15;
		}else if(spf.getInt("text", 0)==1){
			size=20;
		}else if(spf.getInt("text", 0)==2){
			size=25;
		}else if(spf.getInt("text", 0)==3){
			size=30;
		}
		tgtk.setTextSize(size);
		tv.setTextSize(size);
		xy=new ArrayList<Integer>();
		tiem2 = new ArrayList<HashMap>();
		lv=new Listview2(Fthn.this, tiem2);
		listview.setOnItemClickListener(new ListViewClickListener());
		listview.setAdapter(lv);
	}	
	class ListViewClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> parent,
				View view,
				int position,
				long id) {
			Intent intent = new Intent();
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			intent.setClass(getApplicationContext(), Ghth.class);
			intent.putExtra("SELECTED_GREETING",xy.get(position));
			Fthn.this.startActivity(intent);
		}
	}
}

	
