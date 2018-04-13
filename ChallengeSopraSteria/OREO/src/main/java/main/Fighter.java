package main;

import java.util.ArrayList;
import java.util.List;

public class Fighter {

	public String fighterClass ;
	public int orderNumberInTeam ;
	public boolean isDead ;
	public int maxAvailableMana ;
	public int maxAvailableLife ;
	public int currentMana ;
	public int currentLife ;
	public List<States> states = new ArrayList<>();
	public String action ;
	public String fighterID ;
	public String receivedAttacks ;
	public String diffMana; 
	public String diffLife ;
	
	public Fighter(String fighterClass, int orderNumberInTeam, boolean isDead, int maxAvailableMana,
			int maxAvailableLife, int currentMana, int currentLife, String action,
			String fighterID, String receivedAttacks, String diffMana, String diffLife) {
		this.fighterClass = fighterClass;
		this.orderNumberInTeam = orderNumberInTeam;
		this.isDead = isDead;
		this.maxAvailableMana = maxAvailableMana;
		this.maxAvailableLife = maxAvailableLife;
		this.currentMana = currentMana;
		this.currentLife = currentLife;
		this.action = action;
		this.fighterID = fighterID;
		this.receivedAttacks = receivedAttacks;
		this.diffMana = diffMana;
		this.diffLife = diffLife;
	}
	public String getFighterID() {
		return fighterID;
	}
	public void setFighterID(String fighterID) {
		this.fighterID = fighterID;
	}
	public String getFighterClass() {
		return fighterClass;
	}
	public void setFighterClass(String fighterClass) {
		this.fighterClass = fighterClass;
	}
	public int getOrderNumberInTeam() {
		return orderNumberInTeam;
	}
	public void setOrderNumberInTeam(int orderNumberInTeam) {
		this.orderNumberInTeam = orderNumberInTeam;
	}
	public boolean isDead() {
		return isDead;
	}
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	public int getMaxAvailableMana() {
		return maxAvailableMana;
	}
	public void setMaxAvailableMana(int maxAvailableMana) {
		this.maxAvailableMana = maxAvailableMana;
	}
	public int getMaxAvailableLife() {
		return maxAvailableLife;
	}
	public void setMaxAvailableLife(int maxAvailableLife) {
		this.maxAvailableLife = maxAvailableLife;
	}
	public int getCurrentMana() {
		return currentMana;
	}
	public void setCurrentMana(int currentMana) {
		this.currentMana = currentMana;
	}
	public int getCurrentLife() {
		return currentLife;
	}
	public void setCurrentLife(int currentLife) {
		this.currentLife = currentLife;
	}
	public List<States> getStates() {
		return states;
	}
	public void setStates(List<States> states) {
		this.states = states;
	}
	public String getReceivedAttacks() {
		return receivedAttacks;
	}
	public void setReceivedAttacks(String receivedAttacks) {
		this.receivedAttacks = receivedAttacks;
	}
	@Override
	public String toString() {
		if(states != null)
		{
			return "Fighter [fighterID=" + fighterID + ", fighterClass=" + fighterClass + ", orderNumberInTeam="
					+ orderNumberInTeam + ", isDead=" + isDead + ", maxAvailableMana=" + maxAvailableMana
					+ ", maxAvailableLife=" + maxAvailableLife + ", currentMana=" + currentMana + ", currentLife="
					+ currentLife + ", states=" + states.toString() + ", receivedAttacks=" + receivedAttacks + "]";
		}
		else
		{
			return "Fighter [fighterID=" + fighterID + ", fighterClass=" + fighterClass + ", orderNumberInTeam="
					+ orderNumberInTeam + ", isDead=" + isDead + ", maxAvailableMana=" + maxAvailableMana
					+ ", maxAvailableLife=" + maxAvailableLife + ", currentMana=" + currentMana + ", currentLife="
					+ currentLife + ", states="/* + states.toString()*/ + ", receivedAttacks=" + receivedAttacks + "]";
		}
		
		 
	}
	
	public boolean is_orc()
	{
		if (this.fighterClass.equals("ORC"))
		{
			return true ;
		}
		return false ;
	}
	public boolean is_priest()
	{
		if (this.fighterClass.equals("PRIEST"))
		{
			return true ;
		}
		return false ;
	}
	public boolean is_guard()
	{
		if (this.fighterClass.equals("GUARD"))
		{
			return true ;
		}
		return false ;
	}
	
	public boolean is_chaman() {
		if (this.fighterClass.equals("CHAMAN"))
		{
			return true ;
		}
		return false ;
	}
	public boolean is_paladin() {
		if (this.fighterClass.equals("PALADIN"))
		{
			return true ;
		}
		return false ;
	}
	public boolean is_archer() {
		if (this.fighterClass.equals("ARCHER"))
		{
			return true ;
		}
		return false ;
	}
	
	public String Fleche(Fighter E)
	{
		String play = "A"+this.orderNumberInTeam+",FIREBOLT,"+"E"+E.getOrderNumberInTeam();
		return play;
	}
	
	public String Stunt(Fighter E)
	{
		String play = "A"+this.orderNumberInTeam+",CHARGE,"+"E"+E.getOrderNumberInTeam();
		return play;
	}
	public String purifie(Fighter E)
	{
		String play = "A"+this.orderNumberInTeam+",CLEANSE,"+"A"+E.getOrderNumberInTeam();
		return play;
	}
	
	public String attack(Fighter E)
	{
		String play = "A"+this.orderNumberInTeam+",ATTACK,"+"E"+E.getOrderNumberInTeam();
		return play;
	}
	public String heal(Fighter A)
	{
		String play = "A"+this.orderNumberInTeam+",HEAL,"+"A"+A.getOrderNumberInTeam();
		return play;
	}
	public String tank(Fighter A)
	{
		String play = "A"+this.orderNumberInTeam+",PROTECT,"+"A"+A.getOrderNumberInTeam();
		return play;
	}
	public String defend()
	{
		String play = "A"+this.orderNumberInTeam+",DEFEND,"+"A"+this.orderNumberInTeam;
		return play;
	}
	public String yell(Fighter E)
	{
		String play = "A"+this.orderNumberInTeam+",YELL,"+"E"+E.getOrderNumberInTeam();
		return play;
	}
	public String rest()
	{
		String play = "A"+this.orderNumberInTeam+",REST,"+"A"+this.orderNumberInTeam;
		return play;
	}
	
	
	
	

	
}