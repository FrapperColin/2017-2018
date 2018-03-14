package mouvement;

public class StateStopEV3 implements StateEV3
{
	private EV3Car ev3;
	
	
	public StateStopEV3(EV3Car ev3)
	{
		this.ev3 = ev3;
	}
		
	//-----------------------------------------------------------------actions
	@Override
	public void accelerate(int value)
	{
		System.out.println("j etais arrete, j avance");
		this.ev3.setSpeedLeftMotor(value);
		this.ev3.setSpeedRightMotor(value);
		this.ev3.updateSpeedMotors();
		System.out.println("moteur gauche");
		System.out.println(this.ev3.getSpeedLeftMotor());
		System.out.println("moteur droit");
		System.out.println(this.ev3.getSpeedRightMotor());
		this.ev3.forward();
		this.ev3.changeStateToStart();
	}

	@Override
	public void decelerated(int value)
	{
		System.out.println("j etais arrete, je recule");
		this.ev3.setSpeedLeftMotor(value);
		this.ev3.setSpeedRightMotor(value);
		this.ev3.updateSpeedMotors();
		System.out.println("moteur gauche");
		System.out.println(this.ev3.getSpeedLeftMotor());
		System.out.println("moteur droit");
		System.out.println(this.ev3.getSpeedRightMotor());
		this.ev3.backward();
		this.ev3.changeStateToStart();
	}

	@Override
	public void turnLeft(int ratio){}
	@Override
	public void turnRight(int ratio){}
	@Override
	public void stop() {}
}
