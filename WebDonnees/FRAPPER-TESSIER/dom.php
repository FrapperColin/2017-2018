<?php header('Content-type: text/xml');
//import de base
$doc = new DOMDocument();
$doc->load("mondial.xml");
//creation dtd et balise de base
$domImpl = new DOMImplementation();
$docType = $domImpl->createDocumentType('em', '', 'em.dtd');
$em = $domImpl->createDocument('', 'em',$docType);
$em->encoding = 'UTF-8';
$em->standalone = false;
$em->formatOutput = true;
$racine = $em->documentElement;

//liste pays 
//creation balise de base
$listepaysElem = $em->createElement('liste-pays');
$racine->appendChild($listepaysElem);

$listeEspaceMaritimeElem = $em->createElement('liste-espace-maritime');
$racine->appendChild($listeEspaceMaritimeElem);

$tableauPays = array();
$tableauPaysParcouru = array();

//on récupère les estuaires correspondants aux différentes mers
foreach($doc->documentElement->childNodes as $seaNode) {
    if ($seaNode->nodeName == 'sea') {
    	//ajout des attributs
    	$espaceElem = $em->createElement('espace-maritime');
    	$espaceElem->setAttribute('id-e', $seaNode->getAttribute('id'));

    	//recupere le nom de la mer
    	foreach ($seaNode->childNodes as $seaChild) {
    		if($seaChild->nodeName == 'name'){
    			$espaceElem->setAttribute('nom-e', $seaChild->nodeValue);
    		}
    	} 
    	$espaceElem->setAttribute('type', 'inconnu');

    	//creation des balises cotoie grace aux pays de l attribut country
    	$arrayPaysCotoie = explode(" ", $seaNode->getAttribute('country'));
		foreach ($arrayPaysCotoie as $paysCotoie) {
			$cotoieElem = $em->createElement('cotoie');
			$cotoieElem->setAttribute('id-p', $paysCotoie);
			$espaceElem->appendChild($cotoieElem);

			//ajoute aussi le pays au tableau general pour la cration des balises pays plus loin
			array_push($tableauPays, $paysCotoie);
		}
    	//ajout de la balise dans la hierarchie 
    	$listeEspaceMaritimeElem->appendChild($espaceElem);

    }
}

//array unique
$tableauPays = array_unique($tableauPays);

//creation pays 
foreach($doc->documentElement->childNodes as $pays) {
	//verifie si il s agit d une balise <country> et si il est dans le tableau des pays a ajouter
    if ($pays->nodeName == 'country' && in_array($pays->getAttribute('car_code'), $tableauPays)) {
		$paysElem = $em->createElement('pays');
		$paysElem->setAttribute('id-p', $pays->getAttribute('car_code'));
		$paysElem->setAttribute('superficie', $pays->getAttribute('area'));

		//recupere la derniere annee de population
		$annee_derniere_pop = NULL;
		$current_population = NULL;

		foreach($pays->childNodes as $paysChild) {
			//cherche parmi les balises population
			if($paysChild->nodeName == 'population'){
				if(!isset($annee_derniere_pop) || $annee_derniere_pop < $paysChild->nodeValue){
					$annee_derniere_pop = $paysChild->getAttribute('year');
					$current_population = $paysChild->nodeValue;
				}
			}
			//defini l attribut nom quand il s agit de la balise name
			if($paysChild->nodeName == 'name'){
				$paysElem->setAttribute('nom-p', $paysChild->nodeValue);
			}
		}
		$paysElem->setAttribute('nbhab', $current_population);
		$listepaysElem->appendChild($paysElem);
	}
}

//parcours des fleuves

