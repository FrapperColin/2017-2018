package main;


import lejos.hardware.sensor.*;
public class Sensor extends Thread{

	private EV3TouchSensor capteur ;  
	private Controller controller;
	private boolean running;

	public Sensor(EV3TouchSensor _capteur) {
		this.capteur = _capteur; 
		this.running = true ;
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
			int size = this.capteur.sampleSize();
			float[] sample = new float[size];
			capteur.fetchSample(sample,0);
			if( sample[0] == 1.0) {
				this.controller.saveContact();
			}
		}
	}
	
	public boolean isContact() {
		int size = this.capteur.sampleSize();
		float[] sample = new float[size];
		capteur.fetchSample(sample,0);
		if( sample[0] == 1.0) {
			return true;
		}
		return false;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public void arret() {
		running = false ;
	}
}
