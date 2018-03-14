package mouvement;

public interface StateEV3
{		
	//-----------------------------------------------------------------actions
	public void accelerate(int value);
	public void decelerated(int value);
	public void turnLeft(int ratio);
	public void turnRight(int ratio);
	public void stop();
}
