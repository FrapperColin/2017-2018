import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import javax.rmi.ssl.SslRMIClientSocketFactory;

/**
 * 
 * @author FRAPPER, N'GUESSAN
 * @role Classe main
 */
public class main {
	
	public static void main(String[] args) 
	{	
		// menu
		System.out.println("Taper 1 : fichier vers AABRR");
		System.out.println("Taper 2 : AABRR vers fichier");
		System.out.println("Taper 3 : AABRR aléatoire");
		System.out.println("Taper 4 : Vérification");
		System.out.println("Taper 5 : Recherche d’un entier");
		System.out.println("Taper 6 : Suppression d’un entier");
		System.out.println("Taper 7 : Insertion d’un entier");
		System.out.println("Taper 8 : ABR vers AABRR");
		System.out.println("Taper 8 : AABRR vers ABR");
		System.out.println("Taper 9 : QUIT");

		Scanner sc = new Scanner(System.in);
		int select ;
		boolean fini = false ;
		File file= new File("test.txt");

		while (!fini)
		{
			select = sc.nextInt();
			
			switch(select)
			{
				case 1:
					// ----------------------- creation AABRR à partir d'un fichier ------------------------------ //
					AABRR file_to_aabrr = new AABRR();
					file_to_aabrr = file_to_AABRR(file,file_to_aabrr);
					//System.out.println("\n\n Parcours préfixe \n\n");
					//file_to_aabrr.parcoursPrefixe();
//					System.out.println("\n\n Affichage graphique console \n\n");
//					file_to_aabrr.parcoursInfixe();
					System.out.println(file_to_aabrr.parcoursWrite());
					file_to_aabrr.printBeauty();

					break ;
				case 2:
					// ----------------------- Creation de fichier à partir d'AABRR ------------------------------ //
					AABRR aabrr_to_file = new AABRR();
					aabrr_to_file = file_to_AABRR(file,aabrr_to_file);
					AABRR_to_file(aabrr_to_file);
					break ;
				case 3:
					// -------------------------- AABRR aléatoire ----------------------------------- //

					AABRR aleatoire = new AABRR();
					
					aleatoire = aleatoire_AABRR(3,10);
					if(aleatoire != null)
					{
						AABRR_to_file(aleatoire);
						//aleatoire.printBeauty();
						System.out.println(aleatoire.parcoursWrite());
					}
					break ;
				case 4:
					// ---------------------------  Verification AABRR  ------------------------------------- //
					verif_AABRR(file);
					break ;
				case 5:
					// ---------------------------  Recherche entier  ------------------------------------- //
					AABRR recherche = new AABRR();
					recherche = file_to_AABRR(file,recherche);
					// 3 types de tests :
					recherche.recherche(36);
					recherche.recherche(57);
					recherche.recherche(500);

					break ;
				case 6:
					// ---------------------------  Suppression d'un entier  ------------------------------------- //
					AABRR supprimer = new AABRR();
					supprimer = file_to_AABRR(file,supprimer);
					supprimer.suppression(82);
					supprimer.suppression(36);
					supprimer.suppression(500);
					System.out.println(supprimer.parcoursWrite());

					//supprimer.printBeauty();
					//AABRR_to_file(supprimer);
					break ;
				case 7:
					// ---------------------------  Insertion d'un entier  ------------------------------------- //
					AABRR inserer = new AABRR();
					inserer = file_to_AABRR(file,inserer);	
					inserer.insertion(28);
					inserer.insertion(55);
					inserer.insertion(81);
					inserer.insertion(7000);
					System.out.println(inserer.parcoursWrite());

					//inserer.printBeauty();
					//AABRR_to_file(inserer);

					break ;
				case 8:
					// --------------------------------  ABR vers AABRR  ------------------------------------- //
					ABR abr_to_aabrr = new ABR();
					AABRR result = new AABRR();

					abr_to_aabrr.insert(10);
					abr_to_aabrr.insert(5);
					abr_to_aabrr.insert(3);
					abr_to_aabrr.insert(7);
					abr_to_aabrr.insert(15);
					abr_to_aabrr.insert(13);
					abr_to_aabrr.insert(20);
					result = ABR_to_AABRR(abr_to_aabrr,3);
					if(result !=null)
					{
						//result.printBeauty();
						System.out.println(result.parcoursWrite());

						AABRR_to_file(result);
					}
					break ;	
				case 9:
					// -------------------------------- AABRR vers ABR  ------------------------------------- //
					AABRR aabrr_to_abr = new AABRR();
					aabrr_to_abr = file_to_AABRR(file,aabrr_to_abr);
					ABR resultat = new ABR();
					resultat = AABRR_to_ABR(aabrr_to_abr);
					resultat.beautyPrint();
					System.out.println("\n\nparcours infixe : \n\n ");
					resultat.parcoursInfixe();
					System.out.println("\n\nparcours prefixe \n\n");
					resultat.parcoursPrefixe();
					break ;
				case 10 : 
					System.out.println("Aurevoir");
					fini = true ;
					break ;
				default:
					break;
			}
		}
	}
	
