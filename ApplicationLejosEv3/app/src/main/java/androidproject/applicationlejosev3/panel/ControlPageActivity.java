package androidproject.applicationlejosev3.panel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.github.capur16.digitspeedviewlib.DigitSpeedView;

import androidproject.applicationlejosev3.R;
import androidproject.applicationlejosev3.connection.ConnectBluetoothActivity;
import androidproject.applicationlejosev3.connection.ConnectionBluetoothActivity;
import de.nitri.gauge.Gauge;
/**
 * Created by moi on 07/02/2018.
 */

public class ControlPageActivity extends AppCompatActivity {

    ConnectBluetoothActivity BTConnect;
    int currentSpeed = 0 ;
    Button buttonA ;
    Button buttonR ;
    ImageButton buttonG ;
    ImageButton buttonD ;
    Button buttonExit ;
    Button buttonStop;
    Gauge gauge ;
    DigitSpeedView digit ;
    AppCompatSeekBar seekBar ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_page);

        buttonA = (Button) findViewById(R.id.buttonA);
        buttonR = (Button) findViewById(R.id.buttonR);
        buttonG = (ImageButton) findViewById(R.id.buttonG);
        buttonD = (ImageButton) findViewById(R.id.buttonD);
        buttonExit = (Button) findViewById(R.id.buttonQuit);
        buttonStop = (Button) findViewById(R.id.buttonS);

        gauge = findViewById(R.id.gauge);
        BTConnect = new ConnectBluetoothActivity();
        seekBar = findViewById(R.id.seek);
        seekBar.incrementProgressBy(10);
        digit = findViewById(R.id.digitView);

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

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i==0) {
                    try {
                        BTConnect.writeMessage((byte) 7);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentSpeed = 0;
                    gauge.setValue(currentSpeed);
                    digit.updateSpeed(currentSpeed);
                } else if(i> currentSpeed) {
                    try {
                        BTConnect.writeMessage((byte) 5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentSpeed += 10 ;
                    gauge.setValue(currentSpeed);
                    digit.updateSpeed(currentSpeed);
                } else if (i< currentSpeed) {
                    try {
                        BTConnect.writeMessage((byte) 6);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentSpeed -= 10;
                    gauge.setValue(currentSpeed);
                    digit.updateSpeed(currentSpeed);
                }
                seekBar.setProgress(currentSpeed);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        buttonA.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(currentSpeed == 0) {
                            currentSpeed = 10;
                            digit.updateSpeed(currentSpeed);
                            gauge.setValue(currentSpeed);
                            seekBar.setProgress(currentSpeed);
                        }
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

        buttonR.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        try {
                            BTConnect.writeMessage((byte)2);
                            if(currentSpeed == 0) {
                                currentSpeed = 10;
                                digit.updateSpeed(currentSpeed);
                                gauge.setValue(currentSpeed);
                                seekBar.setProgress(currentSpeed);
                            }
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }
                }
                return false;
            }
        });

        buttonG.setOnTouchListener(new View.OnTouchListener() {
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
                }
                return false;

            }
        });

        buttonD.setOnTouchListener(new View.OnTouchListener() {
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

        buttonStop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            BTConnect.writeMessage((byte) 7);
                            currentSpeed = 0;
                            digit.updateSpeed(currentSpeed);
                            gauge.setValue(currentSpeed);
                            seekBar.setProgress(currentSpeed);
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }
                }
                return false;
            }
        });

        buttonExit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            BTConnect.writeMessage((byte) 10);
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
