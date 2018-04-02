package main;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import state.StateCar;
import state.StateCarMovingBackward;
import state.StateCarMovingForward;
import state.StateCarStopped;
import state.StateCarSlowingDown;
import state.StateCarBackUp;

public class Controller {
	private StateCar actualState ;
	private StateCar stateMovingForward ;
	private StateCar stateMovingBackward ;
	private StateCar stateStopped ;
	private StateCar stateSlowingDown ;
	private StateCar stateBackUp ;


	private Motor leftMotor; 
    private Motor rightMotor;
    
    public Sensor ultrasonicSensor;
    
	public Controller()
	{
		this.leftMotor = new Motor(new EV3LargeRegulatedMotor(MotorPort.D));
	    this.rightMotor = new Motor(new EV3LargeRegulatedMotor(MotorPort.A));
	    this.ultrasonicSensor = new Sensor(new EV3UltrasonicSensor(SensorPort.S2));
		this.ultrasonicSensor.setController(this);
		this.ultrasonicSensor.start();

    	RegulatedMotor T[] = {this.rightMotor.getMotor()};
    	leftMotor.getMotor().synchronizeWith(T);

		this.actualState = new StateCarStopped();
		this.stateMovingForward = new StateCarMovingForward();
		this.stateMovingBackward = new StateCarMovingBackward();
		this.stateStopped = new StateCarStopped();
		this.stateSlowingDown = new StateCarSlowingDown();
		this.stateBackUp = new StateCarBackUp();

	}

	public void movingForward () {
		if(actualState instanceof StateCarStopped) {
			System.out.println("want to go forward after stop");

	    	leftMotor.getMotor().startSynchronization();
	    	leftMotor.movingForward();
	        rightMotor.movingForward();
	        actualState = stateMovingForward;
	        leftMotor.getMotor().endSynchronization();
		} else if(actualState instanceof StateCarMovingBackward) {
			System.out.println("Can't moving forward if you're moving backward, better stop first");
		} else if(actualState instanceof StateCarMovingForward) {
			System.out.println("Already movingForward");
		}
	}
	
	public void movingBackward() {
		if(actualState instanceof StateCarStopped) {
			System.out.println("want to go backward after stop");
			System.out.println(leftMotor.getMotor().getSpeed());
	    	leftMotor.getMotor().startSynchronization();
	    	leftMotor.movingBackward();
	    	rightMotor.movingBackward();
	        actualState = stateMovingBackward;
	        leftMotor.getMotor().endSynchronization();
		} else if(actualState instanceof StateCarMovingForward) {
			System.out.println("Can't moving backward if you're moving forward, better stop first");
		} else if(actualState instanceof StateCarMovingBackward) {
			System.out.println("Already moving backward");
		}
	}
	
	public void stop() {
		if(actualState instanceof StateCarStopped) {
	    	System.out.println("AlreadyStopped");
		} else if(actualState instanceof StateCarMovingForward) {
	    	leftMotor.getMotor().startSynchronization();
	    	leftMotor.stop();
	    	rightMotor.stop();
	        actualState = stateStopped;
	        leftMotor.getMotor().endSynchronization();
		} else if(actualState instanceof StateCarMovingBackward) {
	    	leftMotor.getMotor().startSynchronization();
	    	leftMotor.stop();
	    	rightMotor.stop();
	        actualState = stateStopped;
	        leftMotor.getMotor().endSynchronization();
		} else if(actualState instanceof StateCarSlowingDown) {
	    	leftMotor.getMotor().startSynchronization();
	    	leftMotor.stop();
	    	rightMotor.stop();
	        actualState = stateStopped;
	        leftMotor.getMotor().endSynchronization();
		} else if(actualState instanceof StateCarBackUp) {
	    	leftMotor.getMotor().startSynchronization();
	    	leftMotor.stop();
	    	rightMotor.stop();
	        actualState = stateStopped;
	        leftMotor.getMotor().endSynchronization();
		} else if(actualState instanceof StateCarSlowingDown) {
	    	leftMotor.getMotor().startSynchronization();
	    	leftMotor.stop();
	    	rightMotor.stop();
	        actualState = stateStopped;
	        leftMotor.getMotor().endSynchronization();
		}
	}
	
	public void turnLeft(int ratio) {
    	leftMotor.getMotor().startSynchronization();
    	leftMotor.setSpeed(leftMotor.getSpeed());
        rightMotor.setSpeed(rightMotor.getSpeed() *ratio);
        leftMotor.getMotor().endSynchronization();
	}
	
	public void turnRight(int ratio) {
		leftMotor.getMotor().startSynchronization();
    	leftMotor.setSpeed(leftMotor.getSpeed() * ratio);
        rightMotor.setSpeed(rightMotor.getSpeed());
        leftMotor.getMotor().endSynchronization();
	}

	public void accelerate(int value) {
		leftMotor.getMotor().startSynchronization();
    	leftMotor.setSpeed(leftMotor.getSpeed() + value);
        rightMotor.setSpeed(rightMotor.getSpeed() + value);
        leftMotor.getMotor().endSynchronization();
	}

	public void decelerated(int value) {
		if(leftMotor.getSpeed() - value <= 0 || rightMotor.getSpeed() - value <= 0) {
			leftMotor.getMotor().startSynchronization();
	    	leftMotor.stop();
	    	rightMotor.stop();
	        actualState = stateStopped;
	        leftMotor.getMotor().endSynchronization();
		} else {
			leftMotor.getMotor().startSynchronization();
	    	leftMotor.setSpeed(leftMotor.getSpeed() - value);
	        rightMotor.setSpeed(rightMotor.getSpeed() - value);
	        leftMotor.getMotor().endSynchronization();
		}
	}

	public void slowDown() {
		if(actualState instanceof StateCarMovingForward) {
			leftMotor.setPreviousSpeed(leftMotor.getSpeed());
			rightMotor.setPreviousSpeed(rightMotor.getSpeed());
			actualState = stateSlowingDown ;
			leftMotor.getMotor().startSynchronization();
	    	leftMotor.setSpeed(30);
	        rightMotor.setSpeed(30);
	        leftMotor.getMotor().endSynchronization();
		}
	}

	public void contact() throws InterruptedException {
		if(actualState instanceof StateCarSlowingDown) {
			actualState = stateBackUp ;
			leftMotor.getMotor().startSynchronization();
			leftMotor.stop();
			rightMotor.stop();
	        leftMotor.getMotor().endSynchronization();
	        while(ultrasonicSensor.isUltrasonicDetected()) {
				leftMotor.getMotor().startSynchronization();
		        leftMotor.movingForward();
		        rightMotor.movingBackward();
		        leftMotor.getMotor().endSynchronization();
	        }
	        Thread.sleep(4000);
			leftMotor.getMotor().startSynchronization();
	        leftMotor.movingForward();
	        leftMotor.setSpeed(leftMotor.getPreviousSpeed());
	        rightMotor.movingForward();
	        rightMotor.setSpeed(rightMotor.getPreviousSpeed());
	        leftMotor.getMotor().endSynchronization();
	        actualState = stateMovingForward;
		}
	}
}