	/**
	 * 
	 * @param f : fichier à charger
	 * @param a : arbre à créer.
	 * @role fonction permettant de créer un AABRR à partir d'un fichier
	 * @return l'arbre créé à partir du fichier en paramètre
	 */
	private static AABRR file_to_AABRR(File f, AABRR a)
	{
		System.out.println("Nom du fichier : " + f.getName());
		if (f != null) {
			try {

				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				int m;
				int M;
				try {
					String line = br.readLine();	
					String[] tab ;
					String[] mTab ;
		
					while (line != null) 
					{
						if (line.length() > 0) 
						{
							tab = line.split(";");
							// premiere partie du tableau (le m et M) en soit l'intervalle
							mTab = tab[0].split(":");
							m = Integer.parseInt(mTab[0]);
							M = Integer.parseInt(mTab[1]);
							// Les noeuds à insérer
							String[] tabNoeud = tab[1].split(":");
							a.insert(m, M, tabNoeud);
						}
						line = br.readLine();	
					}
					br.close();
					fr.close();
				} catch (IOException exception) {
					
					System.out.println("Erreur lors de la lecture :"+ exception);
				}
			} catch (FileNotFoundException exception) {

				System.out.println("Le fichier n'a pas été trouvée");
			}
		}
		return a ;
	}

