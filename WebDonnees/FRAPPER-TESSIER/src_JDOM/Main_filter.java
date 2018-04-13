import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.jdom.filter.Filter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

public class Main_filter {

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
	        
			Element racine = document.getRootElement();
			Element listepaysElem = new Element("liste-pays");
			Element listeEspaceMaritimeElem = new Element("liste-espace-maritime");
			racine_cible.addContent(listepaysElem);
			racine_cible.addContent(listeEspaceMaritimeElem);
			
			
			Filter mer = new ElementFilter("sea");

			List<Element> NodeListMers = racine.getContent(mer);
			
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
			
			
			Filter country = new ElementFilter("country");

			List<Element> NodeListPays = racine.getContent(country); ;
			boolean already_wrote = false ;
			for(Element countryNode : NodeListPays) 
			{
			    Element paysElem = new Element("pays");

				String idCountry = countryNode.getAttributeValue("car_code");
				String nomCountry = countryNode.getChildText("name");
				String superficie = countryNode.getAttributeValue("area"); 

			    List<Element> NodeListPopulation = countryNode.getContent(new ElementFilter("population"));
				String nbHab = NodeListPopulation.get(NodeListPopulation.size()-1).getValue() ;

 ;

			    Iterator NodeListMer = racine.getDescendants(new ElementFilter("sea"));
			    while(NodeListMer.hasNext())
			    {
			    	Element merNode = (Element) NodeListMer.next();
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
			    	Iterator NodeListRiver = racine.getDescendants(new ElementFilter("river"));
			    	
			    	while(NodeListRiver.hasNext())
			        {
			    	
			        	Element riverNode = (Element) NodeListRiver.next();
			        	if(riverNode.getChild("to")!=null)
			        	{
			        		if(riverNode.getChild("to").getAttributeValue("watertype").equals("sea"))
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
			        	
			        	
			        }
				  	
			    }
			    
				Iterator NodeListRiver = racine.getDescendants(new ElementFilter("river"));
				while(NodeListRiver.hasNext())
			    {
			    	Element riverNode = (Element) NodeListRiver.next();
			    	if(riverNode.getChild("to")!=null)
		        	{
		        		if(riverNode.getChild("to").getAttributeValue("watertype").equals("sea"))
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
		        	}
			    	  	
			    }

			  	if(already_wrote)
			  	{
					listepaysElem.addContent(paysElem);
			  		already_wrote = false ;
			  	}
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
