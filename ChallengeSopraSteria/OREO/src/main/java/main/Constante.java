package main;

public class Constante {

	public static String getPong =  "http://prod.codeandplay.date/epic-ws/epic/ping" ;
	/**
	 * Retourne syst�matiquement "pong".
	 * Cette m�thode n'est d'aucune utilit� dans le d�roulement d'une partie. 
	 */
	
	public static String getIdentifiant =  "http://prod.codeandplay.date/epic-ws/epic/player/getIdEquipe/" ;
	/**
	 *  nomEquipe : Nom de l��quipe
	 *	motDePasse : Mot de passe de l'�quipe 
	 *  Envoie l'identifiant de l'�quipe � partir de son nom et du mot de passe associ�.
	 *	Utilisez cette m�thode pour obtenir votre identifiant ! 
	 */
	public static String getIdentifiantVersus =  "http://prod.codeandplay.date/epic-ws/epic/player/getIdEquipe/" ;

	
	public static String getIdentifiantPartie =  "http://prod.codeandplay.date/epic-ws/epic/versus/next/" ;
	
	/**
	 *  idEquipe : Identifiant de l��quipe 
	 * Retourne l�identifiant de la partie � laquelle l��quipe doit participer
	 * "NA" si aucune partie n'est ouverte pour cette �quipe.
	 * 
	 */
	
	public static String getIdentifiantVersusBot =  "http://prod.codeandplay.date/epic-ws/epic/practice/new/" ;
	/**
	 * Cr�e une nouvelle partie contre une IA du niveau souhait� pour l��quipe concern�e
	 * "NA" si la partie ne peut pas �tre cr��e 
	 *  numberBot : num�ro de l�IA
	 * num�ro de 1 � 12 et de 30 � 32
	 * idEquipe : Identifiant de l��quipe 
	 */
	
	
	public static String getAction =  "http://prod.codeandplay.date/epic-ws/epic/game/status/" ;
	/**
	 * Indique si c�est au tour de l'�quipe indiqu�e de jouer dans la partie concern�e
		Retourne :
		
		    "CANPLAY" si vous pouvez jouer
		    "CANTPLAY" si vous ne pouvez pas encore jouer
		    "VICTORY" si vous avez gagn� la partie
		    "DEFEAT" si vous avez perdu la partie
		    "DRAW" si la partie s'est finie sur un match nul
		    "CANCELLED" si la partie a �t� annul�e

	 */
	
	public static String getPlateauJeu =  "http://prod.codeandplay.date/epic-ws/epic/game/board/" ;

	/**
	 * Retourne le plateau de jeu de la partie concern�e.
	 */
	
	public static String getPremiereEquipe =  "http://prod.codeandplay.date/epic-ws/epic/game/board/" ;

	/**
	 * Retourne le plateau de jeu de la partie concern�e. 
	 * La premi�re �quipe retourn�e est celle dont l'id est renseign�
	 */
	public static String getLastMove =  "http://prod.codeandplay.date/epic-ws/epic/game/getlastmove/" ;

	/**
	 * Retourne le dernier coup jou� sur le plateau de la partie indiqu�e
	 */
	public static String getResultatCoups =  "http://prod.codeandplay.date/epic-ws/epic/game/play/" ;

	public static String getNameOpponent =  "http://prod.codeandplay.date/epic-ws/epic/game/opponent/" ;

	
	public static String nomEquipe  = "Team%20Oreo";
	public static String password = "LeBonGateauNoirEtBlanc!" ;

	
	
	public Constante()
	{}
	
}
