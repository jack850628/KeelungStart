package com.example.keelungstart;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Gte extends Activity {
	
    private Spinner sp;
    private Spinner sp2;
    private Spinner sp3;
    private Spinner sp4;
    private Spinner sp5;
    EditText et;
    EditText et2;
    EditText et3;
    private final static String gte_NAME="Sfgtsql.pref";//¦Û­q¸q¦WºÙ


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gte); 
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
		sp = (Spinner) findViewById(R.id.gtesoinner);
		sp2 = (Spinner) findViewById(R.id.gtesoinner2);
		sp5 = (Spinner) findViewById(R.id.gtesoinner3);
		sp3 = (Spinner) findViewById(R.id.Sp1);
		sp4 = (Spinner) findViewById(R.id.Sp2);
		final TextView tv4=(TextView)findViewById(R.id.textView4);
		final TextView tv5=(TextView)findViewById(R.id.textView5);
		et = (EditText) findViewById(R.id.et);
		et.addTextChangedListener(watcher); 
		et2 = (EditText) findViewById(R.id.et2);
		et2.addTextChangedListener(watcher2); 
		et3 = (EditText) findViewById(R.id.et3);
		et3.addTextChangedListener(watcher3); 
		TextView tv1=(TextView)findViewById(R.id.textView1);
		TextView tv2=(TextView)findViewById(R.id.textView2);
		TextView tv3=(TextView)findViewById(R.id.textView3);
		int size = 0;
		if(spf.getInt("text", 0)==0){
			size=15;
			 tv1.setTextSize(15);
			 tv2.setTextSize(15);
			 tv3.setTextSize(15);
			 tv4.setTextSize(15);
			 tv5.setTextSize(15);
		}else if(spf.getInt("text", 0)==1){
			size=20;
			tv1.setTextSize(20);
			tv2.setTextSize(20);
			tv3.setTextSize(20);
			tv4.setTextSize(20);
			tv5.setTextSize(20);
		}else if(spf.getInt("text", 0)==2){
			size=25;
			tv1.setTextSize(25);
			tv2.setTextSize(25);
			tv3.setTextSize(25);
			tv4.setTextSize(25);
			tv5.setTextSize(25);
		}else if(spf.getInt("text", 0)==3){
			size=30;
			tv1.setTextSize(30);
			tv2.setTextSize(30);
			tv3.setTextSize(30);
			tv4.setTextSize(30);
			tv5.setTextSize(30);
		}
		et.setTextSize(size);
		et2.setTextSize(size);
		et3.setTextSize(size);
		tit=res.getStringArray(R.array.gtestring);
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
		sp2.setAdapter(new Listview2(this, tiem2));
		tit=res.getStringArray(R.array.gz);
		tiem2 = new ArrayList<HashMap>();
		for(int i=0;i<tit.length;i++)
		{
			hh = new HashMap();
    		hh.put("id", i);
    		hh.put("name",tit[i]);
    		hh.put("tv", size);
    		tiem2.add(hh); 
		}
		sp3.setAdapter(new Listview2(this, tiem2));
		sp4.setAdapter(new Listview2(this, tiem2));
		tit=res.getStringArray(R.array.gtestring2);
		tiem2 = new ArrayList<HashMap>();
		for(int i=0;i<tit.length;i++)
		{
			hh = new HashMap();
    		hh.put("id", i);
    		hh.put("name",tit[i]);
    		hh.put("tv", size);
    		tiem2.add(hh); 
		}
		sp5.setAdapter(new Listview2(this, tiem2));
		sp3.setVisibility(View.GONE);
		sp4.setVisibility(View.GONE);
		tv4.setVisibility(View.GONE);
		tv5.setVisibility(View.GONE);
		et.setVisibility(View.GONE);
		et2.setVisibility(View.GONE);
		et3.setVisibility(View.GONE);
		et.setText(spf.getString("gzg","0"));
		et2.setText(spf.getString("gzg2","0"));
		et3.setText(spf.getString("gzg3","0"));
		sp.setSelection(spf.getInt("td", 0));
		if(spf.getInt("td", 0)==4){
			sp3.setVisibility(View.VISIBLE);
			et.setVisibility(View.VISIBLE);
		}
		sp2.setSelection(spf.getInt("td2", 0));
		if(spf.getInt("td2", 0)==4){
			sp4.setVisibility(View.VISIBLE);
			et2.setVisibility(View.VISIBLE);
		}
		sp5.setSelection(spf.getInt("td3", 0));
		if(spf.getInt("td3", 0)==3){
			tv5.setVisibility(View.VISIBLE);
		}else if(spf.getInt("td3", 0)==4){
			tv4.setVisibility(View.VISIBLE);
			et3.setVisibility(View.VISIBLE);
		}
		sp3.setSelection(spf.getInt("gz", 0));
		sp4.setSelection(spf.getInt("gz2", 0));
        sp.setOnItemSelectedListener(new OnItemSelectedListener(){
        	SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
        	SharedPreferences.Editor ed=spf.edit();
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int po, long arg3) {
         	   ed.putInt("td", po);
               switch(po){
               case 4:	
            	   ed.commit();
            	   sp3.setVisibility(View.VISIBLE);
       			et.setVisibility(View.VISIBLE);
            	   break;
               default:		
            	   ed.commit();
            	   sp3.setVisibility(View.GONE);
           		et.setVisibility(View.GONE);
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
         	   ed.putInt("td2", po);
               switch(po){
               case 4:	
            	   ed.commit();
            	   sp4.setVisibility(View.VISIBLE);
       			et2.setVisibility(View.VISIBLE);
            	   break;
               default:	
            	   ed.commit();
           		sp4.setVisibility(View.GONE);
        		et2.setVisibility(View.GONE);
            	   break;
               }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
               // TODO Auto-generated method stub
            }
        });
        sp5.setOnItemSelectedListener(new OnItemSelectedListener(){
        	SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
        	SharedPreferences.Editor ed=spf.edit();
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int po, long arg3) {
                ed.putInt("td3", po);
               switch(po){
               case 3:	
            	   ed.commit();
            	   tv5.setVisibility(View.VISIBLE);
            	   tv4.setVisibility(View.GONE);
          		   et3.setVisibility(View.GONE);
            	   break;
               case 4:	
            	   ed.commit();
            	   if(Double.valueOf(spf.getString("gzg3","0"))>100){
            		   tv5.setVisibility(View.VISIBLE);
            	   }else{
                       tv5.setVisibility(View.GONE);
            	   }
            	   tv4.setVisibility(View.VISIBLE);
       			et3.setVisibility(View.VISIBLE);
            	   break;
               default:	
            	   ed.commit();
            	   tv4.setVisibility(View.GONE);
              	tv5.setVisibility(View.GONE);
           		et3.setVisibility(View.GONE);
            	   break;
               }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
               // TODO Auto-generated method stub
            }
        });
        sp3.setOnItemSelectedListener(new OnItemSelectedListener(){
        	SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
        	SharedPreferences.Editor ed=spf.edit();
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int po, long arg3) {
            	ed.putInt("gz", po);
         	   ed.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
               // TODO Auto-generated method stub
            }
        });
        sp4.setOnItemSelectedListener(new OnItemSelectedListener(){
        	SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
        	SharedPreferences.Editor ed=spf.edit();
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int po, long arg3) {
            	 ed.putInt("gz2", po);
          	   ed.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
               // TODO Auto-generated method stub
            }
        });
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
        	SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
        	SharedPreferences.Editor ed=spf.edit();
			if("".equals(et.getText().toString().trim())){
				ed.putString("gzg","0");
			}else{
				ed.putString("gzg",et.getText().toString());
			}
     	   ed.commit();
        }  
          
    };
    private  TextWatcher watcher2 =  new  TextWatcher(){  
		  
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
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
        	SharedPreferences.Editor ed=spf.edit();
        	if("".equals(et2.getText().toString().trim())){
        		ed.putString("gzg2","0");
        	}else{
            	ed.putString("gzg2",et2.getText().toString());
        	}
     	   ed.commit();
        }  
          
    };
    private  TextWatcher watcher3 =  new  TextWatcher(){  
		  
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
        	TextView tv5=(TextView)findViewById(R.id.textView5);
			SharedPreferences spf =  getSharedPreferences(gte_NAME,0);
        	SharedPreferences.Editor ed=spf.edit();
        	if("".equals(et3.getText().toString().trim())){
        		ed.putString("gzg3","0");
        		tv5.setVisibility(View.GONE);
        	}else{
            	ed.putString("gzg3",et3.getText().toString());
            	if(Double.valueOf(et3.getText().toString())>100){
           		   tv5.setVisibility(View.VISIBLE);
           	   }else{
                		tv5.setVisibility(View.GONE);
           	   }
        	}
     	   ed.commit();
        }  
          
    };
}