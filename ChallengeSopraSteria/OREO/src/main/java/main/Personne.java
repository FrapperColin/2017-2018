package main;

public class Personne {

	public String ID ;
	public String classe ;
	public int orderNumber ;
	public boolean isDead ;
	public int maxMana ;
	public int maxLife ;
	public int currentMana ;
	public int currentLife ;
	public String receiveAttacks ;
	
	public Personne(String iD, String classe, int orderNumber, boolean isDead, int maxMana, int maxLife,
			int currentMana, int currentLife, String receiveAttacks) {
		ID = iD;
		this.classe = classe;
		this.orderNumber = orderNumber;
		this.isDead = isDead;
		this.maxMana = maxMana;
		this.maxLife = maxLife;
		this.currentMana = currentMana;
		this.currentLife = currentLife;
		this.receiveAttacks = receiveAttacks;
	}

	
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public boolean isDead() {
		return isDead;
	}
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	public int getMaxMana() {
		return maxMana;
	}
	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}
	public int getMaxLife() {
		return maxLife;
	}
	public void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
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
	public String getReceiveAttacks() {
		return receiveAttacks;
	}
	public void setReceiveAttacks(String receiveAttacks) {
		this.receiveAttacks = receiveAttacks;
	}
	@Override
	public String toString() {
		return "Personne [ID=" + ID + ", classe=" + classe + ", orderNumber=" + orderNumber + ", isDead=" + isDead
				+ ", maxMana=" + maxMana + ", maxLife=" + maxLife + ", currentMana=" + currentMana + ", currentLife="
				+ currentLife + ", receiveAttacks=" + receiveAttacks + "]";
	}
	
}
