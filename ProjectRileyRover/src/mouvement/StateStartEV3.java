package mouvement;

public class StateStartEV3 implements StateEV3
{
	private EV3Car ev3;
	
	public StateStartEV3(EV3Car ev3)
	{
		this.ev3 = ev3;
	}
		
	//-----------------------------------------------------------------actions
	@Override
	public void accelerate(int value)
	{
			System.out.println("j accelere");
			this.ev3.increasedSpeedMotors(value,value);
			System.out.println("moteur gauche");
			System.out.println(this.ev3.getSpeedLeftMotor());
			System.out.println("moteur droit");
			System.out.println(this.ev3.getSpeedRightMotor());
			this.ev3.updateSpeedMotors();
	}

	@Override
	public void decelerated(int value)
	{
		System.out.println("je ralentis");
		if(this.ev3.getSpeedLeftMotor()-value == 0 && this.ev3.getSpeedLeftMotor() == this.ev3.getSpeedRightMotor())
		{
			stop();
		}
		else
		{
			this.ev3.decreasedSpeedMotors(value, value);
			//this.ev3.setSpeedLeftMotor(this.ev3.getSpeedLeftMotor()-value);
			//this.ev3.setSpeedRightMotor(this.ev3.getSpeedRightMotor()-value);
			this.ev3.updateSpeedMotors();
		}
		System.out.println("moteur gauche");
		System.out.println(this.ev3.getSpeedLeftMotor());
		System.out.println("moteur droit");
		System.out.println(this.ev3.getSpeedRightMotor());
	}

	@Override
	public void turnLeft(int ratio) /* ratio entre 1 et 3 ?*/
	{
		this.ev3.setSpeedRightMotor(this.ev3.getSpeedRightMotor()*ratio);
		System.out.println("moteur gauche");
		System.out.println(this.ev3.getSpeedLeftMotor());
		System.out.println("moteur droit");
		System.out.println(this.ev3.getSpeedRightMotor());
		this.ev3.updateSpeedMotors();
	}

	@Override
	public void turnRight(int ratio)
	{
		this.ev3.setSpeedLeftMotor(this.ev3.getSpeedLeftMotor()*ratio);
		System.out.println("moteur gauche");
		System.out.println(this.ev3.getSpeedLeftMotor());
		System.out.println("moteur droit");
		System.out.println(this.ev3.getSpeedRightMotor());
		this.ev3.updateSpeedMotors();
	}

	@Override
	public void stop()
	{
		System.out.println("je stop");
		this.ev3.getLeftMotor().startSynchronization();
		this.ev3.getLeftMotor().stop();
		this.ev3.getRightMotor().stop();
		this.ev3.getLeftMotor().endSynchronization();
		this.ev3.changeStateToStop();
	}
}
