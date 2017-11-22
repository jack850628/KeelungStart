package com.example.keelungstart;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

public class Music extends Activity {
    private Spinner sp;
    private final static String gte_NAME="Sfgtsql.pref";//¦Û­q¸q¦WºÙ
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music); 
		SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
		if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		Resources res = getResources(); 
		String[] tit=res.getStringArray(R.array.arrangement);
		setTitle(tit[bundle.getInt("PF")]);
		sp = (Spinner) findViewById(R.id.gtemu);
		TextView tv=(TextView)findViewById(R.id.textView1);
		int size=0;
		if(spf.getInt("text", 0)==0){
			size=15;
			 tv.setTextSize(15);
		}else if(spf.getInt("text", 0)==1){
			size=20;
			tv.setTextSize(20);
		}else if(spf.getInt("text", 0)==2){
			size=25;
			tv.setTextSize(25);
		}else if(spf.getInt("text", 0)==3){
			size=30;
			tv.setTextSize(30);
		}
		tit=res.getStringArray(R.array.music);
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
		sp.setSelection(spf.getInt("music", 0));
        sp.setOnItemSelectedListener(new OnItemSelectedListener(){
        	SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
        	SharedPreferences.Editor ed=spf.edit();
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int po, long arg3) {
               switch(po){
               case 0:
            	   ed.putInt("music", 0);
            	   ed.commit();
            	   break;
               case 1:
            	   ed.putInt("music", 1);
            	   ed.commit();
  			 break;
              
               }
               

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
               // TODO Auto-generated method stub
            }
        });
	}
}
