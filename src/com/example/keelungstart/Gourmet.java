package com.example.keelungstart;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Gourmet extends Activity {
	private final static String gte_NAME="Sfgtsql.pref";
	private ArrayList<HashMap> tiem2;
	TextView tv;
	ListView listview;
	Button gtkbut;
	SoundPool sound;
	private SQLiteDatabase mydb=null;
	private final static String SQL_NAME="Sfgtsql.db";//SQL名稱
	private final static String SFGTB_NAME="Sfgtbname";//表名稱
	private final static String NAME="name";//資料項目:景點名稱
	private final static String GEVI2="gevi2";
	int size,pts=0,ButtonSound;
	private ViewPager viewPager;
	private PagerTabStrip pagerTabStrip;
	private List<View> viewList;
	private List<String> titleList;
	protected static final int MENU_BUTTON_1 = Menu.FIRST;
	protected static final int MENU_BUTTON_2 = Menu.FIRST + 1;
	protected static final int MENU_BUTTON_3 = Menu.FIRST + 2;
	String[] st;
	Menu gmenu;
	Listview lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gourmet);
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		setTitle(getString(R.string.gt));
		sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		ButtonSound = sound.load(this, R.raw.button , 1);
		if(spf.getInt("text", 0)==0){
			 size=15;
		}else if(spf.getInt("text", 0)==1){
			size=20;
		}else if(spf.getInt("text", 0)==2){
			size=25;
		}else if(spf.getInt("text", 0)==3){
			size=30;
		}
		initView();
	}
	
	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		pagerTabStrip=(PagerTabStrip) findViewById(R.id.pagertab);
		pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.white)); 
		pagerTabStrip.setDrawFullUnderline(false);
		pagerTabStrip.setBackgroundColor(getResources().getColor(R.color.darkgray));
		pagerTabStrip.setTextSpacing(50);
		pagerTabStrip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
		LayoutInflater lf = getLayoutInflater().from(this);
		viewList = new ArrayList<View>();
		viewList.add(lf.inflate(R.layout.gourmet2, null));
		viewList.add(lf.inflate(R.layout.sfgt, null));

		titleList = new ArrayList<String>();
		titleList.add(getString(R.string.gt));
		titleList.add(getString(R.string.sfgt12));

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
			public CharSequence getPageTitle(int position) {

				return titleList.get(position);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(viewList.get(position));
				if(position==0){
					gt();
				}else if(position==1){
					sql(false);
				}
				return viewList.get(position);
			}
		};
		viewPager.setAdapter(pagerAdapter);
	}
	public void gt(){
		listview = (ListView) findViewById(R.id.listview);
		tiem2 = new ArrayList<HashMap>();
		Resources res=getResources();
		st=res.getStringArray(R.array.gtent);
		HashMap hh;
		for(int i=0;i<st.length;i++)
		{
			hh = new HashMap();
    		hh.put("id", i);
    		hh.put("name",st[i]);
    		hh.put("pf", 1);
    		hh.put("pf2", i);
    		hh.put("tv", size);
    		tiem2.add(hh); 
		}
		listview.setOnItemClickListener(new ListViewClickListener());
		listview.setAdapter(new Listview(Gourmet.this, tiem2));
	}
	public void sql(boolean sc){
		tv=(TextView)findViewById(R.id.textView1);
		listview = (ListView) findViewById(R.id.listViewgtk);
		listview.setSelected(true);
		tiem2 = new ArrayList<HashMap>();
		HashMap hh;
		tv.setTextSize(size);
		try
		{
			mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
			Cursor cur=mydb.query(SFGTB_NAME, new String[] {NAME,GEVI2}, null, null, null, null, null);
			int sln=cur.getCount();
			if(sln==0){
            	tv.setVisibility(View.VISIBLE);
            	listview.setVisibility(View.INVISIBLE);
				tv.setText(getString(R.string.nosfgt));
			}else{
            	tv.setVisibility(View.INVISIBLE); 
            	listview.setVisibility(View.VISIBLE);
				for(int i=0;i<sln;i++)
				{
					cur.moveToPosition(i);
					hh = new HashMap();
            		hh.put("id", i);
            		hh.put("name",cur.getString(0));
            		hh.put("tp", cur.getString(1));
            		hh.put("pf", 1);
            		hh.put("pf2", -1);
            		hh.put("tv", size);
            		tiem2.add(hh); 
				}
			}
			cur.close();
			mydb.close();
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
		}

		if(sc){
			lv.setListview(tiem2);
			lv.notifyDataSetChanged();
		}else{
			lv=new Listview(Gourmet.this, tiem2);
			listview.setOnItemClickListener(new ListViewClickListener2());
			listview.setAdapter(lv);
		}
	}
	
	class ListViewClickListener2 implements OnItemClickListener{
		public void onItemClick(AdapterView<?> parent,
				View view,
				int position,
				long id) {
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), Sfgt2.class);
			intent.putExtra("SELECTED_GREETING",position);
			Gourmet.this.startActivityForResult(intent,0);
		}
	}
	
	class ListViewClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> parent,
				View view,
				int position,
				long id) {
			Intent intent = new Intent();
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			intent.setClass(getApplicationContext(), Gt.class);
			intent.putExtra("SELECTED_GREETING",position);
			Gourmet.this.startActivity(intent);
		}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		gt();
	    sql(true);
	}
	 public class MyOnPageChangeListener implements OnPageChangeListener {
	        @Override
	        public void onPageSelected(int arg0) {
				if(arg0==0){
					setTitle(getString(R.string.gt));
					if(gmenu!=null){
						gmenu.getItem(0).setTitle(getString(R.string.fggt));
						gmenu.getItem(1).setVisible(false);
					}
				}else if(arg0==1){
					setTitle(getString(R.string.sfgt12));
					if(gmenu!=null){
						gmenu.getItem(0).setTitle(getString(R.string.fgsfgt));
						gmenu.getItem(1).setVisible(true);
					}
				}
				pts=arg0;
	        }

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
	    }
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_BUTTON_1, 0, getString((pts==0)?R.string.fggt:R.string.fgsfgt));
        menu.add(0, MENU_BUTTON_2, 0, getString(R.string.gz)+getString(R.string.sfgt12));
        menu.add(0, MENU_BUTTON_3, 0, getString(R.string.search2));
		gmenu=menu;
		if(pts==0)
			gmenu.getItem(1).setVisible(false);
	    return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
    	int item_id = item.getItemId();
    	if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
			sound.play(ButtonSound, 1, 1, 0, 0, 1);
		}
		Intent intent;
    	switch (item_id){
    	    case MENU_BUTTON_1:
    	    	if(pts==0){
    			    intent = new Intent(Gourmet.this,Gtksu.class);
    			    Gourmet.this.startActivity(intent);
    	    	}else{
    		        intent = new Intent(Gourmet.this, Ftsf.class); 
    		        Gourmet.this.startActivityForResult(intent,0);
    	    	}
			    break;
    		case MENU_BUTTON_2:
    	        intent = new Intent(Gourmet.this, Gz.class); 
    	        Gourmet.this.startActivityForResult(intent,0);
    			break;
    		case MENU_BUTTON_3:
    	        intent = new Intent(Gourmet.this, Search.class);
    			intent.putExtra("SELECTED_GREETING",pts);
    	        Gourmet.this.startActivityForResult(intent,0);
    			break;
    		default: return false;
    	}
    	return true;
    }
}

