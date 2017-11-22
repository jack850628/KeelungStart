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
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Ftsf extends Activity {

	private SQLiteDatabase mydb=null;
	private final static String SQL_NAME="Sfgtsql.db";//SQL名稱
	private final static String SFGTB_NAME="Sfgtbname";//表名稱
	private final static String NAME="name";//資料項目:景點名稱
	private final static String GPSX="gpsx";//資料項目:GPSX座標
	private final static String GPSY="gpsy";//資料項目:GPSY座標
	private final static String GEVI2="gevi2";
	double gpsx,gpsy,fg;
	LocationManager lm;								//宣告LocationManager
	LocationListener ll;
	SoundPool sound;
	private GpsStatus.Listener statusListener;
    private final static String gte_NAME="Sfgtsql.pref";//自訂義名稱
	String teg,gl;
	ArrayList<String> im=new ArrayList<String>(),item3=new ArrayList<String>();
	ArrayList<Integer> start=new ArrayList<Integer>();
	ArrayList<Sfgtxy> gpsxy=new ArrayList<Sfgtxy>();
	private ArrayList<HashMap> tiem2;
	ListView listview;
	Listview lv;
	HashMap hh;
	int size,ButtonSound;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ftsf);
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
	            		hh.put("tp", im.get(i));
	            		hh.put("pf", 1);
	            		hh.put("pf2", -1);
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
		if(spf.getInt("td", 0)==0){
			fg=0.003;
			teg=getString(R.string.nfg)+"300"+getString(R.string.mno)+getString(R.string.sfgt12);
			gl="300"+getString(R.string.m);
		}else if(spf.getInt("td", 0)==1){
			fg=0.005;
			teg=getString(R.string.nfg)+"500"+getString(R.string.mno)+getString(R.string.sfgt12);
			gl="500"+getString(R.string.m);
		}else if(spf.getInt("td", 0)==2){
			fg=0.008;
			teg=getString(R.string.nfg)+"800"+getString(R.string.mno)+getString(R.string.sfgt12);
			gl="800"+getString(R.string.m);
		}else if(spf.getInt("td", 0)==3){
			fg=0.01;
			teg=getString(R.string.nfg)+"1"+getString(R.string.kno)+getString(R.string.sfgt12);
			gl="1"+getString(R.string.k);
		}else if(spf.getInt("td", 0)==4){
			if(spf.getInt("gz", 0)==0){
				fg=Double.valueOf(spf.getString("gzg","0"))*0.00001;
				gl=getString(R.string.m);
			}else if(spf.getInt("gz", 0)==1){
				fg=Double.valueOf(spf.getString("gzg","0"))*0.01;
				gl=getString(R.string.k);
			}
			teg=getString(R.string.nfg)+spf.getString("gzg","0")+gl+getString(R.string.no)+getString(R.string.sfgt12);
		}
		if(spf.getInt("td", 0)==4){
			setTitle(getString(R.string.fgsfgt)+"("+spf.getString("gzg","0")+gl+")");
		}else{
			setTitle(getString(R.string.fgsfgt)+"("+gl+")");
		}
	
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
	sql(true);
	}	
	public void sql(boolean One){
		TextView tv=(TextView)findViewById(R.id.textView1);
		ListView listview = (ListView) findViewById(R.id.listViewgtk);
		mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
		try
		{
			Cursor cur=mydb.query(SFGTB_NAME, new String[] {NAME,GPSX,GPSY,GEVI2}, null, null, null, null, null);
			tv.setVisibility(View.VISIBLE);
	    	listview.setVisibility(View.INVISIBLE);
			tv.setText(teg);
			if(cur.getCount()!=0){
				im.clear();
				item3.clear();
				gpsxy.clear();
				for(int i=0;i<cur.getCount();i++)
				{
					cur.moveToPosition(i);
					item3.add(cur.getString(0));
					gpsxy.add(new Sfgtxy(cur.getDouble(1),cur.getDouble(2)));
					im.add(cur.getString(3));
				}
			}
			cur.close();
			mydb.close();
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
		}
		tiem2 = new ArrayList<HashMap>();
		if(One){
			listview.setOnItemClickListener(new ListViewClickListener());
			lv=new Listview(Ftsf.this, tiem2);
			listview.setAdapter(lv);
		}else{
			lv.setListview(tiem2);
			lv.notifyDataSetChanged();
		}
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
			intent.setClass(getApplicationContext(), Sfgt2.class);
			Bundle bundle = new Bundle();
			bundle.putInt("SELECTED_GREETING",start.get(position));
			intent.putExtras(bundle);
			Ftsf.this.startActivityForResult(intent,0);
		}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		sql(false);
	}
}

	



