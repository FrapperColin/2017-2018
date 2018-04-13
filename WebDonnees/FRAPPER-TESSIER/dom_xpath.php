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
$xpath = new DOMXpath($doc);

//liste pays 
//creation balise de base
$listepaysElem = $em->createElement('liste-pays');
$racine->appendChild($listepaysElem);

$listeEspaceMaritimeElem = $em->createElement('liste-espace-maritime');
$racine->appendChild($listeEspaceMaritimeElem);

$tableauPays = array();
$tableauPaysParcouru = array();

//on récupère les estuaires correspondants aux différentes mers
$NodeListMers = $xpath->query('/mondial/sea');
foreach($NodeListMers as $seaNode) {
	//ajout des attributs
	$espaceElem = $em->createElement('espace-maritime');
	$espaceElem->setAttribute('id-e', $seaNode->getAttribute('id'));

	//recupere le nom de la mer
	$nodeNameMer = $xpath->query('./name', $seaNode);
	$espaceElem->setAttribute('nom-e', $nodeNameMer->item(0)->nodeValue);


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

//array unique
$tableauPays = array_unique($tableauPays);

//creation pays 
$NodeListPays = $xpath->query('/mondial/country');
foreach($NodeListPays as $pays) {
	//verifie si il s agit d une balise <country> et si il est dans le tableau des pays a ajouter
    if (in_array($pays->getAttribute('car_code'), $tableauPays)) {
		$paysElem = $em->createElement('pays');
		$paysElem->setAttribute('id-p', $pays->getAttribute('car_code'));
		$paysElem->setAttribute('superficie', $pays->getAttribute('area'));

		//recupere la derniere annee de population
		$annee_derniere_pop = NULL;
		$current_population = NULL;

		//cherche parmi les balises population
		$NodeListPopulation = $xpath->query('./population', $pays);
		foreach($NodeListPopulation as $paysChild) {
			if(!isset($annee_derniere_pop) || $annee_derniere_pop < $paysChild->nodeValue){
				$annee_derniere_pop = $paysChild->getAttribute('year');
				$current_population = $paysChild->nodeValue;
			}
		}

		$NodePaysName = $xpath->query('./name', $pays);
		$paysElem->setAttribute('nom-p', $NodePaysName->item(0)->nodeValue);
		$paysElem->setAttribute('nbhab', $current_population);
		$listepaysElem->appendChild($paysElem);
	}
}

//parcours des fleuves
$NodeListRiver = $xpath->query('/mondial/river[./to/@watertype = "sea"]');
foreach($NodeListRiver as $fleuve) {
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

	//recherche le pays 
	//ajout de la balise dans le pays correspondant, le créer si besoin
	//on ne peut pas utiliser xpath car il s'agit d'un type différent
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
		$nb_source = explode(" ", $paysSource);
		if(sizeof($nb_source) == 1 ){
			$NodePays = $xpath->query('/mondial/country[@car_code = "'.$paysSource.'"]');

			$paysElem = $em->createElement('pays');
			$paysElem->setAttribute('id-p', $NodePays->item(0)->getAttribute('car_code'));
			$paysElem->setAttribute('superficie', $NodePays->item(0)->getAttribute('area'));
			
			//recupere la derniere annee de population
			$annee_derniere_pop = NULL;
			$current_population = NULL;

			$NodeListPopulation = $xpath->query('./population', $NodePays->item(0));
			foreach ($NodeListPopulation as $paysChild) {
				if(!isset($annee_derniere_pop) || $annee_derniere_pop < $paysChild->nodeValue){
					$annee_derniere_pop = $paysChild->getAttribute('year');
					$current_population = $paysChild->nodeValue;
				}
			}
			
			//defini l attribut nom 
			$NodeNomPays = $xpath->query('./name', $NodePays->item(0));
			$paysElem->setAttribute('nom-p', $NodeNomPays->item(0)->nodeValue);
			
			$paysElem->setAttribute('nbhab', $current_population);
			$listepaysElem->appendChild($paysElem);
			$paysElem->appendChild($fleuveElem);
			//ajout dans le tableau pour la suite et eviter les doublons 
			array_push($tableauPays, $NodePays->item(0)->getAttribute('car_code'));
		}
	}

	//ajout dans le tableau pour ajouter les pays manquant 
	foreach ($arrayParcourtFleuve as $fleuveParcourt) {
		if(!in_array($fleuveParcourt, $tableauPays)) {
			array_push($tableauPaysParcouru, $fleuveParcourt);
		}
	}
}

//creation des pays qui n existe pas mais qui sont traversés
$tableauPaysParcouru = array_unique($tableauPaysParcouru);

foreach ($tableauPaysParcouru as $paysAAjouter){
	//recupere le pays a ajouter
	$paysXML = $xpath->query('/mondial/country[@car_code = "'.$paysAAjouter.'"]');

	$paysElem = $em->createElement('pays');
	$paysElem->setAttribute('id-p', $paysXML->item(0)->getAttribute('car_code'));
	$paysElem->setAttribute('superficie', $paysXML->item(0)->getAttribute('area'));

	//recupere la derniere annee de population
	$annee_derniere_pop = NULL;
	$current_population = NULL;

	$NodeListPopulation = $xpath->query('./population', $paysXML->item(0));
	foreach ($NodeListPopulation as $paysChild) {
		if(!isset($annee_derniere_pop) || $annee_derniere_pop < $paysChild->nodeValue){
			$annee_derniere_pop = $paysChild->getAttribute('year');
			$current_population = $paysChild->nodeValue;
		}
	}

	//defini l attribut nom 
	$NodeNomPays = $xpath->query('./name', $paysXML->item(0));
	$paysElem->setAttribute('nom-p', $NodeNomPays->item(0)->nodeValue);

	$paysElem->setAttribute('nbhab', $current_population);
	$listepaysElem->appendChild($paysElem);
}

//echo $em->saveXML();*/
$em->save('exo_dom_xpath.xml');

?>