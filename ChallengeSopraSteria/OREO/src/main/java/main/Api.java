package main;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Api {

	private Client clientCourant ;
	private Constante constante ;
	public Api(Client c)
	{
		clientCourant = c ;
		constante = new Constante();
		
	}
	
	
	public String raccourci(String url)
	{
		String retour = "" ;
		try {
    		WebResource webResource = clientCourant
    		   .resource(url);

    		ClientResponse response = webResource.accept("application/json")
                       .get(ClientResponse.class);

    		if (response.getStatus() != 200) {
    		   throw new RuntimeException("Failed : HTTP error code : "
    			+ response.getStatus());
    		}

    		retour = response.getEntity(String.class);

    		System.out.println("Valeur retour : " + retour);
    		
    		//System.out.println("\n");
    

    	  } catch (Exception e) {

    		e.printStackTrace();

    	  }
		return retour ;
	}
	public String  pong()
	{
		//System.out.println(" PONG LAND :  \n"  );
		return raccourci(constante.getPong);
	}
	
	/**
	 * {nomEquipe}/{motDePasse} 
	 */
	public String getIdentifiant()
	{
		//System.out.println(" Identifiant de l'équipe :  \n"  );

		return raccourci(constante.getIdentifiant  + constante.nomEquipe + "/" + constante.password);
	}
	public String getIdentifiantVersus()
	{
		//System.out.println(" Identifiant de l'équipe :  \n"  );

		return raccourci(constante.getIdentifiantVersus  + constante.nomEquipe + "/" + constante.password);
	}
	
	/**
	 * 
	 */
	public String getIdentifiantPartie (String idEquipe)
	{
		//System.out.println(" Identifiant de la partie :  \n"  );

		return raccourci(constante.getIdentifiantPartie + idEquipe) ;
	}
	/**
	 * 
	 * @param id
	 * @return
	 */
	public String getIdentifiantVersusBot(int nbBot, String idEquipe)
	{
		//System.out.println(" Création de la partie contre le bot :  \n"  );

		return raccourci(constante.getIdentifiantVersusBot + nbBot + "/" + idEquipe) ;
	}
	
	public String getAction(String idPartie, String idEquipe)
	{
		System.out.println(" CAN PLAY BAIL :  \n"   );

		return raccourci(constante.getAction + idPartie + "/" + idEquipe) ;
	}
	
	public String getPlateauJeu(String idPartie)
	{
		//System.out.println(" LE PLATEAU DE JEU   \n"  );

		return raccourci(constante.getPlateauJeu + idPartie) ;
	}
	
	public String getPremiereEquipe(String idPartie, String idEquipe)
	{
		//System.out.println(" Retourne le plateau de jeu de la partie concernée. La première équipe retournée est celle dont l'id est renseigné :  \n"  );

		return raccourci(constante.getPremiereEquipe + idPartie + "/" + idEquipe) ;
	}
	
	public String getLastMove(String idPartie, String idEquipe)
	{ 
		//System.out.println(" Last MOVE :  \n"  );

		return raccourci(constante.getLastMove + idPartie + "/" + idEquipe) ;
	}
	
	public String getPlay(String idPartie, String idEquipe, String move)
	{	
		return raccourci(constante.getResultatCoups + idPartie + "/" + idEquipe +"/" + move);
	}
	
	public String getNomAdversaire(String idPartie, String idEquipe)
	{
		//System.out.println("Nom de l'opposant :  \n"  );

		return raccourci(constante.getNameOpponent + idPartie + "/" + idEquipe);
		
	}
}
