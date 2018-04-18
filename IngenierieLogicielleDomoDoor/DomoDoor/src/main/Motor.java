package main;

import lejos.robotics.RegulatedMotor;

public class Motor {

	private RegulatedMotor motor;

	public Motor(RegulatedMotor _motor) {
		this.motor =_motor;
 		motor.setSpeed(20);
	}

	public RegulatedMotor getMotor() {
		return motor;
	}

	public void setMotor(RegulatedMotor motor) {
		this.motor = motor;
	}

	public void push() {
		motor.backward();
	}
	
	public void pull() {
		motor.forward();
	}
	
	
	public void stop() {
		motor.stop();
	}
	
}
