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
import android.media.MediaPlayer;
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

public class Search2 extends Activity{
	private final static String gte_NAME="Sfgtsql.pref";
	private SQLiteDatabase mydb=null;
	private final static String SQL_NAME="Sfgtsql.db";
	private final static String THHN_NAME="Thhnname";
	private final static String NAME="name";
	int size,pf;
	TextView tv;
	EditText et;
	ListView listview;
	String[] att;
	boolean start=true;
	private ArrayList<Integer> gt;
	private ArrayList<HashMap> tiem2;
	Listview2 lv;
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
		listview = (ListView) findViewById(R.id.listView1);
		gt=new ArrayList<Integer>();
		tiem2 = new ArrayList<HashMap>();
		Resources res=getResources();
		att=res.getStringArray(R.array.list_name);
		HashMap hh;
		for(int i=0;i<att.length;i++)
		{
			if(att[i].indexOf(et.getText().toString())!=-1){
	    		gt.add(i);
	    		hh = new HashMap();
	    		hh.put("id", i);
	    		hh.put("name",att[i]);
	    		hh.put("tv", size);
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
			lv=new Listview2(Search2.this, tiem2);
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
				MediaPlayer.create(getApplicationContext(), R.raw.button).start();
			}
			intent.setClass(getApplicationContext(), Ghth.class);
			intent.putExtra("SELECTED_GREETING",gt.get(position));
			Search2.this.startActivity(intent);
		}
	}
    class ListViewClickListener2 implements OnItemClickListener{
		public void onItemClick(AdapterView<?> parent,
				View view,
				int position,
				long id) {
			Intent intent = new Intent();
			if(getSharedPreferences(gte_NAME,0).getInt("music", 0)==0){
				MediaPlayer.create(getApplicationContext(), R.raw.button).start();
			}
			intent.setClass(getApplicationContext(), Tsfgt2.class);
			intent.putExtra("SELECTED_GREETING",gt.get(position));
			Search2.this.startActivityForResult(intent,0);
		}
	}
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    	sfgt();
	}
    public void sfgt(){
		listview = (ListView) findViewById(R.id.listView1);
		gt=new ArrayList<Integer>();
		try
		{
			mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
			Cursor cur=mydb.query(THHN_NAME, new String[] {NAME}, null, null, null, null, null);
			int sln=cur.getCount();
			tiem2 = new ArrayList<HashMap>();
			HashMap hh;
			if(sln==0){
            	tv.setVisibility(View.VISIBLE);
            	listview.setVisibility(View.INVISIBLE);
				tv.setText(getString(R.string.nosetatt));
			}else{
            	tv.setVisibility(View.INVISIBLE); 
            	listview.setVisibility(View.VISIBLE);
				for(int i=0;i<sln;i++)
				{
					cur.moveToPosition(i);
					if(cur.getString(0).indexOf(et.getText().toString())!=-1){
						cur.moveToPosition(i);
		        		gt.add(i); 
						hh = new HashMap();
						hh.put("id", i);
	            		hh.put("name",cur.getString(0));
	            		hh.put("tv", size);
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
			listview.setOnItemClickListener(new ListViewClickListener2());
			lv=new Listview2(Search2.this, tiem2);
			listview.setAdapter(lv);
			start=false;
		}else{
			lv.setListview(tiem2);
			lv.notifyDataSetChanged();
		}
    }
}
