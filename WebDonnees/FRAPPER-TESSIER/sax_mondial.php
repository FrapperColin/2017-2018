<?php 
header('Content-type: text/xml');
include('Sax4PHP/Sax4PHP.php');

/*
	Classe Country, représentant un pays avec comme attribut son id, son nom, sa superficie ainsi que son nombre d'habitant
**/
class Country {

	private $id_p, $nom_p, $superficie, $nb_hab;

	public function __construct($id_p, $nom_p, $superficie, $nb_hab)
	{
		$this->id_p = $id_p ;
		$this->nom_p = $nom_p ;
		$this->superficie = $superficie ;
		$this->nb_hab = $nb_hab ;
	}

	public function getID()
	{
		return $this->id_p ;
	}
	public function getNom()
	{
		return $this->nom_p ;
	}
	public function getSuperficie()
	{
		return $this->superficie ;
	}
	public function getNbhab()
	{
		return $this->nb_hab ;
	}
}

/*
	Classe Fleuve, représentant un fleuve avec comme attribut son id, son nom, sa longueur, ou est ce qu'il se jette (dans quelles mers ou rivières),
	le pays de sa source, les pays que ce fleuve parcourt, et la distance parcourut (inconnu si traversé par plusieurs pays, sinon égale à la distance
	du fleuve)
**/
class Fleuve {

	private $id_f, $nom_f, $longueur, $se_jette, $id_source, $id_pays_traverse, $distance_parcourut;

	public function __construct($id_f, $nom_f, $longueur, $se_jette, $id_source, $id_pays_traverse, $distance_parcourut)
	{
		$this->id_f = $id_f ;
		$this->nom_f = $nom_f ;
		$this->longueur = $longueur ;
		$this->se_jette = $se_jette ;
		$this->id_pays_traverse = $id_pays_traverse ;
		$this->id_source = $id_source ;
		$this->distance_parcourut = $distance_parcourut ;
	}

	public function getID()
	{
		return $this->id_f ;
	}
	public function getNom()
	{
		return $this->nom_f ;
	}
	public function getLongueur()
	{
		return $this->longueur ;
	}
	public function getSejette()
	{
		return $this->se_jette ;
	}
	public function getWaterType()
	{
		return $this->watertype ;
	}
	public function getSource()
	{
		return $this->id_source ;
	}
	public function getIDTraverse()
	{
		return $this->id_pays_traverse ;
	}
	public function getDistanceParcourut()
	{
		return $this->distance_parcourut ;
	}
}

/*
	Classe Mer, représentant une mer avec comme attribut son id, son nom, son type (inconnu dans notre cas) ainsi que les pays cotoyés par cette mer
**/
class Mer {

	private $id_e, $nom_e, $type, $id_mer_country ;

	public function __construct($id_e, $nom_e, $id_mer_country)
	{
		$this->id_e = $id_e ;
		$this->nom_e = $nom_e ;
		$this->id_mer_country = $id_mer_country ;
		$this->type = "inconnu" ;
	}

	public function getID()
	{
		return $this->id_e ;
	}
	public function getNom()
	{
		return $this->nom_e ;
	}
	public function getType()
	{
		return $this->type ;
	}
	public function getIDCountry()
	{
		return $this->id_mer_country ;
	}
}


/*
	Classe général 
**/
class MySaxHandler extends DefaultHandler {

	// tableau qui fonctionnera comme tableau de classe où l'on pourra stocker toutes les informations dont on aura besoin
    private $tableau_country, $tableau_fleuve, $tableau_mer;

	// texte pour avoir les noeuds textes d'un document (marche avec la fonction characters)
	private $texte ;
	// Booleens permettant de savoir s'il faut stocker ou nom le noeud texte d'un élément name ($okname) ou d'un élément population ($okPop), du fait qu'il
	// y ai plusieurs balises avec le nom name ou encore avec le nom population, il nous faut des booleens pour savoir quand s'arreter.
	private $okName, $okPop ;
	// variables correspondant aux pays
	private $id_p, $nom_p, $superficie, $nb_hab;
	// variables correspondant aux fleuves
	private $id_f, $nom_f, $longueur, $se_jette, $watertype, $id_source;
	// variables correspondant aux parcourt (fleuve)
	private $id_pays_traverse, $distance_parcourut;
	// variables correspondant aux mers
	private $id_e, $nom_e, $type, $id_mer_country ;
	// cotoie 
	private $id_pays_cotoie;

	function __construct() {
	    parent::__construct(); 
	    $this->okName = true ;
	    $this->okPop = true ;
	    $this->tableau_country = array();
	    $this->tableau_fleuve = array();
	    $this->tableau_mer = array();
  	}

