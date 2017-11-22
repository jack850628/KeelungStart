
package com.example.keelungstart;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;



public class Home extends Activity implements OnClickListener, OnGestureListener {
	/** Called when the activity is first created. */
	LocationManager lm;								
	LocationListener ll;
	private GpsStatus.Listener statusListener;
	private SQLiteDatabase mydb=null;
	private final static String ID="_id";
	private final static String SQL_NAME="Sfgtsql.db";//SQL名稱
	private final static String SFGTB_NAME="Sfgtbname";//景點表名稱
	private final static String THHN_NAME="Thhnname";//停車場表名稱
	private final static String LUGF2_NAME="Lugf2";//行程規劃表名稱
	private final static String NAME="name";//資料項目:景點名稱
	private final static String GPSX="gpsx";//資料項目:GPSX座標
	private final static String GPSY="gpsy";//資料項目:GPSY座標
	private final static String GEVI="gevi";//資料項目:圖片
	private final static String GEVI2="gevi2";
	private final static String GTMA="gtmn";//資料項目:景點&停車場內容
	private final static String NAME2="name2";//資料項目:交通資訊
	private final static String GTMA2="gtmn2";//資料項目:交通資訊內容
	private final static String PH="ph";//資料項目:行程規劃編號
	private final static String PF="pf";//資料項目:行程規劃判別
	private final static String GDGPSX="gdgpsx";//資料項目:終點座標X
	private final static String GDGPSY="gdgpsy";//資料項目:終點座標Y
	private final static String MC="mc";
	private final static String GT="gt";
	private final static String SQLNAME="sqlname";
	private final static String CREATE_TABLE="CREATE TABLE "+SFGTB_NAME+" ("+ID+" INTEGER PRIMARY KEY,"+ NAME+" TEXT,"+ GPSX+" TEXT,"+ GPSY+" TEXT,"+GTMA+" TEXT,"+NAME2+" TEXT,"+GTMA2+" TEXT,"+GEVI+" TEXT,"+GEVI2+" TEXT)";//景點SQL建表內容
	private final static String THHN_TABLE="CREATE TABLE "+THHN_NAME+" ("+ID+" INTEGER PRIMARY KEY,"+ NAME+" TEXT,"+ GPSX+" TEXT,"+ GPSY+" TEXT,"+GTMA+" TEXT,"+GEVI+" TEXT)";//停車場SQL建表內容
	private final static String LUGF2_TABLE="CREATE TABLE "+LUGF2_NAME+" ("+ID+" INTEGER PRIMARY KEY,"+ NAME+" INTEGER,"+ SQLNAME+" INTEGER,"+ MC+" INTEGER,"+ GT+" INTEGER)";
	private final static String gte_NAME="Sfgtsql.pref";
	SoundPool sound;
	private ViewSwitcher switcher;
	private GestureDetector gesturedetector = null;
	private ViewPager viewPager;
	private List<ImageView> viewList;
	private List<ImageView> image,image2;
	int iv[]={R.id.imageView4,R.id.imageView3,R.id.imageView2,R.id.imageView1,R.id.imageView5,
			R.id.imageView6,R.id.imageView7},mg[]={R.drawable.image,R.drawable.image2},iv2[]={R.id.imageView8,R.id.imageView9};
	int ButtonSound;
	int tin=0;
	int Time=5;//幻燈片計時器
	int pos=0;//幻燈片位置
	int pos2=0;//按鈕頁面位置
	int zs[];
	final int gts=7;//幻燈片數量
	long time1,time2;
	Thread th;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onResume();
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==0){
			DisplayMetrics dm = getResources().getDisplayMetrics();
			// 取得螢幕顯示的資料
			int ScreenWidth = dm.widthPixels;
			int ScreenHeight = dm.heightPixels;
			// 螢幕寬和高的Pixels
			if (ScreenHeight > ScreenWidth){
				setContentView(R.layout.home);
			}
			else{
				setContentView(R.layout.home2);
			}
		}else if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			 setContentView(R.layout.home);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.home2);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		setTitle(getString(R.string.home));
		sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		ButtonSound = sound.load(this, R.raw.button , 1);
		if(spf.getInt("home", 0)==0){
			Toast.makeText(Home.this,getString(R.string.gps), Toast.LENGTH_SHORT).show();
		}
			// TODO Auto-generated method stub
			lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE); 		
			statusListener = new GpsStatus.Listener()
			{
				public void onGpsStatusChanged(int event) {
					lm.getGpsStatus(null);
				}
			};
			lm.addGpsStatusListener(statusListener);
			
			ll=new LocationListener()
			{
				public void onLocationChanged(Location location) 
				{
					
					
				}
				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub
					SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
					if(spf.getInt("home", 0)==0){
					Toast.makeText(getApplicationContext(), getString(R.string.gps5), Toast.LENGTH_LONG).show();
					}
				}
				@Override
				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), getString(R.string.gps6), Toast.LENGTH_LONG).show();
				}
				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {
					// TODO Auto-generated method stub
					
				}
			};
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
			SharedPreferences.Editor ed=spf.edit();
			ed.putInt("home", 0);
	 	    ed.commit();
			mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
			try{
				Cursor cur=mydb.query(SFGTB_NAME, new String[] {NAME}, null, null, null, null, null);
				cur.close();
				}

			catch(Exception e){
		      mydb.execSQL(CREATE_TABLE);
		      }
			try{
				Cursor thhncur=mydb.query(THHN_NAME, new String[] {NAME}, null, null, null, null, null);
				thhncur.close();
				}

			catch(Exception e){
		      mydb.execSQL(THHN_TABLE);
		      }
			try{
				Cursor thhncur=mydb.query(LUGF2_NAME, new String[] {ID}, null, null, null, null, null);
				thhncur.close();
				}

			catch(Exception e){
		      mydb.execSQL(LUGF2_TABLE);
		      }
			/**---------------------------------------------------------------更新碼---------------------------------------------------------**/
			try{
				Cursor cur=mydb.query(LUGF2_NAME, new String[] {NAME}, null, null, null, null, null);
				cur.close();
				}
			catch(Exception e){
				Cursor cur=mydb.query(LUGF2_NAME, new String[] {ID,MC,GT}, null, null, null, null, null);
				int sln=cur.getCount();
				String[][] gs=new String[sln][3];
				for(int i=0;i<sln;i++){
					cur.moveToPosition(i);
					gs[i][0]=cur.getString(0);
					gs[i][1]=cur.getString(1);
					gs[i][2]=cur.getString(2);
				}
				cur.close();
				mydb.execSQL("DROP TABLE "+LUGF2_NAME);
				mydb.execSQL(LUGF2_TABLE);
				StringBuffer bf = null;
				for(int i=0;i<sln;i++){
					ContentValues cv=new ContentValues();
					cv.put(ID,gs[i][0]);
					cv.put(NAME,"未命名的行程");
					cv.put(MC,gs[i][1]);
					cv.put(GT,gs[i][2]);
					int sqlNo=spf.getInt("lugfsqlna", 0);
				    bf=new StringBuffer("lugfsql"+sqlNo);
				    cv.put(SQLNAME,bf.toString());
				    ed=spf.edit();
				    ed.putInt("lugfsqlna", sqlNo+1);
			    	ed.commit();
				    mydb.insert(LUGF2_NAME, null, cv);
			    	mydb.execSQL("CREATE TABLE "+bf.toString()+" ("+ID+" INTEGER PRIMARY KEY,"+ NAME+" TEXT,"+PH+" INTEGER,"+PF+" INTEGER,"+ GDGPSX+" TEXT,"+ GDGPSY+" TEXT)");
				}
				cur=mydb.query("Lugf", new String[] {ID,NAME,PH,PF,GDGPSX,GDGPSY}, null, null, null, null, null);
				sln=cur.getCount();
				gs=new String[sln][6];
				for(int i=0;i<sln;i++){
					cur.moveToPosition(i);
					gs[i][0]=cur.getString(0);
					gs[i][1]=cur.getString(1);
					gs[i][2]=cur.getString(2);
					gs[i][3]=cur.getString(3);
					gs[i][4]=cur.getString(4);
					gs[i][5]=cur.getString(5);
				}
				cur.close();
				mydb.execSQL("DROP TABLE "+"Lugf");
				for(int i=0;i<sln;i++){
					ContentValues cv=new ContentValues();
					cv.put(ID,gs[i][0]);
					cv.put(NAME,gs[i][1]);
					cv.put(PH,gs[i][2]);
					cv.put(PF,gs[i][3]);
					cv.put(GDGPSX,gs[i][4]);
					cv.put(GDGPSY,gs[i][5]);
				    mydb.insert(bf.toString(), null, cv);
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("恭喜您成功更新至版本V3.2");
		    	builder.setMessage("更新紀錄：\nV2.0：\n        1.修正部分BUG。\n        2.新增景點預覽圖。" );
		    	builder.setNegativeButton("確定", null);
		    	builder.show(); 
		      }
			/**------------------------------------------------------------------------------------------------------------------------------**/
			
			mydb.close();
			
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				File sdFile = android.os.Environment.getExternalStorageDirectory();
				String path = sdFile.getPath() + File.separator + "Keelung Start!";
				File dirFile = new File(path);
				if(!dirFile.exists()){
				dirFile.mkdir();
				}
				path = sdFile.getPath() + File.separator + "Keelung Start!/Sfgt";
				dirFile = new File(path);
				if(!dirFile.exists()){
				dirFile.mkdir();
				}
				path = sdFile.getPath() + File.separator + "Keelung Start!/Sfgt2";
				dirFile = new File(path);
				if(!dirFile.exists()){
				dirFile.mkdir();
				}
				path = sdFile.getPath() + File.separator + "Keelung Start!/Tsfgt";
				dirFile = new File(path);
				if(!dirFile.exists()){//如果資料夾不存在
				dirFile.mkdir();//建立資料夾
				}
				path = spf.getString("path", Environment.getExternalStorageDirectory().toString() + "/Keelung Start!/Backup");
				dirFile = new File(path);
				if(!dirFile.exists()){
				dirFile.mkdirs();
				}
			}
			
		
				Button button = (Button) findViewById(R.id.button1);
				button.setOnClickListener(new ButtonClickListener());
				button.setOnTouchListener(new OTL());
				button.setText(getString(R.string.th)+"    ");
				
				Button button2 = (Button) findViewById(R.id.button2);
				button2.setOnClickListener(new Button2ClickListener());
				button2.setOnTouchListener(new OTL());
				button2.setText(getString(R.string.gt)+"    ");
				
				Button button3 = (Button) findViewById(R.id.button3);
				button3.setOnClickListener(new Button3ClickListener());
				button3.setOnTouchListener(new OTL());
				button3.setText(getString(R.string.att)+"  ");
				
				Button button4 = (Button) findViewById(R.id.button4);
				button4.setOnClickListener(new Button4ClickListener());
				button4.setOnTouchListener(new OTL());
				
				Button button6 = (Button) findViewById(R.id.button6);
				button6.setOnClickListener(new Button5ClickListener());
				button6.setOnTouchListener(new OTL());
				
				Button button7 = (Button) findViewById(R.id.button7);
				button7.setOnClickListener(new Button6ClickListener());
				button7.setOnTouchListener(new OTL());
				
				Button lugfbut = (Button) findViewById(R.id.lugfbut);
				lugfbut.setOnClickListener(new LugfbutClickListener());
				lugfbut.setOnTouchListener(new OTL());
				lugfbut.setText(getString(R.string.lugf7)+"    ");
				
				Button button5 = (Button) findViewById(R.id.b5);
				button5.setOnClickListener(this);
				button5.setOnTouchListener(new OTL());
				
				switcher = (ViewSwitcher) findViewById(R.id.viewSwitcher1);
				gesturedetector = new GestureDetector(this, this);
				
				initView();
				th = new Thread(Run);
				th.start();

				
			}
	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setOnPageChangeListener(page);
		
		zs=new int[new Gtivtext().getiv().length];
		for(int i=0;i<zs.length;i++){
			zs[i]=i;
		}
		/**      使用頭尾夾擊型的亂數調換使zs陣列更加雜亂              **/
		for(int i=0,j=zs.length-1;i<zs.length;i++,j--){
			Random ran = new Random();
			int jr,zs2=ran.nextInt(32);
			jr=zs[zs2];
			zs[zs2]=zs[i];
			zs[i]=jr;
			zs2=ran.nextInt(32);
			jr=zs[zs2];
			zs[zs2]=zs[j];
			zs[j]=jr;
		}
		viewList = new ArrayList<ImageView>();
		image = new ArrayList<ImageView>();
		for(int i=0;i<gts;i++){
			ImageView iv = new ImageView(Home.this);
			ViewGroup.LayoutParams vpivp = new ViewGroup.LayoutParams(  
					 ViewGroup.LayoutParams.FILL_PARENT, 
					 ViewGroup.LayoutParams.FILL_PARENT);  
			iv.setLayoutParams(vpivp); 
			iv.setImageResource(new Gtivtext().getiv()[zs[i]]); 
			iv.setScaleType(ImageView.ScaleType.FIT_XY);  
			iv.setOnClickListener(new viewon());
			viewList.add(iv);
			iv=(ImageView)findViewById(this.iv[i]);
			iv.setImageResource((i==0)?mg[1]:mg[0]);
			iv.setOnClickListener(new imageclick());
			image.add(iv);
		}
		image2 = new ArrayList<ImageView>();
		for(int i=0;i<iv2.length;i++){
			ImageView iv=(ImageView)findViewById(this.iv2[i]);
			iv.setImageResource((i==0)?mg[1]:mg[0]);
			iv.setOnClickListener(new imageclick());
			image2.add(iv);
		}

		PagerAdapter pagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {

				return arg0 == arg1;
			}

			@Override
			public int getCount() {

				return viewList.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(viewList.get(position));

			}

			@Override
			public int getItemPosition(Object object) {

				return super.getItemPosition(object);
			}


			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(viewList.get(position));
				return viewList.get(position);
			}

		};
		viewPager.setAdapter(pagerAdapter);
	}
	private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
        	switch(msg.what) {
            case 1:
                int toItem = viewPager.getCurrentItem() == gts-1 ? 0 : viewPager.getCurrentItem() + 1;
                viewPager.setCurrentItem(toItem, true);
                th = new Thread(Run);
                th.start();
        	}
        }
    };
    Runnable Run=new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while(Time>0){
					Thread.sleep(1000);
					Time--;
	            }
				mHandler.sendEmptyMessage(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    };
    OnPageChangeListener page=new OnPageChangeListener(){

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
            image.get(pos).setImageResource(mg[0]);
			image.get(arg0).setImageResource(mg[1]);
			pos=arg0;
			Time=5;
		}
    	
    };
    class viewon implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			Intent intent = new Intent();
			intent.setClass(Home.this, Gt.class);
			intent.putExtra("SELECTED_GREETING",zs[viewPager.getCurrentItem()]);
			Home.this.startActivity(intent);
		}
    }
    class imageclick implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.imageView4:
				viewPager.setCurrentItem(0, true);
				break;
			case R.id.imageView3:
				viewPager.setCurrentItem(1, true);
				break;
			case R.id.imageView2:
				viewPager.setCurrentItem(2, true);
				break;
			case R.id.imageView1:
				viewPager.setCurrentItem(3, true);
				break;
			case R.id.imageView5:
				viewPager.setCurrentItem(4, true);
				break;
			case R.id.imageView6:
				viewPager.setCurrentItem(5, true);
				break;
			case R.id.imageView7:
				viewPager.setCurrentItem(6, true);
				break;	
			case R.id.imageView8:
				if(pos2==1)
					nextView();
				break;	
			case R.id.imageView9:
				if(pos2==0)
					previousView();
				break;	
			}
		}
    }
	@Override
     public boolean onTouchEvent(MotionEvent event) {
             return gesturedetector.onTouchEvent(event);
     }
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	int SWIPE_MIN_VELOCITY = 100;
	int SWIPE_MIN_DISTANCE = 100;
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
		//Get Position
		float ev1X = e1.getX();
		float ev2X = e2.getX();
		
		//Get distance of X (e1) to X (e2)
		final float xdistance = Math.abs(ev1X - ev2X);
        final float xvelocity = Math.abs(velocityX);
        
        if( (xvelocity > SWIPE_MIN_VELOCITY) && (xdistance > SWIPE_MIN_DISTANCE) )
        {
			if(ev1X > ev2X)//Switch Left
				previousView();
			else//Switch Right
				nextView();
        }
        
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	
	//Next, Previous Views
	private void previousView() {
		
			//Previous View
			switcher.setInAnimation(this, R.anim.in);
			switcher.setOutAnimation(this,R.anim.out);
			switcher.showPrevious();
			image2.get(pos2).setImageResource(mg[0]);
			pos2=(pos2==0)?1:0;
			image2.get(pos2).setImageResource(mg[1]);
	}
	private void nextView() {
		
			//Next View
			switcher.setInAnimation(this, R.anim.in2);
			switcher.setOutAnimation(this, R.anim.out2);
			switcher.showNext();
			image2.get(pos2).setImageResource(mg[0]);
			pos2=(pos2==0)?1:0;
			image2.get(pos2).setImageResource(mg[1]);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
        	if(tin<=1){
            	tin++;
        	}
        	if(tin==1){
            	time1 = System.currentTimeMillis();
        		Toast.makeText(getApplicationContext(), getString(R.string.bre), Toast.LENGTH_LONG).show();
        	}else{
        		time2 = System.currentTimeMillis();
        		if(time2-time1<=2000){
        			lm.removeUpdates(ll);							
					lm.removeGpsStatusListener(statusListener);
					 System.exit(0);
        		}
        		time1 = System.currentTimeMillis();
        		Toast.makeText(getApplicationContext(), getString(R.string.bre), Toast.LENGTH_LONG).show();
        	}
        }
        return false;
    }
	public void onClick(View v) {
        if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
			sound.play(ButtonSound, 1, 1, 0, 0, 1);
		}
    	QuitApp qa = new QuitApp();
    	new AlertDialog.Builder(this).setTitle(getString(R.string.con)).setMessage(getString(R.string.con1)).
    	setNegativeButton(getString(R.string.sfgt1), qa).setPositiveButton(getString(R.string.ca), qa).show();
	}
	private class QuitApp implements
	android.content.DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			switch(which){
			case DialogInterface.BUTTON_NEGATIVE:
				dialog.dismiss();
				break;
			case DialogInterface.BUTTON_POSITIVE:
				lm.removeUpdates(ll);							
				lm.removeGpsStatusListener(statusListener);
				System.exit(0);
				break;
			}
		}
	}
	class OTL implements OnTouchListener{
		int X1;
		boolean down=false;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(event.getAction()==MotionEvent.ACTION_DOWN&&!down){
				X1=(int)event.getRawX();
				down=true;
			}else if(event.getAction()==MotionEvent.ACTION_UP){
				if(Math.abs(X1 - (int)event.getRawX())>
				(int)(getResources().getDisplayMetrics().widthPixels*0.125))//滑動距離大於螢幕寬度的1/8
					if(X1 > (int)event.getRawX())
						previousView();
					else
						nextView();
				down=false;
			}
			return false;
		}
	}
	class ButtonClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
		    Intent intent = new Intent ();
		    intent.setClass(Home.this,Gogo.class);
		    Home.this.startActivity(intent);
		}
	}
	
	class Button2ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
		    Intent intent = new Intent ();
		    intent.setClass(Home.this,Gourmet.class);
		    Home.this.startActivity(intent);
		}
	}
	class Button3ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
		    Intent intent = new Intent ();
		    intent.setClass(Home.this,Attractions.class);
		    Home.this.startActivity(intent);
		}
	}
	class Button4ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
		    Intent intent = new Intent ();
		    intent.setClass(Home.this,Arrangement.class);
		    Home.this.startActivity(intent);
		    finish();
		}
	}
	class Button5ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
		    Intent intent = new Intent ();
		    //intent.setClass(Home.this,Jtgs.class);
		    //Home.this.startActivity(intent);
		    intent.setClass(Home.this,Kegjn.class);
	        Home.this.startActivity(intent);
		}
	}
	class Button6ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
		    Intent intent = new Intent ();
		    intent.setClass(Home.this,Gng.class);
		    Home.this.startActivity(intent);
		}
	}
    class LugfbutClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
		    Intent intent = new Intent ();
		    intent.setClass(Home.this,Lugfitem.class);
		    Home.this.startActivity(intent);
		}
	}
}
