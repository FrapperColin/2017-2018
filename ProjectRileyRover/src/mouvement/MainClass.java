package mouvement;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.hardware.Bluetooth;
import lejos.hardware.Wifi;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

public class MainClass {
	
	private static DataOutputStream out; 
	private static DataInputStream in;
	private static BTConnection BTConnect;
	private static int commande=0;
	private static int power = 50;
	private static boolean stop_app;
	
	private static NXTRegulatedMotor moteurGauche;
	private static NXTRegulatedMotor moteurDroit;
	  
	public static void main(String[] args)
	{
		  
		connect(); // connection au bluetooth
 		stop_app = true;
		moteurGauche = Motor.A;
		moteurDroit = Motor.B;
		moteurGauche.setSpeed(power);
		moteurDroit.setSpeed(power);
		moteurGauche.stop();
		moteurDroit.stop();
		// while
		while(stop_app)
		{
			try {
				commande = (int) in.readByte();
				System.out.println("Reçu " + commande);
				switch(commande){
					
					// Avancer
					case 1:
						moteurGauche.forward();
					   	moteurDroit.forward();
				   		break;
	   
				    // Reculer
				    case 2: 
				    	moteurGauche.backward();
				    	moteurDroit.backward();
				    	break;
					   
				    case 3:
				    	moteurGauche.backward();
				    	moteurDroit.forward();
				    	break ;
				    
				    case 4:
				    	moteurDroit.backward();
				    	moteurGauche.forward();
				    	break ;
				    
				    	
				   // Quittez
				   case 7:
					   stop_app = false;
					   break;
				}

			}
    
			catch (IOException ioe) {
				System.out.println("IO Exception readInt");
			}
		}

		moteurGauche.flt();
		moteurDroit.flt();
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
