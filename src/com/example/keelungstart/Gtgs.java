package com.example.keelungstart;

public class Gtgs {
	String gkn;//���I�W��
	double gpsxy1;//���I�y��X
	double gpsxy2;//���I�y��Y
	int xy;//���I�P�O��
	int pf;//���I�����P�O
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
