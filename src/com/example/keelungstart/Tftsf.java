package com.example.keelungstart;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Tftsf extends Activity {

	private SQLiteDatabase mydb=null;
	private final static String SQL_NAME="Sfgtsql.db";//SQL名稱
	private final static String THHN_NAME="Thhnname";//停車場表名稱
	private final static String NAME="name";//資料項目:景點名稱
	private final static String GPSX="gpsx";//資料項目:GPSX座標
	private final static String GPSY="gpsy";//資料項目:GPSY座標
	double gpsx,gpsy,fg;
	LocationManager lm;								//宣告LocationManager
	LocationListener ll;
	private GpsStatus.Listener statusListener;
    private final static String gte_NAME="Sfgtsql.pref";//自訂義名稱
	String teg,gl;
	ArrayList<Integer> start=new ArrayList<Integer>();
	ArrayList<String> item3=new ArrayList<String>();
	ArrayList<Sfgtxy> gpsxy=new ArrayList<Sfgtxy>();
	private ArrayList<HashMap> tiem2;
	Listview2 lv;
	HashMap hh;
	int size;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tftsf);
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		Intent intent = this.getIntent();
		setResult(RESULT_OK, intent);
		final TextView tgtk=(TextView)findViewById(R.id.tvgtk);
		TextView tv=(TextView)findViewById(R.id.textView1);
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
				start.clear();
				for(int i=0;i<item3.size();i++)
				{
					if(Math.pow(gpsxy.get(i).getx()-gpsx, 2)+Math.pow(gpsxy.get(i).gety()-gpsy, 2)<=Math.pow(fg, 2)){
						hh = new HashMap();
						hh.put("id", i);
	            		hh.put("name",item3.get(i));
	            		hh.put("tv", size);
	            		tiem2.add(hh); 
						start.add(i);
					}
				}
				if(start.size()!=0){
	            	tv.setVisibility(View.INVISIBLE); 
	            	listview.setVisibility(View.VISIBLE);
				}else{
					tv.setVisibility(View.VISIBLE);
	            	listview.setVisibility(View.INVISIBLE);
				}
				lv.setListview(tiem2);
				lv.notifyDataSetChanged();
			}
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				tgtk.setText(getString(R.string.gps5));
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
			teg=getString(R.string.nfg)+"300"+getString(R.string.mno)+getString(R.string.setatt);
			gl="300"+getString(R.string.m);
		}else if(spf.getInt("td2", 0)==1){
			fg=0.005;
			teg=getString(R.string.nfg)+"500"+getString(R.string.mno)+getString(R.string.setatt);
			gl="500"+getString(R.string.m);
		}else if(spf.getInt("td2", 0)==2){
			fg=0.008;
			teg=getString(R.string.nfg)+"800"+getString(R.string.mno)+getString(R.string.setatt);
			gl="800"+getString(R.string.m);
		}else if(spf.getInt("td2", 0)==3){
			fg=0.01;
			teg=getString(R.string.nfg)+"1"+getString(R.string.kno)+getString(R.string.setatt);
			gl="1"+getString(R.string.k);
		}else if(spf.getInt("td2", 0)==4){
			if(spf.getInt("gz2", 0)==0){
				fg=Double.valueOf(spf.getString("gzg2","0"))*0.00001;
				gl=getString(R.string.m);
			}else if(spf.getInt("gz2", 0)==1){
				fg=Double.valueOf(spf.getString("gzg2","0"))*0.01;
				gl=getString(R.string.k);
			}
			teg=getString(R.string.nfg)+spf.getString("gzg2","0")+gl+getString(R.string.no)+getString(R.string.setatt);
		}
		if(spf.getInt("td2", 0)==4){
			setTitle(getString(R.string.fgsetatt)+"("+spf.getString("gzg2","0")+gl+")");
		}else{
			setTitle(getString(R.string.fgsetatt)+"("+gl+")");
		}
		
		if(spf.getInt("text", 0)==0){
			size=15;
			tgtk.setTextSize(15);
			 tv.setTextSize(15);
		}else if(spf.getInt("text", 0)==1){
			size=20;
			tgtk.setTextSize(20);
			 tv.setTextSize(20);
		}else if(spf.getInt("text", 0)==2){
			size=25;
			tgtk.setTextSize(25);
			 tv.setTextSize(25);
		}else if(spf.getInt("text", 0)==3){
			size=30;
			tgtk.setTextSize(30);
			 tv.setTextSize(30);
		}
	    sql();
	}	
	public void sql(){
		ListView listview = (ListView) findViewById(R.id.listViewgtk);
		TextView tv=(TextView)findViewById(R.id.textView1);
		mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
		try
		{
			Cursor cur=mydb.query(THHN_NAME, new String[] {NAME,GPSX,GPSY}, null, null, null, null, null);
			int sln=cur.getCount();
			tv.setVisibility(View.VISIBLE);
        	listview.setVisibility(View.INVISIBLE);
			tv.setText(teg);
			if(sln!=0){
				item3.clear();
				gpsxy.clear();
				for(int i=0;i<sln;i++)
				{
					cur.moveToPosition(i);
					item3.add(cur.getString(0));
					gpsxy.add(new Sfgtxy(cur.getDouble(1),cur.getDouble(2)));
				}
			}
			cur.close();
			mydb.close();
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
		}
		listview.setOnItemClickListener(new ListViewClickListener());
		tiem2 = new ArrayList<HashMap>();
		lv=new Listview2(Tftsf.this, tiem2);
		listview.setAdapter(lv);
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
			intent.setClass(getApplicationContext(), Tsfgt2.class);
			Bundle bundle = new Bundle();
			bundle.putInt("SELECTED_GREETING",start.get(position));
			intent.putExtras(bundle);
			Tftsf.this.startActivityForResult(intent,0);
		}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		sql();
	}
}

	



