package no.hvl.dat100ptc.oppgave5;

import javax.swing.JOptionPane;

import easygraphics.EasyGraphics;
import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

public class ShowRoute extends EasyGraphics {

	private static int MARGIN = 50;
	private static int MAPXSIZE = 800;
	private static int MAPYSIZE = 800;

	private GPSPoint[] gpspoints;
	private GPSComputer gpscomputer;
	
	public ShowRoute() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");
		gpscomputer = new GPSComputer(filename);

		gpspoints = gpscomputer.getGPSPoints();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void run() {

		makeWindow("Route", MAPXSIZE + 2 * MARGIN, MAPYSIZE + 2 * MARGIN);

		showRouteMap(MARGIN + MAPYSIZE);
		
		showStatistics();
	}

	// antall x-pixels per lengdegrad
	public double xstep() {

		double maxlon = GPSUtils.findMax(GPSUtils.getLongitudes(gpspoints));
		double minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));

		double xstep = MAPXSIZE / (Math.abs(maxlon - minlon)); 

		return xstep;
	}

	// antall y-pixels per breddegrad
	public double ystep() {
		
		double maxlon = GPSUtils.findMax(GPSUtils.getLatitudes(gpspoints));
		double minlon = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));

		double ystep = MAPYSIZE / (Math.abs(maxlon - minlon)); 

		return ystep;
		
	}

	public void showRouteMap(int ybase) {
		setColor(0,255,0);
		
		double[] latitudes = GPSUtils.getLatitudes(gpspoints);
		double[] longitudes = GPSUtils.getLongitudes(gpspoints);
		int x;
		int y;
		
		for (int i = 0;i < gpspoints.length; i++) {
			x = (int) Math.round((longitudes[i]-GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints)))*xstep()); //tar lengdegrad og trekker fra minste lengdegrad deretter ganger med antall piksler per
			y = ybase - (int) Math.round(((latitudes[i]-GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints)))*ystep()));
			fillCircle(x,y,4);
			
			System.out.println(x);
			System.out.println(y);
		}
		
	}

	public void showStatistics() {

		int TEXTDISTANCE = 20;

		setColor(0,0,0);
		setFont("Courier",12);
		double WEIGHT = 80.0;
		int y = TEXTDISTANCE;
		
		String totalTime = "Total time     :" + String.format("%11s", GPSUtils.formatTime(gpscomputer.totalTime()));
		String totalDistance = "Total distance :" + String.format("%11s", GPSUtils.formatDouble(gpscomputer.totalDistance())) + " km";
		String totalElevation = "Total elevation:" + String.format("%11s", GPSUtils.formatDouble(gpscomputer.totalElevation())) + " m";
		String maxSpeed = "Max speed      :" + String.format("%11s", GPSUtils.formatDouble(gpscomputer.maxSpeed())) + " km/t";
		String averageSpeed = "Average speed  :" + String.format("%11s", GPSUtils.formatDouble(gpscomputer.averageSpeed())) + " km";
		String energy = "Energy         :" + String.format("%11s", GPSUtils.formatDouble(gpscomputer.totalKcal(WEIGHT))) + " kcal";
		
		drawString(totalTime,TEXTDISTANCE,y);
		y += TEXTDISTANCE;
		drawString(totalDistance,TEXTDISTANCE,y);
		y += TEXTDISTANCE;
		drawString(totalElevation,TEXTDISTANCE,y);
		y += TEXTDISTANCE;
		drawString(maxSpeed,TEXTDISTANCE,y);
		y += TEXTDISTANCE;
		drawString(averageSpeed,TEXTDISTANCE,y);
		y += TEXTDISTANCE;
		drawString(energy,TEXTDISTANCE,y);
	}

}
