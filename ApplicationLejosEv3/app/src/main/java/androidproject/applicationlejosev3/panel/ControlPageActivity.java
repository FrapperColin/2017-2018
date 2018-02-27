package androidproject.applicationlejosev3.panel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidproject.applicationlejosev3.R;
import androidproject.applicationlejosev3.connection.ConnectBluetoothActivity;
import androidproject.applicationlejosev3.connection.ConnectionBluetoothActivity;

/**
 * Created by moi on 07/02/2018.
 */

public class ControlPageActivity extends FragmentActivity {

    ConnectBluetoothActivity BTConnect;
    int currentSpeed = 0 ;
    Speedometer speed ;
    Wheel wheel ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_page);

        speed = (Speedometer) getSupportFragmentManager().findFragmentById(R.id.speedometer);
        wheel = (Wheel) getSupportFragmentManager().findFragmentById(R.id.wheel);


        //getFragmentManager().beginTransaction().replace(R.id.fragment2, speed).commit();
       /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment2, new FragmentScreenB()).commit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment3, new FragmentScreenC()).commit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment4, new FragmentScreenD()).commit();*/



        BTConnect = new ConnectBluetoothActivity();

        // Avertir l'utilisateur d'autoriser la connection bluetooth
        AlertDialog.Builder builder = new AlertDialog.Builder(ControlPageActivity.this);

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

        // Toast pour avertir l'utilisateur
        Toast btDisabledToast = Toast.makeText(context, text1, Toast.LENGTH_LONG);
        Toast btFailedToast = Toast.makeText(context, text2, Toast.LENGTH_LONG);

        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.Preference), Context.MODE_PRIVATE);


        if (!BTConnect.initBT()) {
            // L'utilisateur n'a pas activé son bluetooth
            btDisabledToast.show();
            Intent intent = new Intent(ControlPageActivity.this, ConnectionBluetoothActivity.class);
            startActivity(intent);
        }

        if (!BTConnect.connectToEV3(sharedpreferences.getString(getString(R.string.EV3_key), ""))) {
            //Impossible de se connecter, on le revoie à la page de connection
            btFailedToast.show();
            Intent intent = new Intent(ControlPageActivity.this, ConnectionBluetoothActivity.class);
            startActivity(intent);
        }


        wheel.getButtonAvancer().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(currentSpeed == 0)
                        {
                            currentSpeed = 50;
                            speed.changeValue(currentSpeed);
                            //gauge.setValue(currentSpeed);
                        }
                        try {
                            BTConnect.writeMessage((byte) 1);
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }
                    /*
                    case MotionEvent.ACTION_UP:
                        try {
                            BTConnect.writeMessage((byte) 10);
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }*/
                }
                return false;
            }
        });


        wheel.getButtonReculer().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        try {
                            BTConnect.writeMessage((byte) 2);
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }
                        /*
                    case MotionEvent.ACTION_UP:
                        try {
                            BTConnect.writeMessage((byte) 10);
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }*/
                }
                return false;
            }
        });


        wheel.getButtonGauche().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            BTConnect.writeMessage((byte) 3);
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }
                    /*
                    case MotionEvent.ACTION_UP:
                        try {
                            BTConnect.writeMessage((byte) 10);
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }*/
                }
                return false;

            }
        });


        wheel.getButtonDroite().setOnTouchListener(new View.OnTouchListener() {
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
                    /*
                    case MotionEvent.ACTION_UP:
                        try {
                            BTConnect.writeMessage((byte) 10);
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }*/
                }
                return false;
            }
        });

        wheel.getButtonAccelerer().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        currentSpeed += 50 ;
                        speed.changeValue(currentSpeed);

                        //gauge.setValue(currentSpeed);
                        try {
                            BTConnect.writeMessage((byte) 5);
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }

                }


                return false;
            }
        });


        wheel.getButtonRalentir().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        currentSpeed -= 50 ;
                        speed.changeValue(currentSpeed);

                        //gauge.setValue(currentSpeed);
                        try {

                            BTConnect.writeMessage((byte) 6);
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }

                }


                return false;
            }
        });


        wheel.getButtonQuitter().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            BTConnect.writeMessage((byte) 7);
                            Intent intent = new Intent(ControlPageActivity.this, ConnectionBluetoothActivity.class);
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
