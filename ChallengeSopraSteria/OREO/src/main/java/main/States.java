package main;

public class States {

	public String type ;
	public int remainingDuration ;
	
	public States() {
		
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getRemainingDuration() {
		return remainingDuration;
	}
	public void setRemainingDuration(int remainingDuration) {
		this.remainingDuration = remainingDuration;
	}
	@Override
	public String toString() {
		return "States [type=" + type + ", remainingDuration=" + remainingDuration + "]";
	}
	
	
}

