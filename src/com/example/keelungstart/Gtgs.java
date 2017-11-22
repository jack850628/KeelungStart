package com.example.keelungstart;

public class Gtgs {
	String gkn;//終點名稱
	double gpsxy1;//終點座標X
	double gpsxy2;//終點座標Y
	int xy;//終點判別值
	int pf;//景點類型判別
	public Gtgs(String a,double b,double c,int d,int e){
		gkn=a;
		gpsxy1=b;
		gpsxy2=c;
		xy=d;
		pf=e;
	}
	public String getgkn(){
		return gkn;
	}
	public double getgpsxy1(){
		return gpsxy1;
	}
	public double getgpsxy2(){
		return gpsxy2;
	}
	public int getxy(){
		return xy;
	}
	public int getpf(){
		return pf;
	}
}
