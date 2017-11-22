package com.example.keelungstart;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Sfgt2 extends Activity {
	/** Called when the activity is first created. */
	LocationManager lm;								//宣告LocationManager
	LocationListener ll;
	private GpsStatus.Listener statusListener;
	SharedPreferences spf;
	SoundPool sound;
	Listview2 lv;
	double latitude;
	double longitude;
	private SQLiteDatabase mydb=null;
	private final static String SQL_NAME="Sfgtsql.db";//SQL名稱
	private final static String SFGTB_NAME="Sfgtbname";//表名稱
	private final static String GPSX="gpsx";//資料項目:GPSX座標
	private final static String GPSY="gpsy";//資料項目:GPSY座標
	String LUGF_NAME;//行程規劃表名稱
	private final static String ID="_id";
	private final static String SQLNAME="sqlname";
	private final static String LUGF2_NAME="Lugf2";
	private final static String GDGPSX="gdgpsx";//資料項目:終點座標X
	private final static String GDGPSY="gdgpsy";//資料項目:終點座標Y
	private final static String PH="ph";//資料項目:行程規劃編號
	private final static String PF="pf";//資料項目:行程規劃判別
	private final static String MC="mc";
	private final static String GT="gt";
	private final static String NAME="name";//資料項目:景點名稱
	private final static String GTMA="gtmn";//資料項目:景點內容
	private final static String GEVI="gevi";//資料項目:圖片
	private final static String NAME2="name2";//資料項目:交通資訊
	private final static String GTMA2="gtmn2";//資料項目:交通資訊內容
	private static final String MAP_URL = "file:///android_asset/googlemap.html";
	private WebView webView;
	double gpsx,gpsy;
	int i2,kg,size;
	Bitmap bi;
	
	private DrawerLayout mDrawerLayout = null;
	EditText et;
	TextView tv2;
	String ng[]=new String[50];//計算顯示值
	int nh[]=new int[49];//運算符號判別值
	double gh=0,mg=0,st=0;
	int i=0,j=0;
	boolean hn=true,fh=true,pf=true,pf2=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		// 取得螢幕顯示的資料
		int ScreenWidth = dm.widthPixels;
		int ScreenHeight = dm.heightPixels;
		spf =  getSharedPreferences("Sfgtsql.pref",0);
		sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		kg = sound.load(this, R.raw.button , 1);
		if(spf.getInt("nmfc", 0)==0){
			// 螢幕寬和高的Pixels
			if (ScreenHeight > ScreenWidth){
				if(ScreenWidth>=1280){
					setContentView(R.layout.sfgt2);
				}else{
					setContentView(R.layout.sfgt2w);
				}
			}
			else{
				setContentView(R.layout.sfgt2);
			}
		}else if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			 if(ScreenWidth>=1280){
					setContentView(R.layout.sfgt2);
				}else{
					setContentView(R.layout.sfgt2w);
				}
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.sfgt2);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		setupWebView();
		Intent intent = this.getIntent();
		setResult(0);
		i2=intent.getExtras().getInt("SELECTED_GREETING");
        final TextView tv=(TextView)findViewById(R.id.textgtksu5);
        TextView textView1 = (TextView) findViewById(R.id.textView1);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        TextView textView5 = (TextView) findViewById(R.id.textView5);
        TextView textView6 = (TextView) findViewById(R.id.textView6);
        TextView textView7 = (TextView) findViewById(R.id.textView7);
	    ImageView imageView = (ImageView) findViewById(R.id.iv);
	    textView1.setSelected(true);
	    TabHost th=(TabHost)findViewById(R.id.tabhost);
		th.setup();
		th.addTab(th.newTabSpec("sfgt").setContent(R.id.tab1).setIndicator(getString(R.string.gtin)));
		th.addTab(th.newTabSpec("map").setContent(R.id.tab2).setIndicator(getString(R.string.map)));
		TabWidget tb=th.getTabWidget();
		TextView thtv = (TextView) tb.getChildAt(0).findViewById(android.R.id.title),
				thtv2 = (TextView) tb.getChildAt(1).findViewById(android.R.id.title);
        mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
		try
		{
			Cursor cur=mydb.query(SFGTB_NAME, new String[] {NAME,GPSX,GPSY,GTMA,NAME2,GTMA2,GEVI}, null, null, null, null, null);
			cur.moveToPosition(i2);
			textView1.setText(cur.getString(0));
			setTitle(cur.getString(0));
			textView2.setText(cur.getString(1)+","+cur.getString(2));
			textView3.setText(cur.getString(3));
			textView6.setText(cur.getString(4));
			textView7.setText(cur.getString(5));
			Uri uri = Uri.parse("file://" + cur.getString(6));
			ContentResolver cr = this.getContentResolver();
		    try {
		    	bi = BitmapFactory.decodeStream(cr.openInputStream(uri));
			    imageView.setImageBitmap(bi);
			    System.gc();
			      } catch (FileNotFoundException e) {
			        Log.e("Exception", e.getMessage(),e);
			      }
			gpsx=cur.getDouble(1);
			gpsy=cur.getDouble(2);
			cur.close();
			mydb.close();
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
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
		textView1.setTextSize(size+10);
		textView2.setTextSize(size);
		textView3.setTextSize(size);
		textView4.setTextSize(size);
		textView5.setTextSize(size);
		textView6.setTextSize(size+10);
		textView7.setTextSize(size);
		tv.setTextSize(size);
		thtv.setTextSize(size);
		thtv2.setTextSize(size);
		lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		lm.addGpsStatusListener(statusListener);
		ll=new LocationListener()
		{
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				latitude=location.getLatitude();
				longitude=location.getLongitude();
				double xy;
				xy= Math.sqrt(Math.pow(gpsx-latitude, 2)+Math.pow(gpsy-longitude, 2));
				xy=xy/0.00001;
				xy= (float)(Math.round(xy*1));
				if(xy<1000){
					tv.setText(getString(R.string.gps9)+xy+getString(R.string.m));
				}else{
					xy=xy/1000;
					tv.setText(getString(R.string.gps9)+xy+getString(R.string.k));
				}
			}
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				tv.setText(getString(R.string.gps5));
			}
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				tv.setText(getString(R.string.gps));
			}
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
			}
		};
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
		
		
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		Button button111 = (Button) findViewById(R.id.button111);
		button111.setOnClickListener(new Button111ClickListener());
		Button button222 = (Button) findViewById(R.id.button222);
		button222.setOnClickListener(new Button222ClickListener());
		Button button333 = (Button) findViewById(R.id.button333);
		button333.setOnClickListener(new Button333ClickListener());
		Button button4 = (Button) findViewById(R.id.button4);
		button4.setOnClickListener(new Button4ClickListener());
		Button button5 = (Button) findViewById(R.id.button5);
		button5.setOnClickListener(new Button5ClickListener());
		Button button6 = (Button) findViewById(R.id.button6);
		button6.setOnClickListener(new Button6ClickListener());
		Button button7 = (Button) findViewById(R.id.button7);
		button7.setOnClickListener(new Button7ClickListener());
		Button button8 = (Button) findViewById(R.id.button8);
		button8.setOnClickListener(new Button8ClickListener());
		Button button10 = (Button) findViewById(R.id.button10);
		button10.setOnClickListener(new Button10ClickListener());
		Button button11 = (Button) findViewById(R.id.button11);
		button11.setOnClickListener(new Button11ClickListener());
		Button button12 = (Button) findViewById(R.id.button12);
		button12.setOnClickListener(new Button12ClickListener());
		Button button13 = (Button) findViewById(R.id.button13);
		button13.setOnClickListener(new Button13ClickListener());
		Button button14 = (Button) findViewById(R.id.button14);
		button14.setOnClickListener(new Button14ClickListener());
		Button button15 = (Button) findViewById(R.id.button15);
		button15.setOnClickListener(new Button15ClickListener());
		Button button16 = (Button) findViewById(R.id.button16);
		button16.setOnClickListener(new Button16ClickListener());
		Button button17 = (Button) findViewById(R.id.button17);
		button17.setOnClickListener(new Button17ClickListener());
		Button button18 = (Button) findViewById(R.id.button18);
		button18.setOnClickListener(new Button18ClickListener());
		Button button19 = (Button) findViewById(R.id.button19);
		button19.setOnClickListener(new Button19ClickListener());
		Button button20 = (Button) findViewById(R.id.button20);
		button20.setOnClickListener(new Button20ClickListener());
		Button button21 = (Button) findViewById(R.id.button21);
		button21.setOnClickListener(new Button21ClickListener());
		Button button22 = (Button) findViewById(R.id.button22);
		button22.setOnClickListener(new Button22ClickListener());
		Button button23 = (Button) findViewById(R.id.button23);
		button23.setOnClickListener(new Button23ClickListener());
		Button button24 = (Button) findViewById(R.id.button24);
		button24.setOnClickListener(new Button24ClickListener());
		Button button25 = (Button) findViewById(R.id.button25);
		button25.setOnClickListener(new Button25ClickListener());
		tv2=(TextView)findViewById(R.id.textViewgn);
		tv2.setTextSize(30);
		et = (EditText) findViewById(R.id.editText1);
		ng[i]=spf.getString("gng", null);
		if(ng[i]!=null){
			pf2=true;
			et.setText(ng[i]);
		}else{
			et.setText("0");
		}
		st=0;
		if(ng[i]!=null){
			st=Double.valueOf(ng[i]);
		}
		if(st!=(int)st){
			hn=false;
		}
		if(st<0){
			fh=false;
		}
		
	}
	private void setupWebView(){
		 
	     webView = (WebView) findViewById(R.id.webview);
	     webView.getSettings().setJavaScriptEnabled(true);
	     webView.setWebViewClient(new WebViewClient(){ 
	       @Override 
	       public void onPageFinished(WebView view, String url) 
	       {
	    	   mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
	    	   Cursor cur=mydb.query(SFGTB_NAME, new String[] {GPSX,GPSY}, null, null, null, null, null);
				cur.moveToPosition(i2);
				webView.loadUrl("javascript:getTitle(\'"+getTitle()+"\')");
				webView.loadUrl("javascript:mark("+cur.getFloat(0)+","+cur.getFloat(1)+")");
				webView.loadUrl("javascript:centerAt("+cur.getFloat(0)+","+cur.getFloat(1)+")");
				cur.close();
				mydb.close();
	       }

	     });
	     webView.loadUrl(MAP_URL); 
	   }
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
        	finish();
			ImageView imageView = (ImageView) findViewById(R.id.iv);
	        if(bi!=null&&!bi.isRecycled()){
				 bi.recycle();
		    }
		    System.gc();
			bi=null;
			imageView.setImageBitmap(bi);
        }
        return super.onKeyDown(keyCode, event);
    }
	//不關閉對話方塊的關鍵 close:"true"=可關閉 "false"=不可關閉
	 private void canCloseDialog(DialogInterface dialogInterface, boolean close) {
	        try {
	            Field field = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
	            field.setAccessible(true);
	            field.set(dialogInterface, close);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	   class SetupLugf implements DialogInterface.OnClickListener{
		   View sfgtview;
		   public SetupLugf(View sfgtview){
			   this.sfgtview=sfgtview;
		   }
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
			switch(which){
			case DialogInterface.BUTTON_NEGATIVE:
				canCloseDialog(dialog, true);
				break;
			case DialogInterface.BUTTON_POSITIVE:
				canCloseDialog(dialog, false);
				EditText et=new EditText(Sfgt2.this);
				et.setHint(getString(R.string.lugfname));
				AlertDialog.Builder builder = new AlertDialog.Builder(Sfgt2.this);
				builder.setTitle(getString(R.string.addlugf));
		    	builder.setView(et);
		    	builder.setCancelable(false);
		    	Addlugfda al =new Addlugfda(et,sfgtview);
		    	builder.setNegativeButton(getString(R.string.sfgt1), al);
		    	builder.setPositiveButton(getString(R.string.ca), al);
		    	builder.show(); 
				break;
			}
		}
	   }
	   class Addlugfda implements DialogInterface.OnClickListener{
			EditText et;
			View sfgtview;
			public Addlugfda(EditText et,View sfgtview){
				this.et=et;
				this.sfgtview=sfgtview;
			}
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(spf.getInt("music", 0)==0){
					sound.play(kg, 1, 1, 0, 0, 1);
				}
				switch(which){
				case DialogInterface.BUTTON_NEGATIVE:
					dialog.dismiss();
					break;
				case DialogInterface.BUTTON_POSITIVE:
					mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
					ContentValues cv=new ContentValues();
					int i;
					if("".equals(et.getText().toString().trim())){
						i=spf.getInt("lugfna", 1);
						cv.put(NAME,getString(R.string.sfgt13)+getString(R.string.lugf7)+i);
						SharedPreferences.Editor ed=spf.edit();
					    ed.putInt("lugfna", i+1);
			        	ed.commit();
					}else{
					    cv.put(NAME,et.getText().toString());
					}

					i=spf.getInt("lugfsqlna", 0);
				    StringBuffer bf=new StringBuffer("lugfsql"+i);
				    cv.put(SQLNAME,bf.toString());
				    cv.put(MC,0);
				    cv.put(GT,0);
				    SharedPreferences.Editor ed=spf.edit();
				    ed.putInt("lugfsqlna", i+1);
			    	ed.commit();
				    mydb.insert(LUGF2_NAME, null, cv);
			    	mydb.execSQL("CREATE TABLE "+bf.toString()+" ("+ID+" INTEGER PRIMARY KEY,"+ NAME+" TEXT,"+PH+" INTEGER,"+PF+" INTEGER,"+ GDGPSX+" TEXT,"+ GDGPSY+" TEXT)");
				    mydb.close();
				    lv.setListview(sql());
					lv.notifyDataSetChanged();
 				((ListView)sfgtview.findViewById(R.id.listViewgtk)).setVisibility(View.VISIBLE);
 				((TextView)sfgtview.findViewById(R.id.textView1)).setVisibility(View.INVISIBLE);
					break;
				}
			}
		}
		public boolean onCreateOptionsMenu(Menu menu) {
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.sfgt, menu);
		    return true;
		}
		class addLugf implements OnItemClickListener{
	    	AlertDialog listad;
	    	public addLugf(AlertDialog listad){
	    		this.listad=listad;
	    	}
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
				TextView textView1 = (TextView) findViewById(R.id.textView1);
            	mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
            	Cursor cur=mydb.query(LUGF2_NAME, new String[] {NAME,SQLNAME}, null, null, null, null, null);
            	cur.moveToPosition(position);
    			ContentValues cv=new ContentValues();
    			 cv.put(NAME,textView1.getText().toString());
	 			 cv.put(PH,i2);
	 			 cv.put(PF,1);
	 			 cv.put(GDGPSX,gpsx);
	 			 cv.put(GDGPSY,gpsy);
			    mydb.insert(cur.getString(1), null, cv);
	       		Toast.makeText(getApplicationContext(), getString(R.string.lugf6)+"("+cur.getString(0)+")", Toast.LENGTH_LONG).show();
			    cur.close();
			    mydb.close();
			    canCloseDialog(listad, true);
				listad.dismiss();
			}
	    }
		public ArrayList<HashMap> sql(){
	    	ArrayList<HashMap> tiem2= new ArrayList<HashMap>();
			HashMap hh;
			mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
			Cursor cur=mydb.query(LUGF2_NAME, new String[] {NAME}, null, null, null, null, null);
			for(int i=0;i<cur.getCount();i++){
            	hh = new HashMap();
            	cur.moveToPosition(i);
        		hh.put("id", i);
        		hh.put("name", cur.getString(0));
        		hh.put("tv", size);
            	tiem2.add(hh); 
            }
			cur.close();
	    	return tiem2;
	    }
		public boolean onOptionsItemSelected(MenuItem item) {
	    	int item_id = item.getItemId();
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	        TextView textView2 = (TextView) findViewById(R.id.textView2);
	        Intent intent;
	    	switch (item_id){
	    		case R.id.sfgt:
	    			intent = new Intent(Sfgt2.this, Sfgtdet.class);
	    			intent.putExtra("SELECTED_GREETING", i2);
	    	        startActivity(intent);
	    			finish();
	    	        if(bi!=null&&!bi.isRecycled()){
	    				 bi.recycle();
	    		    }
	    		    System.gc();
	    			bi=null;
	    			((ImageView) findViewById(R.id.iv)).setImageBitmap(bi);
	    			break;
	    		case R.id.ghgn:
	    			mDrawerLayout.openDrawer(Gravity.LEFT);
	    			break;
	    		case R.id.th:
	    			intent = new Intent(); 
	    			intent.setAction(android.content.Intent.ACTION_VIEW);
	    			intent.setData(Uri.parse("http://maps.google.com/maps?f=d&saddr="+latitude+","+longitude+"&daddr="+textView2.getText().toString()+"&hl=tw"));
	    			startActivity(intent);
	    			break;
	    		case R.id.sql:
	    			View sfgtview = getLayoutInflater().inflate(R.layout.sfgt,
    						(ViewGroup) findViewById(R.layout.sfgt));
                	ArrayList<HashMap> tiem2=sql();
    				lv=new Listview2(Sfgt2.this, tiem2);
    				ListView list=(ListView)sfgtview.findViewById(R.id.listViewgtk);
    				TextView tv=(TextView)sfgtview.findViewById(R.id.textView1);
    				tv.setText(getString(R.string.notlugf));
    				tv.setTextSize(size);
    				Builder listad=new AlertDialog.Builder(Sfgt2.this); 
    				listad.setTitle(getString(R.string.chooselugf));
    				if(tiem2.size()==0){
    					tv.setVisibility(View.VISIBLE);
    					list.setVisibility(View.INVISIBLE);
    				}else{
    					tv.setVisibility(View.INVISIBLE);
    					list.setVisibility(View.VISIBLE);
    				}
    				listad.setView(sfgtview);
    				SetupLugf af=new SetupLugf(sfgtview);
    				listad.setPositiveButton(getString(R.string.addlugf), af);
    				listad.setNegativeButton(getString(R.string.sfgt1), af);
    				list.setOnItemClickListener(new addLugf(listad.show()));
    				list.setAdapter(lv);
	    			break;
	    		default: return false;
	    	}
	    	return true;
	    }
		

		class Button111ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	tv2.setText("");
		    	mg=0;
			}
		}
		class Button222ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	if(mg<0){
		    		fh=false;
		    	}
		    	if(mg==0){
		    		ng[i]=null;
			    	et.setText("0");
		    	}else if(mg==(int)mg){
		    		ng[i]=Integer.toString(Integer.valueOf((int)mg));
			    	et.setText(ng[i]);
		    	}else{
		    		ng[i]=Double.toString(mg);
		    		hn=false;
			    	et.setText(ng[i]);
		    	}
		    	pf2=true;
			}
		}
		class Button333ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	tv2.setText("M");
		    	if(ng[i]==null){
			    	mg=Double.valueOf("0");
		    	}else{
		    		mg=Double.valueOf(ng[i]);
		    	}
			}
		}
		class Button4ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	tv2.setText("M");
		    	if(ng[i]!=null){
			    	mg=mg+Double.valueOf(ng[i]);
		    	}
			}
		}
		class Button5ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	tv2.setText("M");
		    	if(ng[i]!=null){
			    	mg=mg-Double.valueOf(ng[i]);
		    	}
			}
		}
		class Button6ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	if(pf){
		    		if(ng[i]==null||ng[i]=="0"){
				    	ng[i]="7";
				    	et.setText(ng[i]);
		    		}else{
				    	ng[i]=ng[i]+"7";
				    	et.setText(ng[i]);
		    		}
		    		pf2=true;
		    	}
			}
		}
		class Button7ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	if(pf){
		    		if(ng[i]==null||ng[i]=="0"){
				    	ng[i]="8";
				    	et.setText(ng[i]);
		    		}else{
				    	ng[i]=ng[i]+"8";
				    	et.setText(ng[i]);
		    		}
		    		pf2=true;
		    	}
			}
		}
		class Button8ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	if(pf){
		    		if(ng[i]==null||ng[i]=="0"){
				    	ng[i]="9";
				    	et.setText(ng[i]);
		    		}else{
				    	ng[i]=ng[i]+"9";
				    	et.setText(ng[i]);
		    		}
		    		pf2=true;
		    	}
			}
		}
		class Button10ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	if(pf){
		    		if(ng[i]==null||ng[i]=="0"){
				    	ng[i]="4";
				    	et.setText(ng[i]);
		    		}else{
				    	ng[i]=ng[i]+"4";
				    	et.setText(ng[i]);
		    		}
		    		pf2=true;
		    	}
			}
		}
		class Button11ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	if(pf){
		    		if(ng[i]==null||ng[i]=="0"){
				    	ng[i]="5";
				    	et.setText(ng[i]);
		    		}else{
				    	ng[i]=ng[i]+"5";
				    	et.setText(ng[i]);
		    		}
		    		pf2=true;
		    	}
			}
		}
		class Button12ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	if(pf){
		    		if(ng[i]==null||ng[i]=="0"){
				    	ng[i]="6";
				    	et.setText(ng[i]);
		    		}else{
				    	ng[i]=ng[i]+"6";
				    	et.setText(ng[i]);
		    		}
		    		pf2=true;
		    	}
			}
		}
		class Button13ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	if(pf){
		    		if(ng[i]==null||ng[i]=="0"){
				    	ng[i]="1";
				    	et.setText(ng[i]);
		    		}else{
				    	ng[i]=ng[i]+"1";
				    	et.setText(ng[i]);
		    		}
		    		pf2=true;
		    	}
			}
		}
		class Button14ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	if(pf){
		    		if(ng[i]==null||ng[i]=="0"){
				    	ng[i]="2";
				    	et.setText(ng[i]);
		    		}else{
				    	ng[i]=ng[i]+"2";
				    	et.setText(ng[i]);
		    		}
		    		pf2=true;
		    	}
			}
		}
		class Button15ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	if(pf){
		    		if(ng[i]==null||ng[i]=="0"){
				    	ng[i]="3";
				    	et.setText(ng[i]);
		    		}else{
				    	ng[i]=ng[i]+"3";
				    	et.setText(ng[i]);
		    		}
		    		pf2=true;
		    	}
			}
		}
		class Button16ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
	        	SharedPreferences.Editor ed=spf.edit();
		    	if(pf2){
		    		while(j!=0){
			    		if(nh[j-1]==1){
			    			if(ng[i-1]==null){
					    		gh=0+Double.valueOf(ng[i]);
			    			}else if(ng[i]==null){
					    		gh=Double.valueOf(ng[i-1])+0;
			    			}else{
					    		gh=Double.valueOf(ng[i-1])+Double.valueOf(ng[i]);
			    			}
				    		if(gh==(int)gh){
				    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
				    			if(gh==0){
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", null);
					            	ed.commit();
					    		}else{
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", ng[i-1]);
					            	ed.commit();
					    		}
				    		}else{
				    			ng[i-1]=Double.toString(gh);
					    		et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
				    		ng[i]=null;
				    		i--;
				    		j--;
				    	}else if(nh[j-1]==2){
			    			if(ng[i-1]==null){
					    		gh=0-Double.valueOf(ng[i]);
			    			}else if(ng[i]==null){
					    		gh=Double.valueOf(ng[i-1])-0;
			    			}else{
					    		gh=Double.valueOf(ng[i-1])-Double.valueOf(ng[i]);
			    			}
				    		if(gh==(int)gh){
				    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
				    			if(gh==0){
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", null);
					            	ed.commit();
					    		}else{
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", ng[i-1]);
					            	ed.commit();
					    		}
				    		}else{
				    			ng[i-1]=Double.toString(gh);
					    		et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
				    		ng[i]=null;
				    		i--;
				    		j--;
				    	}else if(nh[j-1]==3){
			    			if(ng[i-1]==null){
					    		gh=0*Double.valueOf(ng[i]);
			    			}else if(ng[i]==null){
					    		gh=Double.valueOf(ng[i-1])*0;
			    			}else{
					    		gh=Double.valueOf(ng[i-1])*Double.valueOf(ng[i]);
			    			}
				    		if(gh==(int)gh){
				    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
				    			if(gh==0){
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", null);
					            	ed.commit();
					    		}else{
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", ng[i-1]);
					            	ed.commit();
					    		}
				    		}else{
				    			ng[i-1]=Double.toString(gh);
					    		et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
				    		ng[i]=null;
				    		i--;
				    		j--;
				    	}else if(nh[j-1]==4){
			    			if(ng[i-1]==null){
					    		gh=0/Double.valueOf(ng[i]);
			    			}else if(ng[i]==null){
					    		gh=Double.valueOf(ng[i-1])/0;
			    			}else{
					    		gh=Double.valueOf(ng[i-1])/Double.valueOf(ng[i]);
			    			}
				    		if(gh==(int)gh){
				    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
				    			if(gh==0){
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", null);
					            	ed.commit();
					    		}else{
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", ng[i-1]);
					            	ed.commit();
					    		}
				    		}else{
				    			ng[i-1]=Double.toString(gh);
					    		et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
				    		ng[i]=null;
				    		i--;
				    		j--;
				    	}
			    	}
		    		pf2=false;
			    	hn=true;
			    	fh=true;
			    	nh[j]=1;
			    	i++;
			    	j++;
		    	}
			}
		}
		class Button17ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	SharedPreferences.Editor ed=spf.edit();
		    	if(pf2){
		    		while(j!=0){
			    		if(nh[j-1]==1){
			    			if(ng[i-1]==null){
					    		gh=0+Double.valueOf(ng[i]);
			    			}else if(ng[i]==null){
					    		gh=Double.valueOf(ng[i-1])+0;
			    			}else{
					    		gh=Double.valueOf(ng[i-1])+Double.valueOf(ng[i]);
			    			}
				    		if(gh==(int)gh){
				    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
				    			if(gh==0){
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", null);
					            	ed.commit();
					    		}else{
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", ng[i-1]);
					            	ed.commit();
					    		}
				    		}else{
				    			ng[i-1]=Double.toString(gh);
					    		et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
				    		ng[i]=null;
				    		i--;
				    		j--;
				    	}else if(nh[j-1]==2){
			    			if(ng[i-1]==null){
					    		gh=0-Double.valueOf(ng[i]);
			    			}else if(ng[i]==null){
					    		gh=Double.valueOf(ng[i-1])-0;
			    			}else{
					    		gh=Double.valueOf(ng[i-1])-Double.valueOf(ng[i]);
			    			}
				    		if(gh==(int)gh){
				    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
				    			if(gh==0){
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", null);
					            	ed.commit();
					    		}else{
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", ng[i-1]);
					            	ed.commit();
					    		}
				    		}else{
				    			ng[i-1]=Double.toString(gh);
					    		et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
				    		ng[i]=null;
				    		i--;
				    		j--;
				    	}else if(nh[j-1]==3){
			    			if(ng[i-1]==null){
					    		gh=0*Double.valueOf(ng[i]);
			    			}else if(ng[i]==null){
					    		gh=Double.valueOf(ng[i-1])*0;
			    			}else{
					    		gh=Double.valueOf(ng[i-1])*Double.valueOf(ng[i]);
			    			}
				    		if(gh==(int)gh){
				    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
				    			if(gh==0){
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", null);
					            	ed.commit();
					    		}else{
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", ng[i-1]);
					            	ed.commit();
					    		}
				    		}else{
				    			ng[i-1]=Double.toString(gh);
					    		et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
				    		ng[i]=null;
				    		i--;
				    		j--;
				    	}else if(nh[j-1]==4){
			    			if(ng[i-1]==null){
					    		gh=0/Double.valueOf(ng[i]);
			    			}else if(ng[i]==null){
					    		gh=Double.valueOf(ng[i-1])/0;
			    			}else{
					    		gh=Double.valueOf(ng[i-1])/Double.valueOf(ng[i]);
			    			}
				    		if(gh==(int)gh){
				    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
				    			if(gh==0){
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", null);
					            	ed.commit();
					    		}else{
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", ng[i-1]);
					            	ed.commit();
					    		}
				    		}else{
				    			ng[i-1]=Double.toString(gh);
					    		et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
				    		ng[i]=null;
				    		i--;
				    		j--;
				    	}
			    	}
		    		pf2=false;
			    	hn=true;
			    	fh=true;
			    	nh[j]=2;
			    	i++;
			    	j++;
		    	}
			}
		}
		class Button18ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	SharedPreferences.Editor ed=spf.edit();
		    	if(pf2){
		    		if(j!=0){
		    			if(nh[j-1]==3){
		    				if(ng[i-1]==null){
					    		gh=0*Double.valueOf(ng[i]);
			    			}else if(ng[i]==null){
					    		gh=Double.valueOf(ng[i-1])*0;
			    			}else{
					    		gh=Double.valueOf(ng[i-1])*Double.valueOf(ng[i]);
			    			}
				    		if(gh==(int)gh){
				    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
				    			if(gh==0){
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", null);
					            	ed.commit();
					    		}else{
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", ng[i-1]);
					            	ed.commit();
					    		}
				    		}else{
				    			ng[i-1]=Double.toString(gh);
					    		et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
				    		ng[i]=null;
				    		i--;
				    		j--;
		    			}
			    	}
		    		pf2=false;
			    	hn=true;
			    	fh=true;
			    	nh[j]=3;
			    	i++;
			    	j++;
		    	}
			}
		}
		class Button19ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	SharedPreferences.Editor ed=spf.edit();
		    	if(pf2){
		    		if(j!=0){
		    			if(nh[j-1]==4){
		    				if(ng[i-1]==null){
					    		gh=0/Double.valueOf(ng[i]);
			    			}else if(ng[i]==null){
					    		gh=Double.valueOf(ng[i-1])/0;
			    			}else{
					    		gh=Double.valueOf(ng[i-1])/Double.valueOf(ng[i]);
			    			}
				    		if(gh==(int)gh){
				    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
				    			if(gh==0){
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", null);
					            	ed.commit();
					    		}else{
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", ng[i-1]);
					            	ed.commit();
					    		}
				    		}else{
				    			ng[i-1]=Double.toString(gh);
					    		et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
				    		ng[i]=null;
				    		i--;
				    		j--;
		    			}
			    	}
		    		pf2=false;
			    	hn=true;
			    	fh=true;
			    	nh[j]=4;
			    	i++;
			    	j++;
		    	}
			}
		}
		class Button20ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	SharedPreferences.Editor ed=spf.edit();
		    	if(pf2){
		    		while(j!=0){
			    		if(nh[j-1]==1){
			    			if(ng[i-1]==null){
					    		gh=0+Double.valueOf(ng[i]);
			    			}else if(ng[i]==null){
					    		gh=Double.valueOf(ng[i-1])+0;
			    			}else{
					    		gh=Double.valueOf(ng[i-1])+Double.valueOf(ng[i]);
			    			}
				    		if(gh==(int)gh){
				    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
				    			if(gh==0){
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", null);
					            	ed.commit();
					    		}else{
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", ng[i-1]);
					            	ed.commit();
					    		}
				    		}else{
				    			ng[i-1]=Double.toString(gh);
					    		et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
				    		i--;
				    		j--;
				    	}else if(nh[j-1]==2){
			    			if(ng[i-1]==null){
					    		gh=0-Double.valueOf(ng[i]);
			    			}else if(ng[i]==null){
					    		gh=Double.valueOf(ng[i-1])-0;
			    			}else{
					    		gh=Double.valueOf(ng[i-1])-Double.valueOf(ng[i]);
			    			}
				    		if(gh==(int)gh){
				    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
				    			if(gh==0){
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", null);
					            	ed.commit();
					    		}else{
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", ng[i-1]);
					            	ed.commit();
					    		}
				    		}else{
				    			ng[i-1]=Double.toString(gh);
					    		et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
				    		i--;
				    		j--;
				    	}else if(nh[j-1]==3){
			    			if(ng[i-1]==null){
					    		gh=0*Double.valueOf(ng[i]);
			    			}else if(ng[i]==null){
					    		gh=Double.valueOf(ng[i-1])*0;
			    			}else{
					    		gh=Double.valueOf(ng[i-1])*Double.valueOf(ng[i]);
			    			}
				    		if(gh==(int)gh){
				    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
				    			if(gh==0){
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", null);
					            	ed.commit();
					    		}else{
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", ng[i-1]);
					            	ed.commit();
					    		}
				    		}else{
				    			ng[i-1]=Double.toString(gh);
					    		et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
				    		i--;
				    		j--;
				    	}else if(nh[j-1]==4){
			    			if(ng[i-1]==null){
					    		gh=0/Double.valueOf(ng[i]);
			    			}else if(ng[i]==null){
					    		gh=Double.valueOf(ng[i-1])/0;
			    			}else{
					    		gh=Double.valueOf(ng[i-1])/Double.valueOf(ng[i]);
			    			}
				    		if(gh==(int)gh){
				    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
				    			if(gh==0){
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", null);
					            	ed.commit();
					    		}else{
					    			et.setText(ng[i-1]);
						    		ed.putString("gng", ng[i-1]);
					            	ed.commit();
					    		}
				    		}else{
				    			ng[i-1]=Double.toString(gh);
					    		et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
				    		i--;
				    		j--;
				    	}
			    	}
			    	nh=new int[49];
			    	gh=0;
			    	i=0;
			    	j=0;
			    	hn=false;
			    	fh=false;
			    	if(ng[i]!=null){
						st=Double.valueOf(ng[i]);
			    		ng=new String[50];
			    		if(st==0){
							ng[i]=null;
			    		}else if(st==(int)st){
			    			ng[i]=Integer.toString(Integer.valueOf((int)st));
							hn=true;
			    		}else{
							ng[i]=Double.toString(st);
			    		}
					}else{
			    		ng=new String[50];
						ng[i]=null;
					}
					if(st>0){
						fh=true;
					}
			    	pf=true;
			    	pf2=true;
		    	}
			}
		}
		class Button21ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	if(pf){
		    		ng[i]=null;
			    	hn=true;
			    	fh=true;
			    	pf=true;
			    	pf2=false;
			    	et.setText("0");
		    	}
			}
		}
		class Button22ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	SharedPreferences.Editor ed=spf.edit();
		    	ng=new String[50];//計算顯示值
		    	nh=new int[49];//運算符號判別值
		    	gh=0;
		    	i=0;
		    	j=0;
		    	hn=true;
		    	fh=true;
		    	pf=true;
		    	pf2=false;
		    	et.setText("0");
	    		ed.putString("gng", null);
	        	ed.commit();
			}
		}
		class Button23ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	if(fh){
		    		if(ng[i]!=null&&ng[i]!="0"){
				    	ng[i]="-"+ng[i];
				    	et.setText(ng[i]);
				    	fh=false;
		    		}
		    	}
			}
		}
		class Button24ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	if(hn){
		    		if(ng[i]==null||ng[i]=="0"){
				    	ng[i]="0.";
				    	et.setText(ng[i]);
		    		}else{
		    			ng[i]=ng[i]+".";
				    	et.setText(ng[i]);
		    		}
			    	hn=false;
		    	}
			}
		}
		class Button25ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
		    	if(spf.getInt("music", 0)==0){
		    		sound.play(kg, 1, 1, 0, 0, 1);
				}
		    	if(pf){
		    		if(ng[i]==null||ng[i]=="0"){
				    	ng[i]="0";
				    	et.setText(ng[i]);
		    		}else{
				    	ng[i]=ng[i]+"0";
				    	et.setText(ng[i]);
		    		}
		    		pf2=true;
		    	}
			}
		}
}