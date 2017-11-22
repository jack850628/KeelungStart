package com.example.keelungstart;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

public class Nmfc extends Activity {
    private Spinner sp,sp2;
    private final static String gte_NAME="Sfgtsql.pref";//¦Û­q¸q¦WºÙ
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nmfc);
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		sp = (Spinner) findViewById(R.id.nmfc);
		sp2 = (Spinner) findViewById(R.id.nmfc2);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		Resources res = getResources(); 
		String[] tit=res.getStringArray(R.array.arrangement);
		setTitle(tit[bundle.getInt("PF")]);
		TextView tv=(TextView)findViewById(R.id.textView1);
		TextView tv2=(TextView)findViewById(R.id.textView2);
		int size = 0;
		if(spf.getInt("text", 0)==0){
			size=15;
			 tv.setTextSize(15);
			 tv2.setTextSize(15);
		}else if(spf.getInt("text", 0)==1){
			size=20;
			tv.setTextSize(20);
			tv2.setTextSize(20);
		}else if(spf.getInt("text", 0)==2){
			size=25;
			tv.setTextSize(25);
			tv2.setTextSize(25);
		}else if(spf.getInt("text", 0)==3){
			size=30;
			tv.setTextSize(30);
			tv2.setTextSize(30);
		}
		tit=res.getStringArray(R.array.nmfc);
		HashMap hh;
		ArrayList<HashMap> tiem2 = new ArrayList<HashMap>();
		for(int i=0;i<tit.length;i++)
		{
			hh = new HashMap();
    		hh.put("id", i);
    		hh.put("name",tit[i]);
    		hh.put("tv", size);
    		tiem2.add(hh); 
		}
		sp.setAdapter(new Listview2(this, tiem2));
		tit=res.getStringArray(R.array.nmfc2);
		tiem2 = new ArrayList<HashMap>();
		for(int i=0;i<tit.length;i++)
		{
			hh = new HashMap();
    		hh.put("id", i);
    		hh.put("name",tit[i]);
    		hh.put("tv", size);
    		tiem2.add(hh); 
		}
		sp2.setAdapter(new Listview2(this, tiem2));
		if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			 sp.setSelection(1);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			 sp.setSelection(2);
		}
		if(spf.getInt("nmfc2", 0)==1){
    	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		if(spf.getInt("nmfc2", 0)==0){
			 sp2.setSelection(0);
		}else if(spf.getInt("nmfc2", 0)==1){
			 sp2.setSelection(1);
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
        sp.setOnItemSelectedListener(new OnItemSelectedListener(){
        	SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
        	SharedPreferences.Editor ed=spf.edit();
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int po, long arg3) {
               switch(po){
               case 0:
            	   ed.putInt("nmfc", 0);
            	   ed.commit();
            	   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            	   break;
               case 1:
      			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            	   ed.putInt("nmfc", 1);
            	   ed.commit();
  			 break;
               case 2:	
       			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            	   ed.putInt("nmfc", 2);
            	   ed.commit();
            	   break;
               }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
               // TODO Auto-generated method stub
            }
        });
        sp2.setOnItemSelectedListener(new OnItemSelectedListener(){
        	SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
        	SharedPreferences.Editor ed=spf.edit();
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int po, long arg3) {
               switch(po){
               case 0:
            	   ed.putInt("nmfc2", 0);
            	   ed.commit();
      			   getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            	   break;
               case 1:
            	   ed.putInt("nmfc2", 1);
            	   ed.commit();
            	   getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  			 break;
               }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
               // TODO Auto-generated method stub
            }
        });
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
        	Intent intent=new Intent(Nmfc.this, Arrangement.class);
	    	Nmfc.this.startActivity(intent);
	    	finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
