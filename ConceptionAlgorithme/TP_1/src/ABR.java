import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 
 * @author FRAPPER, N'GUESSAN
 * @role Classe ABR
 */
public class ABR {
	
	private int cle ;
	private ABR fils_gauche ;
	private ABR fils_droit ;
	private ABR parent ;
	private ABR racine;
    static int niveauCourant  = -1;
    static int espaceRestant = -1;
    static final int H_SPREAD = 3; 

    private int profondeur=0;
    private int niveau=0;
    private int Position=0;
	
    
	public ABR() {}
	public ABR(int cle) 
	{	
		this.cle = cle;	
	}
	public int getCle() {
		return cle;
	}
	public void setCle(int cle) {
		this.cle = cle;
	}
	public ABR getFils_gauche() {
		return fils_gauche;
	}
	public void setFils_gauche(ABR fils_gauche) {
		this.fils_gauche = fils_gauche;
	}
	public ABR getFils_droit() {
		return fils_droit;
	}
	public void setFils_droit(ABR fils_droit) {
		this.fils_droit = fils_droit;
	}
	
	/**
	 * @role Fonction permettant de connaitre le nombre d'élément dans l'ABR
	 * @return
	 */
	public int nombre_value()
	{
		int nombre = 1 ; 
		int nb = parcoursNombre(racine);
		return nb ;
	}
	
	/**
	 * @role Fonction permettant de connaitre le nombre d'élément dans l'ABR
	 * @param a
	 * @return
	 */
	public int parcoursNombre(ABR a)
	{
		  int nb = 1;                                      
		  if ( a.getFils_droit() != null ) nb += a.parcoursNombre(a.getFils_droit());        
		  if ( a.getFils_gauche()!= null ) nb += a.parcoursNombre(a.getFils_gauche());
		  return nb;
	}

	/**
	 * @role Fonction permettant d'inserer un élément
	 * @param value
	 */
	public void insert(int value) 
	 {
	       racine = insertRec(racine, value);
	 }
     
	/**
	 * @role Fonction permettant d'inserer un élément
	 * @param a
	 * @param value
	 * @return
	 */
	public ABR insertRec(ABR a, int value) {
 
        if (a == null) {
            a = new ABR(value);
            return a;
        }
        if (value < a.getCle())
        {
            a.setFils_gauche(insertRec(a.getFils_gauche(), value));
            a.getFils_gauche().parent = a;
        }
        else if (value > a.getCle())
        {
            a.setFils_droit(insertRec(a.getFils_droit(), value));
            a.getFils_droit().parent = a;
        }
        return a;
    }
    
	/**
	 * @role Fonction permettant de réaliser un parcours préfixe
	 */
    public void parcoursPrefixe()  {
    	int n=0;
        parcoursPrefixeRecur(racine, n);
     }
    
    /**
     * @role : Fonction permettant de réaliser un parcours préfixe
     * @param a
     * @param n
     */
    public void parcoursPrefixeRecur(ABR a, int n) {
        if (a != null) {
            System.out.print(a.getCle() + " ");
        	parcoursPrefixeRecur(a.getFils_gauche(), n);
            parcoursPrefixeRecur(a.getFils_droit(), n);
        }
    }
    
    /**
     * @role Fonction permettant d'avoir le plus petit élément d'un ABR
     * @return
     */
	public int getMinimum() 
	{
		return getMinimumRecur(racine);
	}
	
	/**
	 * @role Fonction permettant d'avoir le plus petit élément d'un ABR
	 * @param a
	 * @return
	 */
	public int getMinimumRecur(ABR a) 
	{
        while (a.getFils_gauche() != null) 
        	return getMinimumRecur(a.getFils_gauche());
        return a.getCle();   
    }
	
	/**
	 * @role Fonction permettant d'avoir le plus grand élément d'un ABR
	 * @return
	 */
	public int getMaximum() 
	{
		return getMaximumRecur(racine);
	}
	
	/**
	 * @role Fonction permettant d'avoir le plus grand élément d'un ABR
	 * @param a
	 * @return
	 */
	private int getMaximumRecur(ABR a) {
		while (a.getFils_droit() != null) 
        	return getMaximumRecur(a.getFils_droit());
        return a.getCle();
	}
	
