package main;

import lejos.robotics.RegulatedMotor;

public class Motor {

	private RegulatedMotor motor;
	private int speed ;
	private int previousSpeed ;

	public Motor(RegulatedMotor _motor) {
		this.motor =_motor;
	}
	
	public int getPreviousSpeed() {
		return previousSpeed;
	}

	public void setPreviousSpeed(int previousSpeed) {
		this.previousSpeed = previousSpeed;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
		motor.setSpeed(speed);
	}

	public void movingBackward() {
		setSpeed(50);
		motor.backward();
	}
	
	public void movingForward() {
		setSpeed(50);
		motor.forward();
	}
	
	public RegulatedMotor getMotor() {
		return motor;
	}

	public void setMotor(RegulatedMotor motor) {
		this.motor = motor;
	}

	public void stop() {
		motor.stop();
		setSpeed(0);
	}

	public void rotateHalf() {
		motor.rotate(90);
	}
}
