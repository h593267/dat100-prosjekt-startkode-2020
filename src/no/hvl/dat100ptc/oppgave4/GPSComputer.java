package no.hvl.dat100ptc.oppgave4;

import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSData;
import no.hvl.dat100ptc.oppgave2.GPSDataConverter;
import no.hvl.dat100ptc.oppgave2.GPSDataFileReader;
import no.hvl.dat100ptc.oppgave3.GPSUtils;

public class GPSComputer {
	
	private GPSPoint[] gpspoints;
	
	public GPSComputer(String filename) {

		GPSData gpsdata = GPSDataFileReader.readGPSFile(filename);
		gpspoints = gpsdata.getGPSPoints();

	}

	public GPSComputer(GPSPoint[] gpspoints) {
		this.gpspoints = gpspoints;
	}
	
	public GPSPoint[] getGPSPoints() {
		return this.gpspoints;
	}
	
	// beregn total distances (i meter)
	public double totalDistance() {

		double distance = 0;

		for (int i = 0; i < gpspoints.length-1; i++) {
			distance += GPSUtils.distance(gpspoints[i], gpspoints[i+1]); //legger til distansen mellom hvert punkt
		}
		return distance;
	}

	public double totalElevation() {

		double elevation = 0;

		for (int i = 0; i < gpspoints.length-1; i++) {
			if (gpspoints[i].getElevation() < gpspoints[i+1].getElevation()) { //sjekker om det er økning i høydemeter mellom punkter
				elevation += gpspoints[i+1].getElevation() - gpspoints[i].getElevation(); //hvis høydeøkning, legger til i total høyde
			}
		}
		return elevation;

	}

	public int totalTime() {
		
		int time = 0;

		time += gpspoints[gpspoints.length-1].getTime()-gpspoints[0].getTime(); //tid slutt minus tid start
		
		return time;
	
	}
		

	public double[] speeds() {
		
		double[] speeds = new double[gpspoints.length-1]; //tabellen er 1 kortere enn gpspoints
		
		for (int i = 0; i < speeds.length; i++) {
			speeds[i] = GPSUtils.speed(gpspoints[i], gpspoints[i+1]); //bruker speed() fra GPSUtils
		}
		return speeds;
	}
	
	public double maxSpeed() {
		
		double maxspeed = 0;
		
		maxspeed = GPSUtils.findMax(speeds());
		
		return maxspeed;
	}

	public double averageSpeed() {

		double average = 0;
		
		average = (totalDistance()/totalTime())*3.6; //m/s til kmh. Oppgaven fortalte ikke hva slags fartsenhet. Dårlig
		
		return average;
		
	}



	/*
	 * bicycling, <10 mph, leisure, to work or for pleasure 4.0 bicycling,
	 * general 8.0 bicycling, 10-11.9 mph, leisure, slow, light effort 6.0
	 * bicycling, 12-13.9 mph, leisure, moderate effort 8.0 bicycling, 14-15.9
	 * mph, racing or leisure, fast, vigorous effort 10.0 bicycling, 16-19 mph,
	 * racing/not drafting or >19 mph drafting, very fast, racing general 12.0
	 * bicycling, >20 mph, racing, not drafting 16.0
	 */

	// conversion factor m/s to miles per hour
	public static double MS = 2.236936;

	// beregn kcal gitt weight og tid der kjøres med en gitt hastighet
	public double kcal(double weight, int secs, double speed) {

		double kcal;

		// MET: Metabolic equivalent of task angir (kcal x kg-1 x h-1)
		double met = 0;		
		double speedmph = speed * MS;

		if (speedmph < 10) { //finner met
			met = 4.0;
		} else if (speedmph < 12) {
			met = 6.0;
		} else if (speedmph < 14) {
			met = 8.0;
		} else if (speedmph < 16) {
			met = 10.0;
		} else if (speedmph < 20) {
			met = 12.0;
		} else {
			met = 16.0;
		}
		
		kcal = met*weight/3600*secs; //finner kcal i timen for vekt, gjør om til sekunder så ganger med antall sekunder
		
		return kcal;
	}

	public double totalKcal(double weight) {

		double totalkcal = 0;

		for (int i = 0; i < gpspoints.length-1; i++) {
			totalkcal += kcal(weight, gpspoints[i+1].getTime()-gpspoints[i].getTime(), speeds()[i]/3.6);
		}
		
		return totalkcal;
		
	}
	
	private static double WEIGHT = 80.0;
	
	public void displayStatistics() {

		System.out.println("==============================================");

		System.out.print("Total time     :");
		System.out.printf("%11s", GPSUtils.formatTime(totalTime()));
		System.out.println("");
		
		System.out.print("Total distance :");
		System.out.printf("%11s", GPSUtils.formatDouble(totalDistance()));
		System.out.println(" km");
		
		System.out.print("Total elevation:");
		System.out.printf("%11s", GPSUtils.formatDouble(totalElevation()));
		System.out.println(" m");
		
		System.out.print("Max speed      :");
		System.out.printf("%11s", GPSUtils.formatDouble(maxSpeed()));
		System.out.println(" km/t");
		
		System.out.print("Average speed  :");
		System.out.printf("%11s", GPSUtils.formatDouble(averageSpeed()));
		System.out.println(" km/t");
		
		System.out.print("Energy         :");
		System.out.printf("%11s", GPSUtils.formatDouble(totalKcal(WEIGHT)));
		System.out.println(" kcal");
		
		System.out.println("==============================================");

	}
	

}
