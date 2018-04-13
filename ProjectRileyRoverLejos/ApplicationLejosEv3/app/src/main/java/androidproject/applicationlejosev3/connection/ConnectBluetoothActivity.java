package androidproject.applicationlejosev3.connection;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.UUID;

public class ConnectBluetoothActivity extends AppCompatActivity {

    private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";

    BluetoothAdapter localAdapter;
    BluetoothSocket socket_ev3;
    boolean success=false;
    private boolean btPermission=false;
    private boolean alertReplied=false;

    public void reply(){this.alertReplied = true;}
    public void setBtPermission(boolean btPermission) {
        this.btPermission = btPermission;
    }

    public boolean initBT(){
        localAdapter=BluetoothAdapter.getDefaultAdapter();
        return localAdapter.isEnabled();
    }

    public  boolean connectToEV3(String macAdd){

        // get le bluetooth de EV3
        BluetoothDevice ev3_1 = localAdapter.getRemoteDevice(macAdd);
        try {
            socket_ev3 = ev3_1.createRfcommSocketToServiceRecord(UUID.fromString(SPP_UUID));

            socket_ev3.connect();
            success = true;

        } catch (IOException e) {
            Log.d("Bluetooth","Erreur impossible de trouver l'appareil " + macAdd);
            success=false;
        }
        return success;

    }
    // Utile pour envoyer des message à l'EV3 (pour les fonctionnalités)
    public void writeMessage(byte msg) throws InterruptedException{
        BluetoothSocket connSock;

        connSock = socket_ev3;

        if(connSock!=null){
            try {
                OutputStreamWriter out=new OutputStreamWriter(connSock.getOutputStream());
                out.write(msg);
                out.flush();
                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            // TODO MESSAGE ERROR A AFFICHER
        }
    }

}
