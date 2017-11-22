package com.example.keelungstart;

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
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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

public class Ghth extends Activity {
	/** Called when the activity is first created. */
	LocationManager lm;
	LocationListener ll;
	private GpsStatus.Listener statusListener;
	private static final String MAP_URL = "file:///android_asset/googlemap.html";
	private WebView webView;
	private final static String gte_NAME="Sfgtsql.pref";
	double latitude,longitude;
	double[] gpsxy;
	String[] th,content;
	int i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ghth);
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		Resources res = getResources(); 
		th=res.getStringArray(R.array.list_name);
		content=res.getStringArray(R.array.ghth);
		setupWebView();
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		i=bundle.getInt("SELECTED_GREETING");
		Gtgps gtgps = new Gtgps();
		gpsxy=new double[]{gtgps.attgps()[i][0],gtgps.attgps()[i][1]};
		final TextView tv=(TextView)findViewById(R.id.textgtksu);
		TextView tv1 = (TextView) findViewById(R.id.textView1);
		TextView tv2 = (TextView) findViewById(R.id.textView2);
		if(i<=4)
			tv2.setText(content[i]);
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		if(i<=4&&i>=2)
			imageView.setImageResource(new Gtivtext().getghiv()[(i==2)?0:(i==3)?1:2]); 
		setTitle(th[i]);
		TabHost tabhost=(TabHost)findViewById(R.id.tabhost);
		tabhost.setup();
		tabhost.addTab(tabhost.newTabSpec("ghth").setContent(R.id.tab1).setIndicator(getString(R.string.tsin)));
		tabhost.addTab(tabhost.newTabSpec("map").setContent(R.id.tab2).setIndicator(getString(R.string.map)));
		TabWidget tb=tabhost.getTabWidget();
		TextView thtv = (TextView) tb.getChildAt(0).findViewById(android.R.id.title),
				thtv2 = (TextView) tb.getChildAt(1).findViewById(android.R.id.title);
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
				xy= Math.sqrt(Math.pow(gpsxy[0]-latitude, 2)+Math.pow(gpsxy[1]-longitude, 2));
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
		tv1.setText(th[i]);
		if(spf.getInt("text", 0)==0){
			tv1.setTextSize(25);
			 tv.setTextSize(15);
			 tv2.setTextSize(15);
			 thtv.setTextSize(15);
			 thtv2.setTextSize(15);
		}else if(spf.getInt("text", 0)==1){
			tv1.setTextSize(30);
			tv.setTextSize(20);
			tv2.setTextSize(20);
			thtv.setTextSize(20);
			thtv2.setTextSize(20);
		}else if(spf.getInt("text", 0)==2){
			tv1.setTextSize(35);
			tv.setTextSize(25);
			tv2.setTextSize(25);
			thtv.setTextSize(25);
			thtv2.setTextSize(25);
		}else if(spf.getInt("text", 0)==3){
			tv1.setTextSize(40);
			tv.setTextSize(30);
			tv2.setTextSize(30);
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
			 webView.loadUrl("javascript:getTitle(\'"+getTitle()+"\')");
	    	 webView.loadUrl("javascript:mark("+gpsxy[0]+","+gpsxy[1]+")");
			 webView.loadUrl("javascript:centerAt("+gpsxy[0]+","+gpsxy[1]+")");
	       }
	     });
	     webView.loadUrl(MAP_URL); 
	   }
		public boolean onCreateOptionsMenu(Menu menu) {
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.ghtn, menu);
		    return true;
		}
		public boolean onOptionsItemSelected(MenuItem item) {
	    	int item_id = item.getItemId();
	    	if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				MediaPlayer.create(getApplicationContext(), R.raw.button).start();
			}
	    	switch (item_id){
	    		case R.id.th:
	    			Intent intent = new Intent(); 
	    			intent.setAction(android.content.Intent.ACTION_VIEW);
	    			intent.setData(Uri.parse("http://maps.google.com/maps?f=d&saddr="+latitude+","+longitude+"&daddr="+gpsxy[0]+","+gpsxy[1]+"&hl=tw"));
	    			startActivity(intent);
	    			break;
	    		case R.id.tg:
	    			String ty = "http://park.klcg.gov.tw/";
	    			Intent ie = new Intent(Intent.ACTION_VIEW,Uri.parse(ty));
	    			startActivity(ie);
	    			break;
	    		default: return false;
	    	}
	    	return true;
	    }
}

