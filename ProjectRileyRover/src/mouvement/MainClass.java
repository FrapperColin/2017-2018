package mouvement;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.hardware.Bluetooth;
import lejos.hardware.BrickFinder;
import lejos.hardware.Wifi;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.robotics.RegulatedMotor;
import lejos.hardware.sensor.HiTechnicCompass;
public class MainClass {
	
	private static DataOutputStream out; 
	private static DataInputStream in;
	private static BTConnection BTConnect;
	private static int commande=0;
//	private static int power = 50;
//	private static boolean stop_app;
	
//	private static NXTRegulatedMotor moteurGauche;
//	private static NXTRegulatedMotor moteurDroit;
	  
//	public static void main(String[] args)
//	{
//		  
//		connect(); // connection au bluetooth
// 		stop_app = true;
//		moteurGauche = Motor.A;
//		moteurDroit = Motor.B;
//		moteurGauche.setSpeed(power);
//		moteurDroit.setSpeed(power);
//		moteurGauche.stop();
//		moteurDroit.stop();
//		// while
//		while(stop_app)
//		{
//			try {
//				commande = (int) in.readByte();
//				System.out.println("Reçu " + commande);
//				switch(commande){
//					
//					// Avancer
//					case 1:
//						moteurGauche.forward();
//					   	moteurDroit.forward();
//				   		break;
//	   
//				    // Reculer
//				    case 2: 
//				    	moteurGauche.backward();
//				    	moteurDroit.backward();
//				    	break;
//					   
//				    // Gauche
//				    case 3:
//				    	moteurGauche.backward();
//				    	moteurDroit.forward();
//				    	break ;
//				    
//				    // Droite
//				    case 4:
//				    	moteurDroit.backward();
//				    	moteurGauche.forward();
//				    	break ;
//				    
//				    // Accélerer
//				    case 5:
//				    	power += 50 ;
//				    	moteurGauche.setSpeed(power);
//				    	moteurDroit.setSpeed(power);
//				    	break ;
//				    // Ralentir
//				    case 6:
//				    	power -= 50;
//				    	moteurGauche.setSpeed(power);
//				    	moteurDroit.setSpeed(power);
//				    	break ;
//				   // Quittez
//				   case 7:
//					   stop_app = false;
//					   break;
//				}
//
//			}
//    
//			catch (IOException ioe) {
//				System.out.println("IO Exception readInt");
//			}
//		}
//
//		moteurGauche.flt();
//		moteurDroit.flt();
//	}
	public static void main(String[] args) 
    {
//		connect(); // connection au bluetooth

		EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(BrickFinder.getLocal().getPort("A"));
	    EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(BrickFinder.getLocal().getPort("B"));
	    
    	RegulatedMotor T[] = {rightMotor};
    	leftMotor.synchronizeWith(T);
    	
    	EV3Car car = new EV3Car(leftMotor, rightMotor);

 //   	int button;
    	boolean start = true;
    	System.out.println("avant boucle");
    	
    	connect();
    	while(start)
    	{
//    		System.out.println("début de l'attente");
    		try {
				commande = (int) in.readByte();
//				System.out.println("Reçu " + commande);
				switch(commande)
				{
    				case 1: 
				    	car.accelerate(50);
	    				break;	
    				case 3 :
	    				car.turnLeft(2);
	    				break;
    				case 4 : 
	    				car.turnRight(2);
	    				break;
    				case 6 : 
	    				car.decelerated(50);  
	    				break;
    				default :
    					start = false;
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
