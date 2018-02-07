package androidproject.applicationlejosev3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by moi on 07/02/2018.
 */

public class ControlPageActivity extends AppCompatActivity {

    ConnectBluetoothActivity BTConnect;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_page);

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

        // TODO AVANCER, RECULER A LIER AVEC LES BUTTONS
    }
}