	/**
	 * 
	 * @param racine
	 * @role fonction permettant d'écrire une fonction qui effectue la sauvegarde d’un AABRR dans un fichier
	 * 
	 */
	private static void AABRR_to_file(AABRR racine) {
		// fichier où l'on va écrire l'AABRR
		File writenFile = new File("written.txt");
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			String content = racine.parcoursWrite();

			// écriture de l'AABRR
			fw = new FileWriter(writenFile);
			bw = new BufferedWriter(fw);
			bw.write(content);

			System.out.println("Fin écriture");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();
			}
		}
	}

	/**
	 * @role fonction qui prend en argument deux entiers p et q et qui crée un AABRRA à p noeuds, avec des données aléatoires.
	 * @param p : le nombre de noeuds à insérer 
	 * @param q : valeur maximum de tous les intervalles
	 * @return l'AABRR correspondant
	 */
	private static AABRR aleatoire_AABRR(int p, int q)
	{
		// test du cas où c'est impossible de créer l'AABRR
		if(q < 2*p || p == q)
		{
			System.out.println("Impossible de remplir " + p + " noeuds avec ce nombre " + q + " maximum");
		}
		else
		{
			// tableau d'intervalle
			int[] tab_int = new int [2*p];
			tab_int[0] = 1 ;
			tab_int[1] = q ;
			boolean trouve = false ;
			// parcours du tableau pour remplir le tableau d'intervalle
			for (int i = 2 ; i< 2*p ; i++)
			{
				while (!trouve)
				{
					int nb = (int) (1 + (Math.random() * (q - 1)));
					if(!(contains(nb, tab_int))) // si le tableau ne contient pas la valeur 
					{
						tab_int[i] = nb;
						trouve = true ;
					}
				}
				trouve = false ;
			}

			// on trie le tableau par ordre croissant
			Arrays.sort(tab_int);
	
			return create_AABRR_alea(tab_int,p);
		}
		return null ;
	}
	
	/**
	 * @role créé un AABRR à partir d'un tableau d'intervalle et d'un nombre de noeud à créé
	 * @param tab
	 * @param p
	 * @return l'AABRR aléatoire
	 */
	private static AABRR create_AABRR_alea(int[] tab, int p) {
		
		int nb_alea = (int) (Math.random() * (p));		// de O à p -1

		AABRR aleatoire = new AABRR();
		
		// nombre de noeud maximum que l'on peut créer dans l'intervalle
		int noeud_max = (tab[2*nb_alea+1] - tab[2*nb_alea]) +1 ; 
		
		// Nombre de noeuds dans l'intervalle
		int nb_noeuds = (int) (1 + (Math.random() * (noeud_max - 1)));

		String[] tabNoeuds = new String[nb_noeuds] ;
		for(int k = 0 ; k < tabNoeuds.length ; k++)
		{
			tabNoeuds[k] = Integer.toString((int) (tab[2*nb_alea] + (Math.random() * (tab[2*nb_alea+1] - tab[2*nb_alea])))) ;
		}
		// insertion
		aleatoire.insert(tab[2*nb_alea],tab[2*nb_alea+1], tabNoeuds);

		// initialisation à 0 afin de ne pas ré-écrire l'intervalle
		tab[2*nb_alea+1] = 0 ;
		tab[2*nb_alea] = 0;
		int nb_noeud = 1 ;
		// on fini de remplir l'AABRR
		while (nb_noeud != p)
		{
			int nb = (int) (Math.random() * (p));
			if(tab[2*nb] != 0)
			{
				noeud_max = tab[2*nb+1] - tab[2*nb] +1; 
				nb_noeuds = (int) (1 + (Math.random() * (noeud_max )));
				tabNoeuds = new String[nb_noeuds] ;
				for(int k = 0 ; k < nb_noeuds ; k++)
				{
					tabNoeuds[k] = Integer.toString((int) (tab[2*nb] + (Math.random() * (tab[2*nb+1] - tab[2*nb])))) ;
				}
				aleatoire.insert(tab[2*nb],tab[2*nb+1], tabNoeuds);
				nb_noeud++ ;
				tab[2*nb+1] = 0 ;
				tab[2*nb] = 0;
			}			
		}
		return aleatoire ;
	}
	
	/**
	 * @role fonction permettant de vérifier un AABRR à partir d'un fichier passé en paramètre
	 * @param p
	 */
	private static void verif_AABRR(File p)
	{
		boolean result ;
		AABRR verif = new AABRR() ;
		verif = file_to_AABRR(p, verif);
		result = verif.verif_AABRR();
		
		if(result)
		{
			System.out.println("L'AABRR est correct ");
			// on affiche l'arbre
			//verif.printBeauty();
			System.out.println(verif.parcoursWrite());
		}
		else
		{
			System.out.println("L'AABRR n'est pas correct ");
		}		
	}
	
	/**
	 * @role : fonction permettant de construire un AABRR à partir d'un ABR et d'un entier k représentant le nombre d'intervalle
	 * @param a
	 * @param k
	 * @return l'AABRR créé à partir de l'ABR
	 */
	private static AABRR ABR_to_AABRR(ABR a, int k)
	{
		int nombre_element= a.nombre_value();
		AABRR result = null;

		if(k > nombre_element-1)
		{
			System.out.println("Erreur le nombre de noeud est supérieur aux nombre d'élément");
		}
		else
		{
			int m = a.getMinimum();

			int M = a.getMaximum();

			int taille_Intervalles = (M - m) / k ;
			int[] tab_int = new int[2*k];
			tab_int[0] = m ;
			tab_int[2*k-1] = M ;
			for(int i = 1 ; i <2*k-1; i+=2)
			{
				tab_int[i] = tab_int[i-1] + taille_Intervalles ;
				tab_int[i+1] = tab_int[i] + 1 ;
			}
			result = new AABRR();
			a.insert_intervalles(result, tab_int);
			a.insert_for_AABRR(result);
		}
		return result;
	}
	
	/**
	 * @role Fonction permettant de construire un ABR à partir d'un AABRR passé en paramètre
	 * @param a
	 * @return
	 */
	private static ABR AABRR_to_ABR(AABRR a)
	{
		ABR nouveau = new ABR();
		a.createABR(nouveau);
		return nouveau;
		
	}

	/**
	 * Fonction permettant de tester si un entier i est dans un tableau tab
	 * @param i
	 * @param tab
	 * @return true or false selon le cas
	 */
	private static boolean contains(int i, int[] tab) {
		for(int j = 0 ; j < tab.length ; j++)
		{
			if (tab[j] == i)
			{
				return true ;
			}
		}
		return false;
	} 

}
