import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 
 * @author FRAPPER, N'GUESSAN
 * @role Classe ABRR
 */
public class ABRR {

	private int cle ;
	private ABRR fils_gauche ;
	private ABRR fils_droit ;
	private ABRR racine ;
	private ABRR parent;
    
    static int niveauCourant  = -1;
    static int espaceRestant = -1;
    static final int H_SPREAD = 3; 
    private int profondeur=0;
    private int niveau=0;
    private int Position=0;

	
	

	public ABRR(int cle/*, ABRR fils_gauche, ABRR fils_droit, ABRR pere*/) {
		this.cle = cle;

	}
	// arbre vide
	public ABRR(){}

	public ABRR getRacine() {
		return racine;
	}

	public void setRacine(ABRR racine) {
		this.racine = racine;
	}
	
	public int getCle() {
		return cle;
	}

	public void setCle(int cle) {
		this.cle = cle;
	}

	public ABRR getFils_gauche() {
		return fils_gauche;
	}

	public void setFils_gauche(ABRR fils_gauche) {
		this.fils_gauche = fils_gauche;
	}

	public ABRR getFils_droit() {
		return fils_droit;
	}

	public void setFils_droit(ABRR fils_droit) {
		this.fils_droit = fils_droit;
	}

	public void parcoursPrefixe() {
		parcoursPrefixeRecur(racine);
	}
	
	public void parcoursPrefixeRecur(ABRR a) {
		System.out.print(a.getCle() + " ");
		if (a.getFils_gauche() != null)
			parcoursPrefixeRecur(a.getFils_gauche());
		if (a.getFils_droit() != null)
			parcoursPrefixeRecur(a.getFils_droit());
	    }
	
	/**
	 * @role Fonction réalisant un parcours infixe inverse 
	 * @param arl
	 * @param m
	 * @param M
	 * @return
	 */
	
	public boolean parcoursInfixeInverse(ArrayList<Integer> arl, int m, int M) 
	{
		return parcoursInfixeInverseRecur(arl,m,M,racine);
	}
	
	/**
	 * @role  Fonction réalisant un parcours infixe inverse en testant si un ABRR est valide (utilisée pour la vérification d'AABRR).
	 * @param arl
	 * @param m
	 * @param M
	 * @param a
	 * @return
	 */
    public boolean parcoursInfixeInverseRecur(ArrayList<Integer> arl, int m, int M, ABRR a) 
    {
		if (a.getFils_droit() != null)
			parcoursInfixeInverseRecur(arl,m,M,a.getFils_droit());
		arl.add(a.getCle());
		if (a.getFils_gauche() != null)
			parcoursInfixeInverseRecur(arl,m,M,a.getFils_gauche());
		
		for(int i = 0 ; i< arl.size() ; i++)
		{
			if(arl.get(i) < m || arl.get(i) > M)
			{
				return false ;
			}
		}
		for(int i = 0 ; i< arl.size()-1 ; i++)
		{
			if(arl.get(i) > arl.get(i+1))
			{
				return false ; 
			}
		}
		return true ;
    }
    
    /**
     * @role Fonction utilisé pour la fonctionnalité AABRR_to_file 
     * @return
     */
    public String parcoursWrite()
	{
		String s ="";
		return parcoursWriteRecur(s,racine);
	}
    
    /**
     * @role Fonction utilisé pour la fonctionnalité AABRR_to_file 
     * @param s
     * @param a
     * @return
     */
	public String parcoursWriteRecur(String s, ABRR a)
	{
		s = Integer.toString(a.getCle());
		if (a.getFils_gauche() != null)
		{
			s += ":" ;
			s += parcoursWriteRecur(s,a.getFils_gauche());
		}
		if (a.getFils_droit() != null)
		{
			s += ":" ;
			s += parcoursWriteRecur(s,a.getFils_droit());
		}
		return s ;
	}

	/**
	 * @role Fonction utilisée pour la recherche de valeur 
	 * @param value
	 * @return
	 */
	public boolean recherche (int value)
	{
		return rechercheRecur(value, racine);
	}
	
