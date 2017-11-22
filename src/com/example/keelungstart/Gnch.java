package com.example.keelungstart;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Gnch extends Activity {
	LocationManager lm;								
	LocationListener ll;
	private GpsStatus.Listener statusListener;
	double gpsx,gpsy;
	String na,in;
	int ph,phna,gt,hn;
	int pf;//���I����
	String[] gts;//���I�W��
	double[][] gtd;//���I�y��
	private Spinner sp2;
	private Spinner sp3;
	private SQLiteDatabase mydb=null;
	private final static String ID="_id";
	private final static String SQL_NAME="Sfgtsql.db";//SQL�W��
	private final static String gte_NAME="Sfgtsql.pref";
	private final static String SFGTB_NAME="Sfgtbname";//��W��
	private final static String GPSX="gpsx";//��ƶ���:GPSX�y��
	private final static String GPSY="gpsy";//��ƶ���:GPSY�y��
	String LUGF_NAME="Lugf",SqlItemID;//��{�W����W��
	private final static String LUGF2_NAME="Lugf2";
	private final static String GT="gt";
	private final static String GEVI2="gevi2";
	private final static String PH="ph";//��ƶ���:��{�W���s��
	private final static String PF="pf";//��ƶ���:��{�W���P�O
	private final static String NAME="name";//��ƶ���:���I�W��
	private final static String GDGPSX="gdgpsx";//��ƶ���:���I�y��X
	private final static String GDGPSY="gdgpsy";//��ƶ���:���I�y��Y
	boolean sfgt=true,st=false;
	int size,sqlitem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gnch); 
		setTitle(getString(R.string.hz)+getString(R.string.gnch2));
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		Resources res=getResources();
		gts=res.getStringArray(R.array.gtent);//�qstrings.xml������I�W��
		gtd=new Gtgps().gps();//�촺�I�y��
		TextView tv1=(TextView)findViewById(R.id.textView2);
		if(spf.getInt("text", 0)==0){
			size=15;
			 tv1.setTextSize(15);
		}else if(spf.getInt("text", 0)==1){
			size=20;
			tv1.setTextSize(20);
		}else if(spf.getInt("text", 0)==2){
			size=25;
			tv1.setTextSize(25);
		}else if(spf.getInt("text", 0)==3){
			size=30;
			tv1.setTextSize(30);
		}
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();	//���oBundle
		in=bundle.getString("SELECTED_GREETING");
		LUGF_NAME=bundle.getString("sqlname");
		sqlitem=bundle.getInt("item");
		SqlItemID=bundle.getString("sqlitemid");
		phna=Integer.valueOf(in);
		setResult(0);
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
		mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
		Cursor cur=mydb.query(SFGTB_NAME, new String[] {NAME,GEVI2}, null, null, null, null, null);
        int j=cur.getCount();
        String sf[]=new String[j];
        ArrayList<HashMap> tiem2=new ArrayList<HashMap>();
        HashMap hh;
        for(int i=0;i<j;i++){
        	cur.moveToPosition(i);
        	sf[i]=cur.getString(0);
        	hh = new HashMap();
    		hh.put("id", i);
    		hh.put("name",cur.getString(0));
    		hh.put("tp", cur.getString(1));
    		hh.put("pf", 1);
    		hh.put("pf2", -1);
    		hh.put("tv", size);
    		tiem2.add(hh); 
        }
        cur=mydb.query(LUGF2_NAME, new String[] {GT}, null, null, null, null, null);
        cur.moveToPosition(sqlitem);
        gt=cur.getInt(0);
		cur.close();
		mydb.close();
		
		sp3 = (Spinner) findViewById(R.id.gtent2);
		this.getApplicationContext();
		sp3.setVisibility(View.GONE);
		sp3.setAdapter(new Listview(Gnch.this, tiem2));
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
		Button butgf = (Button) findViewById(R.id.butgf);
		butgf.setOnClickListener(new ButgfClickListener());
		butgf.setTextSize(size);
		Button butfa = (Button) findViewById(R.id.button1);
		butfa.setOnClickListener(new ButtonClickListener());
		butfa.setTextSize(size);
		tiem2=new ArrayList<HashMap>();
		String[] gt=res.getStringArray(R.array.gtegt2);
		for(int i=0;i<gt.length;i++){
        	hh = new HashMap();
        	hh.put("id", i);
    		hh.put("name",gt[i]);
    		hh.put("pf", 1);
    		hh.put("pf2", i);
    		hh.put("tv", size);
    		tiem2.add(hh); 
        }
        sp2 = (Spinner) findViewById(R.id.gtent);
        sp2.setAdapter(new Listview(Gnch.this, tiem2));
		this.getApplicationContext();

        sp2.setOnItemSelectedListener(new OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int po, long arg3) {
            	if(po<gts.length){
                	sp3.setVisibility(View.GONE);
                	gpsx=gtd[po][0];
              	   gpsy=gtd[po][1];
              	   na=gts[po];
              	   ph=po;
              	   pf=0;
           		sfgt=true;
            	}else{
            		mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
                	Cursor cur=mydb.query(SFGTB_NAME, new String[] {NAME,GPSX,GPSY}, null, null, null, null, null);
    	            int j=cur.getCount();
                   	if(j!=0){
                    	cur.moveToPosition(0);
                    	gpsx=cur.getDouble(1);
                   	   gpsy=cur.getDouble(2);
                   	   na=cur.getString(0);
                   	   ph=0;
                   	   pf=1;
                 	 sp3.setVisibility(View.VISIBLE);
             	     sp3.setSelection(0);
                   	}else{
                		sfgt=false;
                   	}
         		cur.close();
         		mydb.close();
            	}
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
               // TODO Auto-generated method stub
            }
        });
            sp3.setOnItemSelectedListener(new OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,int po, long arg3) {
                    if(st){
                		mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
                    	Cursor cur=mydb.query(SFGTB_NAME, new String[] {NAME,GPSX,GPSY}, null, null, null, null, null);
                    	cur.moveToPosition(po);
                    	gpsx=cur.getDouble(1);
                   	   gpsy=cur.getDouble(2);
                   	   na=cur.getString(0);
                   	   ph=po;
                		cur.close();
                		mydb.close();
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                   // TODO Auto-generated method stub
                }
            });
		st=true;
	}
	
	class ButgfClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
			if(sfgt){
				mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
				try
				{
					Cursor cur=mydb.query(LUGF_NAME, new String[] {ID,NAME,PH,PF,GDGPSX,GDGPSY}, null, null, null, null, null);
		            int j=hn=cur.getCount();
				    int i=phna;
				    if(j==i){
				    	ContentValues cv=new ContentValues();
			            cur.moveToPosition(j-1);
			            cv.put(NAME,na);
		 			    cv.put(PH,ph);
		 			    cv.put(PF,pf);
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
		            	String r;
			            for(j=cur.getCount()-1;j>i;j--){
			            	cur.moveToPosition(j);
			            	r=cur.getString(0);
			            	cur.moveToPosition(j-1);
			 	            cv.put(NAME,cur.getString(1));
			 			    cv.put(PH,cur.getString(2));
			 			    cv.put(PF,cur.getString(3));
			 			    cv.put(GDGPSX,cur.getString(4));
			 			    cv.put(GDGPSY,cur.getString(5));
			 			   String whereClause="_id=?";
			 				String[] whereArgs={r};
			 				mydb.update(LUGF_NAME, cv, whereClause, whereArgs);
			            }
			            cur.moveToPosition(j);
		            	r=cur.getString(0);
		 	            cv.put(NAME,na);
		 			    cv.put(PH,ph);
		 			    cv.put(PF,pf);
		 			    cv.put(GDGPSX,gpsx);
		 			    cv.put(GDGPSY,gpsy);
		 			   String whereClause="_id=?";
		 				String[] whereArgs={r};
		 				mydb.update(LUGF_NAME, cv, whereClause, whereArgs);
				    }
					cur.close();
					if(phna<=gt&&phna<hn){
						gt++;
				    	ContentValues cv=new ContentValues();
				    	String whereClause="_id=?";
				    	cv.put(GT, gt);
			 			String[] whereArgs={SqlItemID};
			 			mydb.update(LUGF2_NAME, cv, whereClause, whereArgs);
					}
					mydb.close();
				}
				catch(Exception e)
				{
					Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
				}
		        finish();
			}else{
	       		Toast.makeText(getApplicationContext(), getString(R.string.nosfgt)+"!!", Toast.LENGTH_LONG).show();
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
