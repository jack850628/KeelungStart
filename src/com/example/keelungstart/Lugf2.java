package com.example.keelungstart;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class Lugf2 extends Activity {
	private SQLiteDatabase mydb=null;
	private final static String ID="_id";
	private final static String gte_NAME="Sfgtsql.pref";
	private final static String SQL_NAME="Sfgtsql.db";//SQL名稱
	private final static String GDGPSX="gdgpsx";//資料項目:終點座標X
	private final static String GDGPSY="gdgpsy";//資料項目:終點座標Y
	String LUGF_NAME="Lugf",SqlItemID;//行程規劃表名稱
	private final static String LUGF2_NAME="Lugf2";
	private final static String SFGTB_NAME="Sfgtbname";
	private final static String GEVI2="gevi2";
	private final static String PH="ph";//資料項目:行程規劃編號
	private final static String PF="pf";//資料項目:行程規劃判別
	private final static String GT="gt";
	private final static String NAME="name";//資料項目:景點名稱
	String ph,pf2,na,gpsy,gpsx;
	int i,j,phna,pf,gt,etag,size,sqlitem;
	boolean et=false;
	StringBuffer id;
	Listview lv;
	LocationManager lm;								//宣告LocationManager
	LocationListener ll;
	Map map;
	private ArrayList<HashMap> tiem2;
	private GpsStatus.Listener statusListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lugf2); 
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		setTitle(getString(R.string.menu3)+getString(R.string.lugf7));
		map=new Map(this);
	    System.gc();
	    Intent intent = this.getIntent();
		setResult(0);
		Bundle be = intent.getExtras();
		LUGF_NAME=be.getString("sqlname");
		sqlitem=be.getInt("item");
		SqlItemID=be.getString("sqlitemid");
		TextView tv=(TextView)findViewById(R.id.textView1);
		if(spf.getInt("text", 0)==0){
			 size=15;
			 tv.setTextSize(15);
		}else if(spf.getInt("text", 0)==1){
			size=20;
			tv.setTextSize(20);
		}else if(spf.getInt("text", 0)==2){
			size=25;
			tv.setTextSize(25);
		}else if(spf.getInt("text", 0)==3){
			size=30;
			tv.setTextSize(30);
		}
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
			}
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
			}
		};
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new ButtonClickListener());
		sql(false);
	}
	public void sql(boolean sc){
		TextView tv=(TextView)findViewById(R.id.textView1);
		ListView listview = (ListView) findViewById(R.id.listViewlugf2);
		tiem2 = new ArrayList<HashMap>();
		HashMap hh;
		mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
		try
		{
			Cursor cur=mydb.query(LUGF2_NAME, new String[] {GT}, null, null, null, null, null);
            cur.moveToPosition(sqlitem);
            gt=cur.getInt(0);//行程進度值
			cur=mydb.query(LUGF_NAME, new String[] {NAME,PH,PF,GDGPSX,GDGPSY}, null, null, null, null, null);
			Cursor cur2=mydb.query(SFGTB_NAME, new String[] {GEVI2}, null, null, null, null, null);
			j=cur.getCount();
            if(j==0){
            	tv.setVisibility(View.VISIBLE);
            	listview.setVisibility(View.INVISIBLE);
            }else{
            	tv.setVisibility(View.INVISIBLE); 
            	listview.setVisibility(View.VISIBLE);
            	for(i=0;i<j;i++){
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
            		hh.put("mc", 0);
            		hh.put("tv", size);
            		hh.put("gx", cur.getDouble(3));
            		hh.put("gy", cur.getDouble(4));
                	tiem2.add(hh); 
                }
            }
			cur2.close();
			cur.close();
			if(sc){
				lv.setListview(tiem2);
				lv.notifyDataSetChanged();
			}else{
				lv=new Listview(this, tiem2);
				listview.setOnItemClickListener(new ListViewClickListener());
				listview.setAdapter(lv);
			}
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
		}
	    System.gc();
	    map.addMark(tiem2);
	}
	class ListViewClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> parent,
				View view,
				int position,
				long id2) {
			if(et){
		    	etag=position;
				mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
				Cursor cur=mydb.query(LUGF_NAME, new String[] {NAME}, null, null, null, null, null);
				cur.moveToPosition(pf);
				String n1=cur.getString(0);
				cur.moveToPosition(etag);
				String n2=cur.getString(0);
				AlertDialog.Builder builder = new AlertDialog.Builder(Lugf2.this);
				builder.setTitle(getString(R.string.con));
		    	builder.setMessage(getString(R.string.lugf12)+"\""+n1+"\""+getString(R.string.lugf13)+"\""+n2+"\""+getString(R.string.lugf14)+"?");
				cur.close();
				mydb.close();
		    	Gnet de = new Gnet();
		    	builder.setNegativeButton(getString(R.string.lugf15), de);
		    	builder.setPositiveButton(getString(R.string.lugf16), de);
		    	builder.show();
			}else{
				Vibrator myVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
				myVibrator.vibrate(50);
				AlertDialog.Builder builder = new AlertDialog.Builder(Lugf2.this);
				builder.setTitle(getString(R.string.con));
		    	builder.setMessage(getString(R.string.lugf10)+"?");
		    	Gn de = new Gn();
		    	builder.setNegativeButton(getString(R.string.sfgt11), de);
		    	builder.setNeutralButton(getString(R.string.lugf17), de);
		    	builder.setPositiveButton(getString(R.string.hz)+getString(R.string.gt), de);
		    	builder.show();
		    	pf=position;
		    	mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
				try
				{
					Cursor cur=mydb.query(LUGF_NAME, new String[] {ID}, null, null, null, null, null);
					cur.moveToPosition(position);
					id=new StringBuffer(cur.getString(0));
					cur.close();
					mydb.close();
				}
				catch(Exception e)
				{
					Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
				}
			}
		}
	}
	private class Gnet implements
	android.content.DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
		if (which == DialogInterface.BUTTON_NEGATIVE){
			if(pf==gt){
				gt=etag;
				if(pf<etag){
			    	gt--;
			    }
			}else if(pf>gt&&gt>=etag){
		    	gt++;
		    }else if(pf<gt&&gt<etag){
		    	gt--;
		    }
			mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
	    	ContentValues cv=new ContentValues();
	    	String whereClause="_id=?";
	    	cv.put(GT, gt);
 			String[] whereArgs={SqlItemID};
 			mydb.update(LUGF2_NAME, cv, whereClause, whereArgs);
 			mydb.close();
			supet();
		}else if (which == DialogInterface.BUTTON_POSITIVE){
			etag++;
			if(pf==gt){
				gt=etag;
				if(pf<etag){
			    	gt--;
			    }
			}else if(pf>gt&&gt>=etag){
		    	gt++;
		    }else if(pf<gt&&gt<etag){
		    	gt--;
		    }
			mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
	    	ContentValues cv=new ContentValues();
	    	String whereClause="_id=?";
	    	cv.put(GT, gt);
 			String[] whereArgs={SqlItemID};
 			mydb.update(LUGF2_NAME, cv, whereClause, whereArgs);
 			mydb.close();
			supet();
	}
		}
	}
	public void supet(){
		Button button = (Button) findViewById(R.id.button1);
		mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
    	Cursor cur=mydb.query(LUGF_NAME, new String[] {ID,NAME,PH,PF,GDGPSX,GDGPSY}, null, null, null, null, null);
    	cur.moveToPosition(pf);
    	String in=cur.getString(0);
   	    na=cur.getString(1);
    	ph=cur.getString(2);
    	pf2=cur.getString(3);
    	gpsx=cur.getString(4);
   	    gpsy=cur.getString(5);
   	    String whereClet="_id=?";
	    String[] whereAet={in};
	    mydb.delete(LUGF_NAME, whereClet, whereAet);
		cur.close();
		mydb.close();
		mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
		cur=mydb.query(LUGF_NAME, new String[] {ID,NAME,PH,PF,GDGPSX,GDGPSY}, null, null, null, null, null);
        int j=cur.getCount();
	    int i=etag;
	    if(pf<etag){
	    	i--;
	    }
	    if(j==i){
	    	ContentValues cv=new ContentValues();
            cv.put(NAME,na);
			    cv.put(PH,ph);
			    cv.put(PF,pf2);
			    cv.put(GDGPSX,gpsx);
			    cv.put(GDGPSY,gpsy);
		    mydb.insert(LUGF_NAME, null, cv);
	    }else{
            ContentValues cv=new ContentValues();
            cur.moveToPosition(j-1);
            cv.put(NAME,cur.getString(1));
		    cv.put(PH,cur.getString(2));
		    cv.put(PF,cur.getString(3));
		    cv.put(GDGPSX,cur.getString(4));
		    cv.put(GDGPSY,cur.getString(5));
		    mydb.insert(LUGF_NAME, null, cv);
		    StringBuffer r;
            for(j=cur.getCount()-1;j>i;j--){
            	cur.moveToPosition(j);
            	r=new StringBuffer(cur.getString(0));
            	cur.moveToPosition(j-1);
 	            cv.put(NAME,cur.getString(1));
 			    cv.put(PH,cur.getString(2));
 			    cv.put(PF,cur.getString(3));
 			    cv.put(GDGPSX,cur.getString(4));
 			    cv.put(GDGPSY,cur.getString(5));
 			    String whereClause="_id=?";
 				String[] whereArgs={r.toString()};
 				mydb.update(LUGF_NAME, cv, whereClause, whereArgs);
            }
            cur.moveToPosition(j);
            r=new StringBuffer(cur.getString(0));
	            cv.put(NAME,na);
			    cv.put(PH,ph);
			    cv.put(PF,pf2);
			    cv.put(GDGPSX,gpsx);
			    cv.put(GDGPSY,gpsy);
			    String whereClause="_id=?";
				String[] whereArgs={r.toString()};
				mydb.update(LUGF_NAME, cv, whereClause, whereArgs);
	    }
		cur.close();
		mydb.close();
		button.setText(getString(R.string.ok));
    	et=false;
	    sql(true);
	}
	private class Gn implements
	android.content.DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
		if (which == DialogInterface.BUTTON_NEGATIVE){
			mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
			Cursor cur=mydb.query(LUGF_NAME, new String[] {NAME}, null, null, null, null, null);
			cur.moveToPosition(pf);
			AlertDialog.Builder builder = new AlertDialog.Builder(Lugf2.this);
			builder.setTitle(getString(R.string.con));
	    	builder.setMessage(getString(R.string.lugf18)+"\""+cur.getString(0)+"\""+getString(R.string.lugf19)+getString(R.string.sfgt11)+"?");
			cur.close();
			mydb.close();
	    	Gn3 de = new Gn3();
	    	builder.setNegativeButton(getString(R.string.sfgt1), de);
	    	builder.setPositiveButton(getString(R.string.ca), de);
	    	builder.show();
		}else if (which == DialogInterface.BUTTON_NEUTRAL){
			Button button = (Button) findViewById(R.id.button1);
			button.setText(getString(R.string.lugf20));
			et=true;
			Toast.makeText(getApplicationContext(), getString(R.string.lugf21), Toast.LENGTH_SHORT).show();
	    }else if (which == DialogInterface.BUTTON_POSITIVE){
			mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
			Cursor cur=mydb.query(LUGF_NAME, new String[] {NAME}, null, null, null, null, null);
			cur.moveToPosition(pf);
			AlertDialog.Builder builder = new AlertDialog.Builder(Lugf2.this);
			builder.setTitle(getString(R.string.con));
	    	builder.setMessage(getString(R.string.lugf22)+"\""+cur.getString(0)+"\""+getString(R.string.lugf14)+"?");
			cur.close();
			mydb.close();
	    	Gn2 de = new Gn2();
	    	builder.setNegativeButton(getString(R.string.lugf15), de);
	    	builder.setPositiveButton(getString(R.string.lugf16), de);
	    	builder.show();
	}
		}
	}
	private class Gn2 implements
	android.content.DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog, int which) {
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("music", 0)==0){
			MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
   			mp.start();
		}
		if (which == DialogInterface.BUTTON_NEGATIVE){
			if(pf==0){
				Intent intent =new Intent(Lugf2.this, Gnch.class);
				Bundle bundle = new Bundle();
				bundle.putString("SELECTED_GREETING",String.valueOf(pf));
				bundle.putInt("item",sqlitem);
    			bundle.putString("sqlname",LUGF_NAME);
    			bundle.putString("sqlitemid",SqlItemID);
				intent.putExtras(bundle);
				Lugf2.this.startActivityForResult(intent,0);
			}else{
				mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
				Cursor cur=mydb.query(LUGF_NAME, new String[] {NAME}, null, null, null, null, null);
				cur.moveToPosition(pf);
				AlertDialog.Builder builder = new AlertDialog.Builder(Lugf2.this);
				builder.setTitle(getString(R.string.con));
		    	builder.setMessage(getString(R.string.lugf23)+"\""+cur.getString(0)+"\""+getString(R.string.lugf15)+getString(R.string.hz)+"?");
				cur.close();
				mydb.close();
		    	Gn4 de = new Gn4();
		    	builder.setNegativeButton(getString(R.string.gnch2), de);
		    	builder.setPositiveButton(getString(R.string.lugf7), de);
		    	builder.show();
			}
		}else if (which == DialogInterface.BUTTON_POSITIVE){
			mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
			Cursor cur=mydb.query(LUGF_NAME, new String[] {NAME}, null, null, null, null, null);
			cur.moveToPosition(pf);
			AlertDialog.Builder builder = new AlertDialog.Builder(Lugf2.this);
			builder.setTitle(getString(R.string.con));
	    	builder.setMessage(getString(R.string.lugf23)+"\""+cur.getString(0)+"\""+getString(R.string.lugf16)+getString(R.string.hz)+"?");
			cur.close();
			mydb.close();
	    	Gn5 de = new Gn5();
	    	builder.setNegativeButton(getString(R.string.gnch2), de);
	    	builder.setPositiveButton(getString(R.string.lugf7), de);
	    	builder.show();
	}
		}
	}
	private class Gn3 implements
	android.content.DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_NEGATIVE){
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
	        dialog.dismiss();
		}else if (which == DialogInterface.BUTTON_POSITIVE){
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
			mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
		    String whereClause="_id=?";
			String[] whereArgs={id.toString()};
			mydb.delete(LUGF_NAME, whereClause, whereArgs);
			if(pf<gt){
		    	ContentValues cv=new ContentValues();
		    	gt--;
	    		cv.put(GT, gt);
	 			String[] whereArgs2={SqlItemID};
	 			mydb.update(LUGF2_NAME, cv, whereClause, whereArgs2);
			}
		    mydb.close();
		    sql(true);
	}
		}
	}
	private class Gn4 implements
	android.content.DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_NEGATIVE){
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
			Intent intent =new Intent(Lugf2.this, Gnch.class);
			Bundle bundle = new Bundle();
			bundle.putString("SELECTED_GREETING",String.valueOf(pf));
			bundle.putInt("item",sqlitem);
			bundle.putString("sqlname",LUGF_NAME);
			bundle.putString("sqlitemid",SqlItemID);
			intent.putExtras(bundle);
			Lugf2.this.startActivityForResult(intent,0);
		}else if (which == DialogInterface.BUTTON_POSITIVE){
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
		    Intent intent =new Intent(Lugf2.this, Lugfgd.class);
			Bundle bundle = new Bundle();
			bundle.putString("SELECTED_GREETING",String.valueOf(pf));
			bundle.putInt("item",sqlitem);
			bundle.putString("sqlname",LUGF_NAME);
			bundle.putString("sqlitemid",SqlItemID);
			intent.putExtras(bundle);
			Lugf2.this.startActivityForResult(intent,0);
	}
		}
	}
	private class Gn5 implements
	android.content.DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_NEGATIVE){
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
			Intent intent =new Intent(Lugf2.this, Gnch.class);
			Bundle bundle = new Bundle();
			bundle.putString("SELECTED_GREETING",String.valueOf(pf+1));
			bundle.putInt("item",sqlitem);
			bundle.putString("sqlname",LUGF_NAME);
			bundle.putString("sqlitemid",SqlItemID);
			intent.putExtras(bundle);
			Lugf2.this.startActivityForResult(intent,0);
		}else if (which == DialogInterface.BUTTON_POSITIVE){
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
			Intent intent =new Intent(Lugf2.this, Lugfgd.class);
			Bundle bundle = new Bundle();
			bundle.putString("SELECTED_GREETING",String.valueOf(pf+1));
			bundle.putInt("item",sqlitem);
			bundle.putString("sqlname",LUGF_NAME);
			bundle.putString("sqlitemid",SqlItemID);
			intent.putExtras(bundle);
			Lugf2.this.startActivityForResult(intent,0);
	}
		}
	}
	private class Delete implements
	android.content.DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_NEGATIVE){
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
			 dialog.dismiss();
		}else if (which == DialogInterface.BUTTON_POSITIVE){
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
			mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
			mydb.delete(LUGF_NAME, null, null);
	    	ContentValues cv=new ContentValues();
	    	String whereClause="_id=?";
	    	cv.put(GT, 0);
 			String[] whereArgs={SqlItemID};
 			mydb.update(LUGF2_NAME, cv, whereClause, whereArgs);
			mydb.close();
		    sql(true);
	}
		}
	}
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.lugfmenu, menu);
	    return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
    	int item_id = item.getItemId();
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("music", 0)==0){
			MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
   			mp.start();
		}
    	switch (item_id){
    		case R.id.delete:
    			AlertDialog.Builder builder = new AlertDialog.Builder(this);
    			builder.setTitle(getString(R.string.con));
    	    	builder.setMessage(getString(R.string.lugf24));
    	    	Delete de = new Delete();
    	    	builder.setNegativeButton(getString(R.string.sfgt1), de);
    	    	builder.setPositiveButton(getString(R.string.ca), de);
    	    	AlertDialog ad = builder.create();
    	    	ad.show(); 
    			break;
    		case R.id.chgf:
    			Intent intent = new Intent ();
    		    intent.setClass(Lugf2.this,Lugf.class);
    		    Bundle bundle = new Bundle();
    			bundle.putString("sqlname",LUGF_NAME);
    			bundle.putString("sqlitemid",SqlItemID);
    			intent.putExtras(bundle);
    		    Lugf2.this.startActivityForResult(intent,0);
    			break;
    		default: return false;
    	}
    	return true;
    }
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		sql(true);
	}
	class ButtonClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
			Button button = (Button) findViewById(R.id.button1);
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
	    	if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
		    if(et){
		    	Toast.makeText(getApplicationContext(), getString(R.string.moveoff), Toast.LENGTH_SHORT).show();
		    	button.setText(getString(R.string.ok));
		    	et=false;
		    }else{
			    finish();
		    }
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
			Button button = (Button) findViewById(R.id.button1);
        	if(et){
        		Toast.makeText(getApplicationContext(), getString(R.string.moveoff), Toast.LENGTH_SHORT).show();
		    	button.setText(getString(R.string.ok));
		    	et=false;
		    }else{
			    finish();
		    }
        }
        return false;
    }
}
