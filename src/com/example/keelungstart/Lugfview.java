package com.example.keelungstart;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
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
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class Lugfview extends Activity {
	int[][] XY;
	private SQLiteDatabase mydb=null;
	private final static String ID="_id";
	private final static String gte_NAME="Sfgtsql.pref";
	private final static String SQL_NAME="Sfgtsql.db";//SQL??�稱
	String LUGF_NAME,SqlItemID;//行�?��?��?�表??�稱
	private final static String LUGF2_NAME="Lugf2";
	private final static String SFGTB_NAME="Sfgtbname";
	private final static String PH="ph";//資�?��?�目:行�?��?��?�編???
	private final static String PF="pf";//資�?��?�目:行�?��?��?�判?��
	private final static String MC="mc";
	private final static String GT="gt";
	private final static String GEVI2="gevi2";
	private final static String GDGPSX="gdgpsx";//資�?��?�目:終�?�座標X
	private final static String GDGPSY="gdgpsy";//資�?��?�目:終�?�座標Y
	private final static String NAME="name";//資�?��?�目:?��點�?�稱
    protected static final int MENU_BUTTON_1 = Menu.FIRST;
	protected static final int MENU_BUTTON_2 = Menu.FIRST + 1;
	double gpsx,gpsy,fg;
	double ch[][];
	Listview lv;
	SoundPool sound;
	int SfgtQuantity,mc,gt,size,sqlitem,ButtonSound;
	LocationManager lm;								//�???�LocationManager
	LocationListener ll;
	Map map;
	private ArrayList<HashMap> tiem2;
	private GpsStatus.Listener statusListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lugfview); 
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		map=new Map(this);
	    System.gc();
	    sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		ButtonSound = sound.load(this, R.raw.button , 1);
	    Intent intent = this.getIntent();
	    setResult(RESULT_OK, intent);
		Bundle be = intent.getExtras();
		LUGF_NAME=be.getString("sqlname");
		sqlitem=be.getInt("item");
		setTitle(be.getString("name"));
		TextView tv=(TextView)findViewById(R.id.textView1);
		final TextView tv2=(TextView)findViewById(R.id.textView2);
		size=(spf.getInt("text", 0)==0)?15:
			(spf.getInt("text", 0)==1)?20:
				(spf.getInt("text", 0)==2)?25:30;
		tv.setTextSize(size);
		tv2.setTextSize(size);
		TabHost th=(TabHost)findViewById(R.id.tabhost);
		th.setup();
		th.addTab(th.newTabSpec("lugf").setContent(R.id.tab1).setIndicator(getString(R.string.Itinerary_List)));
		th.addTab(th.newTabSpec("map").setContent(R.id.tab2).setIndicator(getString(R.string.map)));
		TabWidget tb=th.getTabWidget();
		((TextView) tb.getChildAt(0).findViewById(android.R.id.title)).setTextSize(size);
		((TextView) tb.getChildAt(1).findViewById(android.R.id.title)).setTextSize(size);
		lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		lm.addGpsStatusListener(statusListener);
		ll=new LocationListener()
		{
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				tv2.setText(getString(R.string.gps4));
				gpsx=location.getLatitude();				
				gpsy=location.getLongitude();
				if(SfgtQuantity!=0){
					if(mc==1&&gt<ch.length){
						if(Math.pow(ch[gt][0]-gpsx, 2)+Math.pow(ch[gt][1]-gpsy, 2)<=Math.pow(fg, 2)){
							Toast.makeText(getApplicationContext(),getString(R.string.gps10)+"\""+(CharSequence) tiem2.get(gt).get("name")+"\"", Toast.LENGTH_LONG).show();
							mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
					    	ContentValues cv=new ContentValues();
					    	String whereClause="_id=?";
					    	gt++;
					    	cv.put(GT, gt);
				 			String[] whereArgs={SqlItemID};
				 			mydb.update(LUGF2_NAME, cv, whereClause, whereArgs);
				 			mydb.close();
							sql(true);
						}
					}
				}
			}
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				tv2.setText(getString(R.string.gps5));
			}
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				tv2.setText(getString(R.string.gps));
			}
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
			}
		};
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
		sql(false);
	}
	public void sql(boolean notOne){
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		TextView tv=(TextView)findViewById(R.id.textView1);
		TextView tv2=(TextView)findViewById(R.id.textView2);
		ListView listview = (ListView) findViewById(R.id.listViewlugf2);
		tiem2 = new ArrayList<HashMap>();
		HashMap hh;
		mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
		try
		{
			Cursor cur=mydb.query(LUGF2_NAME, new String[] {ID,MC,GT}, null, null, null, null, null);
            cur.moveToPosition(sqlitem);
            SqlItemID=cur.getString(0);
            mc=cur.getInt(1);
            gt=cur.getInt(2);
            if(mc==0){
            	tv2.setVisibility(View.GONE); 
            }else{
            	tv2.setVisibility(View.VISIBLE);
            }
			cur=mydb.query(LUGF_NAME, new String[] {NAME,PH,PF,GDGPSX,GDGPSY}, null, null, null, null, null);
			Cursor cur2=mydb.query(SFGTB_NAME, new String[] {GEVI2}, null, null, null, null, null);
			SfgtQuantity=cur.getCount();
            if(SfgtQuantity==0){
            	tv.setVisibility(View.VISIBLE);
            	listview.setVisibility(View.INVISIBLE);
            }else{
            	tv.setVisibility(View.INVISIBLE); 
            	listview.setVisibility(View.VISIBLE);
            	XY=new int[SfgtQuantity][2];
            	ch=new double[SfgtQuantity][2];
            	for(int i=0;i<SfgtQuantity;i++){
                	hh = new HashMap();
                	cur.moveToPosition(i);
            		hh.put("id", i);
            		hh.put("name", (i+1)+"."+cur.getString(0));
            		hh.put("pf", 0);
            		hh.put("pf2", cur.getInt(2));
            		hh.put("pf3", cur.getInt(1));
            		if(cur.getInt(2)==1){
            			cur2.moveToPosition(cur.getInt(1));
            			hh.put("tp2", cur2.getString(0));
            		}
            		if(mc==0){
                		hh.put("mc", 0);
            		}else{
                		hh.put("mc", 1);
                		if(i<gt){
                    		hh.put("tp", 2);
                		}else if(i==gt){
                			hh.put("tp", 1);
                		}else if(i>gt){
                			hh.put("tp", 0);
                		}
            		}
            		hh.put("tv", size);
            		ch[i][0]=cur.getDouble(3);
            		ch[i][1]=cur.getDouble(4);
            		hh.put("gx", ch[i][0]);
            		hh.put("gy", ch[i][1]);
                	tiem2.add(hh); 
                	XY[i][0]=cur.getInt(1);
                	XY[i][1]=cur.getInt(2);
                }
            }
			cur2.close();
			cur.close();
			if(notOne){
				lv.setListview(tiem2);
				lv.notifyDataSetChanged();
			}else{
				lv=new Listview(this, tiem2);
				listview.setOnItemClickListener(new ListViewClickListener());
				listview.setOnItemLongClickListener(new ListViewLongClickListener());
				listview.setAdapter(lv);
			}
			if(mc==1){
				if(spf.getInt("td3", 0)==0){
					fg=0.0001;
				}else if(spf.getInt("td3", 0)==1){
					fg=0.0005;
				}else if(spf.getInt("td3", 0)==2){
					fg=0.001;
				}else if(spf.getInt("td3", 0)==3){
					fg=0.0015;
				}else if(spf.getInt("td3", 0)==4){
					fg=Double.valueOf(spf.getString("gzg3","0"))*0.00001;
				}
			}
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
		}
		mydb.close();
	    System.gc();
	    map.addMark(tiem2);
	}
	class ListViewClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> parent,View view,int position,long id) {
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			Intent intent = new Intent();
			if(XY[position][1]==0){
				intent.setClass(getApplicationContext(), Gt.class);
				intent.putExtra("SELECTED_GREETING",XY[position][0]);
				Lugfview.this.startActivityForResult(intent,0);
			}else{
				intent.setClass(getApplicationContext(), Sfgt2.class);
				intent.putExtra("SELECTED_GREETING",XY[position][0]);
				Lugfview.this.startActivityForResult(intent,0);
			}
		}
	}
	class ListViewLongClickListener implements OnItemLongClickListener{
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			if(mc==1){
				Vibrator myVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
				myVibrator.vibrate(50);
				AlertDialog.Builder builder = new AlertDialog.Builder(Lugfview.this);
				mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
				Cursor cur=mydb.query(LUGF_NAME, new String[] {NAME}, null, null, null, null, null);
				cur.moveToPosition(arg2);
				builder.setTitle(getString(R.string.con));
		    	builder.setMessage(getString(R.string.lugf10)+getString(R.string.lugf11)+"\""+cur.getString(0)+"\"?");
				cur.close();
				mydb.close();
		    	Gn de = new Gn(arg2);
		    	builder.setNegativeButton(getString(R.string.sfgt1), de);
		    	builder.setPositiveButton(getString(R.string.lugf11), de);
		    	builder.show();
		    	return true;
			}
			return false;
		}
	}
	private class Gn implements DialogInterface.OnClickListener{
		int arg2;
		public Gn(int arg2){
			this.arg2=arg2;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			switch(which){
			case DialogInterface.BUTTON_NEGATIVE:
				dialog.dismiss();
				break;
			case DialogInterface.BUTTON_POSITIVE:
				mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
		    	ContentValues cv=new ContentValues();
		    	String whereClause="_id=?";
		    	cv.put(GT, arg2);
	 			String[] whereArgs={SqlItemID};
	 			mydb.update(LUGF2_NAME, cv, whereClause, whereArgs);
	 			mydb.close();
			    sql(true);
				break;
			}
		}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		sql(true);
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		if(mc==0){
			menu.add(0, MENU_BUTTON_1, 0, getString(R.string.sta)+getString(R.string.lugf7));
		}else{
			menu.add(0, MENU_BUTTON_1, 0, getString(R.string.end)+getString(R.string.lugf7));
		}
        menu.add(0, MENU_BUTTON_2, 0, getString(R.string.menu3)+getString(R.string.lugf7));
	    return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
    	int item_id = item.getItemId();
    	TextView tv2=(TextView)findViewById(R.id.textView2);
    	if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
			sound.play(ButtonSound, 1, 1, 0, 0, 1);
		}
    	switch (item_id){
    	    case MENU_BUTTON_1:
    	    	mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
    	    	 ContentValues cv=new ContentValues();
    	    	if(item.getTitle().equals(getString(R.string.sta)+getString(R.string.lugf7)))
                {
                        item.setTitle(getString(R.string.end)+getString(R.string.lugf7));
                        tv2.setVisibility(View.VISIBLE);
                        cv.put(MC, 1);
                        mc=1;
                        Toast.makeText(getApplicationContext(), getString(R.string.sta)+getString(R.string.lugf7), Toast.LENGTH_SHORT).show();
                }
                else
                {
                        item.setTitle(getString(R.string.sta)+getString(R.string.lugf7));
                        tv2.setVisibility(View.GONE); 
                        cv.put(MC, 0);
                        mc=0;
                        Toast.makeText(getApplicationContext(), getString(R.string.end)+getString(R.string.lugf7), Toast.LENGTH_SHORT).show();
                }
    	    	String whereClause="_id=?";
    			String[] whereArgs={SqlItemID};
    			mydb.update(LUGF2_NAME, cv, whereClause, whereArgs);
    			mydb.close();
    			sql(false);
			break;
    		case MENU_BUTTON_2:
    			Intent intent =new Intent(Lugfview.this, Lugf2.class);
    			Bundle bundle = new Bundle();
    			bundle.putString("sqlname",LUGF_NAME);
    			bundle.putInt("item",sqlitem);
    			bundle.putString("sqlitemid",SqlItemID);
    			intent.putExtras(bundle);
    			Lugfview.this.startActivityForResult(intent,0);
    			break;
    		default: return false;
    	}
    	return true;
    }
}