foreach($doc->documentElement->childNodes as $fleuve) {
	//si balise river 
    if ($fleuve->nodeName == 'river'){
    	//verification se jete dans une mer
    	$seJetteMer = false;
    	foreach($fleuve->childNodes as $fleuveChild) {
    		if($fleuveChild->nodeName == 'to'){
    			if($fleuveChild->getAttribute('watertype') == 'sea'){
    				$seJetteMer = true;
    			}
    			break;
    		}
    	}

    	if($seJetteMer){
    		//creation de la balise fleuve avec les infos 
	    	$fleuveElem = $em->createElement('fleuve');
	    	$fleuveElem->setAttribute('id-f', $fleuve->getAttribute('id'));
	    	//sauvegarde pour les balises parcourt
	    	$longueurFleuve = 0;
	    	$paysSource = null;

	    	foreach($fleuve->childNodes as $fleuveChild) {
	    		if($fleuveChild->nodeName == 'name'){
					$fleuveElem->setAttribute('nom-f', $fleuveChild->nodeValue);
	    		} else if ($fleuveChild->nodeName == 'length'){
	    			$fleuveElem->setAttribute('longueur', $fleuveChild->nodeValue);
	    			$longueurFleuve = $fleuveChild->nodeValue;
	    		} else if ($fleuveChild->nodeName == 'to'){
	    			$fleuveElem->setAttribute('se-jette', $fleuveChild->getAttribute('water'));
	    		} else if ($fleuveChild->nodeName == 'source'){
	    			$paysSource = $fleuveChild->getAttribute('country');
	    		}
	    	}

	    	//creation des balises parcours lié au fleuve
	    	$arrayParcourtFleuve = explode(" ", $fleuve->getAttribute('country'));
			foreach ($arrayParcourtFleuve as $fleuveParcourt) {
				$parcourtElem = $em->createElement('parcourt');
				$parcourtElem->setAttribute('id-pays', $fleuveParcourt);
				if(count($arrayParcourtFleuve) == 1){
					$parcourtElem->setAttribute('distance', $longueurFleuve);
				}else {
					$parcourtElem->setAttribute('distance', "inconnu");
				}
				$fleuveElem->appendChild($parcourtElem);
	    	}

	    	//ajout de la balise dans le pays correspondant, le créer si besoin
	    	$paysDejaExistant = false;
	    	//recherche le pays 
	    	foreach ($listepaysElem->childNodes as $pays) {
	    		if($pays->getAttribute('id-p') == $paysSource){
	    			$paysDejaExistant = true;
	    			$pays->appendChild($fleuveElem);
	    		}
	    	}
	    	//si le pays n existe pas on le cree
	    	if(!$paysDejaExistant){
	    		//creation pays 
				foreach($doc->documentElement->childNodes as $pays) {
					//verifie si il s agit d une balise <country> et le pays qu'on souhaite ajouter
				    if ($pays->nodeName == 'country' && $paysSource == $pays->getAttribute('car_code')) {
						$paysElem = $em->createElement('pays');
						$paysElem->setAttribute('id-p', $pays->getAttribute('car_code'));
						$paysElem->setAttribute('superficie', $pays->getAttribute('area'));

						//recupere la derniere annee de population
						$annee_derniere_pop = NULL;
						$current_population = NULL;

						foreach($pays->childNodes as $paysChild) {
							//cherche parmi les balises population
							if($paysChild->nodeName == 'population'){
								if(!isset($annee_derniere_pop) || $annee_derniere_pop < $paysChild->nodeValue){
									$annee_derniere_pop = $paysChild->getAttribute('year');
									$current_population = $paysChild->nodeValue;
								}
							}
							//defini l attribut nom quand il s agit de la balise name
							if($paysChild->nodeName == 'name'){
								$paysElem->setAttribute('nom-p', $paysChild->nodeValue);
							}
						}
						$paysElem->setAttribute('nbhab', $current_population);
						$listepaysElem->appendChild($paysElem);
						$paysElem->appendChild($fleuveElem);
						//ajout dans le tableau pour la suite et eviter les doublons 
						array_push($tableauPays, $pays->getAttribute('car_code'));
						break;

					}
				}
	    	}

	    	//ajout dans le tableau pour ajouter les pays manquant 
			foreach ($arrayParcourtFleuve as $fleuveParcourt) {
				if(!in_array($fleuveParcourt, $tableauPays)){
					array_push($tableauPaysParcouru, $fleuveParcourt);
				}
			}
    	}
    }
}

//creation des pays qui n existe pas mais qui sont traversés
$tableauPaysParcouru = array_unique($tableauPaysParcouru);

foreach($doc->documentElement->childNodes as $pays) {
	//verifie si il s agit d une balise <country> et le pays qu'on souhaite ajouter
	if ($pays->nodeName == 'country' && in_array($pays->getAttribute('car_code'), $tableauPaysParcouru)) {
		$paysElem = $em->createElement('pays');
		$paysElem->setAttribute('id-p', $pays->getAttribute('car_code'));
		$paysElem->setAttribute('superficie', $pays->getAttribute('area'));

		//recupere la derniere annee de population
		$annee_derniere_pop = NULL;
		$current_population = NULL;

		foreach($pays->childNodes as $paysChild) {
			//cherche parmi les balises population
			if($paysChild->nodeName == 'population'){
				if(!isset($annee_derniere_pop) || $annee_derniere_pop < $paysChild->nodeValue){
					$annee_derniere_pop = $paysChild->getAttribute('year');
					$current_population = $paysChild->nodeValue;
				}
			}
			//defini l attribut nom quand il s agit de la balise name
			if($paysChild->nodeName == 'name'){
				$paysElem->setAttribute('nom-p', $paysChild->nodeValue);
			}
		}
		$paysElem->setAttribute('nbhab', $current_population);
		$listepaysElem->appendChild($paysElem);

	}
}

//echo $em->saveXML();
$em->save('exo_dom.xml');

?>