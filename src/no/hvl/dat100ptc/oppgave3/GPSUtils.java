package no.hvl.dat100ptc.oppgave3;

import static java.lang.Math.*;

import no.hvl.dat100ptc.oppgave1.GPSPoint;

public class GPSUtils {

	public static double findMax(double[] da) {

		double max; 
		
		max = da[0];
		
		for (double d : da) {
			if (d > max) {
				max = d;
			}
		}
		
		return max;
	}

	public static double findMin(double[] da) {

		double min;

		min = da[0];
		
		for (double d : da) {
			if (d < min) {
				min = d;
			}
		}
		
		return min;

	}

	public static double[] getLatitudes(GPSPoint[] gpspoints) {

		double[] latitudes = new double[gpspoints.length];
		for (int i = 0; i < gpspoints.length; i++) {
			latitudes[i] = gpspoints[i].getLatitude();
		}
		return latitudes;
	}
	

	public static double[] getLongitudes(GPSPoint[] gpspoints) {

		double[] longitudes = new double[gpspoints.length];
		for (int i = 0; i < gpspoints.length; i++) {
			longitudes[i] = gpspoints[i].getLongitude();
		}
		return longitudes;

	}

	private static int R = 6371000; // jordens radius

	public static double distance(GPSPoint gpspoint1, GPSPoint gpspoint2) {

		double d;
		double latitude1, latitude2, deltaLatitude, deltaLongitude;
		
		latitude1 = toRadians(gpspoint1.getLatitude());
		latitude2 = toRadians(gpspoint2.getLatitude());
		deltaLatitude = toRadians(gpspoint2.getLatitude()-gpspoint1.getLatitude());
		deltaLongitude = toRadians(gpspoint2.getLongitude()-gpspoint1.getLongitude());
		
		double a = Math.pow((Math.sin(deltaLatitude/2)), 2) + Math.cos(latitude1)*Math.cos(latitude2)*Math.pow((Math.sin(deltaLongitude/2)), 2);
		double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		d = R * c;
		return d;
	}

	public static double speed(GPSPoint gpspoint1, GPSPoint gpspoint2) {

		int secs;
		double speed;
		double distance;

		secs = gpspoint2.getTime() - gpspoint1.getTime();
		distance = distance(gpspoint1, gpspoint2);
		
		speed = distance/secs * 3.6; //konvertere m/s til km/h
		
		return speed;

	}

	public static String formatTime(int secs) {

		String timestr;
		String TIMESEP = ":";
		int hours = secs/3600;
		int minutes = (secs%3600)/60;
		int seconds = secs%60;
		
		timestr = String.format("%02d",hours) + TIMESEP + String.format("%02d",minutes) + TIMESEP + String.format("%02d",seconds); 
		timestr = String.format("%10s", timestr); 
		return timestr;
	}
	private static int TEXTWIDTH = 10;

	public static String formatDouble(double d) {

		String str;
		double roundedDouble = Math.round(d * 100.0) / 100.0;
		str = "" + roundedDouble;
		
		str = String.format("%10s", str);
		return str;
	}
}
