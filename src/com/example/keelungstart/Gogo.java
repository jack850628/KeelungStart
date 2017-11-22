package com.example.keelungstart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Gogo extends Activity {
	/** Called when the activity is first created. */
	LocationManager lm;								//«Å§iLocationManager
	LocationListener ll;
	private GpsStatus.Listener statusListener;
	private final static String gte_NAME="Sfgtsql.pref";
	double latitude;
	double longitude;
	String togo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gogo); 
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		final TextView tvgo=(TextView)findViewById(R.id.textViewgogo);
		TextView tv=(TextView)findViewById(R.id.textView1);
		if(spf.getInt("text", 0)==0){
			 tv.setTextSize(15);
			 tvgo.setTextSize(15);
		}else if(spf.getInt("text", 0)==1){
			tv.setTextSize(20);
			tvgo.setTextSize(20);
		}else if(spf.getInt("text", 0)==2){
			tv.setTextSize(25);
			tvgo.setTextSize(25);
		}else if(spf.getInt("text", 0)==3){
			tv.setTextSize(30);
			tvgo.setTextSize(30);
		}
        setTitle(getString(R.string.tht));
        lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		lm.addGpsStatusListener(statusListener);
		ll=new LocationListener()
		{
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				latitude=location.getLatitude();				
				longitude=location.getLongitude();			
				tvgo.setText(getString(R.string.gps4));
				
			}
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				tvgo.setText(getString(R.string.gps5));
			}
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				tvgo.setText(getString(R.string.gps));
			}
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
			}
		};
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
		
		Button go = (Button) findViewById(R.id.bgo);
		go.setOnClickListener(new ButtonClickListener());
	}
	class ButtonClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
	    	if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
			EditText etgo = (EditText) findViewById(R.id.et);
			if("".equals(etgo.getText().toString().trim())){
				Toast.makeText(getApplicationContext(), getString(R.string.nosl), Toast.LENGTH_LONG).show();
			}else{
			togo = etgo.getText().toString();
			Intent intent = new Intent(); 
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setData(Uri.parse("http://maps.google.com/maps?f=d&saddr="+latitude+","+longitude+"&daddr="+togo+"&hl=tw"));
			startActivity(intent);
			}
		}
	}
}
