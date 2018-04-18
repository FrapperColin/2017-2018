package state;

public interface StateDoor {
	
	public void opening();
	public void closing();
	public void pause();
	public void closed();
	public void opened();
	public void blocked();
	public void recovery();
	public void getState();

}
