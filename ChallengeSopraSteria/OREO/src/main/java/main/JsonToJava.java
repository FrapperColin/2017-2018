package main;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.json.simple.JSONObject;

import com.google.gson.Gson;

public class JsonToJava {
	
	public String json ;
	private PlateauJeu plateau ;

	public String getJson() {
		return json;
	}


	public void setJson(String json) {
		this.json = json;
	}


	public PlateauJeu getPlateau() {
		return plateau;
	}


	public void setPlateau(PlateauJeu plateau) {
		this.plateau = plateau;
	}


	public JsonToJava(String json) {
		this.json = json;
		run();
	}

	
	public void run()
	{
        Gson gson = new Gson();

		// Convert JSON to Java Object
        plateau = gson.fromJson(json, PlateauJeu.class);
        System.out.println(plateau.toString() +"\n");
       // System.out.println(plateau.toString());

		// Convert JSON to JsonElement, and later to String
        /*JsonElement json = gson.fromJson(reader, JsonElement.class);
        String jsonInString = gson.toJson(json);
        System.out.println(jsonInString);*/
        
	}
	 

}