	function startDocument() 
	{
	    echo '<?xml version="1.0" encoding="UTF-8"?>'."\n";
	    echo "<!DOCTYPE em SYSTEM 'em.dtd'>" ;
		echo "<em>\n";
		echo "<liste-pays>" ;
	} 
  	

	function characters($txt) {
	    $txt_reduit = trim($txt);
	    $txt = str_replace("'", "\'",$txt);
	    // addslashes ne marche pas 
	    // htmlentities ne marche pas non plus
	    if (!(empty($txt_reduit))) $this->texte .= $txt ;

	  }

	function startElement($nom, $att) 
	{
		switch ($nom) {
			case 'country' :  
				$this->id_p = $att['car_code']; 
				$this->superficie = $att['area']; 
				break;

			case 'population' : 
				if($this->okPop)
				{
					$this->texte = ''; // on veut récupérer le noeud texte
				}
				break;
			case 'name':
				if($this->okName) // du fait que l'on veut uniquement la premiere balise name, on teste si l'on ne l'a pas déja
				{
					$this->texte = '';
				}
				break;

			case 'city' :
				// afin de récupérer la dernière population d'un pays, dès que l'on croise un élement city ou province on met le booleen à faux.
				$this->okPop = false ;	

				break;
			case 'province':
				// afin de récupérer la dernière population d'un pays, dès que l'on croise un élement city ou province on met le booleen à faux.
				$this->okPop = false ;
				break;
			case 'river':
				$this->id_f = $att['id'];
				if($this->okName)
				{
					$this->texte = '';
				}
				$this->id_pays_traverse = $att['country'];
				break;
			case 'to':
				$this->watertype = $att['watertype'];
				if($att['watertype'] == 'sea') // on traite dès maintenant le cas : On ne recherche que les rivières qui se jettent dans des mers ou océans
				{
					$this->se_jette = $att['water'];
				}
				break;
			case 'length':
				$this->texte = '';	// On a besoin de la longueur d'une riviere
				break;
			case 'source':
				$this->id_source = $att['country'];
				break;

			case 'sea':
				$this->id_e= $att['id'];
				$this->id_mer_country = $att['country'];
				if($this->okName)
				{
					$this->texte = '';
				}	
				break;
			default:
				break;
		}
	}

  	function endElement($name) 
  	{
	  	switch ($name) 
	  	{
	  		case 'name' :
				if($this->okName) // si on ne l'a pas encore écrit
				{
					// Du fait que les balises country, river, et sea ne s'entrecroisent pas, il n'est pas "grave" d'écrire le nom du fleuve,pays, et mer en même temps
					$this->nom_p = $this->texte; 
					$this->nom_f = $this->texte;
					$this->nom_e = $this->texte;
					$this->okName = false ;
				}
				break ;
	  		
	  		case 'population' : 
				if($this->okPop) // si on ne l'a pas encore écrit
				{
					$this->nb_hab = $this->texte ;
				}	
				break;
			
			case 'country' :
				// A la fin de notre balise country, on va donc créer un nouveau pays avec les informations recueillis
				$country = new Country($this->id_p, $this->nom_p, $this->superficie, $this->nb_hab); 
				// on la stocke dans notre tableau de pays
				$this->tableau_country[] = $country ;
				// Ne pas oublier de réinitialiser les booleens à la fin de la balise 
				$this->okName = true ; 
				$this->okPop = true ;
				break ;
			
			case 'length':
				$this->longueur = $this->texte ;
				$this->distance_parcourut = $this->texte ;
				break;
			
			case 'river':
				if($this->watertype == 'sea') // On ne va stocker que les fleuves qui nous interessent
				{
					$fleuve = new Fleuve($this->id_f, $this->nom_f, $this->longueur, $this->se_jette, $this->id_source,$this->id_pays_traverse,$this->distance_parcourut);
					$this->tableau_fleuve[] = $fleuve ;
				}
				// Ne pas oublier de réinitialiser les booleens à la fin de la balise 
				$this->okName = true ; 
				break;
			case 'sea':
				// Création d'une nouvelle mer 
				$mer = new Mer($this->id_e, $this->nom_e, $this->id_mer_country);
				// On la stocke dans notre tableau
				$this->tableau_mer[] = $mer ;
				// Ne pas oublier de réinitialiser les booleens à la fin de la balise 
				$this->okName = true ;
				break;
	  		default:
	  			break;
	  	}
	}

