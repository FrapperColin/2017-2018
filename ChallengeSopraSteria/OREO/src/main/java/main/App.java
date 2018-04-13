package main;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Hello world!
 *
 */


public class App 
{
    
    public static void main( String[] args ) throws InterruptedException 
    {
        Options options = new Options();
        options.addOption("p", false, "specifie le paramètre 1");
        CommandLine command = null; 
       
        try
        {	
            CommandLineParser parser = new DefaultParser();
            command = parser.parse(options, args);
        }
        catch (org.apache.commons.cli.ParseException e)
        {
            System.err.printf("Error in the command line please read carefully  ");
            e.printStackTrace();
        }

		if (command.hasOption("p"))
        {
			GenericXmlApplicationContext context = new GenericXmlApplicationContext("GetProperties.xml");
			BattleMain Battle = context.getBean(BattleMain.class);
			Battle.display();
        }

    	
    	
    	
    	
    	
    	/*
        Options options = new Options();
        options.addOption("config", true, "specifie le paramètre 1");
        CommandLine command = null; 
       
        try
        {	
            CommandLineParser parser = new DefaultParser();
            command = parser.parse(options, args);
        }
        catch (org.apache.commons.cli.ParseException e)
        {
            System.err.printf("Error in the command line please read carefully  ");
            e.printStackTrace();
        }
		
		if (command.getOptionValue("p") != null)
        {
			GenericXmlApplicationContext context = new GenericXmlApplicationContext("GetProperties.xml");
			BattleMain Battle = context.getBean(BattleMain.class);
			Battle.display();
        }*/
    	
	    
    }
}
