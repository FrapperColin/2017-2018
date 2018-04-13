package main;

import javax.swing.plaf.synth.SynthSpinnerUI;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


public class InstanceClient {

	private Client clientCourant ;
	private Api p ;
	public InstanceClient(Client c)
	{
		clientCourant = c.create();
		p = new Api(clientCourant);
	}
	
	public Client getClientCourant() {
		return clientCourant;
	}

	public void setClientCourant(Client clientCourant) {
		this.clientCourant = clientCourant;
	}
	
	
	public void requestServeur() throws InterruptedException
	{
		String getId ;
		String getIdPartie ;
		String getLastMove ;
		String getAction ;
		String getIdentifiantEquipe ; 
		int i = 0;
	
		// ETAPE 1
		getId = p.getIdentifiantVersus(); 
		// ETAPE 2 INITIALISATION PARTIE
		getIdPartie = initialiser_partie("versus",getId);
		
		 /*Etape 3 : Jouer la partie par une répétition des étapes suivantes :
		
			    Demander le statut de la partie et attendre son tour
			    Demander l'état du plateau (éventuellement le dernier coup joué)
			    Jouer son coup
		  */
		getAction = p.getAction(getIdPartie, getId);
		System.out.println("Hello");

		while(!getAction.equals("VICTORY") && !getAction.equals("DEFEAT") && !getAction.equals("CANCELLED"))
		{
			Thread.sleep(500);
			if(getAction.equals("CANTPLAY"))
			{
				System.out.println("Attendons notre tour");
			}
			if(getAction.equals("CANPLAY"));
			{
				jouerCoup(getIdPartie, getId, i);
			}
			getAction = p.getAction(getIdPartie, getId);
		}
		if(getAction.equals("VICTORY"))
		{
			p.getPlateauJeu(getIdPartie);

		}
		else if (getAction.equals("DEFEAT"))
		{
			p.getPlateauJeu(getIdPartie);
		}
		else
		{
			p.getPlateauJeu(getIdPartie);
		}
	}
	private String initialiser_partie(String mode, String getId) {
		switch(mode)
		{
			case "versus" :
				// A CHANGER
				return p.getIdentifiantPartie(getId);
			case "practice" :
				return p.getIdentifiantVersusBot(21, getId);
			default :
				return "Erreur";
		}
	}

	public void jouerCoup(String getIdPartie, String getId, int i)
	{
		JsonToJava j = new JsonToJava(p.getPlateauJeu(getIdPartie));

		// Demander le statut de la partie et attendre son tour
		if((j.getPlateau().getNbTurnsLeft() > 50)) // 3 premiers tours
		{
			System.out.println(j.getPlateau().getNbTurnsLeft());
			initialisation_personnage(getIdPartie, getId);
		}
		else
		{
			routine_partie(getIdPartie, getId);
		}
	}

	
	private void routine_partie(String idPartie, String id) {
		String lastMove = p.getLastMove(idPartie, id);
		JsonToJava j = new JsonToJava(p.getPlateauJeu(idPartie));
		PlateauJeu a = j.getPlateau();
		Player ennemies = null ;	
		Player allies = null ;

		String play ;
		for(int i = 0 ; i < a.getPlayerBoards().size();i++)
		{
			if(!a.getPlayerBoards().get(i).getPlayerName().equals("Team Oreo"))
			{
				ennemies = a.getPlayerBoards().get(i) ;
			}
			if(a.getPlayerBoards().get(i).getPlayerName().equals("Team Oreo"))
			{
				allies = a.getPlayerBoards().get(i);
			}
		}

		if(a.getNbTurnsLeft()==50)
		{
			p.getPlay(idPartie, id, "A1,REST,A1$A2,REST,A2$A3,REST,A3");
		}
		else
		{
			System.out.println("LE LAST MOVE " + lastMove);
			String [] coupEnnemies = lastMove.split("$");
			play = make_strategies(coupEnnemies, ennemies, allies, a);
			p.getPlay(idPartie, id, play);
		}
	}

	private String make_strategies(String[] coupEnnemies, Player ennemies, Player allies, PlateauJeu a) {
		String play ;
		
		/*String coup1 = coupEnnemies[0] ;
		System.out.println("Coup ennemies " + coup1);
		String coup2 = coupEnnemies[1];
		String coup3 = coupEnnemies[2];
		String [] action1 = coup1.split(",");
		String [] action2 = coup2.split(",");
		String [] action3 = coup3.split(",");*/
		play = coup(/*action1,*/1,ennemies, allies, a) +"$";
		play += coup(/*action2,*/2,ennemies, allies, a) +"$";
		play += coup(/*action3,*/3,ennemies, allies, a);
		System.out.println("LE PLAY " + play);
		return play; 
	}

