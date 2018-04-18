package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import database.ConnectionJson;
import lejos.hardware.Bluetooth;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

public class MainClassDomoDoor {

	private static DataOutputStream out; 
	private static DataInputStream in;
	private static BTConnection BTConnect;
	private static int commande=0;
	private static boolean stop_app;
	private static ConnectionJson connection ;
		  
	public static void main(String[] args) throws InterruptedException, IOException
	{
		connection = new ConnectionJson();
 		stop_app = true;	

		try {
			connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
 		
 		Controller ctrl = new Controller();
		// while
		while(stop_app) {
			try {
				commande = in.readByte();
				switch(commande) {	
					// Ouvrir la porte
					case 1:
						ctrl.open();
				   		break;		   		
				    // Fermer la porte
				    case 2: 
						ctrl.close();
				    	break;
				    // bouton urgence
				    case 3:
				    	ctrl.urgence();
				    	break;
				    // button reprise
				    case 4:
				    	ctrl.backUpKey();
				    	break;
				   // Quittez
				   case 5:
					   stop_app = false;
					   TimeUnit.SECONDS.sleep(15);
					   ctrl.close();
					   while(!ctrl.sensorDoorClosed2.isContact()) {
					   }
					   break;
				   default:
					   break;
				}
			} catch (IOException ioe) {
				System.out.println("IO Exception readInt");
			}
		}
		   ctrl.sensorDoorClosed2.arret();
		   ctrl.sensorDoorOpened2.arret();
		   out.close();
		   in.close();

	}
	  
	public static void connect() throws InterruptedException, IOException
	{  
		System.out.println("En attente");
		BTConnector BTconnector = (BTConnector) Bluetooth.getNXTCommConnector();
		BTConnect = (BTConnection) BTconnector.waitForConnection(3000, NXTConnection.RAW);
		out = BTConnect.openDataOutputStream();
		in = BTConnect.openDataInputStream();
		
		String bluetooth_code = in.readUTF();

		connection.connectToJsonFile();
		
		if(!connection.checkIfDataExist(bluetooth_code)) {
			writeMessage((byte) 6);
			stop_app = false;
		} else {
			writeMessage ((byte) 7);
		}
	}

	private static void writeMessage(byte msg) throws InterruptedException {
		try {
            out.write(msg);
            out.flush();
            Thread.sleep(1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
}

