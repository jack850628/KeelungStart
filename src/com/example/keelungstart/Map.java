package com.example.keelungstart;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Map {
	private static final String MAP_URL = "file:///android_asset/googlemap.html";
	WebView webView;
	private ArrayList<HashMap> tiem2;
	private Activity Ac;
	public Map(Activity Ac){
		this.Ac=Ac;
	    webView = (WebView) Ac.findViewById(R.id.mapwebView);
	}
	public void addMark(ArrayList<HashMap> tiem2){
		 this.tiem2=tiem2;
	     webView.getSettings().setJavaScriptEnabled(true);
	     webView.setWebViewClient(new WV());
	     webView.loadUrl(MAP_URL); 
	   }
	class WV extends WebViewClient{
		   @Override 
	       public void onPageFinished(WebView view, String url) 
	       {
			   webView.loadUrl("javascript:ma_initialize()"); 
			   for(HashMap t:tiem2){
				   webView.loadUrl("javascript:getTitle(\'"+t.get("name")+"\')");
				   webView.loadUrl("javascript:many_mark("+t.get("gx")+","+t.get("gy")+")");
			   }
	       }
	}
}