	private String coup(/*String[] coup,*/ int nbjoueur, Player ennemies, Player allies, PlateauJeu a) {
		String play = null ;
		
				
		Fighter A1 = allies.fighters.get(0);
		Fighter A2 = allies.fighters.get(1);
		Fighter A3 = allies.fighters.get(2);
		switch (nbjoueur)
		{
			case 1 :
				play  = strategies(A1,allies,ennemies, a);

				
				
				/*if(A1.is_orc())
				{
					if(someone_is_yell(ennemies))
					{
						play = A1.attack(get_yelled_ennemies(ennemies));
					}
					else if(A1.getCurrentMana() >= 2)
					{
						if(!PRIEST.isDead)
						{
							play = A1.yell(PRIEST);
						}
						else if(!ORC.isDead)
						{
							play = A1.yell(ORC);
						}
						else if(!GUARD.isDead)
						{
							play = A1.yell(GUARD);
						}
					}
					else if(GUARD_ALLIES.isDead && PRIEST_ALLIES.isDead)
					{
						if(!PRIEST.isDead)
						{
							play = A1.attack(PRIEST);
						}
						else if(!ORC.isDead)
						{
							play = A1.attack(ORC);
						}
						else if(!GUARD.isDead)
						{
							play = A1.attack(GUARD);
						}
					}
					else
					{
						play = A1.rest();
					}
				}
				else if(A1.is_guard())
				{
					if(someone_is_yell(ennemies))
					{
						play = A1.attack(get_yelled_ennemies(ennemies));
					}
					else if(A1.getCurrentMana() >= 2)
					{
						if(!PRIEST_ALLIES.isDead)
						{
							play = A1.tank(PRIEST_ALLIES);
						}
						else if(!ORC_ALLIES.isDead)
						{
							play = A1.tank(ORC_ALLIES);
						}
						else
						{
							if(!PRIEST.isDead)
							{
								play = A1.attack(PRIEST);
							}
							else if(!ORC.isDead)
							{
								play = A1.attack(ORC);
							}
							else if(!GUARD.isDead)
							{
								play = A1.attack(GUARD);
							}
						}
					}
					else
					{
						if(ORC_ALLIES.isDead && PRIEST_ALLIES.isDead)
						{
							if(!PRIEST.isDead)
							{
								play = A1.attack(PRIEST);
							}
							else if(!ORC.isDead)
							{
								play = A1.attack(ORC);
							}
							else if(!GUARD.isDead)
							{
								play = A1.attack(GUARD);
							}
						}
						else
						{
							play = A1.rest();
						}
					}
				}
				else if(A1.is_priest())
				{
					if(someone_is_yell(ennemies))
					{
						play = A1.attack(get_yelled_ennemies(ennemies));
					}
					else if(A1.getCurrentMana() >= 2)
					{
						if(GUARD_ALLIES.isDead && ORC_ALLIES.isDead)
						{
							if(!PRIEST.isDead)
							{
								play = A1.attack(PRIEST);
							}
							else if(!ORC.isDead)
							{
								play = A1.attack(ORC);
							}
							else if(!GUARD.isDead)
							{
								play = A1.attack(GUARD);
							}
						}
						else
						{
							play = A1.heal(lower_allies(allies));
						}
					}
					else
					{
						if(GUARD_ALLIES.isDead && ORC_ALLIES.isDead)
						{
							if(!PRIEST.isDead)
							{
								play = A1.attack(PRIEST);
							}
							else if(!ORC.isDead)
							{
								play = A1.attack(ORC);
							}
							else if(!GUARD.isDead)
							{
								play = A1.attack(GUARD);
							}
						}
						else
						{
							play = A1.rest();
						}
					}
				}*/
				
				break ;
			case 2 :
				play = strategies(A2,allies,ennemies, a);
				/*
				if(A2.is_orc())
				{
					if(someone_is_yell(ennemies))
					{
						play = A2.attack(get_yelled_ennemies(ennemies));
					}
					else if(A2.getCurrentMana() >= 2)
					{
						if(!PRIEST.isDead)
						{
							play = A2.yell(PRIEST);
						}
						else if(!ORC.isDead)
						{
							play = A2.yell(ORC);
						}
						else if(!GUARD.isDead)
						{
							play = A2.yell(GUARD);
						}
					}
					else if(PRIEST_ALLIES.isDead && GUARD_ALLIES.isDead)
					{
						if(!PRIEST.isDead)
						{
							play = A2.attack(PRIEST);
						}
						else if(!ORC.isDead)
						{
							play = A2.attack(ORC);
						}
						else if(!GUARD.isDead)
						{
							play = A2.attack(GUARD);
						}
					}
					else
					{
						play = A2.rest();
					}
				}
				else if(A2.is_guard())
				{
					if(someone_is_yell(ennemies))
					{
						play = A2.attack(get_yelled_ennemies(ennemies));
					}
					else if(A2.getCurrentMana() >= 2)
					{
						if(!PRIEST_ALLIES.isDead)
						{
							play = A2.tank(PRIEST_ALLIES);
						}
						else if(!ORC_ALLIES.isDead)
						{
							play = A2.tank(ORC_ALLIES);
						}
						else
						{
							if(!PRIEST.isDead)
							{
								play = A2.attack(PRIEST);
							}
							else if(!ORC.isDead)
							{
								play = A2.attack(ORC);
							}
							else if(!GUARD.isDead)
							{
								play = A2.attack(GUARD);
							}
						}
					}
					else
					{
						if(ORC_ALLIES.isDead && PRIEST_ALLIES.isDead)
						{
							if(!PRIEST.isDead)
							{
								play = A2.attack(PRIEST);
							}
							else if(!ORC.isDead)
							{
								play = A2.attack(ORC);
							}
							else if(!GUARD.isDead)
							{
								play = A2.attack(GUARD);
							}
						}
						else
						{
							play = A2.rest();
						}
					}
				}
				else if(A2.is_priest())
				{
					if(someone_is_yell(ennemies))
					{
						play = A2.attack(get_yelled_ennemies(ennemies));
					}
					else if(A1.getCurrentMana() >= 2)
					{
						if(GUARD_ALLIES.isDead && ORC_ALLIES.isDead)
						{
							if(!PRIEST.isDead)
							{
								play = A2.attack(PRIEST);
							}
							else if(!ORC.isDead)
							{
								play = A2.attack(ORC);
							}
							else if(!GUARD.isDead)
							{
								play = A2.attack(GUARD);
							}
						}
						else
						{
							play = A2.heal(lower_allies(allies));
						}
					}
					else
					{
						if(GUARD_ALLIES.isDead && ORC_ALLIES.isDead)
						{
							if(!PRIEST.isDead)
							{
								play = A2.attack(PRIEST);
							}
							else if(!ORC.isDead)
							{
								play = A2.attack(ORC);
							}
							else if(!GUARD.isDead)
							{
								play = A2.attack(GUARD);
							}
						}
						else
						{
							play = A2.rest();
						}
					}
				}*/
				break ;
			case 3 :
				play = strategies(A3,allies,ennemies, a);
				/*if(A3.is_orc())
				{
					if(someone_is_yell(ennemies))
					{
						play = A3.attack(get_yelled_ennemies(ennemies));
					}
					else if(A3.getCurrentMana() >= 2)
					{
						if(!PRIEST.isDead)
						{
							play = A3.yell(PRIEST);
						}
						else if(!ORC.isDead)
						{
							play = A3.yell(ORC);
						}
						else if(!GUARD.isDead)
						{
							play = A3.yell(GUARD);
						}
					}
					else if(A1.isDead && A2.isDead)
					{
						if(!PRIEST.isDead)
						{
							play = A3.attack(PRIEST);
						}
						else if(!ORC.isDead)
						{
							play = A3.attack(ORC);
						}
						else if(!GUARD.isDead)
						{
							play = A3.attack(GUARD);
						}
					}
					else
					{
						play = A3.rest();
					}
				}
				else if(A3.is_guard())
				{
					if(someone_is_yell(ennemies))
					{
						play = A3.attack(get_yelled_ennemies(ennemies));
					}
					else if(got_yelled(PRIEST_ALLIES))
					{
						play = A3.tank(PRIEST_ALLIES);
					}
					else if(got_yelled(ORC_ALLIES))
					{
						play = A3.tank(ORC_ALLIES);
					}
					else if (got_yelled(A3))
					{
						A3.defend();
					}
					else if(A3.getCurrentMana() >= 2)
					{
						if(!PRIEST_ALLIES.isDead)
						{
							play = A3.tank(PRIEST_ALLIES);
						}
						else if(!ORC_ALLIES.isDead)
						{
							play = A3.tank(ORC_ALLIES);
						}
						else
						{
							if(!PRIEST.isDead)
							{
								play = A3.attack(PRIEST);
							}
							else if(!ORC.isDead)
							{
								play = A3.attack(ORC);
							}
							else if(!GUARD.isDead)
							{
								play = A3.attack(GUARD);
							}
						}
					}
					else
					{
						if(ORC_ALLIES.isDead && PRIEST_ALLIES.isDead)
						{
							if(!PRIEST.isDead)
							{
								play = A3.attack(PRIEST);
							}
							else if(!ORC.isDead)
							{
								play = A3.attack(ORC);
							}
							else if(!GUARD.isDead)
							{
								play = A3.attack(GUARD);
							}
						}
						else
						{
							play = A3.rest();
						}
					}
				}
				else if(A3.is_priest())
				{
					if(someone_is_yell(ennemies))
					{
						play = A3.attack(get_yelled_ennemies(ennemies));
					}
					else if(A3.getCurrentMana() >= 2)
					{
						if(GUARD_ALLIES.isDead && ORC_ALLIES.isDead)
						{
							if(!PRIEST.isDead)
							{
								play = A3.attack(PRIEST);
							}
							else if(!ORC.isDead)
							{
								play = A3.attack(ORC);
							}
							else if(!GUARD.isDead)
							{
								play = A3.attack(GUARD);
							}
						}
						else
						{
							play = A3.heal(lower_allies(allies));
						}
					}
					else
					{
						if(GUARD_ALLIES.isDead && ORC_ALLIES.isDead)
						{
							if(!PRIEST.isDead)
							{
								play = A3.attack(PRIEST);
							}
							else if(!ORC.isDead)
							{
								play = A3.attack(ORC);
							}
							else if(!GUARD.isDead)
							{
								play = A3.attack(GUARD);
							}
						}
						else
						{
							play = A3.rest();
						}
					}
				}*/
				break ;
			default :
				break ;
		}
		return play;
	}

	

