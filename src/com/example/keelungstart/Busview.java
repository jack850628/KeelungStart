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
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class Busview extends Activity {
	LocationManager lm;								
	LocationListener ll;
	private GpsStatus.Listener statusListener;
	private final static String gte_NAME="Sfgtsql.pref";
	double latitude;
	double longitude;
	DisplayMetrics dm;
	int ScreenWidth, ScreenHeight;
	private static final String MAP_URL = "file:///android_asset/googlemap.html";
	private WebView webView;
	String[] bus101={"�M���q�`��","�����_�W��","�M����p��","�M���q���鯸","�M���q��","��P�y��t��","���d����","�M������","���ظ���","�����Ϥ��ү�",
			"�G�H������","���ب���","���ճB��","���а�v��","���a��f(�w�i��)��","�����B(�a������)��","�������I","�_����","�ײz�t(�Τ@�y�])","�����",
			"�N�~����(�T���W)","�H�C���f","�H�����f","���F��","���G��(�G�H�`����)","�`��"};
	double[][] gps={{25.1569,121.7716},{25.1559,121.7703},{25.1554,121.7660},{25.1564,121.7649},{25.1549,121.7645},{25.1548,121.7661},{25.1547,121.7674},
			{25.1529,121.7696},{25.1522,121.7702},{25.1522,121.7678},{25.1510,121.7670},{25.1493,121.7638},{25.1473,121.7614},{25.1455,121.7614},{25.1423,121.7606},
			{25.1408,121.7588},{25.1408,121.7562},{25.1395,121.7541},{25.1394,121.7522},{25.1376,121.7507},{25.1376,121.749},{25.1364,121.7478},{25.135,121.7668},
			{25.1325,121.7453},{25.1298,121.7431},{25.1324,121.7401}};
	String pf;
	int i,j;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.busview); 
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
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
		Bundle bundle = intent.getExtras();
		pf=bundle.getString("ciass");
		i=Integer.valueOf(pf);
		pf=bundle.getString("ph");
		j=Integer.valueOf(pf);
		setTitle(bus101[j]);
        final TextView tv1=(TextView)findViewById(R.id.textView1);
        final TextView tv2=(TextView)findViewById(R.id.textView2);
        final TextView tv=(TextView)findViewById(R.id.textView3);
		lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		lm.addGpsStatusListener(statusListener);
		ll=new LocationListener()
		{
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				String str="�ثe��m�G";
				latitude=location.getLatitude();				//���o�n��
				longitude=location.getLongitude();			//���o�g��
				str=str+"�n�סG"+latitude+"\n�g�סG"+longitude;
				tv.setText(str);
				
			}
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				tv.setText("GPS���}��!!");
			}
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				tv.setText("GPS�w�줤....");
			}
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
			}
		};
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
		if(spf.getInt("text", 0)==0){
			tv1.setTextSize(25);
			 tv2.setTextSize(15);
			 tv.setTextSize(15);
		}else if(spf.getInt("text", 0)==1){
			tv1.setTextSize(30);
			tv2.setTextSize(20);
			tv.setTextSize(20);
		}else if(spf.getInt("text", 0)==2){
			tv1.setTextSize(35);
			tv2.setTextSize(25);
			tv.setTextSize(25);
		}else if(spf.getInt("text", 0)==3){
			tv1.setTextSize(40);
			tv2.setTextSize(30);
			tv.setTextSize(30);
		}
		if(i==11){
			tv1.setText(bus101[j]);
			tv2.setText("��p�Z��  05 : 40 ���p�Z��  22 : 40 �F�C�Z��20���� ");
		}
		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new ButtonClickListener());
	}
	 private void setupWebView(){
		 
	     webView = (WebView) findViewById(R.id.webview);
	     webView.getSettings().setJavaScriptEnabled(true);
	     //Wait for the page to load then send the location information
	     webView.setWebViewClient(new WebViewClient(){ 
	       @Override 
	       public void onPageFinished(WebView view, String url) 
	       {
	    	   if(i==11){
	    	 webView.loadUrl("javascript:mark("+gps[j][0]+ "," +gps[j][1]+")");
			 webView.loadUrl("javascript:centerAt("+gps[j][0]+ "," +gps[j][1]+")");
	    	   }
	       }

	     });
	     webView.loadUrl(MAP_URL); 
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
