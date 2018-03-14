package mouvement;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class EV3Car
{
	private EV3LargeRegulatedMotor leftMotor; //= new EV3LargeRegulatedMotor(BrickFinder.getLocal().getPort("A"));
    private EV3LargeRegulatedMotor rightMotor;// = new EV3LargeRegulatedMotor(BrickFinder.getLocal().getPort("B"));

	private int speedLeftMotor;
	private int speedRightMotor;

	private StateEV3 state;
	private StateStartEV3 stateStart;
	private StateStopEV3 stateStop;
	
	//----------------------------------------------constructeur
	
	public EV3Car(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor)
	{
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		speedLeftMotor = 0;
		speedRightMotor = 0;
		stateStart = new StateStartEV3(this);
		stateStop = new StateStopEV3(this);
		state = stateStop;
	}

	//----------------------------------------------transitions
	
	public void changeStateToStart()
	{
		state = stateStart;
	}

	public void changeStateToStop()
	{
		state = stateStop;
	}
	
	//----------------------------------------------

	public void accelerate(int value)
	{
		state.accelerate(value);
	}

	public void decelerated(int value)
	{
		state.decelerated(value);
	}

	public void turnLeft(int ratio)
	{
		state.turnLeft(ratio);
		
	}

	public void turnRight(int ratio)
	{
		state.turnRight(ratio);

	}
	
	public void forward()
    {
    	leftMotor.startSynchronization();
    	leftMotor.forward();
        rightMotor.forward();
        leftMotor.endSynchronization();
    }
	
	public void backward()
    {
    	leftMotor.startSynchronization();
    	leftMotor.backward();
        rightMotor.backward();
        leftMotor.endSynchronization();
        
    }
	
	public void stop()
    {	
    	state.stop();
    }
	
	//----------------------------------------------
	
	public boolean isStop()
	{
		return speedLeftMotor == speedRightMotor && speedLeftMotor == 0;
	}
	
	//----------------------------------------------set
	
	public void setSpeedLeftMotor(int value)
	{
		speedLeftMotor = value;
	}
	
	public void setSpeedRightMotor(int value)
	{
		speedRightMotor = value;
	}
	
	//----------------------------------------------get
	
	public int getSpeedLeftMotor()
	{
		return speedLeftMotor;
	}
	
	public int getSpeedRightMotor()
	{
		return speedRightMotor;
	}
	
	public EV3LargeRegulatedMotor getLeftMotor()
	{
		return leftMotor;
	}
	
	public EV3LargeRegulatedMotor getRightMotor()
	{
		return rightMotor;
	}
	
	public void increasedSpeedMotors(int valueLeftMotor, int valueRightMotor)
	{
		speedLeftMotor+=valueLeftMotor;
		speedRightMotor+=valueRightMotor;
	}
	
	public void decreasedSpeedMotors(int valueLeftMotor, int valueRightMotor)
	{
		speedLeftMotor-=valueLeftMotor;
		speedRightMotor-=valueRightMotor;
	}
	
	public void updateSpeedMotors()
	{
		leftMotor.startSynchronization();
    	leftMotor.setSpeed(speedLeftMotor);
        rightMotor.setSpeed(speedRightMotor);
    	leftMotor.endSynchronization();
	}
}