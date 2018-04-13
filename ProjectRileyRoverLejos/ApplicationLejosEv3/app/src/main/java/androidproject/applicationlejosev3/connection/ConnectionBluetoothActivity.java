package androidproject.applicationlejosev3.connection;
/**
 * Created by moi on 07/02/2018.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidproject.applicationlejosev3.panel.ControlPageActivity;
import androidproject.applicationlejosev3.R;


public class ConnectionBluetoothActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_bluetooth);

        sharedpreferences = getSharedPreferences(getString(R.string.Preference), Context.MODE_PRIVATE);

        final Button button = (Button) findViewById(R.id.connectButton);
        final EditText macAddText = (EditText) findViewById(R.id.editMacAddText);
        macAddText.setText(sharedpreferences.getString(getString(R.string.EV3_key),""));

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!validMacAdd(macAddText.getText().toString())){
                    // Avertir l'utilisateur que l'adresse MAC n'est pas correcte
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConnectionBluetoothActivity.this);

                    builder.setMessage(R.string.entrer_mac_adresse);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

                    AlertDialog macDialog = builder.create();
                    macDialog.show();

                } else {

                    SharedPreferences.Editor speditor = sharedpreferences.edit();
                    speditor.putString(getString(R.string.EV3_key), macAddText.getText().toString());
                    speditor.commit();

                    Intent intent = new Intent(ConnectionBluetoothActivity.this, ControlPageActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validMacAdd(String macAdd)
    {
        return macAdd.length() == 17;
    }
}
