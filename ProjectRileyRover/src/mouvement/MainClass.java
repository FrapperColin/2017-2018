package mouvement;

import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class MainClass {

	public static void main(String[] args) {
		// avancer
		Motor.A.forward();
		Motor.B.forward();
		Delay.msDelay(10000); // 10 secondes
		Motor.A.stop();
		Motor.B.stop();

		// reculer
		Motor.A.backward();
		Motor.B.backward();
		Delay.msDelay(10000); // 10 secondes
		Motor.A.stop();
		Motor.B.stop();
		
		System.out.println("Done");
	}

}
