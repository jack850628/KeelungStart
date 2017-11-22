package com.example.keelungstart;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
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
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class Tsfgt2 extends Activity {
	/** Called when the activity is first created. */
	LocationManager lm;								//宣告LocationManager
	LocationListener ll;
	private GpsStatus.Listener statusListener;
	private final static String gte_NAME="Sfgtsql.pref";
	double latitude;
	double longitude;
	private SQLiteDatabase mydb=null;
	private final static String SQL_NAME="Sfgtsql.db";//SQL名稱
	private final static String THHN_NAME="Thhnname";//停車場表名稱
	private final static String NAME="name";//資料項目:景點名稱
	private final static String GPSX="gpsx";//資料項目:GPSX座標
	private final static String GPSY="gpsy";//資料項目:GPSY座標
	private final static String GTMA="gtmn";//資料項目:景點內容
	private final static String GEVI="gevi";//資料項目:圖片
	private static final String MAP_URL = "file:///android_asset/googlemap.html";
	private WebView webView;
	double gpsx,gpsy;
	int i;
	Bitmap bi;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		setContentView(R.layout.tsfgt2);
		if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		setupWebView();
		Intent intent = this.getIntent();
		setResult(0);
		i=intent.getExtras().getInt("SELECTED_GREETING");
        final TextView tv=(TextView)findViewById(R.id.textgtksu5);
        TextView textView1 = (TextView) findViewById(R.id.textView1);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        TextView textView5 = (TextView) findViewById(R.id.textView5);
        textView1.setSelected(true);
	    ImageView imageView = (ImageView) findViewById(R.id.iv);
	    TabHost tabhost=(TabHost)findViewById(R.id.tabhost);
		tabhost.setup();
		tabhost.addTab(tabhost.newTabSpec("tgz").setContent(R.id.tab1).setIndicator(getString(R.string.tsin)));
		tabhost.addTab(tabhost.newTabSpec("map").setContent(R.id.tab2).setIndicator(getString(R.string.map)));
		TabWidget tb=tabhost.getTabWidget();
		TextView thtv = (TextView) tb.getChildAt(0).findViewById(android.R.id.title),
				thtv2 = (TextView) tb.getChildAt(1).findViewById(android.R.id.title);
        mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
		try
		{
			Cursor cur=mydb.query(THHN_NAME, new String[] {NAME,GPSX,GPSY,GTMA,GEVI}, null, null, null, null, null);
			cur.moveToPosition(i);
			textView1.setText(cur.getString(0));
			setTitle(cur.getString(0));
			textView2.setText(cur.getString(1)+","+cur.getString(2));
			textView3.setText(cur.getString(3));
			Uri uri = Uri.parse("file://" + cur.getString(4));
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
		if(spf.getInt("text", 0)==0){
			textView1.setTextSize(25);
			 textView2.setTextSize(15);
			 textView3.setTextSize(15);
			 textView4.setTextSize(15);
			 textView5.setTextSize(15);
			 tv.setTextSize(15);
			 thtv.setTextSize(15);
			 thtv2.setTextSize(15);
		}else if(spf.getInt("text", 0)==1){
			textView1.setTextSize(30);
			textView2.setTextSize(20);
			textView3.setTextSize(20);
			textView4.setTextSize(20);
			textView5.setTextSize(20);
			tv.setTextSize(20);
			thtv.setTextSize(20);
			thtv2.setTextSize(20);
		}else if(spf.getInt("text", 0)==2){
			textView1.setTextSize(35);
			textView2.setTextSize(25);
			textView3.setTextSize(25);
			textView4.setTextSize(25);
			textView5.setTextSize(25);
			tv.setTextSize(25);
			thtv.setTextSize(25);
			thtv2.setTextSize(25);
		}else if(spf.getInt("text", 0)==3){
			textView1.setTextSize(40);
			textView2.setTextSize(30);
			textView3.setTextSize(30);
			textView4.setTextSize(30);
			textView5.setTextSize(30);
			tv.setTextSize(30);
			thtv.setTextSize(30);
			thtv2.setTextSize(30);
		}
	}
	private void setupWebView(){
		 
	     webView = (WebView) findViewById(R.id.webview);
	     webView.getSettings().setJavaScriptEnabled(true);
	     //Wait for the page to load then send the location information
	     webView.setWebViewClient(new WebViewClient(){ 
	       @Override 
	       public void onPageFinished(WebView view, String url) 
	       {
	    	   mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
	    	   Cursor cur=mydb.query(THHN_NAME, new String[] {GPSX,GPSY}, null, null, null, null, null);
				cur.moveToPosition(i);
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
public boolean onCreateOptionsMenu(Menu menu) {
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.tsfgt, menu);
		    return true;
		}
		public boolean onOptionsItemSelected(MenuItem item) {
	    	int item_id = item.getItemId();

			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	   			mp.start();
			}
			Intent intent;
	    	switch (item_id){
	    		case R.id.sfgt:
	    			intent = new Intent(Tsfgt2.this, Tsfgtdet.class); 
	    			intent.putExtra("SELECTED_GREETING", i);
	    	        startActivity(intent);
	    			finish();
	    	        if(bi!=null&&!bi.isRecycled()){
	    				 bi.recycle();
	    		    }
	    		    System.gc();
	    			bi=null;
	    			((ImageView) findViewById(R.id.iv)).setImageBitmap(bi);
	    			break;
	    		case R.id.th:
	    			TextView textView2 = (TextView) findViewById(R.id.textView2);
	    			intent = new Intent(); 
	    			intent.setAction(android.content.Intent.ACTION_VIEW);
	    			intent.setData(Uri.parse("http://maps.google.com/maps?f=d&saddr="+latitude+","+longitude+"&daddr="+textView2.getText().toString()+"&hl=tw"));
	    			startActivity(intent);
	    			break;
	    		default: return false;
	    	}
	    	return true;
	    }
}

