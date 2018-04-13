package main;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PlateauJeu {

	public List<Player> playerBoards = new ArrayList<>() ;
	private int nbrTurnsLeft ;
	public String playerMoves ;
	
	public PlateauJeu(int nbTurnsLeft1,String playerMoves1) {
		nbrTurnsLeft =nbTurnsLeft1;
		playerMoves = playerMoves1;
	}

	@Override
	public String toString() {
		return "[playerBoards=" + playerBoards.toString() + ", nbrTurnsLeft=" + nbrTurnsLeft + ", playerMoves="
				+ playerMoves + "]";
	}

	public List<Player> getPlayerBoards() {
		return playerBoards;
	}

	public void setPlayerBoards(List<Player> playerBoards) {
		this.playerBoards = playerBoards;
	}

	public int getNbTurnsLeft() {
		return nbrTurnsLeft ;
	}

	public void setNbTurnsLeft(int nbTurnsLeft) {
		this.nbrTurnsLeft = nbTurnsLeft;
	}

	public String getPlayerMoves() {
		return playerMoves;
	}

	public void setPlayerMoves(String playerMoves) {
		this.playerMoves = playerMoves;
	}
	
	
	
	
}
