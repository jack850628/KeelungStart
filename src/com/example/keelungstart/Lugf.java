package com.example.keelungstart;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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

public class Lugf extends Activity {
	LocationManager lm;								
	LocationListener ll;
	private GpsStatus.Listener statusListener;
	double latitude;
	double longitude;
	double x1,x2,y1,y2,jfx,jfy,g1,ix,iy,tf;
	String[] gts;//景點名稱
	double[][] gtd;//景點座標
	double[][] tfg;//起到景&景到終
	String[] sqlgts;
	double[][] sqlgtd;
	String gt,stna;//終點名&起點名
	int glg;//終點編號
	int stph;
	int gpstrue;//判斷GPS是否被選擇
	int pf2[][]=new int[1][2];//景點類型
	ArrayList<Gtgs> end =new ArrayList<Gtgs>();
	ArrayList<Phpf> pf;//景點類型
	private Spinner sp;
	private Spinner sp2;
	private Spinner sp3;
	private Spinner sp4;
	private SQLiteDatabase mydb=null;
	private final static String SQL_NAME="Sfgtsql.db";//SQL名稱
	private final static String gte_NAME="Sfgtsql.pref";
	private final static String SFGTB_NAME="Sfgtbname";//表名稱
	private final static String GPSX="gpsx";//資料項目:GPSX座標
	private final static String GPSY="gpsy";//資料項目:GPSY座標
	String LUGF_NAME="Lugf",SqlItemID;//行程規劃表名稱
	private final static String LUGF2_NAME="Lugf2";
	private final static String GT="gt";
	private final static String GEVI2="gevi2";
	private final static String PH="ph";//資料項目:行程規劃編號
	private final static String PF="pf";//資料項目:行程規劃判別
	private final static String NAME="name";//資料項目:景點名稱
	private final static String GDGPSX="gdgpsx";//資料項目:終點座標X
	private final static String GDGPSY="gdgpsy";//資料項目:終點座標Y
	boolean ch=false,sfgt=true,sfgt2=true,sql=false,st=false,gps=false;
	int size;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lugf); 
		setTitle(getString(R.string.lugf4)+getString(R.string.lugf7));
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
		gts=res.getStringArray(R.array.gtent);//從strings.xml抓取景點名稱
		gtd=new Gtgps().gps();//抓景點座標
		TextView tv=(TextView)findViewById(R.id.textView1);
		TextView tv1=(TextView)findViewById(R.id.textView2);
		TextView tv2=(TextView)findViewById(R.id.textView3);
		TextView tv3=(TextView)findViewById(R.id.textView4);
		sp = (Spinner) findViewById(R.id.gtegt);
        sp2 = (Spinner) findViewById(R.id.gtent2);
		sp3 = (Spinner) findViewById(R.id.gtent3);
		sp4 = (Spinner) findViewById(R.id.gtent4);
		final TextView tvgp=(TextView)findViewById(R.id.tvlugf);
		if(spf.getInt("text", 0)==0){
			size=15;
			 tv.setTextSize(15);
			 tv1.setTextSize(15);
			 tv2.setTextSize(15);
			 tv3.setTextSize(15);
			 tvgp.setTextSize(15);
		}else if(spf.getInt("text", 0)==1){
			size=20;
			tv.setTextSize(20);
			tv1.setTextSize(20);
			tv2.setTextSize(20);
			tv3.setTextSize(20);
			tvgp.setTextSize(20);
		}else if(spf.getInt("text", 0)==2){
			size=25;
			tv.setTextSize(25);
			tv1.setTextSize(25);
			tv2.setTextSize(25);
			tv3.setTextSize(25);
			tvgp.setTextSize(25);
		}else if(spf.getInt("text", 0)==3){
			size=30;
			tv.setTextSize(30);
			tv1.setTextSize(30);
			tv2.setTextSize(30);
			tv3.setTextSize(30);
			tvgp.setTextSize(30);
		}
		Intent intent = this.getIntent();
		setResult(0);
		Bundle be = intent.getExtras();
		LUGF_NAME=be.getString("sqlname");
		SqlItemID=be.getString("sqlitemid");
		lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		lm.addGpsStatusListener(statusListener);
		ll=new LocationListener()
		{
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				latitude=location.getLatitude();
				longitude=location.getLongitude();
				tvgp.setText(getString(R.string.gps4));
				gps=true;
				if(gpstrue==1){
					x1=latitude;
					y1=longitude;
				}
			}
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				tvgp.setText(getString(R.string.gps5));
			}
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				tvgp.setText(getString(R.string.gps));
			}
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
			}
		};
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);

        ArrayList<HashMap> tiem2=new ArrayList<HashMap>();
        HashMap hh;
        
        mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
		Cursor cur=mydb.query(SFGTB_NAME, new String[] {NAME,GEVI2}, null, null, null, null, null);
        int j=cur.getCount();
        String sf[]=new String[j];
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
		cur.close();
		mydb.close();
		sp3.setAdapter(new Listview(Lugf.this, tiem2));
		sp4.setAdapter(new Listview(Lugf.this, tiem2));
        
        tiem2=new ArrayList<HashMap>();
        for(int i=0;i<res.getStringArray(R.array.gtegt).length;i++){
        	hh = new HashMap();
        	hh.put("id", i);
    		hh.put("name",res.getStringArray(R.array.gtegt)[i]);
    		hh.put("pf", 1);
    		hh.put("pf2", (i>2)?i-3:-1);
    		hh.put("tv", size);
    		tiem2.add(hh); 
        }
		sp.setAdapter(new Listview(Lugf.this, tiem2));
		
		tiem2=new ArrayList<HashMap>();
		for(int i=0;i<res.getStringArray(R.array.gtegt2).length;i++){
        	hh = new HashMap();
        	hh.put("id", i);
    		hh.put("name",res.getStringArray(R.array.gtegt2)[i]);
    		hh.put("pf", 1);
    		hh.put("pf2", i);
    		hh.put("tv", size);
    		tiem2.add(hh); 
        }
		sp2.setAdapter(new Listview(Lugf.this, tiem2));
        sp.setOnItemSelectedListener(new L1());
        sp2.setOnItemSelectedListener(new L2());
		sp3.setOnItemSelectedListener(new L3());
        sp4.setOnItemSelectedListener(new L4());
		Button butgf = (Button) findViewById(R.id.butgf);
		butgf.setOnClickListener(new ButgfClickListener());
		butgf.setTextSize(size);
		Button butfa = (Button) findViewById(R.id.button1);
		butfa.setOnClickListener(new ButtonClickListener());
		butfa.setTextSize(size);
	}
	
	class L1 implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int po, long id) {
			// TODO Auto-generated method stub
			switch(po){
            case 0:
         	   ch=true;
         	   sp3.setVisibility(View.GONE);
           		sfgt2=true;
           		sql=false;
         	   break;
            case 1:
         	   ch=false;
         	   sp3.setVisibility(View.GONE);
           		sfgt2=true;
           		sql=false;
         	   break;
            case 2:
         	   mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
            	Cursor cur=mydb.query(SFGTB_NAME, new String[] {NAME,GPSX,GPSY}, null, null, null, null, null);
	            int j=cur.getCount();
            	if(j!=0){
                	cur.moveToPosition(0);
                	stph=0;
                	stna=cur.getString(0);
                	x1=cur.getDouble(1);
              	y1=cur.getDouble(2);
             	sp3.setVisibility(View.VISIBLE);
             	sp3.setSelection(0);
             	ch=false;
            	}else{
         		sfgt2=false;
            	}
           		cur.close();
            		mydb.close();
            		pf2[0][0]=1;
           		sql=true;
         	   break;
            default:
         	   stph=po-3;
         	   stna=gts[po-3];
         	   x1=gtd[po-3][0];
         	   y1=gtd[po-3][1];
         	   ch=false;
         	   sp3.setVisibility(View.GONE);
        		sfgt2=true;
       		sql=true;
       		pf2[0][0]=0;
         	   break;
            }
			gpstrue=po;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class L2 implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int po, long id) {
			// TODO Auto-generated method stub
			if(po<gts.length){
             	x2=gtd[po][0];
          	   y2=gtd[po][1];
          	   gt=gts[po];
          	   glg=po;
          	   pf2[0][1]=0;
          	 sp4.setVisibility(View.GONE);
     		sfgt=true;
        	}else{
           	 mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
            	Cursor cur=mydb.query(SFGTB_NAME, new String[] {NAME,GPSX,GPSY}, null, null, null, null, null);
	            int j=cur.getCount();
            	if(j!=0){
                	cur.moveToPosition(0);
                	  x2=cur.getDouble(1);
               	   y2=cur.getDouble(2);
               	   gt=cur.getString(0);
               	   glg=0;
               	   pf2[0][1]=1;
               	 sp4.setVisibility(View.VISIBLE);
           	     sp4.setSelection(0);
            	}else{
            		sfgt=false;
            	}
     		cur.close();
     		mydb.close();
        	}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class L3 implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int po, long id) {
			// TODO Auto-generated method stub
    		if(st){
    			mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
            	Cursor cur=mydb.query(SFGTB_NAME, new String[] {NAME,GPSX,GPSY}, null, null, null, null, null);
            	cur.moveToPosition(po);
            	stph=po;
            	stna=cur.getString(0);
            	x1=cur.getDouble(1);
          	    y1=cur.getDouble(2);
        		cur.close();
        		mydb.close();
    		}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class L4 implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int po, long id) {
			// TODO Auto-generated method stub
     		if(st){
     			mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
             	Cursor cur=mydb.query(SFGTB_NAME, new String[] {NAME,GPSX,GPSY}, null, null, null, null, null);
             	cur.moveToPosition(po);
           	  x2=cur.getDouble(1);
          	   y2=cur.getDouble(2);
          	   gt=cur.getString(0);
          	   glg=po;
         		cur.close();
         		mydb.close();
     		}
     		/**應為不知道什麼原因，導致下拉是選單的監聽器初始化的順序是1,3,2,4
     		 * 所以設置一個boolean變數st來修正使L4的內碼不要在初始化時被最後觸動**/
     		st=true;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class ButgfClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
			
			mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
			try
			{
				Cursor cur=mydb.query(LUGF_NAME, new String[] {NAME,PH}, null, null, null, null, null);
                int j=cur.getCount();
                if(j!=0){
                	Resources res = getResources(); 
            		String[] lugf=res.getStringArray(R.array.lugf8);
                	AlertDialog.Builder builder = new AlertDialog.Builder(Lugf.this);
            		builder.setTitle(lugf[0]);
                	builder.setMessage(lugf[1]);
                	Delete de = new Delete();
                	builder.setNegativeButton(getString(R.string.sfgt1), de);
                	builder.setPositiveButton(getString(R.string.ca), de);
                	AlertDialog ad = builder.create();
                	ad.show(); 
                }else{
                	chgf();
                }
				cur.close();
				mydb.close();
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
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
			chgf();
	}
		}
	}
	public void chgf() {
		if(sfgt&&sfgt2){
			mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
			try
			{
				Cursor cur=mydb.query(SFGTB_NAME, new String[] {NAME,GPSX,GPSY}, null, null, null, null, null);
	            int j=cur.getCount();
	            sqlgts=new String[j];
	            sqlgtd=new double[j][2];
	            for(int sq=0;sq<j;sq++){
	            	cur.moveToPosition(sq);
	            	sqlgts[sq]=cur.getString(0);
	            	sqlgtd[sq][0]=cur.getDouble(1);
	            	sqlgtd[sq][1]=cur.getDouble(2);
	            }
				cur.close();
				mydb.close();
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
			}
			jfx=0;
			jfy=0;
			tfg=new double[gts.length+sqlgts.length][2];
			String na[]=new String[gts.length+sqlgts.length];
			double gp[][]=new double[gts.length+sqlgts.length][2];
			pf=new ArrayList<Phpf>();
			for(int i=0;i<na.length;i++){
				if(i<gts.length){
					na[i]=gts[i];
					gp[i][0]=gtd[i][0];
					gp[i][1]=gtd[i][1];
					pf.add(new Phpf(i,0));
				}else{
					na[i]=sqlgts[i-gts.length];
					gp[i][0]=sqlgtd[i-gts.length][0];
					gp[i][1]=sqlgtd[i-gts.length][1];
					pf.add(new Phpf(i-gts.length,1));
				}
			}
			if(!ch){
				if(gps||sql){
					if(sql){
						mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
						mydb.delete(LUGF_NAME, null, null);
						ContentValues cv=new ContentValues();
						cv.put(NAME,stna);
					    cv.put(PH,stph);
					    cv.put(PF,pf2[0][0]);
					    cv.put(GDGPSX,x1);
					    cv.put(GDGPSY,y1);
					    mydb.insert(LUGF_NAME, null, cv);
					    mydb.close();
					}
					gpstrue=-1;
					try
					{
				while(true){
					jfx=x1;
					jfy=y1;
					/**起到終**/
					ix=x1-x2;
					ix=ix*ix;
					iy=y1-y2;
					iy=iy*iy;
					tf=ix+iy;
					tf=Math.sqrt(tf);
					
					for(int i=0;i<na.length;i++){
						ix=x1-gp[i][0];
						ix=ix*ix;
						iy=y1-gp[i][1];
						iy=iy*iy;
						tfg[i][0]=ix+iy;
						tfg[i][0]=Math.sqrt(tfg[i][0]);
						
						ix=gp[i][0]-x2;
						ix=ix*ix;
						iy=gp[i][1]-y2;
						iy=iy*iy;
						tfg[i][1]=ix+iy;
						tfg[i][1]=Math.sqrt(tfg[i][1]);
					}
						
					/**比較**/
					Gtgs gtgs=null;
					int k=0;//次數記錄
					for(int i=0;i<na.length;i++){
						if (tfg[i][0]!=0&&tfg[i][1]!=0){
							if(tf>tfg[i][0]){
								if(tf>tfg[i][1]){
									if(k!=0){
										if(g1>=tfg[i][1]){
											x1=gp[i][0];
											y1=gp[i][1];
											g1=tfg[i][1];
											gtgs=new Gtgs(na[i],gp[i][0],gp[i][1],pf.get(i).getph(),pf.get(i).getpf());
											k++;
										}
									}else{
										x1=gp[i][0];
										y1=gp[i][1];
										g1=tfg[i][1];
										gtgs=new Gtgs(na[i],gp[i][0],gp[i][1],pf.get(i).getph(),pf.get(i).getpf());
										k++;
									}
								}
							}
						}
					}
					if(gtgs!=null){
					    end.add(gtgs);
					}
					/**最終比較**/
					if(x1==jfx&&y1==jfy){
						end.add(new Gtgs(gt,0.0,0.0,glg,pf2[0][1]));
						break;
					}
				}
					}
					catch(Exception e)
					{
						Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
					}
				
				mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
				if(!sql){
				    mydb.delete(LUGF_NAME, null, null);
				}
				for(int i=0;i<end.size();i++){
					ContentValues cv=new ContentValues();
					if(end.get(i).getgkn()==gt){
						cv.put(NAME,gt);
					    cv.put(PH,end.get(i).getxy());
					    cv.put(PF,pf2[0][1]);
					    cv.put(GDGPSX,x2);
					    cv.put(GDGPSY,y2);
					    mydb.insert(LUGF_NAME, null, cv);
					}else{
						cv.put(NAME,end.get(i).getgkn());
					    cv.put(PH,end.get(i).getxy());
					    cv.put(PF,end.get(i).getpf());
					    cv.put(GDGPSX,end.get(i).getgpsxy1());
					    cv.put(GDGPSY,end.get(i).getgpsxy2());
					    mydb.insert(LUGF_NAME, null, cv);
					}
				}
				ContentValues cv=new ContentValues();
		    	String whereClause="_id=?";
		    	cv.put(GT, 0);
	 			String[] whereArgs={SqlItemID};
	 			mydb.update(LUGF2_NAME, cv, whereClause, whereArgs);
	 			mydb.close();
		    	finish();
				}else{
					Toast.makeText(getApplicationContext(), getString(R.string.lugf8), Toast.LENGTH_LONG).show();
				}
			}else{
				mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
			    mydb.delete(LUGF_NAME, null, null);
				ContentValues cv=new ContentValues();
				cv.put(NAME,gt);
			    cv.put(PH,glg);
			    cv.put(PF,pf2[0][1]);
			    cv.put(GDGPSX,x2);
			    cv.put(GDGPSY,y2);
			    mydb.insert(LUGF_NAME, null, cv);
				cv=new ContentValues();
		    	String whereClause="_id=?";
		    	cv.put(GT, 0);
	 			String[] whereArgs={SqlItemID};
	 			mydb.update(LUGF2_NAME, cv, whereClause, whereArgs);
	 			mydb.close();
		    	finish();
			}
		}else{
       		Toast.makeText(getApplicationContext(), getString(R.string.lugf9), Toast.LENGTH_LONG).show();
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
