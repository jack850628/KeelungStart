package com.example.keelungstart;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Gng extends Activity {
	SharedPreferences spf;
	SoundPool sound;
	DisplayMetrics dm;
	int ScreenWidth, ScreenHeight,kg;
	EditText et;
	TextView tv;
	String ng[]=new String[50];//計算顯示值
	int nh[]=new int[49];//運算符號判別值
	double gh=0,mg=0,st=0;
	int i=0,j=0;
	boolean hn=true,fh=true,pf=true,pf2=false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.menu4));
		spf =  getSharedPreferences("Sfgtsql.pref",0);
		sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		kg = sound.load(this, R.raw.button , 1);
		if(spf.getInt("nmfc", 0)==0){
			dm = getResources().getDisplayMetrics();
			// 取得螢幕顯示的資料
			ScreenWidth = dm.widthPixels;
			ScreenHeight = dm.heightPixels;
			// 螢幕寬和高的Pixels
			if (ScreenHeight > ScreenWidth){
				setContentView(R.layout.gng);
			}
			else{
				setContentView(R.layout.gng);
			}
		}else if(spf.getInt("nmfc", 0)==1){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			 setContentView(R.layout.gng); 
		}else if(spf.getInt("nmfc", 0)==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.gng); 
		}
		if(spf.getInt("nmfc2", 0)==1){
     	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
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
		Button button7 = (Button) findViewById(R.id.button7);
		button7.setOnClickListener(new Button7ClickListener());
		Button button8 = (Button) findViewById(R.id.button8);
		button8.setOnClickListener(new Button8ClickListener());
		Button button9 = (Button) findViewById(R.id.button9);
		button9.setOnClickListener(new Button9ClickListener());
		Button button10 = (Button) findViewById(R.id.button10);
		button10.setOnClickListener(new Button10ClickListener());
		Button button11 = (Button) findViewById(R.id.button11);
		button11.setOnClickListener(new Button11ClickListener());
		Button button12 = (Button) findViewById(R.id.button12);
		button12.setOnClickListener(new Button12ClickListener());
		Button button13 = (Button) findViewById(R.id.button13);
		button13.setOnClickListener(new Button13ClickListener());
		Button button14 = (Button) findViewById(R.id.button14);
		button14.setOnClickListener(new Button14ClickListener());
		Button button15 = (Button) findViewById(R.id.button15);
		button15.setOnClickListener(new Button15ClickListener());
		Button button16 = (Button) findViewById(R.id.button16);
		button16.setOnClickListener(new Button16ClickListener());
		Button button17 = (Button) findViewById(R.id.button17);
		button17.setOnClickListener(new Button17ClickListener());
		Button button18 = (Button) findViewById(R.id.button18);
		button18.setOnClickListener(new Button18ClickListener());
		Button button19 = (Button) findViewById(R.id.button19);
		button19.setOnClickListener(new Button19ClickListener());
		Button button20 = (Button) findViewById(R.id.button20);
		button20.setOnClickListener(new Button20ClickListener());
		Button button21 = (Button) findViewById(R.id.button21);
		button21.setOnClickListener(new Button21ClickListener());
		Button button22 = (Button) findViewById(R.id.button22);
		button22.setOnClickListener(new Button22ClickListener());
		Button button23 = (Button) findViewById(R.id.button23);
		button23.setOnClickListener(new Button23ClickListener());
		Button button24 = (Button) findViewById(R.id.button24);
		button24.setOnClickListener(new Button24ClickListener());
		Button button25 = (Button) findViewById(R.id.button25);
		button25.setOnClickListener(new Button25ClickListener());
		tv=(TextView)findViewById(R.id.textView1);
		tv.setTextSize(30);
		et = (EditText) findViewById(R.id.editText1);
		ng[i]=spf.getString("gng", null);
		if(ng[i]!=null){
			pf2=true;
			et.setText(ng[i]);
		}else{
			et.setText("0");
		}
		st=0;
		if(ng[i]!=null){
			st=Double.valueOf(ng[i]);
		}
		if(st!=(int)st){
			hn=false;
		}
		if(st<0){
			fh=false;
		}
	}
	class ButtonClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	tv.setText("");
	    	mg=0;
		}
	}
	class Button2ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	if(mg<0){
	    		fh=false;
	    	}
	    	if(mg==0){
	    		ng[i]=null;
		    	et.setText("0");
	    	}else if(mg==(int)mg){
	    		ng[i]=Integer.toString(Integer.valueOf((int)mg));
		    	et.setText(ng[i]);
	    	}else{
	    		ng[i]=Double.toString(mg);
	    		hn=false;
		    	et.setText(ng[i]);
	    	}
	    	pf2=true;
		}
	}
	class Button3ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	tv.setText("M");
	    	if(ng[i]==null){
		    	mg=Double.valueOf("0");
	    	}else{
	    		mg=Double.valueOf(ng[i]);
	    	}
		}
	}
	class Button4ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	tv.setText("M");
	    	if(ng[i]!=null){
		    	mg=mg+Double.valueOf(ng[i]);
	    	}
		}
	}
	class Button5ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	tv.setText("M");
	    	if(ng[i]!=null){
		    	mg=mg-Double.valueOf(ng[i]);
	    	}
		}
	}
	class Button6ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	if(pf){
	    		if(ng[i]==null||ng[i]=="0"){
			    	ng[i]="7";
			    	et.setText(ng[i]);
	    		}else{
			    	ng[i]=ng[i]+"7";
			    	et.setText(ng[i]);
	    		}
	    		pf2=true;
	    	}
		}
	}
	class Button7ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	if(pf){
	    		if(ng[i]==null||ng[i]=="0"){
			    	ng[i]="8";
			    	et.setText(ng[i]);
	    		}else{
			    	ng[i]=ng[i]+"8";
			    	et.setText(ng[i]);
	    		}
	    		pf2=true;
	    	}
		}
	}
	class Button8ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	if(pf){
	    		if(ng[i]==null||ng[i]=="0"){
			    	ng[i]="9";
			    	et.setText(ng[i]);
	    		}else{
			    	ng[i]=ng[i]+"9";
			    	et.setText(ng[i]);
	    		}
	    		pf2=true;
	    	}
		}
	}
	class Button9ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	finish();
		}
	}
	class Button10ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	if(pf){
	    		if(ng[i]==null||ng[i]=="0"){
			    	ng[i]="4";
			    	et.setText(ng[i]);
	    		}else{
			    	ng[i]=ng[i]+"4";
			    	et.setText(ng[i]);
	    		}
	    		pf2=true;
	    	}
		}
	}
	class Button11ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	if(pf){
	    		if(ng[i]==null||ng[i]=="0"){
			    	ng[i]="5";
			    	et.setText(ng[i]);
	    		}else{
			    	ng[i]=ng[i]+"5";
			    	et.setText(ng[i]);
	    		}
	    		pf2=true;
	    	}
		}
	}
	class Button12ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	if(pf){
	    		if(ng[i]==null||ng[i]=="0"){
			    	ng[i]="6";
			    	et.setText(ng[i]);
	    		}else{
			    	ng[i]=ng[i]+"6";
			    	et.setText(ng[i]);
	    		}
	    		pf2=true;
	    	}
		}
	}
	class Button13ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	if(pf){
	    		if(ng[i]==null||ng[i]=="0"){
			    	ng[i]="1";
			    	et.setText(ng[i]);
	    		}else{
			    	ng[i]=ng[i]+"1";
			    	et.setText(ng[i]);
	    		}
	    		pf2=true;
	    	}
		}
	}
	class Button14ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	if(pf){
	    		if(ng[i]==null||ng[i]=="0"){
			    	ng[i]="2";
			    	et.setText(ng[i]);
	    		}else{
			    	ng[i]=ng[i]+"2";
			    	et.setText(ng[i]);
	    		}
	    		pf2=true;
	    	}
		}
	}
	class Button15ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	if(pf){
	    		if(ng[i]==null||ng[i]=="0"){
			    	ng[i]="3";
			    	et.setText(ng[i]);
	    		}else{
			    	ng[i]=ng[i]+"3";
			    	et.setText(ng[i]);
	    		}
	    		pf2=true;
	    	}
		}
	}
	class Button16ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
        	SharedPreferences.Editor ed=spf.edit();
	    	if(pf2){
	    		while(j!=0){
		    		if(nh[j-1]==1){
		    			if(ng[i-1]==null){
				    		gh=0+Double.valueOf(ng[i]);
		    			}else if(ng[i]==null){
				    		gh=Double.valueOf(ng[i-1])+0;
		    			}else{
				    		gh=Double.valueOf(ng[i-1])+Double.valueOf(ng[i]);
		    			}
			    		if(gh==(int)gh){
			    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
			    			if(gh==0){
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", null);
				            	ed.commit();
				    		}else{
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
			    		}else{
			    			ng[i-1]=Double.toString(gh);
				    		et.setText(ng[i-1]);
				    		ed.putString("gng", ng[i-1]);
			            	ed.commit();
			    		}
			    		ng[i]=null;
			    		i--;
			    		j--;
			    	}else if(nh[j-1]==2){
		    			if(ng[i-1]==null){
				    		gh=0-Double.valueOf(ng[i]);
		    			}else if(ng[i]==null){
				    		gh=Double.valueOf(ng[i-1])-0;
		    			}else{
				    		gh=Double.valueOf(ng[i-1])-Double.valueOf(ng[i]);
		    			}
			    		if(gh==(int)gh){
			    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
			    			if(gh==0){
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", null);
				            	ed.commit();
				    		}else{
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
			    		}else{
			    			ng[i-1]=Double.toString(gh);
				    		et.setText(ng[i-1]);
				    		ed.putString("gng", ng[i-1]);
			            	ed.commit();
			    		}
			    		ng[i]=null;
			    		i--;
			    		j--;
			    	}else if(nh[j-1]==3){
		    			if(ng[i-1]==null){
				    		gh=0*Double.valueOf(ng[i]);
		    			}else if(ng[i]==null){
				    		gh=Double.valueOf(ng[i-1])*0;
		    			}else{
				    		gh=Double.valueOf(ng[i-1])*Double.valueOf(ng[i]);
		    			}
			    		if(gh==(int)gh){
			    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
			    			if(gh==0){
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", null);
				            	ed.commit();
				    		}else{
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
			    		}else{
			    			ng[i-1]=Double.toString(gh);
				    		et.setText(ng[i-1]);
				    		ed.putString("gng", ng[i-1]);
			            	ed.commit();
			    		}
			    		ng[i]=null;
			    		i--;
			    		j--;
			    	}else if(nh[j-1]==4){
		    			if(ng[i-1]==null){
				    		gh=0/Double.valueOf(ng[i]);
		    			}else if(ng[i]==null){
				    		gh=Double.valueOf(ng[i-1])/0;
		    			}else{
				    		gh=Double.valueOf(ng[i-1])/Double.valueOf(ng[i]);
		    			}
			    		if(gh==(int)gh){
			    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
			    			if(gh==0){
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", null);
				            	ed.commit();
				    		}else{
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
			    		}else{
			    			ng[i-1]=Double.toString(gh);
				    		et.setText(ng[i-1]);
				    		ed.putString("gng", ng[i-1]);
			            	ed.commit();
			    		}
			    		ng[i]=null;
			    		i--;
			    		j--;
			    	}
		    	}
	    		pf2=false;
		    	hn=true;
		    	fh=true;
		    	nh[j]=1;
		    	i++;
		    	j++;
	    	}
		}
	}
	class Button17ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	SharedPreferences.Editor ed=spf.edit();
	    	if(pf2){
	    		while(j!=0){
		    		if(nh[j-1]==1){
		    			if(ng[i-1]==null){
				    		gh=0+Double.valueOf(ng[i]);
		    			}else if(ng[i]==null){
				    		gh=Double.valueOf(ng[i-1])+0;
		    			}else{
				    		gh=Double.valueOf(ng[i-1])+Double.valueOf(ng[i]);
		    			}
			    		if(gh==(int)gh){
			    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
			    			if(gh==0){
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", null);
				            	ed.commit();
				    		}else{
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
			    		}else{
			    			ng[i-1]=Double.toString(gh);
				    		et.setText(ng[i-1]);
				    		ed.putString("gng", ng[i-1]);
			            	ed.commit();
			    		}
			    		ng[i]=null;
			    		i--;
			    		j--;
			    	}else if(nh[j-1]==2){
		    			if(ng[i-1]==null){
				    		gh=0-Double.valueOf(ng[i]);
		    			}else if(ng[i]==null){
				    		gh=Double.valueOf(ng[i-1])-0;
		    			}else{
				    		gh=Double.valueOf(ng[i-1])-Double.valueOf(ng[i]);
		    			}
			    		if(gh==(int)gh){
			    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
			    			if(gh==0){
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", null);
				            	ed.commit();
				    		}else{
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
			    		}else{
			    			ng[i-1]=Double.toString(gh);
				    		et.setText(ng[i-1]);
				    		ed.putString("gng", ng[i-1]);
			            	ed.commit();
			    		}
			    		ng[i]=null;
			    		i--;
			    		j--;
			    	}else if(nh[j-1]==3){
		    			if(ng[i-1]==null){
				    		gh=0*Double.valueOf(ng[i]);
		    			}else if(ng[i]==null){
				    		gh=Double.valueOf(ng[i-1])*0;
		    			}else{
				    		gh=Double.valueOf(ng[i-1])*Double.valueOf(ng[i]);
		    			}
			    		if(gh==(int)gh){
			    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
			    			if(gh==0){
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", null);
				            	ed.commit();
				    		}else{
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
			    		}else{
			    			ng[i-1]=Double.toString(gh);
				    		et.setText(ng[i-1]);
				    		ed.putString("gng", ng[i-1]);
			            	ed.commit();
			    		}
			    		ng[i]=null;
			    		i--;
			    		j--;
			    	}else if(nh[j-1]==4){
		    			if(ng[i-1]==null){
				    		gh=0/Double.valueOf(ng[i]);
		    			}else if(ng[i]==null){
				    		gh=Double.valueOf(ng[i-1])/0;
		    			}else{
				    		gh=Double.valueOf(ng[i-1])/Double.valueOf(ng[i]);
		    			}
			    		if(gh==(int)gh){
			    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
			    			if(gh==0){
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", null);
				            	ed.commit();
				    		}else{
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
			    		}else{
			    			ng[i-1]=Double.toString(gh);
				    		et.setText(ng[i-1]);
				    		ed.putString("gng", ng[i-1]);
			            	ed.commit();
			    		}
			    		ng[i]=null;
			    		i--;
			    		j--;
			    	}
		    	}
	    		pf2=false;
		    	hn=true;
		    	fh=true;
		    	nh[j]=2;
		    	i++;
		    	j++;
	    	}
		}
	}
	class Button18ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	SharedPreferences.Editor ed=spf.edit();
	    	if(pf2){
	    		if(j!=0){
	    			if(nh[j-1]==3){
	    				if(ng[i-1]==null){
				    		gh=0*Double.valueOf(ng[i]);
		    			}else if(ng[i]==null){
				    		gh=Double.valueOf(ng[i-1])*0;
		    			}else{
				    		gh=Double.valueOf(ng[i-1])*Double.valueOf(ng[i]);
		    			}
			    		if(gh==(int)gh){
			    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
			    			if(gh==0){
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", null);
				            	ed.commit();
				    		}else{
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
			    		}else{
			    			ng[i-1]=Double.toString(gh);
				    		et.setText(ng[i-1]);
				    		ed.putString("gng", ng[i-1]);
			            	ed.commit();
			    		}
			    		ng[i]=null;
			    		i--;
			    		j--;
	    			}
		    	}
	    		pf2=false;
		    	hn=true;
		    	fh=true;
		    	nh[j]=3;
		    	i++;
		    	j++;
	    	}
		}
	}
	class Button19ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	SharedPreferences.Editor ed=spf.edit();
	    	if(pf2){
	    		if(j!=0){
	    			if(nh[j-1]==4){
	    				if(ng[i-1]==null){
				    		gh=0/Double.valueOf(ng[i]);
		    			}else if(ng[i]==null){
				    		gh=Double.valueOf(ng[i-1])/0;
		    			}else{
				    		gh=Double.valueOf(ng[i-1])/Double.valueOf(ng[i]);
		    			}
			    		if(gh==(int)gh){
			    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
			    			if(gh==0){
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", null);
				            	ed.commit();
				    		}else{
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
			    		}else{
			    			ng[i-1]=Double.toString(gh);
				    		et.setText(ng[i-1]);
				    		ed.putString("gng", ng[i-1]);
			            	ed.commit();
			    		}
			    		ng[i]=null;
			    		i--;
			    		j--;
	    			}
		    	}
	    		pf2=false;
		    	hn=true;
		    	fh=true;
		    	nh[j]=4;
		    	i++;
		    	j++;
	    	}
		}
	}
	class Button20ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	SharedPreferences.Editor ed=spf.edit();
	    	if(pf2){
	    		while(j!=0){
		    		if(nh[j-1]==1){
		    			if(ng[i-1]==null){
				    		gh=0+Double.valueOf(ng[i]);
		    			}else if(ng[i]==null){
				    		gh=Double.valueOf(ng[i-1])+0;
		    			}else{
				    		gh=Double.valueOf(ng[i-1])+Double.valueOf(ng[i]);
		    			}
			    		if(gh==(int)gh){
			    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
			    			if(gh==0){
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", null);
				            	ed.commit();
				    		}else{
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
			    		}else{
			    			ng[i-1]=Double.toString(gh);
				    		et.setText(ng[i-1]);
				    		ed.putString("gng", ng[i-1]);
			            	ed.commit();
			    		}
			    		i--;
			    		j--;
			    	}else if(nh[j-1]==2){
		    			if(ng[i-1]==null){
				    		gh=0-Double.valueOf(ng[i]);
		    			}else if(ng[i]==null){
				    		gh=Double.valueOf(ng[i-1])-0;
		    			}else{
				    		gh=Double.valueOf(ng[i-1])-Double.valueOf(ng[i]);
		    			}
			    		if(gh==(int)gh){
			    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
			    			if(gh==0){
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", null);
				            	ed.commit();
				    		}else{
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
			    		}else{
			    			ng[i-1]=Double.toString(gh);
				    		et.setText(ng[i-1]);
				    		ed.putString("gng", ng[i-1]);
			            	ed.commit();
			    		}
			    		i--;
			    		j--;
			    	}else if(nh[j-1]==3){
		    			if(ng[i-1]==null){
				    		gh=0*Double.valueOf(ng[i]);
		    			}else if(ng[i]==null){
				    		gh=Double.valueOf(ng[i-1])*0;
		    			}else{
				    		gh=Double.valueOf(ng[i-1])*Double.valueOf(ng[i]);
		    			}
			    		if(gh==(int)gh){
			    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
			    			if(gh==0){
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", null);
				            	ed.commit();
				    		}else{
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
			    		}else{
			    			ng[i-1]=Double.toString(gh);
				    		et.setText(ng[i-1]);
				    		ed.putString("gng", ng[i-1]);
			            	ed.commit();
			    		}
			    		i--;
			    		j--;
			    	}else if(nh[j-1]==4){
		    			if(ng[i-1]==null){
				    		gh=0/Double.valueOf(ng[i]);
		    			}else if(ng[i]==null){
				    		gh=Double.valueOf(ng[i-1])/0;
		    			}else{
				    		gh=Double.valueOf(ng[i-1])/Double.valueOf(ng[i]);
		    			}
			    		if(gh==(int)gh){
			    			ng[i-1]=Integer.toString(Integer.valueOf((int)gh));
			    			if(gh==0){
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", null);
				            	ed.commit();
				    		}else{
				    			et.setText(ng[i-1]);
					    		ed.putString("gng", ng[i-1]);
				            	ed.commit();
				    		}
			    		}else{
			    			ng[i-1]=Double.toString(gh);
				    		et.setText(ng[i-1]);
				    		ed.putString("gng", ng[i-1]);
			            	ed.commit();
			    		}
			    		i--;
			    		j--;
			    	}
		    	}
		    	nh=new int[49];
		    	gh=0;
		    	i=0;
		    	j=0;
		    	hn=false;
		    	fh=false;
		    	if(ng[i]!=null){
					st=Double.valueOf(ng[i]);
		    		ng=new String[50];
		    		if(st==0){
						ng[i]=null;
		    		}else if(st==(int)st){
		    			ng[i]=Integer.toString(Integer.valueOf((int)st));
						hn=true;
		    		}else{
						ng[i]=Double.toString(st);
		    		}
				}else{
		    		ng=new String[50];
					ng[i]=null;
				}
				if(st>0){
					fh=true;
				}
		    	pf=true;
		    	pf2=true;
	    	}
		}
	}
	class Button21ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	if(pf){
	    		ng[i]=null;
		    	hn=true;
		    	fh=true;
		    	pf=true;
		    	pf2=false;
		    	et.setText("0");
	    	}
		}
	}
	class Button22ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	SharedPreferences.Editor ed=spf.edit();
	    	ng=new String[50];//計算顯示值
	    	nh=new int[49];//運算符號判別值
	    	gh=0;
	    	i=0;
	    	j=0;
	    	hn=true;
	    	fh=true;
	    	pf=true;
	    	pf2=false;
	    	et.setText("0");
    		ed.putString("gng", null);
        	ed.commit();
		}
	}
	class Button23ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	if(fh){
	    		if(ng[i]!=null&&ng[i]!="0"){
			    	ng[i]="-"+ng[i];
			    	et.setText(ng[i]);
			    	fh=false;
	    		}
	    	}
		}
	}
	class Button24ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	if(hn){
	    		if(ng[i]==null||ng[i]=="0"){
			    	ng[i]="0.";
			    	et.setText(ng[i]);
	    		}else{
	    			ng[i]=ng[i]+".";
			    	et.setText(ng[i]);
	    		}
		    	hn=false;
	    	}
		}
	}
	class Button25ClickListener implements  OnClickListener {
		@Override
		public void onClick(View V) {
	    	if(spf.getInt("music", 0)==0){
	    		sound.play(kg, 1, 1, 0, 0, 1);
			}
	    	if(pf){
	    		if(ng[i]==null||ng[i]=="0"){
			    	ng[i]="0";
			    	et.setText(ng[i]);
	    		}else{
			    	ng[i]=ng[i]+"0";
			    	et.setText(ng[i]);
	    		}
	    		pf2=true;
	    	}
		}
	}
}
