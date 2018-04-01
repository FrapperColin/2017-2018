package main;

import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class Sensor extends Thread{

	private EV3UltrasonicSensor capteur ;  
	private Controller controller;
	private boolean running;
	private final SampleProvider distanceMode;
	private final float[] distanceSample;

	public Sensor(EV3UltrasonicSensor _capteur) {
		this.capteur = _capteur; 
		this.running = true ;
		capteur.enable();
	    distanceMode = capteur.getDistanceMode();
	    distanceSample = new float[distanceMode.sampleSize()];
	}
	
	public boolean getRunning() {
		return running;
	}
	
	@Override
	public void run() {
		try {
			this.contact();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void contact() throws InterruptedException {
		while (running) {
			int distance = getDistance();
			System.out.println("Distance" + distance);
			if(20 < distance && distance <50 ) {
				controller.slowDown();
			} else if(distance <= 20) {
				controller.contact();
			}
		}
	}
	
	public boolean isUltrasonicDetected() {
		int distance = getDistance();
		System.out.println("Distance" + distance);
		if(distance<50) {
			return true ;
		}
		return false ;

	}
	
	public int getDistance() {
		distanceMode.fetchSample(distanceSample, 0);
		float result = distanceSample[0];
		return (int) (result * 100);
	}


	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public void arret() {
		capteur.disable();
		running = false ;
	}
}
