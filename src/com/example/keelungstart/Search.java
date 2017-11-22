package com.example.keelungstart;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Search extends Activity{
	private final static String gte_NAME="Sfgtsql.pref";
	private SQLiteDatabase mydb=null;
	private final static String SQL_NAME="Sfgtsql.db";//SQL名稱
	private final static String SFGTB_NAME="Sfgtbname";//表名稱
	private final static String NAME="name";//資料項目:景點名稱
	private final static String GEVI2="gevi2";
	int size,pf;
	TextView tv;
	EditText et;
	ListView listview;
	Listview lv;
	String[] st;
	private ArrayList<HashMap> tiem2;
	private ArrayList<Integer> gt;
	SoundPool sound;
	HashMap hh;
	boolean start=true;
	int ButtonSound;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
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
		et=(EditText)findViewById(R.id.editText1);
		et.addTextChangedListener(watcher); 
		tv=(TextView)findViewById(R.id.textView1);
		tv.setTextSize(size);
		setTitle(getString(R.string.search2));
		Intent intent = this.getIntent();
		setResult(RESULT_OK, intent);
		pf=intent.getExtras().getInt("SELECTED_GREETING");
		if(pf==0){
    		gt();
    	}else{
    		sfgt();
    	}
	}
	private  TextWatcher watcher =  new  TextWatcher(){  
		  
        @Override  
        public  void  afterTextChanged(Editable s) {  
            // TODO Auto-generated method stub  
              
        }  
  
        @Override  
        public  void  beforeTextChanged(CharSequence s,  int  start,  int  count,  
                int  after) {  
            // TODO Auto-generated method stub  
              
        }  
  
        @Override  
        public  void  onTextChanged(CharSequence s,  int  start,  int  before,  
                int  count) { 
        	if(pf==0){
        		gt();
        	}else{
        		sfgt();
        	}
        }  
    };
    public void gt(){
		Resources res=getResources();
		st=res.getStringArray(R.array.gtent);
		listview = (ListView) findViewById(R.id.listView1);
		tiem2 = new ArrayList<HashMap>();
		gt=new ArrayList<Integer>();
		HashMap hh;
		for(int i=0;i<st.length;i++)
		{
			if(st[i].indexOf(et.getText().toString())!=-1){
				hh = new HashMap();
	    		hh.put("id", i);
	    		hh.put("name",st[i]);
	    		hh.put("pf", 1);
	    		hh.put("pf2", i);
        		hh.put("tv", size);
	    		gt.add(i);
	    		tiem2.add(hh); 
	    	}
		}
		if(tiem2.size()==0){
			tv.setVisibility(View.VISIBLE);
        	listview.setVisibility(View.INVISIBLE);
		}else{
			tv.setVisibility(View.INVISIBLE); 
        	listview.setVisibility(View.VISIBLE);
		}
		if(start){
			listview.setOnItemClickListener(new ListViewClickListener());
			lv=new Listview(Search.this, tiem2);
			listview.setAdapter(lv);
			start=false;
		}else{
			lv.setListview(tiem2);
			lv.notifyDataSetChanged();
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
			intent.setClass(getApplicationContext(), (pf==0)?Gt.class:Sfgt2.class);
			intent.putExtra("SELECTED_GREETING",gt.get(position));
			if(pf==0)
			Search.this.startActivity(intent);
			else
			Search.this.startActivityForResult(intent,0);
		}
	}
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    	sfgt();
	}
    public void sfgt(){
		listview = (ListView) findViewById(R.id.listView1);
		tiem2 = new ArrayList<HashMap>();
		gt=new ArrayList<Integer>();
		HashMap hh;
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
					if(cur.getString(0).indexOf(et.getText().toString())!=-1){
						hh = new HashMap();
		        		hh.put("id", i);
		        		hh.put("name",cur.getString(0));
		        		hh.put("tp", cur.getString(1));
		        		hh.put("pf", 1);
		        		hh.put("pf2", -1);
	            		hh.put("tv", size);
		        		gt.add(i);
		        		tiem2.add(hh);  
			    	}
				}
			}
			cur.close();
			mydb.close();
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
		}
		if(tiem2.size()==0){
			tv.setVisibility(View.VISIBLE);
        	listview.setVisibility(View.INVISIBLE);
		}else{
			tv.setVisibility(View.INVISIBLE); 
        	listview.setVisibility(View.VISIBLE);
		}
		if(start){
			listview.setOnItemClickListener(new ListViewClickListener());
			lv=new Listview(Search.this, tiem2);
			listview.setAdapter(lv);
			start=false;
		}else{
			lv.setListview(tiem2);
			lv.notifyDataSetChanged();
		}
    }
}
