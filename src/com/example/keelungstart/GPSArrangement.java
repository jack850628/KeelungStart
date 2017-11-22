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
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class GPSArrangement extends Activity {
	/** Called when the activity is first created. */
	LocationManager lm;								//«Å§iLocationManager
	LocationListener ll;
	private GpsStatus.Listener statusListener;
	private final static String gte_NAME="Sfgtsql.pref";
	private static final String MAP_URL = "file:///android_asset/googlemap.html";
	private WebView webView;
	CheckBox CB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==1)
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		else if(spf.getInt("nmfc", 0)==2)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		setContentView(R.layout.gpsarrangement);
		final TextView tv1=(TextView)findViewById(R.id.textView1);
		int size=(spf.getInt("text", 0)==0)?15:
			(spf.getInt("text", 0)==1)?20:
				(spf.getInt("text", 0)==2)?25:30;
		tv1.setTextSize(size);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		Resources res = getResources(); 
		String[] tit=res.getStringArray(R.array.arrangement);
		setTitle(tit[bundle.getInt("PF")]);
		CB=(CheckBox)findViewById(R.id.checkBox1);
		CB.setText(getString(R.string.gps3));
		CB.setTextSize(size);
		CB.setChecked(true);
	    webView = (WebView) findViewById(R.id.webview);
	    webView.getSettings().setJavaScriptEnabled(true);
	    webView.loadUrl(MAP_URL); 
        lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		lm.addGpsStatusListener(statusListener);
		ll=new LocationListener()
		{
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				tv1.setText(getString(R.string.gps4));
				webView.loadUrl("javascript:getTitle(\'"+getString(R.string.gps3)+":"+location.getLatitude()+","+location.getLongitude()+"\')");
				if(CB.isChecked())
					webView.loadUrl("javascript:centerAt("+location.getLatitude()+","+location.getLongitude()+")");
		    	webView.loadUrl("javascript:mark("+location.getLatitude()+","+location.getLongitude()+")");
			}
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				tv1.setText(getString(R.string.gps5));
			}
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				tv1.setText(getString(R.string.gps));
			}
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
			}
		};
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
    }
}
 
