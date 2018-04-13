import java.util.ArrayList;

/**
 * 
 * @author FRAPPER, N'GUESSAN
 * @role Classe AABRR
 */
public class AABRR {
	
	private int min, max ;
	private ABRR noeud ;
	private AABRR fils_gauche ;
	private AABRR fils_droit ;
	private AABRR racine ;
	private AABRR parent;

	public AABRR(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	// Constructeur vide 
	public AABRR()
	{}
	
	// getters and setters
	public AABRR getRacine() {
		return racine;
	}

	public void setRacine(AABRR racine) {
		this.racine = racine;
	}
	
	public AABRR getParent() {
		return parent;
	}
	public void setParent(AABRR parent) {
		this.parent = parent;
	}
	
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	
	public ABRR getNoeud() {
		return noeud;
	}
	public void setNoeud(ABRR a) {
		noeud = a;
	}
	
	public AABRR getFils_gauche() {
		return fils_gauche;
	}
	public void setFils_gauche(AABRR fils_gauche) {
		this.fils_gauche = fils_gauche;
	}
	
	public AABRR getFils_droit() {
		return fils_droit;
	}
	public void setFils_droit(AABRR fils_droit) {
		this.fils_droit = fils_droit;
	}

	
	/**
	 * @role Fonction d'affichage graphique console
	 */
	public void printBeauty()
	{
		printBeautyRecur(racine);
	}
	
	/**
	 * @role Fonction d'affichage graphique console
	 * @param a
	 */
	public void printBeautyRecur(AABRR a) {
		
		System.out.println("\n\n Intervalle : "+ a.getMin() + " ------------ " + a.getMax() +" \n\n");
		a.getNoeud().beautyPrint();

		if (a.getFils_gauche() != null)
			printBeautyRecur(a.getFils_gauche());
		if (a.getFils_droit() != null)
			printBeautyRecur(a.getFils_droit());
	}
	
	/**
	 * @role Fonction permettant de réaliser un parcours prefixe
	 */
	public void parcoursPrefixe()
	{
		parcoursPrefixeRecur(racine);
	}
	
	/**
	 * @role Fonction permettant de réaliser un parcours prefixe récursivement
	 * @param a
	 */
	public void parcoursPrefixeRecur(AABRR a) 
	{
		a.getNoeud().parcoursPrefixe();
		if (a.getFils_gauche() != null)
			parcoursPrefixeRecur(a.getFils_gauche());
		if (a.getFils_droit() != null)
			parcoursPrefixeRecur(a.getFils_droit());
	}
	
	/**
	 * @role Parcours infixe
	 */
    public void parcoursInfixe() 
    {
		parcoursInfixeRecur(racine);
    }
    
    /**
	 * @role Parcours infixe récursivement
	 */
    public void parcoursInfixeRecur(AABRR a) 
    {
    	if (a.getFils_gauche() != null)
    		parcoursInfixeRecur(a.getFils_gauche());
		if (a.getFils_droit() != null)
			parcoursInfixeRecur(a.getFils_droit());
	}
    
    /**
     * @role Fonction permettant de vérifier si un AABRR est valide
     * @return true or false 
     */
	public boolean verif_AABRR()
	{
		ArrayList<Integer> arl = new ArrayList<Integer>() ;
		boolean result = create_tab(arl, racine);
		return result ;
	}

	/**
	 * @role Fonction annexe permettant de vérifier si un AABRR est valide
	 * @param tab_int
	 * @param a
	 * @return
	 */
    public boolean create_tab(ArrayList<Integer> tab_int, AABRR a)
    {
		ArrayList<Integer> arl = new ArrayList<Integer>() ;

    	if (a.getFils_gauche() != null)
    		if(!create_tab(tab_int, a.getFils_gauche()))
    			return false; 
    	
    	if(!(a.getNoeud().parcoursInfixeInverse(arl,a.getMin(), a.getMax())))
    	{
    		return false ;
    	}
    	tab_int.add(a.getMin());
    	tab_int.add(a.getMax());

    	if (a.getFils_droit() != null)
    		if(!create_tab(tab_int,a.getFils_droit()))
    			return false ;
    	
       	for (int i = 0 ; i< tab_int.size()-1 ;i++)
		{
			if(tab_int.get(i) > tab_int.get(i+1))
			{
				return false;
			}
		}
		return true;
    }
	
    /**
     * @role Fonction utilisée dans la fonctionnalité AABRR_to_file
     * @return
     */
	public String parcoursWrite()
	{
		String s ="";
		return parcoursWriteRecur(s,racine);
	}
	
	 /**
	  * @role Fonction utilisée dans la fonctionnalité AABRR_to_file
	  * @param s
	  * @param a
	  * @return
	  */
	public String parcoursWriteRecur(String s, AABRR a)
	{
		s = a.getMin() + ":" + a.getMax() ;
		s += ";" ;
		s += a.getNoeud().parcoursWrite();
		
		if (a.getFils_gauche() != null)
		{
			s += "\n" ;
			s += parcoursWriteRecur(s, a.getFils_gauche());
		}
		if (a.getFils_droit() != null)
		{
			s += "\n" ;
			s += parcoursWriteRecur(s, a.getFils_droit());
		}
		return s ;
	}

	/**
	 * @role : Fonction permettant d'éffectuer la recherche d'un élément
	 * @param value
	 */
	public void recherche(int value)
	{
		rechercheRecur(value, racine);
	}
	
	/**
	 * @role : Fonction permettant d'éffectuer la recherche d'un élément
	 * @param value
	 * @param a
	 */
    public void rechercheRecur(int value, AABRR a) 
    {
    	// On cherche l'intervalle
		if ((value < a.getMin()) && (a.getFils_gauche() != null))
		    rechercheRecur(value,a.getFils_gauche());
		else if ((value > a.getMax()) && (a.getFils_droit() != null))
		    rechercheRecur(value,a.getFils_droit());
		else if(a.getMin() <= value && value <= a.getMax())
		{
			if(a.getNoeud().recherche(value)) // On test si la valeur existe 
			{
				System.out.println("Element " + value +  " trouvé \n ");
				System.out.println("L'intervalle ::  " + a.getMin() + " ---- " + a.getMax());
			}
			else // sinon elle se trouve dans l'intervalle mais n'existe pas
			{
				System.out.println("Aucun ABR ne contient la valeur " + value +  " mais l'intervalle correspondant est : \n ");
				System.out.println("L'intervalle ::  " + a.getMin() + " ---- " + a.getMax());
			}
		}
		else // sinon elle n'appartient à aucun intervalle
		{
			
			System.out.println("L'élément " + value +  " est introuvable et aucun intervalle ne correspond ");
		}
    }
    
	/**
	 * @role : Fonction permettant d'éffectuer la suppression d'un élément
	 * @param value
	 */
	public void suppression(int value)
	{
		AABRR a1 = trouverNoeud(value, racine);
		if(a1 != null)
		{
			ABRR a = a1.getNoeud() ;
			a.supprimer(value);
			if(a.getRacine()==null) // dans le cas où l'ABRR a contenait un seul élément
			{
				if(a1.getParent().fils_droit == a1) // si c'était un fils droit
				{
					a1.getParent().fils_droit = a1.getFils_droit(); 
				}
				else
				{
					a1.getParent().fils_gauche = a1.getFils_gauche();
				}
			}
			else
			{
				a1.setNoeud(a);
			}
		}
	}
	
	/**
	 * @role Fonction permettant de trouver le noeud (l'intervalle correspondant) à la valeur passé en paramètre
	 * @param value
	 * @param a
	 * @return
	 */
    public AABRR trouverNoeud(int value, AABRR a)
    {
    	if ((value < a.getMin()) && (a.getFils_gauche() != null))
		    return trouverNoeud(value,a.getFils_gauche());
		else if ((value > a.getMax()) && (a.getFils_droit() != null))
		    return trouverNoeud(value,a.getFils_droit());
		else if(a.getMin() <= value && value <= a.getMax())
		{
			if(a.getNoeud().recherche(value))
			{
				System.out.println("Element trouvé \n ");
				System.out.println("L'intervalle ::  " + a.getMin() + " ---- " + a.getMax());
				return a ;
			}
			else
			{
				System.out.println("Aucun ABR ne contient x mais l'intervalle correspondant est : \n ");
				System.out.println("L'intervalle ::  " + a.getMin() + " ---- " + a.getMax());
			}
		}
		else
		{
			System.out.println("Aucun intervalle ne correspond à x ");
		}
		return null;
    }
	
    /**
     * @role Fonction permettant de réaliser l'insertion d'une valeur
     * @param value
     */
    public void insertion(int value) 
    {
    	AABRR a1 = trouverNoeudExistant(value,racine);
		if(a1 != null)
		{
			ABRR a = a1.getNoeud() ;
			a.insert(value);
		}
    }
     
    /**
     * @role Fonction utilisée pour l'insertion d'une valeur, on va retourner le noeud correspondant, si l'intervalle n'existe pas on retourne null
     * @param value
     * @param a
     * @return
     */
    public AABRR trouverNoeudExistant(int value, AABRR a)
    {
    	if ((value < a.getMin()) && (a.getFils_gauche() != null))
		    return trouverNoeudExistant(value,a.getFils_gauche());
		else if ((value > a.getMax()) && (a.getFils_droit() != null))
		    return trouverNoeudExistant(value,a.getFils_droit());
		else if(a.getMin() <= value && value <= a.getMax())
		{
			return a;
		}
		else
		{
			System.out.println("Aucun intervalle ne correspond à " + value );
		}
		return null;
    }
	
    /**
     * Fonction permettant l'insertion d'éléments dans un AABRR
     * @param min
     * @param max
     * @param noeuds
     */
	public void insert(int min, int max, String[] noeuds)
	{
		racine = insert_in_AABRR(racine,min,max,noeuds);
	}
	
	/**
	 * @role Fonction permettant l'insertion d'éléments dans un AABRR
	 * @param a
	 * @param min
	 * @param max
	 * @param noeuds
	 * @return
	 */
	public AABRR insert_in_AABRR(AABRR a, int min, int max, String[] noeuds ) 
	{
		if (a == null) {
            a = new AABRR(min,max);
            ABRR b = new ABRR();
            if(noeuds != null)
            {
            	for(int i=0 ; i< noeuds.length ; i++)
    	        {
    	        	b.insert(Integer.parseInt(noeuds[i]));
    	        }
            }
            a.setNoeud(b);
            return a;
        }
	    if (max < a.getMin()) {
	    	a.setFils_gauche(insert_in_AABRR(a.getFils_gauche(),min,max,noeuds));
            a.getFils_gauche().setParent(a);

	    } else if (min >= a.getMax()) {
	        a.setFils_droit(insert_in_AABRR(a.getFils_droit(),min,max,noeuds));
            a.getFils_droit().setParent(a);

	    }
	    else
	    {
	    	System.out.println("Intervalle non disjoincts :/ on ne peut insérer");
	    }
	    return a ;
	  }

	/**
	 * @role Fonction utilisée pour la fonctionnalité AABRR vers ABR
	 * @param nouveau
	 */
	public void createABR(ABR nouveau) 
	{
		createABRRecur(racine,nouveau);
	}

	/**
	 * @role Fonction utilisée pour la fonctionnalité AABRR vers ABR 
	 * @param a
	 * @param b
	 */
	public void createABRRecur(AABRR a, ABR b)
	{
		a.getNoeud().createABR(b);
		if (a.getFils_gauche() != null)
			createABRRecur(a.getFils_gauche(),b);
		if (a.getFils_droit() != null)
			createABRRecur(a.getFils_droit(),b);
	}
	
	
	
}
		
