package main;

public class Constante {

	public static String getPong =  "http://prod.codeandplay.date/epic-ws/epic/ping" ;
	/**
	 * Retourne systématiquement "pong".
	 * Cette méthode n'est d'aucune utilité dans le déroulement d'une partie. 
	 */
	
	public static String getIdentifiant =  "http://prod.codeandplay.date/epic-ws/epic/player/getIdEquipe/" ;
	/**
	 *  nomEquipe : Nom de l’équipe
	 *	motDePasse : Mot de passe de l'équipe 
	 *  Envoie l'identifiant de l'équipe à partir de son nom et du mot de passe associé.
	 *	Utilisez cette méthode pour obtenir votre identifiant ! 
	 */
	public static String getIdentifiantVersus =  "http://prod.codeandplay.date/epic-ws/epic/player/getIdEquipe/" ;

	
	public static String getIdentifiantPartie =  "http://prod.codeandplay.date/epic-ws/epic/versus/next/" ;
	
	/**
	 *  idEquipe : Identifiant de l’équipe 
	 * Retourne l’identifiant de la partie à laquelle l’équipe doit participer
	 * "NA" si aucune partie n'est ouverte pour cette équipe.
	 * 
	 */
	
	public static String getIdentifiantVersusBot =  "http://prod.codeandplay.date/epic-ws/epic/practice/new/" ;
	/**
	 * Crée une nouvelle partie contre une IA du niveau souhaité pour l’équipe concernée
	 * "NA" si la partie ne peut pas être créée 
	 *  numberBot : numéro de l’IA
	 * numéro de 1 à 12 et de 30 à 32
	 * idEquipe : Identifiant de l’équipe 
	 */
	
	
	public static String getAction =  "http://prod.codeandplay.date/epic-ws/epic/game/status/" ;
	/**
	 * Indique si c’est au tour de l'équipe indiquée de jouer dans la partie concernée
		Retourne :
		
		    "CANPLAY" si vous pouvez jouer
		    "CANTPLAY" si vous ne pouvez pas encore jouer
		    "VICTORY" si vous avez gagné la partie
		    "DEFEAT" si vous avez perdu la partie
		    "DRAW" si la partie s'est finie sur un match nul
		    "CANCELLED" si la partie a été annulée

	 */
	
	public static String getPlateauJeu =  "http://prod.codeandplay.date/epic-ws/epic/game/board/" ;

	/**
	 * Retourne le plateau de jeu de la partie concernée.
	 */
	
	public static String getPremiereEquipe =  "http://prod.codeandplay.date/epic-ws/epic/game/board/" ;

	/**
	 * Retourne le plateau de jeu de la partie concernée. 
	 * La première équipe retournée est celle dont l'id est renseigné
	 */
	public static String getLastMove =  "http://prod.codeandplay.date/epic-ws/epic/game/getlastmove/" ;

	/**
	 * Retourne le dernier coup joué sur le plateau de la partie indiquée
	 */
	public static String getResultatCoups =  "http://prod.codeandplay.date/epic-ws/epic/game/play/" ;

	public static String getNameOpponent =  "http://prod.codeandplay.date/epic-ws/epic/game/opponent/" ;

	
	public static String nomEquipe  = "Team%20Oreo";
	public static String password = "LeBonGateauNoirEtBlanc!" ;

	
	
	public Constante()
	{}
	
}