	/**
	 * @role Fonction utilisée pour la recherche de valeur
	 * @param value
	 * @param a
	 * @return
	 */
    public boolean rechercheRecur(int value, ABRR a) 
    {
		if (value == a.getCle())
		{
		    return true;
		}
		if ((value > a.getCle()) && (a.getFils_gauche() != null))
		{			
		    return rechercheRecur(value, a.getFils_gauche());
		}
		if ((value <= a.getCle()) && (a.getFils_droit() != null))
		{
		    return rechercheRecur(value,a.getFils_droit());
		}
		return false;
    }
    
    /**
     * @role Fonction utilisée pour l'insertion de valeur 
     * @param valeur
     */
    public void insert(int valeur)
    {
    	racine = insert_in_ABRR(racine,valeur);
    }

    /**
     * @role Fonction utilisée pour l'insertion de valeur 
     * @param a
     * @param valeur
     * @return
     */
    private ABRR insert_in_ABRR(ABRR a,int valeur) {
    	if(a == null)
    	{
    		a = new ABRR(valeur);
    		return a ;
    	}
	    if (valeur >= a.getCle()) 
	    {
	    	a.setFils_gauche(insert_in_ABRR(a.getFils_gauche(),valeur));
            a.getFils_gauche().parent = a;

	    }
	    else if (valeur < a.getCle()) 
	    {
	    	a.setFils_droit(insert_in_ABRR(a.getFils_droit(),valeur));
            a.getFils_droit().parent = a;
	    }
	    return a ;
	}
    
    /**
     * @role Fonction utilisée pour supprimer une valeur 
     * @param value
     */
	public void supprimer(int value) 
	{
		racine = supprimerRecur(racine, value);
	}
	
	/**
	 * @role Fonction utilisée pour supprimer une valeur 
	 * @param a
	 * @param value
	 * @return
	 */
	private ABRR supprimerRecur(ABRR a, int value) 
	{
		if(a == null)
			return null ;
		if(value < a.getCle())
			a.setFils_droit(supprimerRecur(a.getFils_droit(),value));
		else if (value > a.getCle())
			a.setFils_gauche(supprimerRecur(a.getFils_gauche(),value));
		else
		{
            if (a.getFils_gauche()== null)
                return a.getFils_droit();
            else if (a.getFils_droit()== null)
                return a.getFils_gauche();
            a.setCle(maxValue(a.getFils_droit())); 
            a.setFils_droit(supprimerRecur(a.getFils_droit(), a.getCle()));
		}
		return a;
	}
	
	/**
	 * @role Fonction utilisé pour rechercher la valeur maximum dans un ABRR
	 * @param a
	 * @return
	 */
	private int maxValue(ABRR a) 
	{
		int maxv = a.getCle();
        while (a.getFils_gauche() != null)
        {
            maxv = a.getFils_gauche().getCle();
            a = a.getFils_gauche();
        }
        return maxv;
	}
	
	/**
	 * @role Fonction utilisé pour créer un ABR 
	 * @param a
	 */
	public void createABR(ABR a) {
		createABRRecur(racine,a);
		
	}
	
	/**
	 * @role Fonction utilisé pour créer un ABR 
	 * @param a
	 * @param b
	 */
	private void createABRRecur(ABRR a,ABR b) {
		
		b.insert(a.getCle());
		if (a.getFils_gauche() != null)
			createABRRecur(a.getFils_gauche(),b);
		if (a.getFils_droit() != null)
			createABRRecur(a.getFils_droit(),b);
	}
	
	/**
	 * @role Fonction pour afficher un ABRR en console 
	 */
	public void beautyPrint()
	{
		drawTree(racine);
	}
	
