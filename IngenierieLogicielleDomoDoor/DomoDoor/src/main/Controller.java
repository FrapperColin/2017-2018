package main;

import java.util.ArrayList;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;
import state.StateDoor;
import state.StateDoorBlocked;
import state.StateDoorClosed;
import state.StateDoorClosing;
import state.StateDoorOpened;
import state.StateDoorOpening;
import state.StateDoorPartial;

public class Controller {

	private StateDoor actualState ;
	private StateDoor previousState ;
	private StateDoor stateBlocked ;
	private StateDoor stateClosed ;
	private StateDoor stateClosing ;
	private StateDoor stateOpened ;
	private StateDoor stateOpening ;
	private StateDoor statePartial ;


	private ArrayList<StateDoor> listState ;
	
	public Sensor sensorDoorOpened2;
	public Sensor sensorDoorClosed2; 

	
	private Motor motor1 ;
	private Motor motor2 ;

	private Port portSensorOpened2 = SensorPort.S1;
	private Port portSensorClosed2 = SensorPort.S2;


	public Controller() {
		this.motor1 = new Motor(new EV3LargeRegulatedMotor(MotorPort.A)) ;
		this.motor2 = new Motor(new EV3LargeRegulatedMotor(MotorPort.D)) ;

    	RegulatedMotor T[] = {this.motor1.getMotor()};
    	motor2.getMotor().synchronizeWith(T);

		this.actualState = new StateDoorClosed() ;

		this.sensorDoorClosed2 = new Sensor(new EV3TouchSensor(portSensorClosed2)) ;
		this.sensorDoorClosed2.setController(this);
		this.sensorDoorClosed2.start();
		this.sensorDoorOpened2 = new Sensor(new EV3TouchSensor(portSensorOpened2)) ;
		this.sensorDoorOpened2.setController(this);
		this.sensorDoorOpened2.start();

	
		this.stateBlocked = new StateDoorBlocked();
		this.stateClosed = new StateDoorClosed();
		this.stateClosing = new StateDoorClosing();
		this.stateOpened = new StateDoorOpened();
		this.stateOpening = new StateDoorOpening();
		this.statePartial = new StateDoorPartial();
		this.listState = new ArrayList<StateDoor>() ;
	}
	
	public void open() throws InterruptedException {
		if(actualState instanceof StateDoorClosed) {
			System.out.println("From closed to opening");
	    	motor2.getMotor().startSynchronization();
			motor1.pull();
			motor2.pull();
	        motor2.getMotor().endSynchronization();

			while(sensorDoorClosed2.isContact()) {}
			Thread.sleep(500);
				actualState = stateOpening;
			
			saveState(actualState);
		} else if(actualState instanceof StateDoorOpened) {
			System.out.println("on ne peut pas ouvrir car deja ouvert");
		} else if(actualState instanceof StateDoorOpening) {
			System.out.println("On met en pause");
	    	motor2.getMotor().startSynchronization();
			motor1.stop();
			motor2.stop();
	        motor2.getMotor().endSynchronization();

			actualState = statePartial;
			saveState(actualState);
		} else if(actualState instanceof StateDoorClosing) {
			System.out.println("On ne peux pas faire pause, car fermeture, press close if you want to");
		} else if(actualState instanceof StateDoorPartial) {
			System.out.println("On reprend l'ouverture");
	    	motor2.getMotor().startSynchronization();
			motor1.pull();
			motor2.pull();
	        motor2.getMotor().endSynchronization();

			actualState = stateOpening;
			saveState(actualState);
		}
	}

	public void close() throws InterruptedException {
		if(actualState instanceof StateDoorClosed) {
			System.out.println("on ne peut pas fermer car deja ferme");
		} else if(actualState instanceof StateDoorOpened) {
			System.out.println("on peut fermer");
			actualState.getState();
	    	motor2.getMotor().startSynchronization();
			motor1.push();
			motor2.push();
	        motor2.getMotor().endSynchronization();

			while(sensorDoorOpened2.isContact()) {}
			Thread.sleep(500);

			actualState = stateClosing;
			
			saveState(actualState);
		} else if(actualState instanceof StateDoorOpening) {			
			System.out.println("Impossible de fermer car ouverture");
		} else if(actualState instanceof StateDoorClosing) {
			System.out.println("Etat pause");
	    	motor2.getMotor().startSynchronization();
			motor1.stop();
			motor2.stop();
	        motor2.getMotor().endSynchronization();

			actualState = statePartial;
			saveState(actualState);
		} else if(actualState instanceof StateDoorPartial) {
			System.out.println("Reprendre la fermeture");
	    	motor2.getMotor().startSynchronization();
			motor1.push();
			motor2.push();
	        motor2.getMotor().endSynchronization();
			actualState = stateClosing;
			saveState(actualState);
		}
	}
	
	public void contact() throws InterruptedException  {
		if(actualState instanceof StateDoorClosing) {
			System.out.println("contact porte ferme");
	    	motor2.getMotor().startSynchronization();
			motor1.stop();
			motor2.stop();
	        motor2.getMotor().endSynchronization();
			actualState = stateClosed; 
			saveState(actualState);
		} else if(actualState instanceof StateDoorOpening) {
			System.out.println("contact porte ouverte");
			actualState.getState();
	    	motor2.getMotor().startSynchronization();
			motor1.stop();
			motor2.stop();
	        motor2.getMotor().endSynchronization();
			actualState = stateOpened; 
			saveState(actualState);
		}
	}
	
	public void displayListSate() {
		for (int i =0 ; i<listState.size();i++) {
			listState.get(i).getState();
		}
	}
	
	public void backUpKey() {
		System.out.println("Bouton reprise activée activé");
		listState.get(listState.size()-2).getState();
		previousState = getLastEtat();
		if(previousState instanceof StateDoorClosed) {
			System.out.println("On réinitialise l'état");
			actualState = stateClosed; 
			saveState(actualState);
		} else if(previousState instanceof StateDoorOpened) {
			System.out.println("On réinitialise l'état");
			actualState = stateOpened; 
			saveState(actualState);
		} else if(previousState instanceof StateDoorOpening) {			
			actualState = stateOpening; 
			saveState(actualState);
	    	motor2.getMotor().startSynchronization();
			motor1.pull();
			motor2.pull();
	        motor2.getMotor().endSynchronization();
		} else if(previousState instanceof StateDoorClosing) {
			actualState = new StateDoorClosing(); 
			saveState(actualState);
	    	motor2.getMotor().startSynchronization();
			motor1.push();
			motor2.push();
	        motor2.getMotor().endSynchronization();
		} else if(previousState instanceof StateDoorPartial) {
			System.out.println("Etat pause, reprendre l'ouverture");
			actualState = statePartial;
			saveState(actualState);
		}
	}
	
	public void urgence() {
		System.out.println("Bouton urgence activé");
    	motor2.getMotor().startSynchronization();
		motor1.stop();
		motor2.stop();
        motor2.getMotor().endSynchronization();
		actualState = stateBlocked; 
		saveState(actualState);
	}
	
	public StateDoor getActualState() {
		return actualState;
	}

	public void setActualState(StateDoor actualState) {
		this.actualState = actualState;
	}

	private StateDoor getLastEtat() {
		return listState.get(listState.size()-2);
	}
	
	public void saveContact() throws InterruptedException {
		contact();
	}
	
	public void saveState(StateDoor e) {
		listState.add(e);
	}
}
