package main;

import java.util.ArrayList;
import java.util.List;

public class Player {

	public int playerId ;
	public String playerName ;
	public List<Fighter> fighters = new ArrayList<>() ;
	
	public Player(int playerId1,String playerName1) {
		playerId = playerId1 ;
		playerName = playerName1;
	}
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public List<Fighter> getFighters() {
		return fighters;
	}
	public void setFighters(List<Fighter> fighters) {
		this.fighters = fighters;
	}
	@Override
	public String toString() {
		if(fighters !=null)
		{
			return "Player [playerId=" + playerId + ", playerName=" + playerName + ", fighters=" + fighters.toString() + "]";
		}
		return "null";
	}
}
