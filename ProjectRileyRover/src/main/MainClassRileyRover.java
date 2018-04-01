package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.hardware.Bluetooth;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

public class MainClassRileyRover {
	
	private static DataOutputStream out; 
	private static DataInputStream in;
	private static BTConnection BTConnect;
	private static int commande=0;

	public static void main(String[] args) 
    {
    	connect();

    	boolean stop_app = true;
 		Controller ctrl = new Controller();

    	while(stop_app)
    	{
    		try {
				commande = (int) in.readByte();
				switch(commande)
				{
    				case 1: 
				    	ctrl.movingForward();
	    				break;	
    				case 2 :
	    				ctrl.movingBackward();
	    				break;
    				case 3 : 
	    				ctrl.turnLeft(2);
	    				break;
    				case 4 : 
    					ctrl.turnRight(2);
	    				break;
    				case 5 :
    					ctrl.accelerate(50);
    					break ;
    				case 6 : 
    					ctrl.decelerated(50);
    					break ;
    				case 7 :
    					ctrl.stop();
    					break;
    				default :
    					stop_app = false;
    					ctrl.ultrasonicSensor.arret();
    					in.close();
    					out.close();
    					break;
				}
    		}catch (IOException ioe) {
    			System.out.println("IO Exception readInt");
    		}
    	}
    }
	  
	public static void connect()
	{  
		System.out.println("En attente");
		BTConnector BTconnector = (BTConnector) Bluetooth.getNXTCommConnector();
		BTConnect = (BTConnection) BTconnector.waitForConnection(30000, NXTConnection.RAW);
		out = BTConnect.openDataOutputStream();
		in = BTConnect.openDataInputStream();
	}


}
