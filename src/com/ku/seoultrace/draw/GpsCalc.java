package com.ku.seoultrace.draw;


public class GpsCalc {

	public static boolean calc(double lat1, double lon1, double lat2, double lon2)
	{
		double distnace = calDistance(lat1,lon1,lat2,lon2);

		if(distnace <=450.0)
			return true;
		else
			return false;
	}
	
	public static double calDistance(double lat1, double lon1, double lat2, double lon2){  
	    
	    double theta, dist;  
	    theta = lon1 - lon2;  
	    dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))   
	          * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));  
	    dist = Math.acos(dist);  
	    dist = rad2deg(dist);  
	      
	    dist= dist*60*1.1515;
	    dist= dist*1.609344;
	    dist = dist * 1000.0;        
	    
	    return dist;  
	}  
	  
	    // �־��� ��(degree) ���� �������� ��ȯ  
	private static double deg2rad(double deg){  
	    return (double)(deg * Math.PI / (double)180d);  
	}  
	  
	    // �־��� ����(radian) ���� ��(degree) ������ ��ȯ  
	private static double rad2deg(double rad){  
	    return (double)(rad * (double)180d / Math.PI);  
	}
	
}
