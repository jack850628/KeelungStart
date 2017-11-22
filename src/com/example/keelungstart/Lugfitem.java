package com.example.keelungstart;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Lugfitem extends Activity{
	private SQLiteDatabase mydb=null;
	private final static String ID="_id";
	private final static String SQL_NAME="Sfgtsql.db";//SQL名稱
	private final static String GDGPSX="gdgpsx";//資料項目:終點座標X
	private final static String GDGPSY="gdgpsy";//資料項目:終點座標Y
	private final static String LUGF2_NAME="Lugf2";
	private final static String PH="ph";//資料項目:行程規劃編號
	private final static String PF="pf";//資料項目:行程規劃判別
	private final static String MC="mc";
	private final static String GT="gt";
	private final static String SQLNAME="sqlname";
	private final static String NAME="name";//資料項目:景點名稱
	private final static String gte_NAME="Sfgtsql.pref";
	SharedPreferences spf;
	int size,ButtonSound,pf,etag;
	SoundPool sound;
	TextView tv;
	Listview2 lv;
	boolean move=false;
	private ArrayList<HashMap> tiem2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sfgt); 
		spf =  getSharedPreferences(gte_NAME,0);
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
		setTitle(getString(R.string.chooselugf));
		tv=(TextView)findViewById(R.id.textView1);
		if(spf.getInt("text", 0)==0){
			 size=15;
		}else if(spf.getInt("text", 0)==1){
			size=20;
		}else if(spf.getInt("text", 0)==2){
			size=25;
		}else if(spf.getInt("text", 0)==3){
			size=30;
		}
		tv.setTextSize(size);
		tv.setText(getString(R.string.notlugf));
		sql(true);
	}
	public void sql(boolean OneCall){
		ListView listview = (ListView) findViewById(R.id.listViewgtk);
		tiem2 = new ArrayList<HashMap>();
		HashMap hh;
		mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
		try
		{
			Cursor cur=mydb.query(LUGF2_NAME, new String[] {NAME}, null, null, null, null, null);
			int j=cur.getCount();
            if(j==0){
            	tv.setVisibility(View.VISIBLE);
            	listview.setVisibility(View.INVISIBLE);
            }else{
            	tv.setVisibility(View.INVISIBLE); 
            	listview.setVisibility(View.VISIBLE);
            	for(int i=0;i<j;i++){
                	hh = new HashMap();
                	cur.moveToPosition(i);
            		hh.put("id", i);
            		hh.put("name", cur.getString(0));
            		hh.put("tv", size);
                	tiem2.add(hh); 
                }
            }
			cur.close();
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
		}
		if(OneCall){
			lv=new Listview2(this, tiem2);
			listview.setOnItemClickListener(new ListViewClick());
			listview.setOnItemLongClickListener(new ListViewLongClick());
			listview.setAdapter(lv);
		}else{
			lv.setListview(tiem2);
			lv.notifyDataSetChanged();
		}
	}
	class ListViewClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			if(!move){
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("item",position);
				mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
				Cursor cur=mydb.query(LUGF2_NAME, new String[] {NAME,SQLNAME}, null, null, null, null, null);
				cur.moveToPosition(position);
				bundle.putString("sqlname",cur.getString(1));
				bundle.putString("name",cur.getString(0));
				intent.putExtras(bundle);
				intent.setClass(getApplicationContext(), Lugfview.class);
		        Lugfitem.this.startActivityForResult(intent,0);
			}else{
				etag=position;
				AlertDialog.Builder builder = new AlertDialog.Builder(Lugfitem.this);
				builder.setTitle(getString(R.string.con));
		    	builder.setMessage(getString(R.string.lugf12)+"\""+tiem2.get(pf).get("name")
		    			+"\""+getString(R.string.lugf13)+"\""+tiem2.get(etag).get("name")+"\""+getString(R.string.lugf14)+"?");
				mydb.close();
		    	Gnet de = new Gnet();
		    	builder.setNegativeButton(getString(R.string.lugf15), de);
		    	builder.setPositiveButton(getString(R.string.lugf16), de);
		    	builder.show();
			}
		}
	}
	class ListViewLongClick implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if(!move){
				Vibrator myVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
				myVibrator.vibrate(50);
				AlertDialog.Builder builder = new AlertDialog.Builder(Lugfitem.this);
				builder.setTitle(getString(R.string.con));
		    	builder.setMessage(getString(R.string.lugf10)+"?");
		    	Options de = new Options(position);
		    	builder.setNegativeButton(getString(R.string.sfgt11), de);
		    	builder.setNeutralButton(getString(R.string.lugf17), de);
		    	builder.setPositiveButton(getString(R.string.rename), de);
		    	builder.show();
		    	return true;
			}
			return false;
		}
	}
	class Options implements DialogInterface.OnClickListener{
		int item;
		public Options(int item){
			this.item=item;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(Lugfitem.this);
			switch(which){
			case DialogInterface.BUTTON_NEGATIVE:
				builder.setTitle(getString(R.string.sfgt11));
		    	builder.setMessage(getString(R.string.del)+"\""+tiem2.get(item).get("name").toString()+"\""+"?");
		    	Delete de =new Delete(item);
		    	builder.setNegativeButton(getString(R.string.sfgt1), de);
		    	builder.setPositiveButton(getString(R.string.ca), de);
		    	builder.show(); 
				break;
			case DialogInterface.BUTTON_NEUTRAL:
				move=true;
				pf=item;
				Toast.makeText(getApplicationContext(), getString(R.string.lugf21), Toast.LENGTH_SHORT).show();
				break;
			case DialogInterface.BUTTON_POSITIVE:
				EditText et=new EditText(Lugfitem.this);
				et.setHint(getString(R.string.lugfname));
				et.setText(tiem2.get(item).get("name").toString());
				builder.setTitle(getString(R.string.rename));
		    	builder.setView(et);
		    	builder.setCancelable(false);
		    	Addlugf al =new Addlugf(et,item,false);
		    	builder.setNegativeButton(getString(R.string.sfgt1), al);
		    	builder.setPositiveButton(getString(R.string.ca), al);
		    	builder.show(); 
				break;
			}
		}
	}
	private class Gnet implements DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			switch(which){
			case DialogInterface.BUTTON_NEGATIVE:
				supet();
				break;
			case DialogInterface.BUTTON_POSITIVE:
				etag++;
				supet();
				break;
			}
		}
	}
	public void supet(){
		mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
    	Cursor cur=mydb.query(LUGF2_NAME, new String[] {ID,NAME,SQLNAME,MC,GT}, null, null, null, null, null);
    	cur.moveToPosition(pf);
    	String list[]=new String[]{
    			cur.getString(1),
    			cur.getString(2),
    			cur.getString(3),
    			cur.getString(4)
    	};
	    mydb.delete(LUGF2_NAME, "_id=?", new String[]{cur.getString(0)});
	    cur.close();
		mydb.close();
		mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
		cur=mydb.query(LUGF2_NAME, new String[] {ID,NAME,SQLNAME,MC,GT}, null, null, null, null, null);
		int j=cur.getCount();
	    int i=etag;
	    if(pf<etag){
	    	i--;
	    }
	    if(j==i){
	    	ContentValues cv=new ContentValues();
            cv.put(NAME,list[0]);
			cv.put(SQLNAME,list[1]);
			cv.put(MC,list[2]);
			cv.put(GT,list[3]);
		    mydb.insert(LUGF2_NAME, null, cv);
	    }else{
            ContentValues cv=new ContentValues();
            cur.moveToPosition(j-1);
            cv.put(NAME,cur.getString(1));
			cv.put(SQLNAME,cur.getString(2));
			cv.put(MC,cur.getString(3));
			cv.put(GT,cur.getString(4));
		    mydb.insert(LUGF2_NAME, null, cv);
        	StringBuffer r;
            for(j=cur.getCount()-1;j>i;j--){
            	cur.moveToPosition(j);
            	r=new StringBuffer(cur.getString(0));
            	cur.moveToPosition(j-1);
 	            cv.put(NAME,cur.getString(1));
 			    cv.put(SQLNAME,cur.getString(2));
 			    cv.put(MC,cur.getString(3));
 			    cv.put(GT,cur.getString(4));
 			    String whereClause="_id=?";
 				String[] whereArgs={r.toString()};
 				mydb.update(LUGF2_NAME, cv, whereClause, whereArgs);
            }
            cur.moveToPosition(j);
            r=new StringBuffer(cur.getString(0));
        	cv.put(NAME,list[0]);
			cv.put(SQLNAME,list[1]);
			cv.put(MC,list[2]);
			cv.put(GT,list[3]);
			String whereClause="_id=?";
			String[] whereArgs={r.toString()};
			mydb.update(LUGF2_NAME, cv, whereClause, whereArgs);
	    }
		cur.close();
		mydb.close();
    	move=false;
	    sql(false);
	}
	class Delete implements DialogInterface.OnClickListener{
		int item;
		public Delete(int item){
			this.item=item;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			switch(which){
			case DialogInterface.BUTTON_NEGATIVE:
				dialog.dismiss();
				break;
			case DialogInterface.BUTTON_POSITIVE:
				mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
				Cursor cur=mydb.query(LUGF2_NAME, new String[] {SQLNAME,ID}, null, null, null, null, null);
		    	cur.moveToPosition(item);
		    	mydb.execSQL("DROP TABLE "+cur.getString(0));
			    String whereClause="_id=?";
				String[] whereArgs={cur.getString(1)};
				mydb.delete(LUGF2_NAME, whereClause, whereArgs);
		    	cur.close();
			    mydb.close();
			    sql(false);
				break;
			}
		}
	}
	class Addlugf implements DialogInterface.OnClickListener{
		EditText et;
		int item;
		boolean add;
		public Addlugf(EditText et,int item,boolean add){
			this.et=et;
			this.item=item;
			this.add=add;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			switch(which){
			case DialogInterface.BUTTON_NEGATIVE:
				dialog.dismiss();
				break;
			case DialogInterface.BUTTON_POSITIVE:
				mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
				ContentValues cv=new ContentValues();
				int i;
				if("".equals(et.getText().toString().trim())){
					i=spf.getInt("lugfna", 1);
					cv.put(NAME,getString(R.string.sfgt13)+getString(R.string.lugf7)+i);
					SharedPreferences.Editor ed=spf.edit();
				    ed.putInt("lugfna", i+1);
		        	ed.commit();
				}else{
				    cv.put(NAME,et.getText().toString());
				}
			    if(add){
					i=spf.getInt("lugfsqlna", 0);
				    StringBuffer bf=new StringBuffer("lugfsql"+i);
				    cv.put(SQLNAME,bf.toString());
				    cv.put(MC,0);
				    cv.put(GT,0);
				    SharedPreferences.Editor ed=spf.edit();
				    ed.putInt("lugfsqlna", i+1);
			    	ed.commit();
				    mydb.insert(LUGF2_NAME, null, cv);
			    	mydb.execSQL("CREATE TABLE "+bf.toString()+" ("+ID+" INTEGER PRIMARY KEY,"+ NAME+" TEXT,"+PH+" INTEGER,"+PF+" INTEGER,"+ GDGPSX+" TEXT,"+ GDGPSY+" TEXT)");
			    }else{
			    	String whereClause="_id=?";
			    	Cursor cur=mydb.query(LUGF2_NAME, new String[] {ID}, null, null, null, null, null);
			    	cur.moveToPosition(item);
			    	String[] whereArgs={cur.getString(0)};
					mydb.update(LUGF2_NAME, cv, whereClause, whereArgs);
			    }
			    mydb.close();
			    sql(false);
				break;
			}
		}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		sql(false);
	}
	public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, Menu.FIRST, 0, getString(R.string.addlugf));
	    return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		if(spf.getInt("music", 0)==0){
			sound.play(ButtonSound, 1, 1, 0, 0, 1);
		}
		EditText et=new EditText(this);
		et.setHint(getString(R.string.lugfname));
		AlertDialog.Builder builder = new AlertDialog.Builder(Lugfitem.this);
		builder.setTitle(getString(R.string.addlugf));
    	builder.setView(et);
    	builder.setCancelable(false);
    	Addlugf al =new Addlugf(et,0,true);
    	builder.setNegativeButton(getString(R.string.sfgt1), al);
    	builder.setPositiveButton(getString(R.string.ca), al);
    	builder.show(); 
    	return true;
    }
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
        	if(move){
        		Toast.makeText(getApplicationContext(), getString(R.string.moveoff), Toast.LENGTH_SHORT).show();
		    	move=false;
		    }else{
			    finish();
		    }
        }
        return false;
    }
}