	/*
		C'est dans cette fonction que toute la reconstitution du document va avoir lieu.
	**/
  	function endDocument() 
	{

		// Booleen afin de ne pas réécrire un pays que l'on a déja écrit.
		$already_wrote = false ;

		// On parcourt chaque pays que l'on a enregistré
        foreach($this->tableau_country as $objetCountry)
        {
        	$id = $objetCountry->getID();
        	$nom = $objetCountry->getNom();
        	$super = $objetCountry->getSuperficie();
        	$nbHab = $objetCountry->getNbhab();
        	// 1er cas : les pays cotoyés par des mers, on va donc parcourir chaque mer.
        	foreach($this->tableau_mer as $objetMer)
        	{
        		$nom_mer = $objetMer->getNom();
	        	$id_mer = $objetMer->getID();
	        	// Création d'un tableau comportant tous les identifiants des pays cotoyés par cette mer
	        	$mer_country = explode(" ", $objetMer->getIDCountry());
	        	foreach ($mer_country as $objetMerCountry) 
	        	{
	        		if($objetMerCountry==$id && !$already_wrote) // si le pays est cotoyés par cette mer et que l'on ne l'a pas déja écrit
		        	{
			        	echo "<pays id-p ='$id' nom-p = '$nom' superficie ='$super' nbhab ='$nbHab'>" ;
			        	$already_wrote = true ;
		        	}
	        	}
	        	
        	}
        	// sinon deuxième cas : le pays est la source d'un fleuve qui se jette dans une mer ou océan OU est traversé par un fleuve qui se jette dans 
        	// une mer ou un océan
        	if(!$already_wrote)
        	{
        		foreach($this->tableau_fleuve as $objetfleuve)
	        	{
	        		$id_source = $objetfleuve->getSource();
	        		$id_pays_trav = explode(" ", $objetfleuve->getIDTraverse());
	        		foreach ($id_pays_trav as $objetPaysTrav) 
	        		{
	        			
	        			if(($objetPaysTrav==$id || $id_source==$id) && !$already_wrote)
			        	{
				        	echo "<pays id-p ='$id' nom-p = '$nom' superficie ='$super' nbhab ='$nbHab'>" ;
				        	$already_wrote = true ;
			        	}
	        		}
	        	}
        	}
        	

        	// On passe maintenant à l'écriture des fleuves (à l'intérieur de l'élement pays), les fleuves présents sont déja "sélectionnés", ce sont 
        	// les fleuves qui se jettent dans des mers
        	foreach($this->tableau_fleuve as $objetfleuve)
        	{
        		$id_fleuve = $objetfleuve->getID();
        		$nom_fleuve = $objetfleuve->getNom();
        		$id_source = $objetfleuve->getSource();
        		$longueur = $objetfleuve->getLongueur();
        		$jette = $objetfleuve->getSejette();

        		$id_pays_trav = explode(" ", $objetfleuve->getIDTraverse());

        		if($id_source == $id) // si le fleuve est la source du pays en question
        		{
        			echo "<fleuve id-f ='$id_fleuve' nom-f = '$nom_fleuve' longueur ='$longueur' se-jette ='$jette'>";
        			// on parcourt les pays traversés par ce fleuve afin d'écrire la balise parcourt
					foreach ($id_pays_trav as $objetPaysTrav) 
					{
						if(count($id_pays_trav) == 1) // test pour savoir quelle valeur distance va prendre
						{
							$distance = $objetfleuve->getDistanceParcourut();
						}
						else
						{
							$distance = 'inconnu' ;
						}
			        	echo "<parcourt id-pays ='$objetPaysTrav' distance = '$distance'></parcourt>" ;
		        	}
					echo "</fleuve>" ;
        		}

        	}
        	// ne pas écrire si l'on a pas écrit le début de la balise
        	if($already_wrote)
        	{
        		echo "</pays>" ; 
        		// réaliser le booleen 
				$already_wrote = false ;
        	}
        }
        echo "</liste-pays>" ;
        echo "<liste-espace-maritime>" ;
        // On va maintenant écrire la "deuxième" partie du document, on parcourt chaque mer
        foreach($this->tableau_mer as $objetMer)
        {
        	$nom_mer = $objetMer->getNom();
        	$id_mer = $objetMer->getID();
        	$type = $objetMer->getType();
        	// Même "technique", on va créer un tableau avec tous les identifiants des pays cotoyés par cette mer
    		$id_pays_trav = explode(" ", $objetMer->getIDCountry());
        	echo "<espace-maritime id-e ='$id_mer' nom-e = '$nom_mer' type ='$type'>"; 
        	foreach ($id_pays_trav as $objetPaysTrav) 
        	{
        		echo "<cotoie id-p = '$objetPaysTrav'></cotoie>";
        	}
        	echo "</espace-maritime>";
        }
        echo "</liste-espace-maritime>" ;
		echo "</em>";
	}
}

$xml = file_get_contents('mondial.xml');
$sax = new SaxParser(new MySaxHandler());
try {
    $sax->parse($xml);
}
catch(SAXException $e){  
    echo "\n",$e; 
}
catch(Exception $e) {
	echo "Default exception >>", $e; 
}

?>