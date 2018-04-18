package androidproject.domodoorapplication.connection;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.UUID;

public class ConnectBluetoothActivity extends AppCompatActivity {

    private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";

    BluetoothAdapter localAdapter;
    BluetoothSocket socket_ev3;
    private DataInputStream in ;
    private DataOutputStream out ;

    boolean success=false;
    private boolean btPermission=false;
    private boolean alertReplied=false;
    private String mac;

    public void reply(){this.alertReplied = true;}
    public void setBtPermission(boolean btPermission) {
        this.btPermission = btPermission;
    }

    public boolean initBT(){
        localAdapter=BluetoothAdapter.getDefaultAdapter();
        /*
        Log.d("hello",localAdapter.getAddress());
        Set<BluetoothDevice> setDevice = localAdapter.getBondedDevices();
        for (BluetoothDevice device : setDevice) {
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress(); // MAC address
        }*/
        // Trouver comment envoyer l'adresse bluetooth en entier
        return localAdapter.isEnabled();
    }

    public  boolean connectToEV3(String macAdd) throws InterruptedException{

        // get le bluetooth de EV3
        BluetoothDevice ev3_1 = localAdapter.getRemoteDevice(macAdd);
        try {
            socket_ev3 = ev3_1.createRfcommSocketToServiceRecord(UUID.fromString(SPP_UUID));
            socket_ev3.connect();
            Field mServiceField = localAdapter.getClass().getDeclaredField("mService");
            mServiceField.setAccessible(true);

            Object btManagerService = mServiceField.get(localAdapter);

            if (btManagerService != null) {
                mac = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
            }
            Log.d("BluetoothAdresseLALAL", mac);

            out=new DataOutputStream(socket_ev3.getOutputStream());
            in = new DataInputStream(socket_ev3.getInputStream());
            out.writeUTF(mac);
            out.flush();
            Thread.sleep(1000);
            success = true;

        } catch (IOException e) {
            Log.d("Bluetooth","Erreur impossible de trouver l'appareil " + macAdd);
            success=false;
        } catch (NoSuchFieldException e) {
            success=false;
        } catch (NoSuchMethodException e) {
            success=false;
        } catch (IllegalAccessException e) {
            success=false;
        } catch (InvocationTargetException e) {
            success=false;
        }
        return success;

    }
    // Utile pour envoyer des message à l'EV3 (pour les fonctionnalités)
    public void writeMessage(byte msg) throws InterruptedException{
        BluetoothSocket connSock;

        if(socket_ev3!=null){
            try {
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

    public boolean authorizeBluetooth() throws InterruptedException
    {
        boolean success =true ;

        if(socket_ev3!=null){
            try {
                if(in.available() != -1) {
                    int commande = (int) in.readByte();
                    if(commande == 6) {
                        success = false ;
                    } else {
                        success = true ;
                    }
                }

                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success ;
    }

    public void close() throws IOException{
        in.close();
        out.close();
    }



}