	/**
	 * @role : Fonction permettant de déssiner l'arbre 
	 * @param root
	 */
    static void drawTree(ABRR root) 
    {
        
        int profondeur = profondeur(root);
        setniveaus (root, 0);
        
        int profondeurChildCount[] = new int [profondeur+1];
        
        
        LinkedList<ABRR> q = new  LinkedList<ABRR> ();
        q.add(root.getFils_gauche());
        q.add(root.getFils_droit());
        
        root.Position = (int)Math.pow(2, profondeur-1)*H_SPREAD;
        niveauCourant = root.niveau;
        espaceRestant = root.Position;
        System.out.print(getSpace(root.Position) + root.getCle());
        
        while (!q.isEmpty())
        {
        	ABRR ele = q.pollFirst();
            drawElement(ele, profondeurChildCount, profondeur, q);
            if (ele == null)
                continue;
            q.add(ele.getFils_gauche());
            q.add(ele.getFils_droit());
        }
        System.out.println();
    }
    
    /**
     * @role Fonction permettant d'afficher les éléments de l'arbre
     * @param ele
     * @param profondeurChildCount
     * @param profondeur
     * @param q
     */
    static void drawElement(ABRR ele, int profondeurChildCount[], int profondeur, LinkedList<ABRR> q) 
    {
        if (ele == null)
            return;
        
        if (ele.niveau != niveauCourant)
        {
            niveauCourant = ele.niveau;
            espaceRestant = 0;
            System.out.println();
            for (int i=0; i<(profondeur-ele.niveau+1); i++)
            {
                int drawn = 0;
                if (ele.parent.getFils_gauche() != null)
                {
                    drawn = ele.parent.Position - 2*i - 2;
                    System.out.print(getSpace(drawn) + "/");
                }
                if (ele.parent.getFils_droit() != null)
                {
                    int drawn2 = ele.parent.Position + 2*i + 2;
                    System.out.print(getSpace(drawn2 - drawn) + "\\");
                    drawn = drawn2;
                }
                
                ABRR doneParent = ele.parent;
                for (ABRR sibling: q)
                {
                    if (sibling == null)
                        continue;
                    if (sibling.parent == doneParent)
                        continue;
                    doneParent = sibling.parent;
                    if (sibling.parent.getFils_gauche() != null)
                    {
                        int drawn2 = sibling.parent.Position - 2*i - 2;
                        System.out.print(getSpace(drawn2-drawn-1) + "/");
                        drawn = drawn2;
                    }
                    
                    if (sibling.parent.getFils_droit() != null)
                    {
                        int drawn2 = sibling.parent.Position + 2*i + 2;
                        System.out.print(getSpace(drawn2-drawn-1) + "\\");
                        drawn = drawn2;
                    }
                }
                System.out.println();
            }
        }
        int offset=0;
        int numDigits = (int)Math.ceil(Math.log10(ele.getCle()));
        if (ele.parent.getFils_gauche() == ele)
        {
            ele.Position = ele.parent.Position - H_SPREAD*(profondeur-niveauCourant+1);
            offset += numDigits/2;
        }
        else
        {
            ele.Position = ele.parent.Position + H_SPREAD*(profondeur-niveauCourant+1);
            offset -= numDigits;
        }
        
        System.out.print (getSpace(ele.Position - espaceRestant + offset) + ele.getCle());
        espaceRestant = ele.Position + numDigits/2;
    }
 
    /**
     * @role Fonction permettant de calculer la profondeur d'une fonction
     * @param n
     * @return
     */
    public static int profondeur (ABRR n)
    {
        if (n == null)
            return 0;
        n.profondeur = 1 + Math.max(profondeur(n.getFils_gauche()), profondeur(n.getFils_droit()));
        return n.profondeur;
    }
    
    /**
     * @role Fonction permettant de ré initialiser le niveau de l'arbre
     * @param r
     * @param niveau
     */
    static void setniveaus (ABRR r, int niveau)
    {
        if (r == null)
            return;
        r.niveau = niveau;
        setniveaus (r.getFils_gauche(), niveau+1);
        setniveaus (r.getFils_droit(), niveau+1);
    }
 
    /**
     * @role Fonction d'affichage 
     * @param i
     * @return
     */
    static String getSpace (int i)
    {
        String s = "";
        while (i-- > 0)
            s += " ";
        return s;
    }
	

	
}
