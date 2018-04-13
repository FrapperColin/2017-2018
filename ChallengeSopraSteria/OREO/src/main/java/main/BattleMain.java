package main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;




@PropertySource("classpath:parameters.properties")
@Component
public class BattleMain 
{
	
	@Value("${rest.base.url}")
	private String rest_base;
	
	@Value("${team.name}")
	private String team_name;
	
	private Client c ;
	
	
	 public BattleMain() {
	 }
	 /*
	 public BattleMain(CommandLine command) {
		 


		 Properties prop = new Properties();
		 InputStream input = null;

		 try {
		      input = getClass().getClassLoader().getResourceAsStream("parameters.properties");

		    // load a properties file
		    prop.load(input);

		    // get the property value and print it out
		   // System.out.println(prop.getProperty("rest.base.url"));
		   // System.out.println(prop.getProperty("team.name"));
		    //System.out.println(prop.getProperty("team.password"));
		} catch (IOException ex) {
		    ex.printStackTrace();
		} finally {
		    if (input != null) {
		        try {
		            input.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}
	 }*/
	 
	 public void display() throws InterruptedException
	 {
		 InstanceClient i = new InstanceClient(c);
		 i.requestServeur();
		 
		 
		 /*
		 try {

	    		Client client = Client.create();

	    		WebResource webResource = client
	    		   .resource("http://challengemiage.codeandplay.date/epic-ws/epic/ping");

	    		ClientResponse response = webResource.accept("application/json")
	                       .get(ClientResponse.class);

	    		if (response.getStatus() != 200) {
	    		   throw new RuntimeException("Failed : HTTP error code : "
	    			+ response.getStatus());
	    		}

	    		String output = response.getEntity(String.class);

	    		System.out.println("Output from Server .... \n");
	    		System.out.println(output);

	    	  } catch (Exception e) {

	    		e.printStackTrace();

	    	  }*/

	 }
}

