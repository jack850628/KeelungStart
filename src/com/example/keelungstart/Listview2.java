package com.example.keelungstart;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Listview2 extends BaseAdapter {
	
	private LayoutInflater myInflater;
    ArrayList<HashMap> list = null;
 
    public Listview2(Context context, ArrayList<HashMap> list){
        myInflater = LayoutInflater.from(context);
        this.list = list;
    }
    
    public void setListview(ArrayList<HashMap> list){
    	this.list = list;
    }
 
    public int getCount() {
        return list.size();
    }
    
    public Object getItem(int position) {
        return list.get(position);
    }
    
    public long getItemId(int position) {
        return Long.valueOf((Integer)list.get(position).get("id"));
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	 
    	TextView viewTag;
 
        if(convertView == null){
        	convertView = myInflater.inflate(R.layout.list, null);
 
            viewTag = (TextView) convertView.findViewById(R.id.textView1);
 
            convertView.setTag(viewTag);
        } else {
            viewTag = (TextView) convertView.getTag();
        }
 
        viewTag.setText((CharSequence) list.get(position).get("name"));
        viewTag.setTextSize((Integer) list.get(position).get("tv"));
        return convertView;
    }
}
