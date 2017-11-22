package com.example.keelungstart;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Backup extends Activity {
	private final static String gte_NAME="Sfgtsql.pref";
	SharedPreferences spf ;
	DisplayMetrics dm;
	Button ba,re;
	SoundPool sound;
	TextView tv,tv2,tv3,tv4,tv5,btv1,btv2,retv,retv2,pathtv,phtv,projecttv,foldertv;
	ListView reli,phli;
	StringBuffer backup,path;
	int size,ButtonSound;
	Thread th;
	AlertDialog ad,read,phad;
	View  layout,relist;
	mHandler mha=new mHandler();
	Listview2 lv;
	//File[] mediaInDir;
	directoryfilefilter df;
	ArrayList<String> items;
	private SQLiteDatabase mydb=null;
	private final static String SQL_NAME="Sfgtsql.db";//SQL名稱
	private final static String SFGTB_NAME="Sfgtbname";//表名稱
	private final static String THHN_NAME="Thhnname";//停車場表名稱
	private final static String GPSX="gpsx";//資料項目:GPSX座標
	private final static String GPSY="gpsy";//資料項目:GPSY座標
	private final static String NAME="name";//資料項目:景點名稱
	private final static String GTMA="gtmn";//資料項目:景點內容
	private final static String GEVI="gevi";//資料項目:圖片
	private final static String GEVI2="gevi2";
	private final static String NAME2="name2";//資料項目:交通資訊
	private final static String GTMA2="gtmn2";//資料項目:交通資訊內容
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backup);
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
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		Resources res = getResources(); 
		String[] tit=res.getStringArray(R.array.arrangement);
		setTitle(tit[bundle.getInt("PF")]);
		ba=(Button)findViewById(R.id.button1);
		ba.setOnClickListener(new Bare());
		re=(Button)findViewById(R.id.button2);
		re.setOnClickListener(new Bare());
		tv=(TextView)findViewById(R.id.textView1);
		tv2=(TextView)findViewById(R.id.textView2);
		tv2.setText(spf.getString("path", Environment.getExternalStorageDirectory().toString() + "/Keelung Start!/Backup"));
		tv3=(TextView)findViewById(R.id.textView3);
		tv4=(TextView)findViewById(R.id.textView4);
		tv4.setText(spf.getString("Time", getString(R.string.nobackup)));
		tv5=(TextView)findViewById(R.id.textView5);
		size=(spf.getInt("text", 0)==0)?15
				:(spf.getInt("text", 0)==1)?20
						:(spf.getInt("text", 0)==2)?25:30;
		tv.setTextSize(size);
		tv2.setTextSize(size);
		tv3.setTextSize(size);
		tv4.setTextSize(size);
		tv5.setTextSize(size);
		ba.setTextSize(size);
		re.setTextSize(size);
	}
	class Run implements Runnable{
		String name;
		public Run(String i){
			name=i;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			path=new StringBuffer(spf.getString("path", Environment.getExternalStorageDirectory().toString() + "/Keelung Start!/Backup")+"/"+name+".xml");
			File file = new File(path.toString());
			FileOutputStream fos = null; 
			try { 
				 fos = new FileOutputStream(file);  
			}catch (FileNotFoundException e) { 
				e.printStackTrace();
			}
			XmlSerializer serializer = Xml.newSerializer(); 
			 try {  
				 serializer.setOutput(fos, "UTF-8"); 
				 serializer.setFeature(  
						 "http://xmlpull.org/v1/doc/features.html#indent-output",
						 true);
				 serializer.startDocument("UTF-8", true);  
				 serializer.startTag(null, "persons");  
				 mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
				 Cursor cur=mydb.query(SFGTB_NAME, new String[] {NAME,GPSX,GPSY,GTMA,NAME2,GTMA2,GEVI}, null, null, null, null, null);
				 double size=cur.getCount();
				 if(size!=0)
					 size=100/size;
				 double calculate=0;
				 for (int i=0;i<cur.getCount();i++) { 	
				 cur.moveToPosition(i);
					 serializer.startTag(null, "Sfgt");
					 serializer.startTag(null, NAME);
					 serializer.text(cur.getString(0));  
					 serializer.endTag(null, NAME); 
					 serializer.startTag(null, GPSX);
					 serializer.text(cur.getString(1));  
					 serializer.endTag(null, GPSX); 
					 serializer.startTag(null, GPSY);
					 serializer.text(cur.getString(2));  
					 serializer.endTag(null, GPSY); 
					 serializer.startTag(null, GTMA);
					 serializer.text(cur.getString(3));  
					 serializer.endTag(null, GTMA); 
					 serializer.startTag(null, NAME2);
					 serializer.text(cur.getString(4));  
					 serializer.endTag(null, NAME2); 
					 serializer.startTag(null, GTMA2);
					 serializer.text(cur.getString(5));  
					 serializer.endTag(null, GTMA2); 
					 serializer.startTag(null, GEVI);
					 serializer.text(backupimg(cur.getString(6)));
					 serializer.endTag(null, GEVI); 
					 serializer.endTag(null, "Sfgt"); 
					 calculate+=size;
					 mha.data(calculate);
					 mha.sendEmptyMessage(1);
				 }
				 mha.Phase(true);
				 mha.data(0);
				 mha.sendEmptyMessage(1);
				 cur=mydb.query(THHN_NAME, new String[] {NAME,GPSX,GPSY,GTMA,GEVI}, null, null, null, null, null);
				 size=cur.getCount();
				 if(size!=0)
					 size=100/size;
				 calculate=0;
				 for (int i=0;i<cur.getCount();i++) { 	
				 cur.moveToPosition(i);
					 serializer.startTag(null, "Tsfgt");
					 serializer.startTag(null, NAME);
					 serializer.text(cur.getString(0));  
					 serializer.endTag(null, NAME); 
					 serializer.startTag(null, GPSX);
					 serializer.text(cur.getString(1));  
					 serializer.endTag(null, GPSX); 
					 serializer.startTag(null, GPSY);
					 serializer.text(cur.getString(2));  
					 serializer.endTag(null, GPSY); 
					 serializer.startTag(null, GTMA);
					 serializer.text(cur.getString(3));  
					 serializer.endTag(null, GTMA); 
					 serializer.startTag(null, GEVI);
					 serializer.text(backupimg(cur.getString(4)));
					 serializer.endTag(null, GEVI); 
					 serializer.endTag(null, "Tsfgt"); 
					 calculate+=size;
					 mha.data(calculate);
					 mha.sendEmptyMessage(1);
				 }
				cur.close();
				mydb.close();
				 serializer.endTag(null, "persons");  
				 serializer.endDocument();
				 fos.flush();
				 fos.close(); 
			 }catch (IOException e) {  
				 e.printStackTrace();
			 }catch (Exception e) {
				 e.printStackTrace();
			 }
			 mha.data(0);
			 mha.end(true);
			 mha.sendEmptyMessage(1);
		}
		
	}
	class mHandler extends Handler{
		double i;
		boolean j=false,p2=false,notontopen=false;
		String gt,file;
		AlertDialog dead;
		File[] filebaukup;
		public void data(double i){
			this.i=i;
		}
		public void end(boolean j){
			this.j=j;
		}
		public void Phase(boolean p2){
			this.p2=p2;
		}
		public void setgt(String gt){
			this.gt=gt;
		}
		public void delete(AlertDialog dead){
			this.dead=dead;
		}
		public void Openbackupfile(boolean notontopen,AlertDialog dead,File[] filebaukup){
			this.notontopen=notontopen;
			this.dead=dead;
			this.filebaukup=filebaukup;
		}
		public void Open(boolean notontopen,String file,File[] filebaukup,AlertDialog dead){
			this.notontopen=notontopen;
			this.file=file;
			this.filebaukup=filebaukup;
			this.dead=dead;
		}
	    	public void handleMessage(android.os.Message msg) {
	        	switch(msg.what) {
	            case 1:
	            	if(!p2){
	            		if(!j){
			            	btv2.setText((int)i+"%");
		            	}else{
		            		ad.dismiss();
		            		Toast.makeText(getApplicationContext(), gt+getString(R.string.ok), Toast.LENGTH_LONG).show();
		            		tv4.setText(spf.getString("Time", getString(R.string.nobackup)));
		            		j=false;
		            	}
	            	}else{
	            		ad.setTitle(gt+"2/2");
	                    btv2.setText((int)i+"%");
	                    p2=false;
	            	}
	            	break;
	            case 2:
	            	dead.dismiss();
		            Backupfile(notontopen,filebaukup);
		            //backupfile=false;
		            break;
	            case 3:
	            	dead.dismiss();
		            filepath(notontopen,file,filebaukup);
            		//openpath=false;
            		break;
	            case 4:
	            	dead.dismiss();
            		View vi = getLayoutInflater().inflate(R.layout.pleasewait,
    						(ViewGroup) findViewById(R.layout.pleasewait));
    				TextView pwtv=(TextView)vi.findViewById(R.id.textView1);
    				pwtv.setTextSize(size);
					AlertDialog dead=new AlertDialog.Builder(Backup.this).setView(vi).setCancelable(false).show();
					new Thread(new Openpath(true,phtv.getText().toString(),dead)).start();
        			//delete=false;
        			break;
		        default:
		        	Toast.makeText(getApplicationContext(),getString(R.string.notfile), Toast.LENGTH_SHORT).show();
            		//openerror=false;
	        	}
	        }
	    }
	class runre implements Runnable{
		int i;
		public runre(int i){
			this.i=i;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			InputStream inStream = null;
			try {
				path=new StringBuffer(spf.getString("path", Environment.getExternalStorageDirectory().toString() + "/Keelung Start!/Backup")+"/"+items.get(i));
	            File xmlFlie = new File(path.toString());
	            inStream  = new FileInputStream(xmlFlie);
	        } catch (Exception e) {}
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;
			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Document document = null;
			try {
				document = builder.parse(inStream);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Element root=document.getDocumentElement();
			mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
			try{
				NodeList nodes=root.getElementsByTagName("Sfgt");
	            String[] vigt=new String[2];
	            ContentValues cv;
	            double size=nodes.getLength();
	            if(size!=0)
					 size=100/size;
				double calculate=0;
			    for(int i=0;i<nodes.getLength();i++){
					cv=new ContentValues();
		            Element bookElement=(Element)nodes.item(i);
		            NodeList nodes2=bookElement.getElementsByTagName(NAME);
		            cv.put(NAME,nodes2.item(0).getTextContent());
		            nodes2=bookElement.getElementsByTagName(GPSX);
		            cv.put(GPSX,nodes2.item(0).getTextContent());
		            nodes2=bookElement.getElementsByTagName(GPSY);
		            cv.put(GPSY,nodes2.item(0).getTextContent());
		            nodes2=bookElement.getElementsByTagName(GTMA);
		            cv.put(GTMA,nodes2.item(0).getTextContent());
		            nodes2=bookElement.getElementsByTagName(NAME2);
		            cv.put(NAME2,nodes2.item(0).getTextContent());
		            nodes2=bookElement.getElementsByTagName(GTMA2);
		            cv.put(GTMA2,nodes2.item(0).getTextContent());
		            nodes2=bookElement.getElementsByTagName(GEVI);
		            String pfimg=nodes2.item(0).getTextContent();
		            if(!pfimg.equals("")){
			            vigt[0] = putimg(nodes2.item(0).getTextContent(),0);
			            vigt[1] = putimg2(nodes2.item(0).getTextContent());
			            cv.put(GEVI,vigt[0]);
			            cv.put(GEVI2,vigt[1]);
		            }else{
			            cv.put(GEVI,"");
			            cv.put(GEVI2,"");
		            }
				    mydb.insert(SFGTB_NAME, null, cv);
				    calculate+=size;
					mha.data(calculate);
					mha.sendEmptyMessage(1);
		        }
			}catch(Exception e){
		    	e.printStackTrace();
		    }
		    mha.Phase(true);
		    mha.data(0);
			mha.sendEmptyMessage(1);
			root=document.getDocumentElement();
			try{
				NodeList nodes=root.getElementsByTagName("Tsfgt");
				ContentValues cv;
				String[] vigt=new String[1];
	            double size=nodes.getLength();
	            if(size!=0)
					 size=100/size;
				double calculate=0;
			    for(int i=0;i<nodes.getLength();i++){
					cv=new ContentValues();
		            Element bookElement=(Element)nodes.item(i);
		            NodeList nodes2=bookElement.getElementsByTagName(NAME);
		            cv.put(NAME,nodes2.item(0).getTextContent());
		            nodes2=bookElement.getElementsByTagName(GPSX);
		            cv.put(GPSX,nodes2.item(0).getTextContent());
		            nodes2=bookElement.getElementsByTagName(GPSY);
		            cv.put(GPSY,nodes2.item(0).getTextContent());
		            nodes2=bookElement.getElementsByTagName(GTMA);
		            cv.put(GTMA,nodes2.item(0).getTextContent());
		            nodes2=bookElement.getElementsByTagName(GEVI);
		            String pfimg=nodes2.item(0).getTextContent();
		            if(!pfimg.equals("")){
			            vigt[0] = putimg(nodes2.item(0).getTextContent(),1);
			            cv.put(GEVI,vigt[0]);
		            }else{
		            	cv.put(GEVI,"");
		            }
				    mydb.insert(THHN_NAME, null, cv);
				    calculate+=size;
					mha.data(calculate);
					mha.sendEmptyMessage(1);
		        }
			}catch(Exception e){
		    	e.printStackTrace();
		    }
		    mydb.close();
		    mha.data(0);
			mha.end(true);
			mha.sendEmptyMessage(1);
		}
		
	}
	public String putimg(String setimg,int pf){
		Bitmap bi=null;
		String path=null;
        int img=spf.getInt((pf==0)?"bi":"bi2", 0);
        if(pf==1){
		    SharedPreferences.Editor ed=spf.edit();
		    ed.putInt("bi2", img+1);
		    ed.commit();
        }
	    try{
		    byte[] bitmapArray; 
		    bitmapArray = Base64.decode(setimg, Base64.DEFAULT); 
		    bi=BitmapFactory.decodeByteArray(bitmapArray, 0,bitmapArray.length); 
		    // 取得外部儲存裝置路徑
		    path = (pf==0)?Environment.getExternalStorageDirectory().toString()+"/Keelung Start!/Sfgt":Environment.getExternalStorageDirectory().toString()+"/Keelung Start!/Tsfgt";
		    // 開啟檔案
		    File file = new File( path, "Image"+img+".jpg");
			dil(file);
		    // 開啟檔案串流
		    FileOutputStream out;
			try {
				out = new FileOutputStream(file );
			    // 將 Bitmap壓縮成指定格式的圖片並寫入檔案串流
			    bi.compress ( Bitmap. CompressFormat.PNG , 100 , out);
			    bi=null;
			    out.flush ();
			    out.close ();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return path+"/"+"Image"+img+".jpg";
	}
	public String backupimg(String img){
		 Uri uri = Uri.parse("file://" + img);
			ContentResolver cr = Backup.this.getContentResolver();
		    try {
		    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    	Bitmap bi = BitmapFactory.decodeStream(cr.openInputStream(uri));
		    	bi.compress(Bitmap. CompressFormat.PNG , 100 , baos);  
		    	byte[] appicon = baos.toByteArray();
		    	img=Base64.encodeToString(appicon, Base64.DEFAULT);
		    	bi=null;
			} catch (FileNotFoundException e) {
			        Log.e("Exception", e.getMessage(),e);
			} catch(Exception e){
				img="";
			}
		    return img;
	}
	public String putimg2(String setimg){
		Bitmap bi=null;
		String path=null;
		int img=spf.getInt("bi", 0);
		SharedPreferences.Editor ed=spf.edit();
	    ed.putInt("bi", img+1);
	    ed.commit();
        try{
        	byte[] bitmapArray; 
		    bitmapArray = Base64.decode(setimg, Base64.DEFAULT); 
		    bi=BitmapFactory.decodeByteArray(bitmapArray, 0,bitmapArray.length); 
        	path = Environment.getExternalStorageDirectory().toString()+"/Keelung Start!/Sfgt2";
            File file = new File( path, "Image"+img+".jpg");
            dil(file);
		    try {
		    	FileOutputStream  out = new FileOutputStream(file );
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
			    bi.compress ( Bitmap. CompressFormat.PNG , 100 , out);
			    bi=null;
			    out.flush ();
			    out.close ();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }catch(Exception e){
	    	e.printStackTrace();
	    }
        return path+"/"+"Image"+img+".jpg";
	}
	class Bare implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			int st,tst;
			switch(v.getId()){
			case R.id.button1:
				mydb=openOrCreateDatabase(SQL_NAME, MODE_PRIVATE, null);
				Cursor cur=mydb.query(SFGTB_NAME, new String[] {NAME}, null, null, null, null, null);
				st=cur.getCount();
				cur=mydb.query(THHN_NAME, new String[] {NAME}, null, null, null, null, null);
				tst=cur.getCount();
				cur.close();
				mydb.close();
				if(st!=0||tst!=0){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
					View bn = getLayoutInflater().inflate(R.layout.backupname,
							(ViewGroup) findViewById(R.layout.backupname));
					EditText et=(EditText)bn.findViewById(R.id.editText1);
					String name="Backup"+spf.getInt("Backup", 0)+"("+sdf.format(new Date())+")";
					et.setText(name);
					AlertDialog.Builder builder = new AlertDialog.Builder(Backup.this);
					builder.setTitle(getString(R.string.backupname));
			    	builder.setView(bn);
			    	builder.setCancelable(false);
			    	Backupna ba =new Backupna(et,name,0,false);
			    	builder.setNegativeButton(getString(R.string.sfgt1), ba);
			    	builder.setPositiveButton(getString(R.string.ca), ba);
			    	builder.show(); 
				}else{
					Toast.makeText(getApplicationContext(), getString(R.string.ontdata), Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.button2:
				View vi = getLayoutInflater().inflate(R.layout.pleasewait,
						(ViewGroup) findViewById(R.layout.pleasewait));
				TextView pwtv=(TextView)vi.findViewById(R.id.textView1);
				pwtv.setTextSize(size);
				AlertDialog dead=new AlertDialog.Builder(Backup.this).setView(vi).setCancelable(false).show();
				new Thread(new Openbackupfile(false,dead)).start();
				break;
			}
		}
	}
	private class Backupna implements
	android.content.DialogInterface.OnClickListener{
		EditText et;
		String tiname;
		boolean rename;
		int fileitem;
		public Backupna(EditText et,String j,int fileitem,boolean rename){
			this.et=et;
			this.tiname=j;
			this.rename=rename;
			this.fileitem=fileitem;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
		if (which == DialogInterface.BUTTON_NEGATIVE){
			canCloseDialog(dialog, true);
			dialog.dismiss();
		}else if (which == DialogInterface.BUTTON_POSITIVE){
			if(et.getText().toString().equals("")){
				canCloseDialog(dialog, false);
				Toast.makeText(getApplicationContext(), getString(R.string.nosl), Toast.LENGTH_LONG).show();
			}else{
				if(et.getText().toString().indexOf("\\")==-1&&et.getText().toString().indexOf("/")==-1
						&&et.getText().toString().indexOf(":")==-1&&et.getText().toString().indexOf("?")==-1
						&&et.getText().toString().indexOf("\"")==-1&&et.getText().toString().indexOf("<")==-1
						&&et.getText().toString().indexOf(">")==-1&&et.getText().toString().indexOf("|")==-1
						&&et.getText().toString().indexOf("*")==-1){
					if(rename&&!items.get(fileitem).equals(et.getText().toString())||!rename){
						String path = spf.getString("path", Environment.getExternalStorageDirectory().toString() + "/Keelung Start!/Backup")+"/"+et.getText().toString();
						path+=(!rename)?".xml":"";
						File out = new File(path);
						if(out.exists()){
							canCloseDialog(dialog, false);
							AlertDialog.Builder builder = new AlertDialog.Builder(Backup.this);
							builder.setTitle(getString(R.string.file));
					    	builder.setMessage("\""+et.getText()+"\""+getString(R.string.file)+"，"+getString(R.string.ynfg)+"?");
					    	Delete2 de =new Delete2(out,et.getText().toString(),tiname,dialog,fileitem,rename);
					    	builder.setNegativeButton(getString(R.string.sfgt1), de);
					    	builder.setPositiveButton(getString(R.string.ca), de);
					    	builder.show(); 
						}else{
							canCloseDialog(dialog, true);
							if(rename){
								new File(spf.getString("path", Environment.getExternalStorageDirectory().toString() + "/Keelung Start!/Backup")+"/"+items.get(fileitem))
								.renameTo(new File(spf.getString("path", Environment.getExternalStorageDirectory().toString() + "/Keelung Start!/Backup")+"/"+et.getText().toString()));
								View vi = getLayoutInflater().inflate(R.layout.pleasewait,
										(ViewGroup) findViewById(R.layout.pleasewait));
								((TextView) vi.findViewById(R.id.textView1)).setTextSize(size);
								AlertDialog dead=new AlertDialog.Builder(Backup.this).setView(vi).setCancelable(false).show();
								new Thread(new Openbackupfile(true,dead)).start();
							}else
								backname(et.getText().toString(),tiname);
							dialog.dismiss();
						}
					}else
						dialog.dismiss();
				}else{
					canCloseDialog(dialog, false);
					Toast.makeText(getApplicationContext(), getString(R.string.nofilename)+"\\/:*?\"<>|", Toast.LENGTH_LONG).show();
				}
			}
		}
		}
	}
	public void backname(String name,String tiname){
		if(name.equals(tiname)){
			SharedPreferences.Editor ed=spf.edit();
		    ed.putInt("Backup", spf.getInt("Backup", 0)+1);
	    	ed.commit();
		}
		review();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		SharedPreferences.Editor ed=spf.edit();
	    ed.putString("Time", sdf.format(new Date()));
    	ed.commit();
		ad=new AlertDialog.Builder(Backup.this).setTitle(getString(R.string.backup)+"1/2").setView(layout).setCancelable(false).show();
        btv2.setText("0%");
		mha.setgt(getString(R.string.backup));
        th = new Thread(new Run(name));
        th.start();
	}
	class onlist implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			read.dismiss();
			review();
			ad=new AlertDialog.Builder(Backup.this).setTitle(getString(R.string.recover)+"1/2").setView(layout).setCancelable(false).show();
            btv2.setText("0%");
		    mha.setgt(getString(R.string.recover));
            th = new Thread(new runre(position));
            th.start();
		}
	}
	class onlong implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			Vibrator myVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
			myVibrator.vibrate(50);
			ArrayList<HashMap> tiem2= new ArrayList<HashMap>();
			HashMap hh;
			for (int i = 0; i < 2; i++) {
				hh = new HashMap();
	    		hh.put("id", i);
	    		hh.put("name",(i==0)?getString(R.string.rename):getString(R.string.sfgt11));
	    		hh.put("tv", size);
	    		tiem2.add(hh); 
			}
			Listview2 lv=new Listview2(Backup.this, tiem2);
			ListView list=new ListView(Backup.this);
			AlertDialog listad=new AlertDialog.Builder(Backup.this).setTitle(items.get(position)).setView(list).show(); 
			list.setOnItemClickListener(new listview(position,listad));
			list.setAdapter(lv);
			return true;
		}
	}
	class listview implements OnItemClickListener{
		int item;
		AlertDialog listad;
		public  listview(int i,AlertDialog ad){
			item=i;
			listad=ad;
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(Backup.this);
			switch(position){
			case 0:
				View bn = getLayoutInflater().inflate(R.layout.backupname,
						(ViewGroup) findViewById(R.layout.backupname));
				EditText et=(EditText)bn.findViewById(R.id.editText1);
				et.setText(items.get(item));
				builder.setTitle(getString(R.string.rename)+getString(R.string.backup));
		    	builder.setView(bn);
		    	builder.setCancelable(false);
		    	Backupna ba =new Backupna(et,null,item,true);
		    	builder.setNegativeButton(getString(R.string.sfgt1), ba);
		    	builder.setPositiveButton(getString(R.string.ca), ba);
		    	builder.show(); 
				break;
			default:
				builder.setTitle(getString(R.string.sfgt11));
		    	builder.setMessage(getString(R.string.del)+"\""+items.get(item)+"\""+"?");
		    	Delete de =new Delete(item);
		    	builder.setNegativeButton(getString(R.string.sfgt1), de);
		    	builder.setPositiveButton(getString(R.string.ca), de);
		    	builder.show();
			}
			listad.dismiss();
		}
	}
	private class Delete implements
	android.content.DialogInterface.OnClickListener{
		int i;
		public Delete(int i){
			this.i=i;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
		if (which == DialogInterface.BUTTON_NEGATIVE){
			 dialog.dismiss();
		}else if (which == DialogInterface.BUTTON_POSITIVE){
			new File(spf.getString("path", Environment.getExternalStorageDirectory().toString() + "/Keelung Start!/Backup")+"/"+items.get(i)).delete();
			View vi = getLayoutInflater().inflate(R.layout.pleasewait,
					(ViewGroup) findViewById(R.layout.pleasewait));
			TextView pwtv=(TextView)vi.findViewById(R.id.textView1);
			pwtv.setTextSize(size);
			AlertDialog dead=new AlertDialog.Builder(Backup.this).setView(vi).setCancelable(false).show();
			new Thread(new Openbackupfile(true,dead)).start();
		}
		}
	}
	private class Delete2 implements
	android.content.DialogInterface.OnClickListener{
		File out;
		String name,tiname;
		DialogInterface di;
		int fileitem;
		boolean rename;
		public Delete2(File out,String name,String tiname,DialogInterface di,int fileitem,boolean rename){
			this.out=out;
			this.name=name;
			this.tiname=tiname;
			this.di=di;
			this.fileitem=fileitem;
			this.rename=rename;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
		if (which == DialogInterface.BUTTON_NEGATIVE){
			 dialog.dismiss();
		}else if (which == DialogInterface.BUTTON_POSITIVE){
			canCloseDialog(di, true);
			out.delete(); 
			if(rename){
				new File(spf.getString("path", Environment.getExternalStorageDirectory().toString() + "/Keelung Start!/Backup")+"/"+items.get(fileitem))
				.renameTo(new File(spf.getString("path", Environment.getExternalStorageDirectory().toString() + "/Keelung Start!/Backup")+"/"+name));
				View vi = getLayoutInflater().inflate(R.layout.pleasewait,
						(ViewGroup) findViewById(R.layout.pleasewait));
				TextView pwtv=(TextView)vi.findViewById(R.id.textView1);
				pwtv.setTextSize(size);
				AlertDialog dead=new AlertDialog.Builder(Backup.this).setView(vi).setCancelable(false).show();
				new Thread(new Openbackupfile(true,dead)).start();
			}else
				backname(name,tiname);
			di.dismiss();
			dialog.dismiss();
		}
		}
	}
	//取得檔案內容前幾個字節 XML文件十六進制開頭為3c3f786d6c 
	 public String bytesToHexString(byte[] src) { 
		 StringBuilder stringBuilder = new StringBuilder();  
		 if (src == null || src.length <= 0) 
			 return null; 
		 for (int i = 0; i < src.length; i++) {  
			 int v = src[i] & 0xFF; 
			 String hv = Integer.toHexString(v);  
			 if (hv.length() < 2)
				 stringBuilder.append(0); 
			 stringBuilder.append(hv);  
		 }
		 return stringBuilder.toString();  
	 }
	//建立檔案過濾器
	FilenameFilter mediafilefilter = new FilenameFilter(){
		@Override
		public boolean accept(File dir, String filename) {
			 try {
				FileInputStream is = new FileInputStream(dir.toString()+"/"+filename);
				 byte[] b = new byte[3];  
				 is.read(b, 0, b.length);
				 is.close();
				 if(bytesToHexString(b).equals("3c3f78"))
					return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return false;
		}};
	public void Backupfile(boolean pf,File[] mediaInDir){
		ArrayList<HashMap> tiem2= new ArrayList<HashMap>();
		items=new ArrayList<String>();
		for(File f:mediaInDir){
			items.add(f.getName());
		}
		Collections.sort(items,String.CASE_INSENSITIVE_ORDER);
		HashMap hh;
		for (int i = 0; i < mediaInDir.length; i++) {
			hh = new HashMap();
    		hh.put("id", i);
    		hh.put("name",items.get(i));
    		hh.put("tv", size);
    		tiem2.add(hh); 
		}
		if(pf){
			lv.setListview(tiem2);
			lv.notifyDataSetChanged();
			if(mediaInDir.length==0){
				retv2.setVisibility(View.VISIBLE);
				retv.setVisibility(View.INVISIBLE);
				reli.setVisibility(View.INVISIBLE);
			}else{
				reli.setVisibility(View.VISIBLE);
				retv2.setVisibility(View.INVISIBLE);
			}
		}else{
			relist = getLayoutInflater().inflate(R.layout.relist,
					(ViewGroup) findViewById(R.layout.relist));
			retv=(TextView)relist.findViewById(R.id.textView1);
			retv2=(TextView)relist.findViewById(R.id.textView2);
			retv.setTextSize(size);
			retv2.setTextSize(size);
			reli=(ListView)relist.findViewById(R.id.listView1);
			if(mediaInDir.length==0){
				retv2.setVisibility(View.VISIBLE);
				retv.setVisibility(View.INVISIBLE);
				reli.setVisibility(View.INVISIBLE);
			}else{
				reli.setVisibility(View.VISIBLE);
				retv2.setVisibility(View.INVISIBLE);
			}
			lv=new Listview2(Backup.this, tiem2);
			reli.setOnItemClickListener(new onlist());
			reli.setOnItemLongClickListener(new onlong());
			reli.setAdapter(lv);
			read=new AlertDialog.Builder(Backup.this).setTitle(getString(R.string.backupfile)).setView(relist).show();
		}
	}
	class Openbackupfile implements Runnable{
		boolean pf;
		AlertDialog dead;
		public Openbackupfile(boolean pf,AlertDialog dead){
			this.pf=pf;
			this.dead=dead;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			File mediaDiPath = new File( spf.getString("path", Environment.getExternalStorageDirectory().toString() + "/Keelung Start!/Backup"));
			//取得media目錄中的媒體檔案，並設定過濾器
			File []mediaInDir = mediaDiPath.listFiles(mediafilefilter);
			mha.Openbackupfile(pf,dead,mediaInDir);
			mha.sendEmptyMessage(2);
		}
	}
	public void review(){
		layout = getLayoutInflater().inflate(R.layout.backuping,
				(ViewGroup) findViewById(R.layout.backuping));
		btv1=(TextView)layout.findViewById(R.id.textView1);
		btv2=(TextView)layout.findViewById(R.id.textView2);
		btv1.setTextSize(size);
		btv2.setTextSize(size);
	}
	//不關閉對話方塊的關鍵 close:"true"=可關閉 "false"=不可關閉
	  private void canCloseDialog(DialogInterface dialogInterface, boolean close) {
	        try {
	            Field field = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
	            field.setAccessible(true);
	            field.set(dialogInterface, close);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	private void dil(File out) {
		if(out.exists()){
			out.delete();
		}
	}
	class path implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			switch(v.getId()){
			case R.id.button2:
				View vi = getLayoutInflater().inflate(R.layout.pleasewait,
						(ViewGroup) findViewById(R.layout.pleasewait));
				TextView pwtv=(TextView)vi.findViewById(R.id.textView1);
				pwtv.setTextSize(size);
				AlertDialog dead=new AlertDialog.Builder(Backup.this).setView(vi).setCancelable(false).show();
				new Thread(new Openpath(false,pathtv.getText().toString(),dead)).start();
				break;
			default:
		    	File pffile=new File(Environment.getExternalStorageDirectory().toString() + "/Keelung Start!/Backup");
				if(!pffile.exists())
					pffile.mkdirs();
				pathtv.setText(Environment.getExternalStorageDirectory().toString() + "/Keelung Start!/Backup");
			}
		}
	}
	//建立檔案過濾器
	class directoryfilefilter implements FilenameFilter{
		int Number_of_Files;
		public directoryfilefilter(int Number_of_Files){
			this.Number_of_Files=Number_of_Files;
		}
		@Override
		public boolean accept(File dir, String filename) {
			// TODO Auto-generated method stub
			Number_of_Files++;
			if(new File(dir.toString()+"/"+filename).isDirectory())
				return true;
		   return false;
		}
		public int getNumber_of_Files(){
			return Number_of_Files;
		}
	}
	public void filepath(boolean pf,String file,File[] mediaInDir){
		ArrayList<String> itemsbackup=items;
		try{
			items=new ArrayList<String>();
			for(File f:mediaInDir){
				items.add(f.getName());
			}
			Collections.sort(items,String.CASE_INSENSITIVE_ORDER);
			ArrayList<HashMap> tiem2= new ArrayList<HashMap>();
			HashMap hh;
			if(!file.equals("/")){
				hh = new HashMap();
				hh.put("id", 0);
				hh.put("name",getString(R.string.previous));
				hh.put("tv", size);
				tiem2.add(hh);
			} 
			for (int i = 0; i < mediaInDir.length; i++) {
				hh = new HashMap();
	    		hh.put("id", i+1);
	    		hh.put("name",items.get(i));
	    		hh.put("tv", size);
	    		tiem2.add(hh); 
			}
			if(pf){
				phtv.setText(file);
				projecttv.setText(String.valueOf(df.getNumber_of_Files()));
				foldertv.setText(String.valueOf(mediaInDir.length));
				lv.setListview(tiem2);
				lv.notifyDataSetChanged();
			}else{
				View path = getLayoutInflater().inflate(R.layout.path,
						(ViewGroup) findViewById(R.layout.path));
				phtv=(TextView)path.findViewById(R.id.textView1);
				projecttv=(TextView)path.findViewById(R.id.textView2);
				foldertv=(TextView)path.findViewById(R.id.textView6);
				phtv.setText(pathtv.getText());
				projecttv.setText(String.valueOf(df.getNumber_of_Files()));
				foldertv.setText(String.valueOf(mediaInDir.length));
				phtv.setTextSize(size);
				projecttv.setTextSize(size);
				foldertv.setTextSize(size);
				((TextView) path.findViewById(R.id.textView3)).setTextSize(size);
				((TextView) path.findViewById(R.id.textView4)).setTextSize(size);
				((TextView) path.findViewById(R.id.textView5)).setTextSize(size);
				phli=(ListView)path.findViewById(R.id.listView1);
				lv=new Listview2(Backup.this, tiem2);
				phli.setOnItemClickListener(new OpenFolder());
				phli.setOnItemLongClickListener(new deFolder());
				phli.setAdapter(lv);
				AlertDialog.Builder phad = new AlertDialog.Builder(Backup.this);
				phad.setTitle(getString(R.string.selectbackuppath));
				phad.setView(path);
				Folder fr =new Folder();
				phad.setNegativeButton(getString(R.string.sfgt1), fr);
				phad.setNeutralButton(getString(R.string.ca), fr);
				phad.setPositiveButton(getString(R.string.addpath),fr);
				phad.setCancelable(false);
				phad.show(); 
			}
		}catch(Exception e){
			e.printStackTrace();
			items=itemsbackup;
			itemsbackup=null;
			Toast.makeText(getApplicationContext(),getString(R.string.notfile), Toast.LENGTH_SHORT).show();
		}
	}
	class Openpath implements Runnable{
		boolean pf;
		String file;
		AlertDialog dead;
		public Openpath(boolean pf,String file,AlertDialog dead){
			this.pf=pf;
			this.file=file;
			this.dead=dead;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				File mediaDiPath = new File(file);
				if(!mediaDiPath.exists())
					mediaDiPath.mkdirs();
				//取得media目錄中的媒體檔案，並設定過濾器
				df=new directoryfilefilter(0);//0:初始化檔案數量變數
				File[] mediaInDir = mediaDiPath.listFiles(df);
				mha.Open(pf,file,mediaInDir,dead);
				mha.sendEmptyMessage(3);
			}catch(Exception e){
				e.printStackTrace();
				mha.sendEmptyMessage(0);
			}
		}
	}
	private class Folder implements DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			canCloseDialog(dialog, false);
		    if (which == DialogInterface.BUTTON_NEGATIVE){
		    	canCloseDialog(dialog, true);
		    	File pffile=new File(pathtv.getText().toString());
				if(!pffile.exists())
					pffile.mkdirs();
			    dialog.dismiss();
		    }else if (which == DialogInterface.BUTTON_NEUTRAL){
		    	canCloseDialog(dialog, true);
		    	pathtv.setText(phtv.getText());
		    	File pffile=new File(phtv.getText().toString());
				if(!pffile.exists())
					pffile.mkdirs();
			    dialog.dismiss();
		    }else if (which == DialogInterface.BUTTON_POSITIVE){
		    	View bn = getLayoutInflater().inflate(R.layout.backupname,
						(ViewGroup) findViewById(R.layout.backupname));
				EditText et=(EditText)bn.findViewById(R.id.editText1);
				et.setHint(getString(R.string.pathname));
				AlertDialog.Builder builder = new AlertDialog.Builder(Backup.this);
				builder.setTitle(getString(R.string.addpath));
		    	builder.setView(bn);
		    	builder.setCancelable(false);
		    	addFolder af =new addFolder(et,0,false);
		    	builder.setNegativeButton(getString(R.string.sfgt1), af);
		    	builder.setPositiveButton(getString(R.string.ca), af);
		    	builder.show(); 
		    }
	    }
	}
	class deFolder implements OnItemLongClickListener{
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if(!phtv.getText().toString().equals("/")&&position!=0||phtv.getText().toString().equals("/")){
				Vibrator myVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
				myVibrator.vibrate(50);
				ArrayList<HashMap> tiem2= new ArrayList<HashMap>();
				HashMap hh;
				for (int i = 0; i < 2; i++) {
					hh = new HashMap();
		    		hh.put("id", i);
		    		hh.put("name",(i==0)?getString(R.string.rename):getString(R.string.sfgt11));
		    		hh.put("tv", size);
		    		tiem2.add(hh); 
				}
				Listview2 lv=new Listview2(Backup.this, tiem2);
				ListView list=new ListView(Backup.this);
				position=(phtv.getText().toString().equals("/"))?position:position-1;
				AlertDialog listad=new AlertDialog.Builder(Backup.this).setTitle(items.get(position)).setView(list).show(); 
				list.setOnItemClickListener(new Folderlistview(position,listad));
				list.setAdapter(lv);
			}
			return true;
		}
	}
	class Folderlistview implements OnItemClickListener{
		int item;
		AlertDialog listad;
		public  Folderlistview(int i,AlertDialog ad){
			item=i;
			listad=ad;
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(Backup.this);
			try{
				switch(position){
				case 0:
					View bn = getLayoutInflater().inflate(R.layout.backupname,
							(ViewGroup) findViewById(R.layout.backupname));
					EditText et=(EditText)bn.findViewById(R.id.editText1);
					et.setHint(getString(R.string.pathname));
					et.setText(items.get(item));
					builder.setTitle(getString(R.string.renamepath));
			    	builder.setView(bn);
			    	builder.setCancelable(false);
			    	addFolder af =new addFolder(et,item,true);
			    	builder.setNegativeButton(getString(R.string.sfgt1), af);
			    	builder.setPositiveButton(getString(R.string.ca), af);
			    	builder.show(); 
					break;
				default:
					builder.setTitle(getString(R.string.sfgt11));
			    	builder.setMessage((new Superds().isEmpty(phtv.getText()+"/"+items.get(item)))?getString(R.string.del)+"\""+items.get(item)+"\""+"?"
					:"\""+items.get(item)+"\""+getString(R.string.notempty)+getString(R.string.del)+"?");
			    	Fdelete de =new Fdelete(item);
			    	builder.setNegativeButton(getString(R.string.sfgt1), de);
			    	builder.setPositiveButton(getString(R.string.ca), de);
			    	builder.show(); 
				}
			}catch(Exception e){
				Toast.makeText(getApplicationContext(),getString(R.string.notfile), Toast.LENGTH_LONG).show();
			}
			listad.dismiss();
		}
		
	}
	private class Fdelete implements
	android.content.DialogInterface.OnClickListener{
		int i;
		public Fdelete(int i){
			this.i=i;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
		if (which == DialogInterface.BUTTON_NEGATIVE){
			 dialog.dismiss();
		}else if (which == DialogInterface.BUTTON_POSITIVE){
			View vi = getLayoutInflater().inflate(R.layout.pleasewait,
					(ViewGroup) findViewById(R.layout.pleasewait));
			TextView pwtv=(TextView)vi.findViewById(R.id.textView1);
			pwtv.setTextSize(size);
			AlertDialog dead=new AlertDialog.Builder(Backup.this).setView(vi).setCancelable(false).show();
			new Thread(new Fdeleteing(dead,i)).start();
		}
		}
	}
	class Fdeleteing implements Runnable{
		AlertDialog dead;
		int i;
		public Fdeleteing(AlertDialog dead,int i){
			this.dead=dead;
			this.i=i;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			new Superds().superdelete(new File(phtv.getText()+"/"+items.get(i)));
			mha.delete(dead);
			mha.sendEmptyMessage(4);
		}
	}
	private class addFolder implements DialogInterface.OnClickListener{
		EditText et;
		boolean rename;
		int folderitem;
		public addFolder(EditText et,int folderitem,boolean rename){
			this.et=et;
			this.rename=rename;
			this.folderitem=folderitem;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			canCloseDialog(dialog, false);
		    if (which == DialogInterface.BUTTON_NEGATIVE){
		    	canCloseDialog(dialog, true);
			    dialog.dismiss();
		    }else if (which == DialogInterface.BUTTON_POSITIVE){
		    	if(rename&&!items.get(folderitem).equals(et.getText().toString())||!rename){
		    		if(et.getText().toString().equals("")){
						Toast.makeText(getApplicationContext(), getString(R.string.nosl), Toast.LENGTH_LONG).show();
			    	}else{
				    	File newFolder = new File(phtv.getText().toString()+"/"+et.getText());
				    	if(newFolder.exists()){
				    		AlertDialog.Builder builder = new AlertDialog.Builder(Backup.this);
				    		builder.setTitle(getString(R.string.file));
				    	    builder.setMessage("\""+et.getText()+"\""+getString(R.string.pathpresence)+"，"+getString(R.string.ynfg)+"?");
				    	    foldersetup de =new foldersetup(et,dialog,newFolder,folderitem,rename);
				    	    builder.setNegativeButton(getString(R.string.sfgt1), de);
				    	    builder.setPositiveButton(getString(R.string.ca), de);
				    	    builder.show(); 
				    	}
				    	else{
				    		canCloseDialog(dialog, true);
				    		if(rename)
				    			new File(phtv.getText().toString()+"/"+items.get(folderitem)).renameTo(new File(phtv.getText().toString()+"/"+et.getText().toString()));
							else
					    		newFolder.mkdir();
				    		View vi = getLayoutInflater().inflate(R.layout.pleasewait,
									(ViewGroup) findViewById(R.layout.pleasewait));
							TextView pwtv=(TextView)vi.findViewById(R.id.textView1);
							pwtv.setTextSize(size);
							AlertDialog dead=new AlertDialog.Builder(Backup.this).setView(vi).setCancelable(false).show();
							new Thread(new Openpath(true,phtv.getText().toString(),dead)).start();
				    		dialog.dismiss();
				    	}
			    	}
		    	}else{
		    		canCloseDialog(dialog, true);
				    dialog.dismiss();
		    	}
		    }
	    }
	}
	class foldersetup implements DialogInterface.OnClickListener{
		DialogInterface ad;
		File Folderpath;
		boolean rename;
		int folderitem;
		EditText et;
		public foldersetup(EditText et,DialogInterface ad,File Folderpath,int folderitem,boolean rename){
			this.ad=ad;
			this.Folderpath=Folderpath;
			this.rename=rename;
			this.folderitem=folderitem;
			this.et=et;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			if (which == DialogInterface.BUTTON_NEGATIVE){
				 dialog.dismiss();
			}else if (which == DialogInterface.BUTTON_POSITIVE){
				canCloseDialog(ad, true);
				View vi = getLayoutInflater().inflate(R.layout.pleasewait,
						(ViewGroup) findViewById(R.layout.pleasewait));
				TextView pwtv=(TextView)vi.findViewById(R.id.textView1);
				pwtv.setTextSize(size);
				AlertDialog dead=new AlertDialog.Builder(Backup.this).setView(vi).setCancelable(false).show();
				new Thread(new Rename(dead,Folderpath,folderitem,et,rename)).start();
				ad.dismiss();
				dialog.dismiss();
			}
		}
	}
	class Rename implements Runnable{
		AlertDialog dead;
		File Folderpath;
		int i;
		EditText et;
		boolean rename;
		public Rename(AlertDialog dead,File Folderpath,int i,EditText et,boolean rename){
			this.dead=dead;
			this.Folderpath=Folderpath;
			this.i=i;
			this.et=et;
			this.rename=rename;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			new Superds().superdelete(Folderpath);
			if(rename){
				new File(phtv.getText().toString()+"/"+items.get(i)).renameTo(new File(phtv.getText().toString()+"/"+et.getText().toString()));
			}else
				Folderpath.mkdir();
			mha.delete(dead);
			mha.sendEmptyMessage(4);
		}
	}
	class OpenFolder implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			View vi = getLayoutInflater().inflate(R.layout.pleasewait,
					(ViewGroup) findViewById(R.layout.pleasewait));
			TextView pwtv=(TextView)vi.findViewById(R.id.textView1);
			pwtv.setTextSize(size);
			AlertDialog dead=new AlertDialog.Builder(Backup.this).setView(vi).setCancelable(false).show();
			switch(position){
			case 0:
				new Thread(new Openpath(true,(phtv.getText().toString().equals("/"))?"/"+items.get(position)
						:new File(phtv.getText().toString()).getParentFile().toString(),dead)).start();
				break;
			default:
				new Thread(new Openpath(true,
						(phtv.getText().toString().equals("/"))?"/"+items.get((!phtv.getText().toString().equals("/"))?position-1:position)
								:phtv.getText().toString()+"/"+items.get(position-1),dead)).start();
			}
		}
	}
	private class Fileok implements
	android.content.DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(spf.getInt("music", 0)==0){
				sound.play(ButtonSound, 1, 1, 0, 0, 1);
			}
			if (which == DialogInterface.BUTTON_NEGATIVE){
				File pffile=new File(tv2.getText().toString());
				if(!pffile.exists())
					pffile.mkdirs();
				 dialog.dismiss();
			}else if (which == DialogInterface.BUTTON_POSITIVE){
				SharedPreferences.Editor ed=spf.edit();
			    ed.putString("path", pathtv.getText().toString());
			    ed.commit();
			    tv2.setText(pathtv.getText().toString());
			}
		}
	}
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.backuppath, menu);
	    return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
    	int item_id = item.getItemId();
    	if(spf.getInt("music", 0)==0){
    		sound.play(ButtonSound, 1, 1, 0, 0, 1);
		}
    	Button path,repath;
    	switch (item_id){
    		case R.id.breakup:
    			View bnph = getLayoutInflater().inflate(R.layout.backuppath,
						(ViewGroup) findViewById(R.layout.backuppath));
    			pathtv=(TextView)bnph.findViewById(R.id.textView1);
    			pathtv.setText(spf.getString("path", Environment.getExternalStorageDirectory().toString() + "/Keelung Start!/Backup"));
    			pathtv.setTextSize(size);
    			((TextView) bnph.findViewById(R.id.textView2)).setTextSize(size);
    			path=(Button)bnph.findViewById(R.id.button1);
    			path.setOnClickListener(new path());
    			repath=(Button)bnph.findViewById(R.id.button2);
    			repath.setOnClickListener(new path());
				AlertDialog.Builder builder = new AlertDialog.Builder(Backup.this);
				builder.setTitle(getString(R.string.backuppath));
		    	builder.setView(bnph);
		    	Fileok fo =new Fileok();
		    	builder.setNegativeButton(getString(R.string.sfgt1), fo);
		    	builder.setPositiveButton(getString(R.string.ca), fo);
		    	builder.show(); 
    			break;
    		default: return false;
    	}
    	return true;
    }
}
