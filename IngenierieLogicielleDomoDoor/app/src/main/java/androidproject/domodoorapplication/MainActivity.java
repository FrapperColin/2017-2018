package androidproject.domodoorapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;

import androidproject.domodoorapplication.connection.ConnectBluetoothActivity;
import androidproject.domodoorapplication.connection.ConnectionBluetoothActivity;

public class MainActivity extends AppCompatActivity {

    ConnectBluetoothActivity BTConnect;
    Button buttonOuvrir ;
    Button buttonFermer ;
    Button buttonUrgence;
    Button buttonReprise ;
    Button buttonQuitter ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonOuvrir = (Button) findViewById(R.id.ouvrir);
        buttonFermer = (Button) findViewById(R.id.fermer);
        buttonUrgence = (Button) findViewById(R.id.urgence);
        buttonReprise = (Button) findViewById(R.id.reprise);
        buttonQuitter = (Button) findViewById(R.id.quitter);


        BTConnect = new ConnectBluetoothActivity();

        // Avertir l'utilisateur d'autoriser la connection bluetooth
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(R.string.enable_bt);
        builder.setPositiveButton(R.string.autoriser, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BTConnect.setBtPermission(true);
                BTConnect.reply();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BTConnect.setBtPermission(false);
                BTConnect.reply();
            }
        });

        AlertDialog btPermissionAlert = builder.create();

        Context context = getApplicationContext();
        CharSequence text1 = getString(R.string.bt_disabled);
        CharSequence text2 = getString(R.string.bt_failed);
        CharSequence forbiddenAcess = getString(R.string.accessForbidden);
        // Toast pour avertir l'utilisateur
        Toast btDisabledToast = Toast.makeText(context, text1, Toast.LENGTH_LONG);
        Toast btFailedToast = Toast.makeText(context, text2, Toast.LENGTH_LONG);
        Toast btAcessFailed = Toast.makeText(context, forbiddenAcess, Toast.LENGTH_LONG);

        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.Preference), Context.MODE_PRIVATE);

        try {
            if (!BTConnect.initBT()) {
                // L'utilisateur n'a pas activé son bluetooth
                btDisabledToast.show();
                Intent intent = new Intent(MainActivity.this, ConnectionBluetoothActivity.class);
                startActivity(intent);
            }
            else if (!BTConnect.connectToEV3 (sharedpreferences.getString(getString(R.string.EV3_key), ""))) {
                //Impossible de se connecter, on le revoie à la page de connection
                btFailedToast.show();
                Intent intent = new Intent(MainActivity.this, ConnectionBluetoothActivity.class);
                startActivity(intent);
            }
            else if(!BTConnect.authorizeBluetooth()) {
                btAcessFailed.show();
                BTConnect.writeMessage((byte) 5);
                Intent intent = new Intent(MainActivity.this, ConnectionBluetoothActivity.class);
                startActivity(intent);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        buttonOuvrir.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        try {
                            BTConnect.writeMessage((byte) 1);
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }
                }
                return false;
            }
        });

        buttonOuvrir.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        try {
                            BTConnect.writeMessage((byte) 1);
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }
                }
                return false;
            }
        });




        buttonFermer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        try {
                            BTConnect.writeMessage((byte)2);
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }

                }
                return false;
            }
        });

        buttonUrgence.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        try {
                            BTConnect.writeMessage((byte) 3);
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }

                }
                return false;
            }
        });

        buttonReprise.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        try {
                            BTConnect.writeMessage((byte) 4);
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }
                }
                return false;
            }
        });


        buttonQuitter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            BTConnect.writeMessage((byte) 5);
                            Intent intent = new Intent(MainActivity.this, ConnectionBluetoothActivity.class);
                            startActivity(intent);

                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }
                }
                return false;

            }
        });

    }
}