	/**
	 * @role Fonction utilisée pour la fonctionnalité ABR vers AABRR, 
	 * @param result
	 */
	public void insert_for_AABRR(AABRR result) {
		insert_for_AABRR_Recur(result,racine);
   }

	/**
	 * @role Fonction réalisant l'insertion de manière "prefixe" dans un AABRR
	 * @param result
	 * @param a
	 */
	private void insert_for_AABRR_Recur(AABRR result, ABR a) 
	{
		if (a != null) {
			result.insertion(a.getCle());
			insert_for_AABRR_Recur(result,a.getFils_gauche());
            insert_for_AABRR_Recur(result,a.getFils_droit());
        }
		
	}
	
	/**
	 * @role Fonction utilisée pour la fonctionnalité ABR vers AABRR, permettant de créer les intervalles d'un AABRR
	 * @param result
	 * @param tab_int
	 */
	public void insert_intervalles(AABRR result, int[] tab_int) {
		
		insert_intervallesRecur(result,tab_int,racine);
		
	}
	
	/**
	 * @role Fonction utilisée pour la fonctionnalité ABR vers AABRR, permettant de créer les intervalles d'un AABRR
	 * @param result
	 * @param tab_int
	 * @param a
	 */
	private void insert_intervallesRecur(AABRR result, int[] tab_int, ABR a)
	{
		
		int indice = getIndice(a.getCle(), tab_int);
		String[] noeuds = null ;
		result.insert(tab_int[indice], tab_int[indice+1], noeuds);

		for(int j = 0 ; j < tab_int.length ; j+=2)
		{
			if(tab_int[j] != tab_int[indice])
				result.insert(tab_int[j], tab_int[j+1], noeuds);
		}
	}
	
	/**
	 * @role Fonction permettant d'avoir l'indice d'une valeur dans un tableau
	 * @param value
	 * @param tab
	 * @return
	 */
	private int getIndice(int value, int[] tab) {
		
		for(int i = 0 ; i < tab.length ; i+=2)
		{
			if(tab[i] <=value && value <= tab[i+1])
				return i;
		}
		return 0;
	}
	
	/**
	 * @role Fonction affichage d'un parcours infixe
	 */
	public void parcoursInfixe() 
	{
		parcoursInfixeRecur(racine);
	}
	
	/**
	 * @role Fonction affichage d'un parcours infixe
	 * @param a
	 */
	private void parcoursInfixeRecur(ABR a) 
	{
		if (a != null) 
		{
			parcoursInfixeRecur(a.getFils_gauche());
	        System.out.print(a.getCle() + " ");
	        parcoursInfixeRecur(a.getFils_droit());
	    }
		
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
	
    static void drawTree(ABR root) 
    {
        int profondeur = profondeur(root);
        setniveaus(root, 0);
        
        int profondeurChildCount[] = new int [profondeur+1];
        
        
        LinkedList<ABR> q = new  LinkedList<ABR> ();
        q.add(root.getFils_gauche());
        q.add(root.getFils_droit());
        
        root.Position = (int)Math.pow(2, profondeur-1)*H_SPREAD;
        niveauCourant = root.niveau;
        espaceRestant = root.Position;
        System.out.print(getSpace(root.Position) + root.getCle());
        
        while (!q.isEmpty())
        {
            ABR ele = q.pollFirst();
            drawElement (ele, profondeurChildCount, profondeur, q);
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
    
    static void drawElement(ABR ele, int profondeurChildCount[], int profondeur, LinkedList<ABR> q) 
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
                
                ABR doneParent = ele.parent;
                for (ABR sibling: q)
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
            //offset = 2;
            offset += numDigits/2;
        }
        else
        {
            ele.Position = ele.parent.Position + H_SPREAD*(profondeur-niveauCourant+1);
            //offset = -2;
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
    public static int profondeur (ABR n)
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
    static void setniveaus (ABR r, int niveau)
    {
        if (r == null)
            return;
        r.niveau = niveau;
        setniveaus(r.getFils_gauche(), niveau+1);
        setniveaus(r.getFils_droit(), niveau+1);
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
