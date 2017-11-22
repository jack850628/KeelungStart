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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Lugfgd extends Activity {
	LocationManager lm;								
	LocationListener ll;
	private GpsStatus.Listener statusListener;
	double x1,x11,x2,y1,y11,y2,jfx,jfy,g1,ix,iy,tf;
	String[][] sql;
	String[] gts;//景點名稱
	double[][] gtd;//景點座標
	double[][] tfg;//起到景&景到終
	String[] sqlgts;
	double[][] sqlgtd;
	String gt;//終點名
	int j2,i2;
	int glg;//終點編號
	int pf2;//景點類型
	String in;
	int ph,phna,gs,gt2,hn;
	ArrayList<Gtgs> end =new ArrayList<Gtgs>();
	ArrayList<Phpf> pf;//景點類型
	boolean ch=false,ch2=false,sfgt=true,st=false;
	boolean sfgtpf=false;//判斷下一個景點是否是私房景點
	private Spinner sp2;
	private Spinner sp3;
	private SQLiteDatabase mydb=null;
	private final static String ID="_id";
	private final static String SQL_NAME="Sfgtsql.db";//SQL名稱
	private final static String gte_NAME="Sfgtsql.pref";
	private final static String SFGTB_NAME="Sfgtbname";//表名稱
	private final static String GPSX="gpsx";//資料項目:GPSX座標
	private final static String GPSY="gpsy";//資料項目:GPSY座標
	String LUGF_NAME="Lugf",SqlItemID;//行程規劃表名稱
	private final static String LUGF2_NAME="Lugf2";
	private final static String PH="ph";//資料項目:行程規劃編號
	private final static String PF="pf";//資料項目:行程規劃判別
	private final static String GT="gt";
	private final static String GEVI2="gevi2";
	private final static String NAME="name";//資料項目:景點名稱
	private final static String GDGPSX="gdgpsx";//資料項目:終點座標X
	private final static String GDGPSY="gdgpsy";//資料項目:終點座標Y
	int size,sqlitem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lugfgd); 
		setTitle(getString(R.string.hz)+getString(R.string.lugf7));
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
		if(spf.getInt("text", 0)==0){
			size=15;
			 tv.setTextSize(15);
			 tv1.setTextSize(15);
			 tv2.setTextSize(15);
			 tv3.setTextSize(15);
		}else if(spf.getInt("text", 0)==1){
			size=20;
			tv.setTextSize(20);
			tv1.setTextSize(20);
			tv2.setTextSize(20);
			tv3.setTextSize(20);
		}else if(spf.getInt("text", 0)==2){
			size=25;
			tv.setTextSize(25);
			tv1.setTextSize(25);
			tv2.setTextSize(25);
			tv3.setTextSize(25);
		}else if(spf.getInt("text", 0)==3){
			size=30;
			tv.setTextSize(30);
			tv1.setTextSize(30);
			tv2.setTextSize(30);
			tv3.setTextSize(30);
		}
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();	//取得Bundle
		in=bundle.getString("SELECTED_GREETING");
		phna=Integer.valueOf(in);
		LUGF_NAME=bundle.getString("sqlname");
		sqlitem=bundle.getInt("item");
		SqlItemID=bundle.getString("sqlitemid");
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
        gt2=cur.getInt(0);
		cur.close();
		mydb.close();
		
		sp3 = (Spinner) findViewById(R.id.gtent2);
		this.getApplicationContext();
		sp3.setVisibility(View.GONE);
		sp3.setAdapter(new Listview(Lugfgd.this, tiem2));
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
		TextView gd = (TextView) findViewById(R.id.textView1);
		Button butgf = (Button) findViewById(R.id.butgf);
		butgf.setOnClickListener(new ButgfClickListener());
		butgf.setTextSize(size);
		Button butfa = (Button) findViewById(R.id.button1);
		butfa.setOnClickListener(new ButtonClickListener());
		butfa.setTextSize(size);
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
        sp2 = (Spinner) findViewById(R.id.gtent);
        sp2.setAdapter(new Listview(Lugfgd.this, tiem2));
        ImageView iv=(ImageView)findViewById(R.id.imageView1);
		mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
		cur=mydb.query(LUGF_NAME, new String[] {ID,NAME,PH,PF,GDGPSX,GDGPSY}, null, null, null, null, null);
		Cursor cur2=mydb.query(SFGTB_NAME, new String[] {GEVI2}, null, null, null, null, null);
        j2=cur.getCount();
	    i2=phna;
		if(i2==j2){
            cur.moveToPosition(j2-1);
        	gd.setText(cur.getString(1));
        	if(cur.getInt(3)==0){
        		iv.setImageResource(new Gtivtext().getiv2()[cur.getInt(2)]);
        	}else{
        		cur2.moveToPosition(cur.getInt(2));
        		Bitmap bi =BitmapFactory.decodeFile(cur2.getString(0));
            	iv.setImageBitmap(bi);
        	}
        	x11=cur.getDouble(4);
        	y11=cur.getDouble(5);
        	ch2=true;
		}else{
            cur.moveToPosition(i2-1);
        	gd.setText(cur.getString(1));
        	if(cur.getInt(3)==0){
        		iv.setImageResource(new Gtivtext().getiv2()[cur.getInt(2)]);
        	}else{
        		cur2.moveToPosition(cur.getInt(2));
        		Bitmap bi =BitmapFactory.decodeFile(cur2.getString(0));
            	iv.setImageBitmap(bi);
        	}
        	x11=cur.getDouble(4);
        	y11=cur.getDouble(5);
        	cur.moveToPosition(i2);
        	gs=cur.getInt(2);
        	if(cur.getInt(3)==0){
            	sp2.setSelection(gs);
        	}else{
        		st=true;
        		sfgtpf=true;
        		sp2.setSelection(gts.length);
        	}
		}
		cur2.close();
		cur.close();
		mydb.close();
		this.getApplicationContext();

        sp2.setOnItemSelectedListener(new OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int po, long arg3) {
            	if(po<gts.length){
                	sp3.setVisibility(View.GONE);
                	x2=gtd[po][0];
               	   y2=gtd[po][1];
               	   gt=gts[po];
               	   glg=po;
               	   pf2=0;
           		sfgt=true;
          	   if(ch2){
          		   ch=true;
          	   }else{
          		 if(gs==po&&!sfgtpf){
            		  ch=false;
            	  }else{
            		  ch=true;
            	  }
          	   }
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
                   	   pf2=1;
                   	 sp3.setVisibility(View.VISIBLE);
               	     sp3.setSelection((sfgtpf)?gs:0);
                	}else{
                		sfgt=false;
                	}
             		cur.close();
             		mydb.close();
             		 if(ch2){
                		 ch=true;
                	 }else{
                		 if(gs==0&&sfgtpf){
                  		  ch=false;
                  	     }else{
                  		  ch=true;
                  	     }
                     }
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
                  	  x2=cur.getDouble(1);
                 	   y2=cur.getDouble(2);
                 	   gt=cur.getString(0);
                 	   glg=po;
                		cur.close();
                		mydb.close();
                		 if(ch2){
                    		 ch=true;
                    	 }else{
                    		 if(gs==po&&sfgtpf){
                      		  ch=false;
                      	     }else{
                      		  ch=true;
                      	     }
                         }
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
			if(sfgt){
				SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
				try
				{
					Cursor cur=mydb.query(LUGF_NAME, new String[] {ID,NAME,PH,GDGPSX,GDGPSY}, null, null, null, null, null);
		            hn=cur.getCount();
					cur=mydb.query(SFGTB_NAME, new String[] {NAME,GPSX,GPSY}, null, null, null, null, null);
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
				x1=x11;
				y1=y11;
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
						if(ch){
							end.add(new Gtgs(gt,0.0,0.0,glg,pf2));
						}
						break;
					}
				}
				mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
				int gs=0;
				if(i2==j2){
					for(int i=0;i<end.size();i++){
						ContentValues cv=new ContentValues();
						if(end.get(i).getgkn()==gt){
							cv.put(NAME,gt);
						    cv.put(PH,end.get(i).getxy());
						    cv.put(PF,pf2);
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
						gs++;
					}
				}else{
					Cursor cur=mydb.query(LUGF_NAME, new String[] {ID,NAME,PH,PF,GDGPSX,GDGPSY}, null, null, null, null, null);
					 int ii=cur.getCount();
					 sql=new String[ii-i2][5];
					 int f=sql.length-1;
					 for(int i=ii;i>i2;i--){
				            cur.moveToPosition(i-1);
				            String id=cur.getString(0);
				            sql[f][0]=cur.getString(1);
				            sql[f][1]=cur.getString(2);
				            sql[f][2]=cur.getString(3);
				            sql[f][3]=cur.getString(4);
				            sql[f][4]=cur.getString(5);
						    String whereClause="_id=?";
							String[] whereArgs={id};
							mydb.delete(LUGF_NAME, whereClause, whereArgs);
							f--;
					 }
					ContentValues cv=new ContentValues();
					for(int i=0;i<end.size();i++){
						if(end.get(i).getgkn()==gt){
							cv.put(NAME,gt);
						    cv.put(PH,end.get(i).getxy());
						    cv.put(PF,end.get(i).getpf());
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
						gs++;
					}
					 for(int j=0;j<sql.length;j++){
							cv.put(NAME,sql[j][0]);
						    cv.put(PH,sql[j][1]);
						    cv.put(PF,sql[j][2]);
						    cv.put(GDGPSX,sql[j][3]);
						    cv.put(GDGPSY,sql[j][4]);
						    mydb.insert(LUGF_NAME, null, cv);
						}
					cur.close();
				}
				if(phna<=gt2&&phna<hn){
					gt2+=gs;
			    	ContentValues cv=new ContentValues();
			    	String whereClause="_id=?";
			    	cv.put(GT, gt2);
		 			String[] whereArgs={SqlItemID};
		 			mydb.update(LUGF2_NAME, cv, whereClause, whereArgs);
				}
				mydb.close();
		        finish();
			}else{
        		Toast.makeText(getApplicationContext(), getString(R.string.lugf9), Toast.LENGTH_LONG).show();
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
