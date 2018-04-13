import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

public class Main {

   public static void main(String[] args)
   {

      File mondial = new File("mondial.xml");
      Document result = parse(mondial);
      enregistre("cible.xml", result);
   }	

	private static Document parse(File f) {
		Document document = null ;
		Element racine_cible = new Element("em");

		Document document_cible = new Document(racine_cible);
	
        try {
            /* On crée une instance de SAXBuilder */
            SAXBuilder sxb = new SAXBuilder();
            document = sxb.build(f);
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier " 
				+ e.getMessage() );
            e.printStackTrace();
        } catch (JDOMException e){
            System.out.println("Erreur lors de la construction du fichier JDOM " 
				+ e.getMessage() );
            e.printStackTrace();
        }
        
        try {
            /* On initialise un nouvel élément avec l'élément racine du
               document. */
            Element racine = document.getRootElement();
            Element listepaysElem = new Element("liste-pays");
            Element listeEspaceMaritimeElem = new Element("liste-espace-maritime");
            racine_cible.addContent(listepaysElem);
            racine_cible.addContent(listeEspaceMaritimeElem);
            
            XPath mer = XPath.newInstance("/mondial/sea");  

            List<Element> NodeListMers = mer.selectNodes(racine) ;
            
            for (Element seaNode : NodeListMers){
            	Element espaceElem = new Element ("espace-maritime");
            	Element nodeNameMer = seaNode.getChild("name");

            	espaceElem.setAttribute("id-e", seaNode.getAttributeValue("id"));
            	espaceElem.setAttribute("nom-e", nodeNameMer.getText());
            	espaceElem.setAttribute("type","inconnu");
            	
            	String PaysCotoie = seaNode.getAttribute("country").getValue();
            	String [] arrayPaysCotoie = PaysCotoie.split(" ");
            	
            	for (String s : arrayPaysCotoie)
            	{
            		s = s.trim();
            		Element cotoieElem = new Element("cotoie");
            		cotoieElem.setAttribute("id-p",s);
            		espaceElem.addContent(cotoieElem);
            	}
            	listeEspaceMaritimeElem.addContent(espaceElem);
            }
            
            
            XPath country = XPath.newInstance("/mondial/country");  
  
            List<Element> NodeListPays = country.selectNodes(racine) ;
            boolean already_wrote = false ;
        	for(Element countryNode : NodeListPays) 
        	{
                Element paysElem = new Element("pays");

        		String idCountry = countryNode.getAttributeValue("car_code");
        		String nomCountry = countryNode.getChildText("name");
        		String superficie = countryNode.getAttributeValue("area"); 
        		XPath population = XPath.newInstance("/mondial/country[@car_code ='"+countryNode.getAttribute("car_code").getValue() +"']/population");  
        		List<Element> NodeListPopulation = population.selectNodes(racine);
        		String nbHab = NodeListPopulation.get(NodeListPopulation.size()-1).getValue() ;
                mer = XPath.newInstance("/mondial/sea");  
                List<Element> NodeListMer = mer.selectNodes(racine) ;
                for(Element merNode : NodeListMer) 
            	{
                	String merCountry = merNode.getAttributeValue("country");
                	String[] tab_mer_country = merCountry.split(" ");
                	for(String s : tab_mer_country)
                	{
                		s = s.trim();
                		if(s.equals(idCountry) && !already_wrote)
                		{
                    		paysElem.setAttribute("id-p",idCountry);
                    		paysElem.setAttribute("nom-p",nomCountry);
                    		paysElem.setAttribute("superficie",superficie);
                    		paysElem.setAttribute("nbhab",nbHab);
                    		already_wrote = true ;
                		}
                	}
            	}
                if(already_wrote == false)
                {
                	XPath fleuves = XPath.newInstance("/mondial/river[./to/@watertype = 'sea']"); 
				  	List<Element> NodeListRiver = fleuves.selectNodes(racine);
				  	for(Element riverNode : NodeListRiver) 
					{
				  		String id_source = riverNode.getChild("source").getAttributeValue("country");
	                	String riverCountry = riverNode.getAttributeValue("country");
	                	String[] tab_river_country = riverCountry.split(" ");
	                	for(String s : tab_river_country)
	                	{
	                		s = s.trim();
	                		if((s.equals(idCountry) || id_source.equals(idCountry) && !already_wrote))
	                		{
	                    		paysElem.setAttribute("id-p",idCountry);
	                    		paysElem.setAttribute("nom-p",nomCountry);
	                    		paysElem.setAttribute("superficie",superficie);
	                    		paysElem.setAttribute("nbhab",nbHab);
	                    		already_wrote = true ;
	                		}
	                	}
					}
                }
                
                XPath fleuves = XPath.newInstance("/mondial/river[./to/@watertype = 'sea']"); 
			  	List<Element> NodeListRiver = fleuves.selectNodes(racine);
			  	for(Element riverNode : NodeListRiver) 
				{
			  		String nom_fleuve = riverNode.getChildText("name");
			  		String id_fleuve = riverNode.getAttributeValue("id");
			  		String id_source = riverNode.getChild("source").getAttributeValue("country");
			  		String longueur = riverNode.getChildText("length");
			  		String jette = riverNode.getChild("to").getAttributeValue("water");
			  		
			  		String distance ;

                	String riverCountry = riverNode.getAttributeValue("country");
                	String[] tab_river_country = riverCountry.split(" ");
                	
                	if(id_source.equals(idCountry))
                	{
                		Element fleuveElem = new Element("fleuve");
    	            	fleuveElem.setAttribute("id-f",riverNode.getAttributeValue("id"));
    	            	fleuveElem.setAttribute("nom-f",nom_fleuve);
    	            	fleuveElem.setAttribute("longueur",longueur);
    	            	fleuveElem.setAttribute("se-jette",jette);
    	            	paysElem.addContent(fleuveElem);
    	            	for(String s : tab_river_country )
                    	{
    	            		s = s.trim();
                    		if(tab_river_country.length ==1)
                			{
                    			distance = longueur;
                			}
                    		else
                    		{
                    			distance = "inconnu";
                    		}
                    		
                    		Element parcourtElem = new Element("parcourt");
                    		parcourtElem.setAttribute("id-pays", s);
                    		parcourtElem.setAttribute("distance", distance);
                    		fleuveElem.addContent(parcourtElem);	
                    	}
                	}
                	  	
				}
			  	
			  	if(already_wrote)
			  	{
            		listepaysElem.addContent(paysElem);
			  		already_wrote = false ;
			  	}
        	}

        } catch (JDOMException e) {
            System.out.println("Erreur JDOM " + e.getMessage() );
            e.printStackTrace();            
        } 
        return document_cible ;

	}

	static void enregistre(String fichier, Document doc)
	{
	   try
	   {
	      XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
	      sortie.output(doc, new FileOutputStream(fichier));
	   }
	   catch (java.io.IOException e){}
	}
}
