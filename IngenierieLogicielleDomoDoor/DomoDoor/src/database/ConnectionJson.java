package database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConnectionJson {
	
	private InputStream file ;
	
	public ConnectionJson() {
		super();
	}

	public void connectToJsonFile() {
		file = getClass().getResourceAsStream("/access.json");
	}
	
	public boolean checkIfDataExist(String dataBluetooth) {
		
        JSONParser parser = new JSONParser();
        try {
        	
        	JSONObject jsonObject = (JSONObject)parser.parse(new InputStreamReader(file, "UTF-8"));
        	
        	JSONArray arrayUser = (JSONArray) jsonObject.get("Users");
        	for(int i=0; i<arrayUser.size();i++) {
                JSONObject jsonobject = (JSONObject) arrayUser.get(i);
                String name = (String) jsonobject.get("name");
                String surname = (String) jsonobject.get("surname");
                String bluetooth_code = (String) jsonobject.get("bluetooth_code");
                if(dataBluetooth.equals(bluetooth_code)) {
                    System.out.println("Bonjour " + name + " " + surname);
                    return true;
                }
        	}
        } catch (FileNotFoundException e) {
	          e.printStackTrace();
	      } catch (IOException e) {
	          e.printStackTrace();
	      } catch (ParseException e) {
	          e.printStackTrace();
	      }
		return false ;
	}
}