	private String strategies(Fighter A, Player allies, Player ennemies, PlateauJeu a2) {
		String play = null ;
		
		Fighter E1 = ennemies.fighters.get(0);
		Fighter E2 = ennemies.fighters.get(1);
		Fighter E3 = ennemies.fighters.get(2);
		Fighter GUARD = getTankEnnemies(ennemies);
		Fighter PRIEST = getPriestEnnemies(ennemies);
		Fighter ORC = getOrcEnnemies(ennemies);
		Fighter CHAMAN = getChamanEnnemies(ennemies);
		Fighter PALADIN = getPaladinEnnemies(ennemies);
		Fighter ARCHER ;
		Fighter GUARD_ALLIES = getTankEnnemies(allies);
		Fighter PRIEST_ALLIES = getPriestEnnemies(allies);
		Fighter ORC_ALLIES = getOrcEnnemies(allies);
		Fighter CHAMAN_ALLIES = getChamanEnnemies(allies);
		Fighter PALADIN_ALLIES = getPaladinEnnemies(allies);
		Fighter ARCHER_ALLIES = getArcherEnnemies(allies);
			
		
		if(A.is_orc())
		{
			if(someone_is_yell(ennemies))
			{
				play = A.attack(get_yelled_ennemies(ennemies));
			}
			else if(A.getCurrentMana() >= 2)
			{
				if(!E2.isDead)
				{
					play = A.yell(E2);
				}
				else if(!E1.isDead)
				{
					play = A.yell(E1);
				}
				else if(!E3.isDead)
				{
					play = A.yell(E3);
				}
			}
			else if(PRIEST_ALLIES.isDead && GUARD_ALLIES.isDead)
			{
				if(!E2.isDead)
				{
					play = A.attack(E2);
				}
				else if(!E1.isDead)
				{
					play = A.attack(E1);
				}
				else if(!E3.isDead)
				{
					play = A.attack(E3);
				}
			}
			else
			{
				play = A.rest();
			}
		}
		
		else if(A.is_guard())
		{
			if(someone_is_yell(ennemies))
			{
				play = A.attack(get_yelled_ennemies(ennemies));
			}
			else if(A.getCurrentMana() >= 2)
			{
				if(!PRIEST_ALLIES.isDead)
				{
					play = A.tank(PRIEST_ALLIES);
				}
				else if(!ORC_ALLIES.isDead)
				{
					play = A.tank(ORC_ALLIES);
				}
				else
				{
					if(!E2.isDead)
					{
						play = A.attack(E2);
					}
					else if(!E1.isDead)
					{
						play = A.attack(E1);
					}
					else if(!E3.isDead)
					{
						play = A.attack(E3);
					}
				}
			}
			else
			{
				if(ORC_ALLIES.isDead && PRIEST_ALLIES.isDead)
				{
					if(!E2.isDead)
					{
						play = A.attack(E2);
					}
					else if(!E1.isDead)
					{
						play = A.attack(E1);
					}
					else if(!E3.isDead)
					{
						play = A.attack(E3);
					}
				}
				else
				{
					play = A.rest();
				}
			}
		}
		else if(A.is_priest())
		{
			if(someone_is_yell(ennemies))
			{
				play = A.attack(get_yelled_ennemies(ennemies));
			}
			else if(A.getCurrentMana() >= 2)
			{
				if(GUARD_ALLIES.isDead && ORC_ALLIES.isDead)
				{
					if(!E2.isDead)
					{
						play = A.attack(E2);
					}
					else if(!E1.isDead)
					{
						play = A.attack(E1);
					}
					else if(!E3.isDead)
					{
						play = A.attack(E3);
					}
				}
				else
				{
					play = A.heal(lower_allies(allies));
				}
			}
			else
			{
				if(GUARD_ALLIES.isDead && ORC_ALLIES.isDead)
				{
					if(!E2.isDead)
					{
						play = A.attack(E2);
					}
					else if(!E1.isDead)
					{
						play = A.attack(E1);
					}
					else if(!E3.isDead)
					{
						play = A.attack(E3);
					}
				}
				else
				{
					play = A.rest();
				}
			}
		}
		
		/*if(A.is_orc())
		{
			if(got_yelled(A))
			{
				play = A.defend();
			}
			else if(someone_is_yell(ennemies))
			{
				play = A.attack(get_yelled_ennemies(ennemies));
			}
			else if(A.getCurrentMana() >= 2 && PALADIN_ALLIES.getCurrentMana() >= 2 && ARCHER_ALLIES.getCurrentMana() >= 2)
			{
				if(E1.is_chaman() && !E1.isDead)
				{
					play = A.yell(E1);
				}
				else if(E2.is_chaman() && !E2.isDead)
				{
					play = A.yell(E2);
				}
				else if(E3.is_chaman() && !E3.isDead)
				{
					play = A.yell(E3);
				}
				else if (E1.is_guard()  && !E1.isDead)
				{
					play = A.yell(E1);
				}
				else if (E2.is_guard() && !E2.isDead)
				{
					play = A.yell(E2);
				}
				else if (E3.is_guard() && !E3.isDead)
				{
					play = A.yell(E3);
				}
				else if (E1.is_priest() && !E1.isDead)
				{
					play = A.yell(E1);
				}
				else if (E2.is_priest()  && !E2.isDead)
				{
					play = A.yell(E2);
				}
				else if (E3.is_priest() && !E3.isDead)
				{
					play = A.yell(E3);
				}
				else if (E1.is_archer()&& !E1.isDead)
				{
					play = A.yell(E1);
				}
				else if (E2.is_archer() && !E2.isDead)
				{
					play = A.yell(E2);
				}
				else if (E3.is_archer()&& !E3.isDead)
				{
					play = A.yell(E3);
				}
				else if (E1.is_paladin()&& !E1.isDead)
				{
					play = A.yell(E1);
				}
				else if (E2.is_paladin()&& !E2.isDead)
				{
					play = A.yell(E2);
				}
				else if (E3.is_paladin()&& !E3.isDead)
				{
					play = A.yell(E3);
				}
				else if(!E1.isDead)
				{
					play = A.yell(E1);
				}
			}
			else if (lower_allies(allies).equals(A)) // si c'est le plus low life
			{
				play = A.defend();
			}
			else if(A.getCurrentMana() == A.getMaxAvailableMana())
			{
				play = A.yell(lower_allies(ennemies));
			}
			else // sinon il attaque 
			{
				play = A.rest();			
			}
		}
		else if(A.is_archer())
		{
			if(got_yelled(A))
			{
				play = A.defend();
			}
			else if(someone_is_yell(ennemies) && A.getCurrentMana() >= 2)
			{
				play = A.Fleche(get_yelled_ennemies(ennemies));
			}
			else if (lower_allies(allies).equals(A)) // si c'est le plus low life
			{
				play = A.defend();
			}
			else if(A.getCurrentMana() == A.getMaxAvailableMana())
			{
				play = A.Fleche(lower_allies(ennemies));
			}
			else // sinon il attaque 
			{
				play = A.rest();			
			}
		}
		else if(A.is_guard())
		{
			if(got_yelled(A))
			{
				play = A.defend();
			}
			else if(lower_allies(allies).equals(A))
			{
				play = A.defend();
			}
			else if(!PRIEST_ALLIES.isDead) // si le priest n'est pas mort
			{
				if (A.getCurrentMana() >= 2 && someone_need_to_be_healed(allies) && PRIEST_ALLIES.getCurrentMana() >= 2) // 
				{
					play = A.tank(lower_allies(allies));
				}
				else if (A.getCurrentMana() >= 2 && someone_need_to_be_healed(allies)) // tank pour que le priest puisse se rest
				{
					play = A.tank(lower_allies(allies));
				}
				else if(A.getCurrentMana() == A.getMaxAvailableMana())
				{
					if(!E1.isDead || !E2.isDead) // personne n'a besoin d'etre tanker
					{
						play = A.attack(most_life(E1,E2));
					}
					else if(!E3.isDead)
					{
						play = A.attack(E3);
					}	
				}
				else
				{
					play = A.rest();
				}
				
			} // le pretre est donc mort
			else if(CHAMAN_ALLIES.isDead && PRIEST_ALLIES.isDead)
			{
				if(!E1.isDead || !E2.isDead)
				{
					play = A.attack(most_life(E1,E2));
				}
				else if(!E3.isDead)
				{
					play = A.attack(E3);
				}
			}
			else if(A.getCurrentMana() == A.getMaxAvailableMana())
			{
				if(!E1.isDead || !E2.isDead)
				{
					play = A.attack(most_life(E1,E2));
				}
				else if(!E3.isDead)
				{
					play = A.attack(E3);
				}
			}
			else
			{
				play = A.rest();
			}
		}*/
		/*if(A.is_paladin())
		{
			if(!A.isDead)
			{
				if(someone_get_affected(allies))
				{
					if(get_affected(allies).equals(A) && A.getCurrentMana() >=1)
					{
						play = A.defend();
					}
				}
				else if(A.getCurrentMana() >= 2)
				{
					if(!E1.isDead || !E2.isDead)
					{
						
						play = A.Stunt(most_life(E1,E2));
						
					}
					else if(!E3.isDead)
					{
						
						play = A.Stunt(most_life(E1,E2));
						
					}
					
				}
				else if(A.getCurrentMana() == A.getMaxAvailableMana())
				{
					play = A.attack(lower_allies(ennemies));
				}
				else if(a2.getNbTurnsLeft() == 1 )
				{
					if(A.getCurrentMana() >= 1 )
					{
						play = A.attack(lower_allies(ennemies));
					}
				}
				else 
				{
					play = A.rest();
				}
			}
			else
			{
				play = "";
			}
			
		}
		else if(A.is_priest())
		{
			if(!A.isDead)
			{
				if(someone_get_affected(allies))
				{
					if(get_affected(allies).equals(A) && A.getCurrentMana() >=1)
					{
						play = A.defend();
					}
				}
				else if(A.getCurrentMana() >= 2 && someone_need_to_be_healed(allies))
				{
					play = A.heal(lower_allies(allies));
				}			
				else if(lower_allies(allies).equals(A))
				{
					play = A.defend();
				}
				else if(PALADIN_ALLIES.isDead && CHAMAN_ALLIES.isDead)
				{
					if(!E1.isDead || !E2.isDead)
					{
						if(A.getCurrentMana() >= 1)
						{
							play = A.attack(most_life(E1,E2));
						}
					}
					else if(!E3.isDead)
					{
						if(A.getCurrentMana() >= 1)
						{
							play = A.attack(E3);
						}
					}
				}
				else if(A.getCurrentMana() == A.getMaxAvailableMana())
				{
					if(!E1.isDead || !E2.isDead)
					{
						if(A.getCurrentMana() >= 1)
						{
							play = A.attack(most_life(E1,E2));
						}
					}
					else if(!E3.isDead)
					{
						if(A.getCurrentMana() >= 1)
						{
							play = A.attack(E3);
						}
					}
				}
				else if(a2.getNbTurnsLeft() == 1 )
				{
					if(A.getCurrentMana() >= 1 )
					{
						play = A.attack(lower_allies(ennemies));
					}
				}
				else
				{
					play = A.rest();
				}
			}
			else
			{
				play = "" ;
			}
			
		}
		else if(A.is_chaman())
		{
			if(!A.isDead)
			{
				if(someone_get_affected(allies))
				{
					if(get_affected(allies).equals(A) && A.getCurrentMana() >=1)
					{
						play = A.defend();
					}
					else if( A.getCurrentMana() >=2)
					{
						play = A.purifie(get_affected(allies));
					}
				}
				else if(lower_allies(allies).equals(A))
				{
					if( A.getCurrentMana() >=1)
					{
						play = A.defend();
					}
				}
				else if(A.getCurrentMana() == A.getMaxAvailableMana())
				{
					if(!E1.isDead || !E2.isDead)
					{
						if(A.getCurrentMana() >= 1)
						{
							play = A.attack(most_life(E1,E2));
						}
					}
					else if(!E3.isDead)
					{
						if(A.getCurrentMana() >= 1)
						{
							play = A.attack(E3);
						}
					}
				}
				else if(a2.getNbTurnsLeft() == 1 )
				{
					if(A.getCurrentMana() >= 1 )
					{
						play = A.attack(lower_allies(ennemies));
					}
				}
				else
				{
					play = A.rest();
				}
			}
			else 
			{
				play = "";
			}
			
		}*/
		return play ;
	}

