package com.example.keelungstart;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Toast;

public class Superds {
	/**傳入File參數刪除有檔案/空資料夾
	 * @param f 路徑參數**/
	public void superdelete(File f){
		File[] Dir = f.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String filename) {
				return true;
			}});
		for(File i:Dir){
			if(i.isDirectory())
				superdelete(i);
			else
				i.delete();
		}
		f.delete();
	}
	/**傳入File參數檢查是否為空資料夾
	 * @param f 路徑參數**/
	public boolean isEmpty(String s){
		File f=new File(s){
			public String[] list(FilenameFilter filter) {
		        String[] filenames = list();
		        if (filter == null || filenames == null) {
		            return filenames;
		        }
		        List<String> result = new ArrayList<String>();
		        /**for (String filename : filenames) {
		            if (filter.accept(this, filename)) {
		                result.add(filename);
		            }
		        }**/
		        if (filenames.length!=0&&filter.accept(this,filenames[0])) {
	                result.add(filenames[0]);
	            }
		        return result.toArray(new String[result.size()]);
		    }
		};
		File[] Dir = f.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String filename) {
				return true;
			}});
		return (Dir.length!=0)?false:true;
	}
}
