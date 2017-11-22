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
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.Toast;

public class Sfgtdet extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	LocationManager lm;								//�?????�LocationManager
	LocationListener ll;
	Handler han=new Handler();
	private GpsStatus.Listener statusListener;
	private final static String gte_NAME="Sfgtsql.pref";
	double latitude;
	double longitude;
	private SQLiteDatabase mydb=null;
	private final static String ID="_id";
	private final static String SQL_NAME="Sfgtsql.db";//SQL??�稱
	String LUGF_NAME;//行�?��?��?�表??�稱
	private final static String LUGF2_NAME="Lugf2";
	private final static String GT="gt";
	private final static String PH="ph";//資�?��?�目:行�?��?��?�編???
	private final static String PF="pf";//資�?��?�目:行�?��?��?�判?��
	private final static String GDGPSX="gdgpsx";//資�?��?�目:終�?�座標X
	private final static String GDGPSY="gdgpsy";//資�?��?�目:終�?�座標Y
	private final static String SFGTB_NAME="Sfgtbname";//表�?�稱
	private final static String NAME="name";//資�?��?�目:?��點�?�稱
	private final static String GPSX="gpsx";//資�?��?�目:GPSX座�??
	private final static String GPSY="gpsy";//資�?��?�目:GPSY座�??
	private final static String GTMA="gtmn";//資�?��?�目:?��點內�???
	private final static String GEVI="gevi";//資�?��?�目:??��??
	private final static String GEVI2="gevi2";//資�?��?�目:??��??
	private final static String NAME2="name2";//資�?��?�目:交�?��?��??
	private final static String GTMA2="gtmn2";//資�?��?�目:交�?��?��?�內�???
	private final static String SQLNAME="sqlname";
	String bt,na,imggps;
	SoundPool sound;
	String[] vigt=new String[]{"",""},di=new String[2],imgps=new String[2];
	Bitmap bi;
	int item,ButtonSound,size;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==0){
			DisplayMetrics dm = getResources().getDisplayMetrics();
			// ??��?�螢幕顯示�?��?��??
			int ScreenWidth = dm.widthPixels;
			int ScreenHeight = dm.heightPixels;
			// ?��幕寬??��?��?�Pixels
			if (ScreenHeight > ScreenWidth){
				setContentView(R.layout.sfgtdet);
			}
			else{
				setContentView(R.layout.sfgtdet2);
			}
		}else if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				setContentView(R.layout.sfgtdet);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.sfgtdet2);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		setTitle(getString(R.string.menu3)+getString(R.string.sfgt12));
		sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		ButtonSound = sound.load(this, R.raw.button , 1);
		TextView tv=(TextView)findViewById(R.id.textView1);
		TextView tv1=(TextView)findViewById(R.id.textView2);
		TextView tv2=(TextView)findViewById(R.id.textView3);
		TextView tv3=(TextView)findViewById(R.id.textView4);
		TextView tv4=(TextView)findViewById(R.id.textView5);
		TextView tv5=(TextView)findViewById(R.id.textView6);
        final TextView tvgp=(TextView)findViewById(R.id.textgtksu5);
		size=(spf.getInt("text", 0)==0)?15:
			(spf.getInt("text", 0)==1)?20:
				(spf.getInt("text", 0)==2)?25:30;
		 tv.setTextSize(size);
		 tv1.setTextSize(size);
		 tv2.setTextSize(size);
		 tv3.setTextSize(size);
		 tv4.setTextSize(size);
		 tv5.setTextSize(size);
		 tvgp.setTextSize(size);
		Intent intent = this.getIntent();
	    item=intent.getExtras().getInt("SELECTED_GREETING");
		EditText editText1 = (EditText) findViewById(R.id.editText1);
		EditText editText2 = (EditText) findViewById(R.id.editText2);
		EditText editText3 = (EditText) findViewById(R.id.editText3);
		EditText editText4 = (EditText) findViewById(R.id.editText4);
		EditText editText5 = (EditText) findViewById(R.id.editText5);
		EditText editText6 = (EditText) findViewById(R.id.editText6);
	    ImageView imageView = (ImageView) findViewById(R.id.iv);
        mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
        Uri uri=null;
		try
		{
			Cursor cur=mydb.query(SFGTB_NAME, new String[] {ID,NAME,GPSX,GPSY,GTMA,NAME2,GTMA2,GEVI,GEVI2}, null, null, null, null, null);

			cur.moveToPosition(item);
			bt=cur.getString(0);
			editText1.setText(cur.getString(1));
			editText2.setText(cur.getString(2));
			editText4.setText(cur.getString(3));
			editText3.setText(cur.getString(4));
			editText5.setText(cur.getString(5));
			editText6.setText(cur.getString(6));
			di[0]=cur.getString(7);
			di[1]=cur.getString(8);
			uri = Uri.parse("file://" + di[0]);
			imggps = uri.toString();
			ContentResolver cr = this.getContentResolver();
		    try {
		    	bi = BitmapFactory.decodeStream(cr.openInputStream(uri));
			    imageView.setImageBitmap(bi);
			    System.gc();
			      } catch (FileNotFoundException e) {
			        Log.e("Exception", e.getMessage(),e);
			      }
			cur.close();
			mydb.close();
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
		}
		try{
			String i;
			String[] gh = imggps.split(":");
		       if(gh[0].equals("content")){
		           String[] proj = { MediaStore.Images.Media.DATA };
		           Cursor act = managedQuery(uri,proj,null,null,null);
		           int actual = act.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		           act.moveToFirst();
		           i=act.getString(actual);
		        }else{
		        	i=imggps;
		        }
			ExifInterface exif = new ExifInterface(i);
			imgps[0]=exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
			imgps[1]=exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
		}catch( IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
	    }
		lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		lm.addGpsStatusListener(statusListener);
		ll=new LocationListener()
		{
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				latitude=location.getLatitude();				//??��?�緯�???
				longitude=location.getLongitude();			//??��?��?�度
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
		button4.setOnClickListener(this);
		Button button5 = (Button) findViewById(R.id.button5);
		button5.setOnClickListener(new Button5ClickListener());
		Button button6 = (Button) findViewById(R.id.button6);
		button6.setOnClickListener(new Button6ClickListener());
		Button button7 = (Button) findViewById(R.id.button7);
		button7.setOnClickListener(new Button7ClickListener());
		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
        	Intent intent = new Intent(Sfgtdet.this, Sfgt2.class);
			intent.putExtra("SELECTED_GREETING", item);
		    startActivity(intent);
	        finish();
	        if(bi!=null&&!bi.isRecycled()){
				 bi.recycle();
		    }
		    System.gc();
			bi=null;
			((ImageView) findViewById(R.id.iv)).setImageBitmap(bi);
        }
        return super.onKeyDown(keyCode, event);
    }
	class ButtonClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			EditText editText1 = (EditText) findViewById(R.id.editText1);
			EditText editText2 = (EditText) findViewById(R.id.editText2);
			EditText editText3 = (EditText) findViewById(R.id.editText3);
			EditText editText4 = (EditText) findViewById(R.id.editText4);
			EditText editText5 = (EditText) findViewById(R.id.editText5);
			EditText editText6 = (EditText) findViewById(R.id.editText6);
			mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
			ContentValues cv=new ContentValues();
			if("".equals(editText1.getText().toString().trim())){
				int i=spf.getInt("sa", 1);
				na=getString(R.string.sfgt13)+getString(R.string.sfgt12)+i;
				cv.put(NAME,na);
				SharedPreferences.Editor ed=spf.edit();
			    ed.putInt("sa", i+1);
	        	ed.commit();
			}else{
			    cv.put(NAME,editText1.getText().toString());
			}
		    cv.put(GPSX, editText2.getText().toString());
		    cv.put(GPSY, editText4.getText().toString());
		    cv.put(GTMA,editText3.getText().toString());
		    cv.put(NAME2,editText5.getText().toString());
		    cv.put(GTMA2,editText6.getText().toString());
		    if(vigt[0]!=""&&bi!=null||vigt[0]==""&&bi==null){
		    	try {
		    		dil(di);
					if(bi!=null){
						int i=spf.getInt("bi", 0);
					    // ??��?��?�部?��存�?�置路�??
					    String path = Environment.getExternalStorageDirectory().toString()+"/Keelung Start!/Sfgt";
					    // ??��?��?��??
					    File file = new File( path, "Image"+i+".jpg");
					    SharedPreferences.Editor ed=spf.edit();
					    ed.putInt("bi", i+1);
		            	   ed.commit();
					    // ??��?��?��?�串�???
					    FileOutputStream out = new FileOutputStream(file );
					    // �??? Bitmap壓縮??��?��?�格式�?��?��?�並寫入檔�?�串�???
					    bi.compress ( Bitmap. CompressFormat.JPEG , 100 , out);
					    vigt[0] = path+"/"+"Image"+i+".jpg"; 
					    ExifInterface exif = new ExifInterface(vigt[0]);
					    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, editText2.getText().toString());
					    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, editText4.getText().toString());
					    exif.saveAttributes();
					    path = Environment.getExternalStorageDirectory().toString()+"/Keelung Start!/Sfgt2";
					    file = new File( path, "Image"+i+".jpg");
					    out = new FileOutputStream(file );
					    if(bi.getHeight()>80){
					    	int h= bi.getHeight();
					    	int w= bi.getWidth();
						    h=h/80;
						    w=w/h;
						    bi=Bitmap.createScaledBitmap(bi,w,80,true);
					    }else if(bi.getWidth()>80){
					    	int h= bi.getHeight();
					    	int w= bi.getWidth();
						    w=w/80;
						    h=h/w;
						    bi=Bitmap.createScaledBitmap(bi,80,h,true);
					    }
					    bi.compress ( Bitmap. CompressFormat.JPEG , 100 , out);
					    vigt[1] = path+"/"+"Image"+i+".jpg"; 
					    // ?��?��並�?��?��?��?�串�???
					    out.flush ();
					    out.close ();
					}
				} catch (FileNotFoundException e) {
				    // TODO Auto-generated catch block
				    e.printStackTrace ();
				} catch (IOException e) {
				    // TODO Auto-generated catch block
				    e.printStackTrace ();
				}
			    cv.put(GEVI,vigt[0]);
			    cv.put(GEVI2,vigt[1]);
		    }
		    try 
	        { 
		    	//?��??��?��?�入GPS座�?�目??�無??�能，�?��?��?��??
			    ExifInterface exif = new ExifInterface(vigt[0]);
			    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, imgps[0]);
			    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, imgps[1]);
			    exif.saveAttributes();
	        }catch (IOException e) { 
	            e.printStackTrace(); 
	        } catch(Exception e){
	        	e.printStackTrace();
		    }
			mydb.update(SFGTB_NAME, cv, "_id=?", new String[]{bt});
			cv=new ContentValues();
			if("".equals(editText1.getText().toString().trim())){
				cv.put(NAME,na);
			}else{
			    cv.put(NAME,editText1.getText().toString());
			}
			 cv.put(GDGPSX,editText2.getText().toString());
			 cv.put(GDGPSY,editText4.getText().toString());
			 Cursor cur=mydb.query(LUGF2_NAME, new String[] {GT,SQLNAME}, null, null, null, null, null);
				for(int i=0;i<cur.getCount();i++){
					cur.moveToPosition(i);
					mydb.update(cur.getString(1), cv, "ph=? AND pf=?", new String[]{String.valueOf(item),"1"});
				}
		    mydb.close();
	        Intent intent = new Intent(Sfgtdet.this, Sfgt2.class); 
			intent.putExtra("SELECTED_GREETING", item);
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
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
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
			lv.setAdapter(new Listview2(Sfgtdet.this, tiem2));
			ad=new AlertDialog.Builder(Sfgtdet.this).setTitle(getString(R.string.sfgt7)).setView(layout).show();
		}
	}
	class onlist implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
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
				if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
					sound.play(ButtonSound, 1, 1, 0, 0, 1);
				}
				Intent intent = new Intent(Sfgtdet.this, Sfgt2.class); 
				intent.putExtra("SELECTED_GREETING", item);
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
		
		public void onClick(View v) {
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.con));
	    	builder.setMessage(getString(R.string.sfgt14));
	    	Delete de = new Delete();
	    	builder.setNegativeButton(getString(R.string.sfgt1), de);
	    	builder.setPositiveButton(getString(R.string.ca), de);
	    	builder.show();
		}
		private class Delete implements
		android.content.DialogInterface.OnClickListener{
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
					sound.play(ButtonSound, 1, 1, 0, 0, 1);
				}
				if (which == DialogInterface.BUTTON_NEGATIVE){
					 dialog.dismiss();
				}else if (which == DialogInterface.BUTTON_POSITIVE){
					View vi = getLayoutInflater().inflate(R.layout.pleasewait,
							(ViewGroup) findViewById(R.layout.pleasewait));
					TextView pwtv=(TextView)vi.findViewById(R.id.textView1);
					pwtv.setTextSize(size);
					AlertDialog dead=new AlertDialog.Builder(Sfgtdet.this).setView(vi).setCancelable(false).show();
					new Thread(new Run(dead)).start();
				}
			}
		}
		class Run implements Runnable{
			AlertDialog dead;
			public Run(AlertDialog dead){
				this.dead=dead;
			}
			@Override
			public void run() {
				dil(di);
				mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
				Cursor cur=mydb.query(LUGF2_NAME, new String[] {ID,GT,SQLNAME}, null, null, null, null, null);
				for(int i=0;i<cur.getCount();i++){
		            cur.moveToPosition(i);
		            int sg=0,gt=cur.getInt(1);
		            LUGF_NAME=cur.getString(2);
		            Cursor cur2=mydb.query(LUGF_NAME, new String[] {PH,PF}, null, null, null, null, null);
		            for(int f=0;f<gt;f++){
		            	cur2.moveToPosition(f);
		    			if(cur2.getInt(0)==item&&cur2.getInt(1)==1){
		    				sg++;
		    			}
		            }
		            ContentValues cv=new ContentValues();
			    	if(gt!=0){
				    	cv.put(GT, gt-sg);
			 			mydb.update(LUGF2_NAME, cv, "_id=?", new String[]{cur.getString(0)});
			    	}
					mydb.delete(LUGF_NAME, "ph=? AND pf=?", new String[]{String.valueOf(item),"1"});
					cur2=mydb.query(SFGTB_NAME, null, null, null, null, null, null);
				    cv=new ContentValues();
		            int j=cur2.getCount();
		            for(int l=item;l<j;l++){
		            	cv.put(PH,String.valueOf(l));
		    			mydb.update(LUGF_NAME, cv, "ph=? AND pf=?", new String[]{String.valueOf(l+1),"1"});
		            }
				}
	            mydb.delete(SFGTB_NAME, "_id=?", new String[]{bt});
				cur.close();
				mydb.close();
				han.post(new Fal(dead));
			}
		}
		class Fal implements Runnable{
			AlertDialog dead;
			public Fal(AlertDialog dead){
				this.dead=dead;
			}
			@Override
			public void run() {
				dead.dismiss();
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
		class Button5ClickListener implements  OnClickListener {
			@Override
			public void onClick(View V) {
				if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
					sound.play(ButtonSound, 1, 1, 0, 0, 1);
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
	       ContentResolver cr = this.getContentResolver();
	     try {
	    	 uri = data.getData();
		     Log.e("uri", uri.toString());
		     vigt[0]=uri.toString();
		     imggps=uri.toString();
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
		    	builder.show();
	      }
	       try{
				String i;
				String[] gh = imggps.split(":");
			       if(gh[0].equals("content")){
			           String[] proj = { MediaStore.Images.Media.DATA };
			           Cursor act = managedQuery(uri,proj,null,null,null);
			           int actual = act.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			           act.moveToFirst();
			           i=act.getString(actual);
			        }else{
			        	i=imggps;
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
				if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
					sound.play(ButtonSound, 1, 1, 0, 0, 1);
				}
				if (which == DialogInterface.BUTTON_NEGATIVE){
					 dialog.dismiss();
				}else if (which == DialogInterface.BUTTON_POSITIVE){
					Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
				    startActivity(intent);
				}
			}
		}
		class Button6ClickListener implements  OnClickListener {
			@Override
			public void onClick(View v) {
				if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
					sound.play(ButtonSound, 1, 1, 0, 0, 1);
				}
			     Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			     startActivityForResult(intent, 0);
			}
		}
		class Button7ClickListener implements  OnClickListener {
			@Override
			public void onClick(View v) {
				if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
					sound.play(ButtonSound, 1, 1, 0, 0, 1);
				}
			    if(bi!=null&&!bi.isRecycled()){
					 bi.recycle();
			    }
			    System.gc();
				bi=null;
				vigt[0]="";
				vigt[1]="";
				((ImageView) findViewById(R.id.iv)).setImageBitmap(bi);
			}
		}
		private void dil(String[] fi) {
			File out = new File(fi[0]);
			if(out.exists()){
				out.delete();
			}
			out = new File(fi[1]);
			if(out.exists()){
				out.delete();
			}
	}
}