	private boolean there_is(String nom, Player ennemies) {
		for(int i = 0;i<ennemies.getFighters().size(); i++)
		{
			if(ennemies.getFighters().get(i).getFighterClass().equals(nom))
			{
				return true;
			}		
		}
		return false;
	}
	
	private Fighter find(String nom, Player ennemies)
	{
		for(int i = 0;i<ennemies.getFighters().size(); i++)
		{
			if(ennemies.getFighters().get(i).getFighterClass().equals(nom))
			{
				return ennemies.getFighters().get(i);
			}		
		}
		return null;
	}
	
	private boolean someone_get_affected(Player allies) {
		for(int i = 0;i<allies.getFighters().size(); i++)
		{
			if(allies.getFighters().get(i).getStates() != null)
			{
				for(int j = 0 ; j< allies.getFighters().get(i).getStates().size(); j++)
				{
					if(allies.getFighters().get(i).getStates().get(j).getType().equals("SCARED") || allies.getFighters().get(i).getStates().get(j).getType().equals("BURNING") || allies.getFighters().get(i).getStates().get(j).getType().equals("STUNNED"))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private Fighter get_affected(Player allies)
	{
		for(int i = 0;i<allies.getFighters().size(); i++)
		{
			if(allies.getFighters().get(i).getStates() != null)
			{
				for(int j = 0 ; j< allies.getFighters().get(i).getStates().size(); j++)
				{
					if(allies.getFighters().get(i).getStates().get(j).getType().equals("SCARED") || allies.getFighters().get(i).getStates().get(j).getType().equals("BURNING") || allies.getFighters().get(i).getStates().get(j).getType().equals("STUNNED"))
					{
						return allies.getFighters().get(i);
					}
				}
			}
		}
		return null;
	}
	

	/*private boolean god_affected(Fighter pALADIN_ALLIES) {
		for(int j = 0 ; j< ennemies.getFighters().get(i).getStates().size(); j++)
		{
			if(ennemies.getFighters().get(i).getStates().get(j).getType().equals("SCARED"))
			{
				return ennemies.getFighters().get(i);
			}
		}
		return false;
	}*/

	private Fighter getArcherEnnemies(Player ennemies) {
		for(int i = 0;i<ennemies.getFighters().size(); i++)
		{
			if(ennemies.getFighters().get(i).getFighterClass().equals("ARCHER"))
			{
				return ennemies.getFighters().get(i);
			}
		}
		return null;
	}

	private Fighter getPaladinEnnemies(Player ennemies) {
		for(int i = 0;i<ennemies.getFighters().size(); i++)
		{
			if(ennemies.getFighters().get(i).getFighterClass().equals("PALADIN"))
			{
				return ennemies.getFighters().get(i);
			}
		}
		return null;
	}

	private Fighter getChamanEnnemies(Player ennemies) {
		for(int i = 0;i<ennemies.getFighters().size(); i++)
		{
			if(ennemies.getFighters().get(i).getFighterClass().equals("CHAMAN"))
			{
				return ennemies.getFighters().get(i);
			}
		}
		return null;
	}

	private boolean someone_need_to_be_healed(Player allies) {
		for(int i = 0;i<allies.getFighters().size(); i++)
		{	
			if(allies.getFighters().get(i).getCurrentLife() +4 <= allies.getFighters().get(i).getMaxAvailableLife())
			{
				return true ;
			}
		}
		return false;
	}

	private Fighter most_life(Fighter oRC, Fighter pRIEST) {
		if(oRC.getCurrentLife() > pRIEST.getCurrentLife())
		{
			return oRC ;
		}
		else
		{
			return pRIEST ;
		}
	}

	private Fighter getOrcEnnemies(Player ennemies) {
		for(int i = 0;i<ennemies.getFighters().size(); i++)
		{
			if(ennemies.getFighters().get(i).getFighterClass().equals("ORC"))
			{
				return ennemies.getFighters().get(i);
			}
		}
		return null;
	}

	private Fighter getPriestEnnemies(Player ennemies) {
		for(int i = 0;i<ennemies.getFighters().size(); i++)
		{
			if(ennemies.getFighters().get(i).getFighterClass().equals("PRIEST"))
			{
				return ennemies.getFighters().get(i);
			}
		}
		return null;
	}

	private Fighter getTankEnnemies(Player ennemies) {
		for(int i = 0;i<ennemies.getFighters().size(); i++)
		{
			if(ennemies.getFighters().get(i).getFighterClass().equals("GUARD"))
			{
				return ennemies.getFighters().get(i);
			}
		}
		return null;
	}

	private Fighter lower_allies(Player allies) {
		int low_life =1000 ;
		//int diff_life = 0 ;
		for(int i = 0;i<allies.getFighters().size(); i++)
		{
			//diff_life = allies.getFighters().get(i).getMaxAvailableLife() - allies.getFighters().get(i).getCurrentLife();
			if(allies.getFighters().get(i).getCurrentLife() != 0)
			{
				if(allies.getFighters().get(i).getCurrentLife() <= low_life)
				{
					low_life = allies.getFighters().get(i).getCurrentLife();
				}
			}
		}
		for(int i = 0;i<allies.getFighters().size(); i++)
		{
			System.out.println(low_life);
			if(allies.getFighters().get(i).getCurrentLife() == low_life)
			{
				return allies.getFighters().get(i);
			}
		}

		return null;
	}

	private Fighter get_yelled_ennemies(Player ennemies) {
		
		for(int i = 0;i<ennemies.getFighters().size(); i++)
		{
			if(ennemies.getFighters().get(i).getStates() != null)
			{
				for(int j = 0 ; j< ennemies.getFighters().get(i).getStates().size(); j++)
				{
					if(ennemies.getFighters().get(i).getStates().get(j).getType().equals("SCARED"))
					{
						return ennemies.getFighters().get(i);
					}
				}
			}
		}
		return null;
	}

	private boolean someone_is_yell(Player ennemies) {
		
		for(int i = 0;i<ennemies.getFighters().size(); i++)
		{
			if(ennemies.getFighters().get(i).getStates() != null)
			{
				for(int j = 0 ; j< ennemies.getFighters().get(i).getStates().size(); j++)
				{
					if(ennemies.getFighters().get(i).getStates().get(j).getType().equals("SCARED"))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean got_yelled(Fighter allies) {
		
		if(allies.getStates()!=null)
		{
			for(int i = 0; i< allies.getStates().size(); i++)
			{
				if(allies.getStates().get(i).getType().equals("SCARED"))
				{
					return true ; 
				}
			}
			
		}
		
		return false;
	}

	public void initialisation_personnage(String idPartie, String id)
	{
		System.out.println("Initialisation personnage :::: " + p.getLastMove(idPartie, id));
		String lastMove = p.getLastMove(idPartie, id);
		String guard = "GUARD" ; 
		String pala = "PALADIN" ;
		String chaman = "CHAMAN" ;
		String priest = "PRIEST";
		String orc = "ORC" ; 
		String archer = "ARCHER";
		
		
		switch(lastMove)
		{
			case "ARCHER" : 
				if(!already_in_team(idPartie,guard))
				{
					System.out.println("On prends orc");
					p.getPlay(idPartie, id, guard);
				}
				else if(!already_in_team(idPartie,orc))
				{
					System.out.println("On prends garde");
					p.getPlay(idPartie, id, orc);
				}
				else if(!already_in_team(idPartie,priest))
				{
					System.out.println("On prends pretre");
					p.getPlay(idPartie, id,priest);
				}
				break ;
			case "CHAMAN" :
				if(!already_in_team(idPartie,guard))
				{
					System.out.println("On prends orc");
					p.getPlay(idPartie, id, guard);
				}
				else if(!already_in_team(idPartie,orc))
				{
					System.out.println("On prends garde");
					p.getPlay(idPartie, id, orc);
				}
				else if(!already_in_team(idPartie,priest))
				{
					System.out.println("On prends pretre");
					p.getPlay(idPartie, id,priest);
				}
				break ;
				
			case "PALADIN" :
				if(!already_in_team(idPartie,guard))
				{
					System.out.println("On prends orc");
					p.getPlay(idPartie, id, guard);
				}
				else if(!already_in_team(idPartie,orc))
				{
					System.out.println("On prends garde");
					p.getPlay(idPartie, id, orc);
				}
				else if(!already_in_team(idPartie,priest))
				{
					System.out.println("On prends pretre");
					p.getPlay(idPartie, id,priest);
				}
				break ;
			case "ORC" : 
				if(!already_in_team(idPartie,guard))
				{
					System.out.println("On prends orc");
					p.getPlay(idPartie, id, guard);
				}
				else if(!already_in_team(idPartie,orc))
				{
					System.out.println("On prends garde");
					p.getPlay(idPartie, id, orc);
				}
				else if(!already_in_team(idPartie,priest))
				{
					System.out.println("On prends pretre");
					p.getPlay(idPartie, id,priest);
				}
				break;
			case "PRIEST" :
				if(!already_in_team(idPartie,guard))
				{
					System.out.println("On prends orc");
					p.getPlay(idPartie, id, guard);
				}
				else if(!already_in_team(idPartie,orc))
				{
					System.out.println("On prends garde");
					p.getPlay(idPartie, id, orc);
				}
				else if(!already_in_team(idPartie,priest))
				{
					System.out.println("On prends pretre");
					p.getPlay(idPartie, id,priest);
				}
				break;
			case "GUARD" :
				if(!already_in_team(idPartie,guard))
				{
					System.out.println("On prends orc");
					p.getPlay(idPartie, id, guard);
				}
				else if(!already_in_team(idPartie,orc))
				{
					System.out.println("On prends garde");
					p.getPlay(idPartie, id, orc);
				}
				else if(!already_in_team(idPartie,priest))
				{
					System.out.println("On prends pretre");
					p.getPlay(idPartie, id,priest);
				}
				break;
			default :
				System.out.println("On prends pretre ds default");
				p.getPlay(idPartie, id, priest);	
				
				break;
		}
	}
	
	public boolean already_in_team(String getIdPartie, String nom)
	{
		JsonToJava j = new JsonToJava(p.getPlateauJeu(getIdPartie));
		int i  ;
		Player our = null ;
		for(i= 0; i< j.getPlateau().playerBoards.size();i++)
		{
			if(j.getPlateau().playerBoards.get(i).getPlayerName().equals("Team Oreo"))
			{
				System.out.println("Hello world c'est ma team zoo");
				our = j.getPlateau().playerBoards.get(i);
			}
		}
		for(i= 0; i< our.fighters.size();i++)
		{
			if(our.fighters.get(i).getFighterClass().equals(nom))
			{
				
				System.out.println("J'ai déja ce joueur");
				return true ;
			}
		}
		System.out.println("Je n'ai pas déja ce joueur");
		return false ;

	}
	
	
	
}
