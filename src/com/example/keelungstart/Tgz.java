package com.example.keelungstart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Tgz extends Activity {
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
	String[] imgps=new String[2];
	String vigt="";
	Bitmap bi;
	int size;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==0){
			DisplayMetrics dm = getResources().getDisplayMetrics();
			// 取得螢幕顯示的資料
			int ScreenWidth = dm.widthPixels;
			int ScreenHeight = dm.heightPixels;
			// 螢幕寬和高的Pixels
			if (ScreenHeight > ScreenWidth){
				setContentView(R.layout.tgz);
			}
			else{
				setContentView(R.layout.tgz2);
			}
		}else if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				setContentView(R.layout.tgz);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.tgz2);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		setTitle(getString(R.string.gz)+getString(R.string.setatt));
		Intent intent = this.getIntent();
		setResult(RESULT_OK, intent);
		TextView tv=(TextView)findViewById(R.id.textView1);
		TextView tv1=(TextView)findViewById(R.id.textView2);
		TextView tv2=(TextView)findViewById(R.id.textView3);
		TextView tv3=(TextView)findViewById(R.id.textView4);
        final TextView tvgp=(TextView)findViewById(R.id.textgtksu5);
        size=(spf.getInt("text", 0)==0)?15:
			(spf.getInt("text", 0)==1)?20:
				(spf.getInt("text", 0)==2)?25:30;
		 tv.setTextSize(size);
		 tv1.setTextSize(size);
		 tv2.setTextSize(size);
		 tv3.setTextSize(size);
		 tvgp.setTextSize(size);
		lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		lm.addGpsStatusListener(statusListener);
		ll=new LocationListener()
		{
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				latitude=location.getLatitude();				//取得緯度
				longitude=location.getLongitude();			//取得經度
				String str=getString(R.string.gps3)+"\n"+getString(R.string.gps7)+latitude+"\n"+getString(R.string.gps8)+longitude;
				tvgp.setText(str);
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
		
		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new ButtonClickListener());
		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new Button2ClickListener());
		Button button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(new Button3ClickListener());
		Button button4 = (Button) findViewById(R.id.button4);
		button4.setOnClickListener(new Button4ClickListener());
		Button button5 = (Button) findViewById(R.id.button5);
		button5.setOnClickListener(new Button5ClickListener());
		Button button6 = (Button) findViewById(R.id.button6);
		button6.setOnClickListener(new Button6ClickListener());
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
	class ButtonClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
			EditText editText1 = (EditText) findViewById(R.id.editText1);
			EditText editText2 = (EditText) findViewById(R.id.editText2);
			EditText editText3 = (EditText) findViewById(R.id.editText3);
			EditText editText4 = (EditText) findViewById(R.id.editText4);
			try {
				if(bi!=null){
					int i=spf.getInt("bi2", 0);
				    // 取得外部儲存裝置路徑
				    String path = Environment.getExternalStorageDirectory().toString()+"/Keelung Start!/Tsfgt";
				    // 開啟檔案
				    File file = new File( path, "Image"+i+".jpg");
				    SharedPreferences.Editor ed=spf.edit();
				    ed.putInt("bi2", i+1);
	            	   ed.commit();
	            	   String di=path+"/"+"Image"+i+".jpg";
						dil(di);
				    // 開啟檔案串流
				    FileOutputStream out = new FileOutputStream(file );
				    // 將 Bitmap壓縮成指定格式的圖片並寫入檔案串流
				    bi.compress ( Bitmap. CompressFormat.JPEG , 100 , out);
				    // 刷新並關閉檔案串流
				    out.flush ();
				    out.close ();
				    vigt = path+"/"+"Image"+i+".jpg"; 
				}
		} catch (FileNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace ();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace ();
		}
		mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
		ContentValues cv=new ContentValues();

		if("".equals(editText1.getText().toString().trim())){
			int i=spf.getInt("tsa", 1);
			cv.put(NAME,getString(R.string.sfgt13)+getString(R.string.setatt)+i);
			SharedPreferences.Editor ed=spf.edit();
		    ed.putInt("tsa", i+1);
        	ed.commit();
		}else{
		    cv.put(NAME,editText1.getText().toString());
		}
	    cv.put(GPSX, editText2.getText().toString());
	    cv.put(GPSY, editText4.getText().toString());
	    cv.put(GTMA,editText3.getText().toString());
	    cv.put(GEVI,vigt);
	    mydb.insert(THHN_NAME, null, cv);
	    try 
        { 
	    	//在圖片插入GPS座標目前無功能，原因不明
		    ExifInterface exif = new ExifInterface(vigt);
		    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, imgps[0]);
		    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, imgps[1]);
		    exif.saveAttributes();
        }catch (IOException e) { 
            e.printStackTrace(); 
        } catch(Exception e){
        	e.printStackTrace();
	    }
	    Cursor cur=mydb.query(THHN_NAME, new String[] {NAME}, null, null, null, null, null);
		Intent intent = new Intent(Tgz.this, Tsfgt2.class); 
		intent.putExtra("SELECTED_GREETING",cur.getCount()-1);
		cur.close();
	    mydb.close();
        startActivity(intent);
        finish();
		ImageView imageView = (ImageView) findViewById(R.id.iv);
        if(bi!=null&&!bi.isRecycled()){
			 bi.recycle();
	    }
	    System.gc();
		bi=null;
		imageView.setImageBitmap(bi);
		}
	}
	AlertDialog ad;
	class Button2ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
			View layout = getLayoutInflater().inflate(R.layout.gpslist,
					(ViewGroup) findViewById(R.layout.gpslist));
			ListView lv=(ListView)layout.findViewById(R.id.listView1);
			lv.setOnItemClickListener(new onlist());
			ArrayList<HashMap> tiem2= new ArrayList<HashMap>();
			HashMap hh;
			for (int i = 0; i < 2; i++) {
				hh = new HashMap();
	    		hh.put("id", i);
	    		hh.put("name",(i<1)?getString(R.string.fronim):getString(R.string.frongps));
	    		hh.put("tv", size);
	    		tiem2.add(hh); 
			}
			lv.setOnItemClickListener(new onlist());
			lv.setAdapter(new Listview2(Tgz.this, tiem2));
			ad=new AlertDialog.Builder(Tgz.this).setTitle(getString(R.string.sfgt7)).setView(layout).show();
		}
	}
	class onlist implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
       			mp.start();
			}
			EditText editText2 = (EditText) findViewById(R.id.editText2);
			EditText editText4 = (EditText) findViewById(R.id.editText4);
			switch(position){
			case 0:
				editText2.setText(getgpsxy(imgps[0]));
				editText4.setText(getgpsxy(imgps[1]));
				break;
			case 1:
				editText2.setText(new Double(latitude).toString());
				editText4.setText(new Double(longitude).toString());
				break;
			}
			ad.dismiss();
		}
	}
	String getgpsxy(String sDMS){
		  double dRV = 0;
		  try {
		    String[] DMSs = sDMS.split(",", 3);
		    String s[] = DMSs[0].split("/", 2);
		    dRV = (new Double(s[0])/new Double(s[1]));
		    s = DMSs[1].split("/", 2);
		    dRV += ((new Double(s[0])/new Double(s[1]))/60);
		    s = DMSs[2].split("/", 2);
		    dRV += ((new Double(s[0])/new Double(s[1]))/3600);
		  } catch (Exception e) {}
		  return String.valueOf(dRV);
		}
		class Button3ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
				SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
		        finish();
				ImageView imageView = (ImageView) findViewById(R.id.iv);
		        if(bi!=null&&!bi.isRecycled()){
					 bi.recycle();
			    }
			    System.gc();
				bi=null;
				imageView.setImageBitmap(bi);
			}
		}
		class Button4ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
				SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				Intent intent = new Intent();
		           intent.setType("image/*");                                   
		            intent.setAction(Intent.ACTION_GET_CONTENT); 
		            startActivityForResult(intent, 1);
			}
		}
		@Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_OK) {
	    	Uri uri=null;
	    	ImageView imageView = (ImageView) findViewById(R.id.iv);
	        if(bi!=null&&!bi.isRecycled()){
				 bi.recycle();
		    }
		    System.gc();
			bi=null;
			imageView.setImageBitmap(bi);
	     try {
		       uri = data.getData();
		       Log.e("uri", uri.toString());
		       vigt = uri.toString();
		       ContentResolver cr = this.getContentResolver();
	           bi = BitmapFactory.decodeStream(cr.openInputStream(uri));
	           imageView.setImageBitmap(bi);
		       System.gc();
	      } catch (FileNotFoundException e) {
	        Log.e("Exception", e.getMessage(),e);
	      }catch(Exception e){
	    	  Resources res = getResources(); 
	  		  String[] cam=res.getStringArray(R.array.cam);
	    	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(cam[0]);
		    	builder.setMessage(cam[1]);
		    	Gn de = new Gn();
		    	builder.setNegativeButton(getString(R.string.ca), de);
		    	builder.setPositiveButton(getString(R.string.opcam), de);
		    	AlertDialog ad = builder.create();
		    	ad.show(); 
	      }
	     try{
				String i;
				String[] gh = vigt.toString().split(":");
			       if(gh[0].equals("content")){
			           String[] proj = { MediaStore.Images.Media.DATA };
			           Cursor act = managedQuery(uri,proj,null,null,null);
			           int actual = act.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			           act.moveToFirst();
			           i=act.getString(actual);
			        }else{
			        	i=vigt;
			        }
				ExifInterface exif = new ExifInterface(i);
				imgps[0]=exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
				imgps[1]=exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
			}catch( IOException e){
				e.printStackTrace();
			}catch(Exception e){
				
			}
	    }
	      super.onActivityResult(requestCode, resultCode, data);
	    }
		private class Gn implements
		android.content.DialogInterface.OnClickListener{
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
				if (which == DialogInterface.BUTTON_NEGATIVE){
					if(spf.getInt("music", 0)==0){
						MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
		       			mp.start();
					}
					 dialog.dismiss();
				}else if (which == DialogInterface.BUTTON_POSITIVE){
					if(spf.getInt("music", 0)==0){
						MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
		       			mp.start();
					}
					Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
				    startActivity(intent);
				}
			}
		}
		class Button5ClickListener implements  OnClickListener {
			@Override
			public void onClick(View v) {
				SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
			     Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			     startActivityForResult(intent, 0);
			}
		}
		class Button6ClickListener implements  OnClickListener {
			@Override
			public void onClick(View v) {
				SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
				if(spf.getInt("music", 0)==0){
					MediaPlayer mp=MediaPlayer.create(getApplicationContext(), R.raw.button);
	       			mp.start();
				}
				ImageView imageView = (ImageView) findViewById(R.id.iv);
			    if(bi!=null&&!bi.isRecycled()){
					 bi.recycle();
			    }
			    System.gc();
				bi=null;
				vigt="";
				imageView.setImageBitmap(bi);
			}
		}
		private void dil(String fi) {
			File out = new File(fi);
			if(out.exists()){
				out.delete();
			}
	}
}